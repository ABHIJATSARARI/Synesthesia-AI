package com.example.test_camera

import android.Manifest
import android.animation.ValueAnimator
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import java.nio.ByteBuffer
import java.util.Locale
import java.util.concurrent.Executors
import android.animation.ArgbEvaluator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.drawable.ColorDrawable

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    // --- Views ---
    private lateinit var resultText: TextView
    private lateinit var viewFinder: PreviewView
    private lateinit var starfieldView: StarfieldView
    private lateinit var holographicDisplay: FrameLayout
    private lateinit var toggleCameraButton: ImageButton
    private lateinit var switchCameraButton: ImageButton
    private lateinit var toggleSoundButton: ImageButton
    private lateinit var overlayView: View

    // --- Camera ---
    private val cameraExecutor = Executors.newSingleThreadExecutor()
    private var cameraProvider: ProcessCameraProvider? = null
    private var camera: Camera? = null
    private var imageAnalysis: ImageAnalysis? = null
    private var cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var isCameraOn = true

    // --- Inference ---
    private var lastInferenceTime = 0L

    // --- Audio ---
    private var primaryPlayer: MediaPlayer? = null
    private var secondaryPlayer: MediaPlayer? = null
    private var currentLabel = ""
    private var isSoundMuted = false

    // --- TTS ---
    private lateinit var tts: TextToSpeech
    private var isTtsInitialized = false

    // --- Haptics ---
    private lateinit var vibrator: Vibrator

    companion object {
        init {
            System.loadLibrary("synesthesia-ai")
        }
    }

    external fun classify(
        yBuffer: ByteBuffer, uBuffer: ByteBuffer, vBuffer: ByteBuffer,
        yPixelStride: Int, uPixelStride: Int, vPixelStride: Int,
        yRowStride: Int, uRowStride: Int, vRowStride: Int,
        width: Int, height: Int
    ): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // --- View Initialization ---
        resultText = findViewById(R.id.resultTextView)
        viewFinder = findViewById(R.id.previewView)
        starfieldView = findViewById(R.id.starfieldView)
        holographicDisplay = findViewById(R.id.holographicDisplay)
        toggleCameraButton = findViewById(R.id.toggle_camera_button)
        switchCameraButton = findViewById(R.id.switch_camera_button)
        toggleSoundButton = findViewById(R.id.toggle_sound_button)
        overlayView = findViewById(R.id.overlay_view)

        tts = TextToSpeech(this, this)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // --- Event Listeners ---
        toggleCameraButton.setOnClickListener { toggleCamera() }
        switchCameraButton.setOnClickListener { switchCamera() }
        toggleSoundButton.setOnClickListener { toggleSound() }

        // --- Permissions ---
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 10)
        }

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    private fun toggleCamera() {
        vibrator.vibrate(50)
        if (isCameraOn) {
            stopCamera()
            toggleCameraButton.setImageResource(android.R.drawable.ic_media_pause)
        } else {
            startCamera()
            toggleCameraButton.setImageResource(android.R.drawable.ic_media_play)
        }
        isCameraOn = !isCameraOn
    }

    private fun switchCamera() {
        vibrator.vibrate(50)
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
        startCamera() // Re-bind the use cases
    }

    private fun toggleSound() {
        vibrator.vibrate(50)
        isSoundMuted = !isSoundMuted
        if (isSoundMuted) {
            primaryPlayer?.setVolume(0f, 0f)
            toggleSoundButton.setImageResource(android.R.drawable.ic_lock_silent_mode)
        } else {
            primaryPlayer?.setVolume(1f, 1f)
            toggleSoundButton.setImageResource(android.R.drawable.ic_lock_silent_mode_off)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }

            imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
                .build()

            imageAnalysis?.setAnalyzer(cameraExecutor) { imageProxy ->
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastInferenceTime > 300) {
                    val yBuffer = imageProxy.planes[0].buffer
                    val uBuffer = imageProxy.planes[1].buffer
                    val vBuffer = imageProxy.planes[2].buffer

                    val yPixelStride = imageProxy.planes[0].pixelStride
                    val uPixelStride = imageProxy.planes[1].pixelStride
                    val vPixelStride = imageProxy.planes[2].pixelStride

                    val yRowStride = imageProxy.planes[0].rowStride
                    val uRowStride = imageProxy.planes[1].rowStride
                    val vRowStride = imageProxy.planes[2].rowStride

                    val resultString = classify(
                        yBuffer, uBuffer, vBuffer,
                        yPixelStride, uPixelStride, vPixelStride,
                        yRowStride, uRowStride, vRowStride,
                        imageProxy.width, imageProxy.height
                    )

                    runOnUiThread {
                        handleResult(resultString)
                    }
                    lastInferenceTime = currentTime
                }
                imageProxy.close()
            }

            try {
                cameraProvider?.unbindAll()
                camera = cameraProvider?.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
            } catch (exc: Exception) {
                Log.e("Synesthesia", "Camera fail", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun stopCamera() {
        cameraProvider?.unbindAll()
    }


    private fun handleResult(resultString: String) {
        if (resultString.startsWith("Inference Error")) {
            resultText.text = "Inference Paused"
            Log.e("Synesthesia", resultString)
            return
        }

        val parts = resultString.split(":")
        if (parts.size < 2) return

        val label = parts[0]
        val score = parts[1].toFloatOrNull() ?: 0.0f

        if (score < 0.70f) {
            resultText.text = "Analyzing..."
            return
        }

        val emoji = when(label) {
            "Serenity" -> "🌿"
            "High_Stimulus" -> "⚡"
            "Geometric_Order" -> "📐"
            "Danger" -> "⚠️"
            else -> ""
        }

        resultText.text = "$emoji $label (${(score * 100).toInt()}%)"

        if (label == "Serenity") {
            startBreathingAnimation(resultText)
        } else {
            resultText.animation?.cancel()
        }

        if (label != currentLabel) {
            if (isTtsInitialized) {
                tts.speak("Environment: $label", TextToSpeech.QUEUE_FLUSH, null, null)
            }
            updateVibe(label)
            playMusic(label)
        }
    }

    private fun startBreathingAnimation(view: View) {
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.1f).apply {
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
        }
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.1f).apply {
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
        }
        val set = AnimatorSet()
        set.playTogether(scaleX, scaleY)
        set.duration = 2000 // 2 seconds in, 2 seconds out
        set.start()
    }

    private fun updateVibe(label: String) {
        val color = when(label) {
            "Serenity" -> 0x332E8B57.toInt()       // 20% Transparent Green
            "High_Stimulus" -> 0x338B0000.toInt()  // 20% Transparent Red
            "Geometric_Order" -> 0x334682B4.toInt()// 20% Transparent Blue
            else -> 0x00000000.toInt()             // Transparent
        }

        // Animate the color change (Smooth cross-fade)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), (overlayView.background as ColorDrawable).color, color)
        colorAnimation.duration = 500 // 500ms fade
        colorAnimation.addUpdateListener { animator ->
            overlayView.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimation.start()

        val nebulaColor = when(label) {
            "Serenity" -> 0xFF2E8B57.toInt()
            "High_Stimulus" -> 0xFF8B0000.toInt()
            "Geometric_Order" -> 0xFF00008B.toInt()
            "Danger" -> 0xFFFF4500.toInt()
            else -> Color.BLACK
        }
        starfieldView.setNebulaColor(nebulaColor)
    }

    private fun playMusic(label: String) {
        val soundID = when (label) {
            "Serenity" -> R.raw.calm
            "High_Stimulus" -> R.raw.brown_noise
            "Geometric_Order" -> R.raw.techno
            "Danger" -> R.raw.alarm
            else -> 0
        }

        if (soundID != 0) {
            if (primaryPlayer == null) {
                primaryPlayer = MediaPlayer.create(this, soundID).apply {
                    isLooping = true
                    setVolume(0f, 0f)
                    start()
                }
                fade(null, primaryPlayer)
            } else {
                secondaryPlayer = MediaPlayer.create(this, soundID).apply {
                    isLooping = true
                    setVolume(0f, 0f)
                    start()
                }
                fade(primaryPlayer, secondaryPlayer)
                primaryPlayer = secondaryPlayer
                secondaryPlayer = null
            }
            currentLabel = label
        }
    }

    private fun fade(fadeOut: MediaPlayer?, fadeIn: MediaPlayer?) {
        if (fadeOut != null) {
            ValueAnimator.ofFloat(1f, 0f).apply {
                duration = 500
                addUpdateListener { animator ->
                    val volume = animator.animatedValue as Float
                    if (!isSoundMuted) fadeOut.setVolume(volume, volume)
                }
                start()
            }.addUpdateListener { if(it.animatedFraction == 1.0f) fadeOut.release() }
        }

        if (fadeIn != null) {
            ValueAnimator.ofFloat(0f, 1f).apply {
                duration = 800
                addUpdateListener { animator ->
                    val volume = animator.animatedValue as Float
                    if (!isSoundMuted) fadeIn.setVolume(volume, volume)
                }
                start()
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("Synesthesia", "The Language specified is not supported!")
            } else {
                isTtsInitialized = true
            }
        } else {
            Log.e("Synesthesia", "TTS Initialization Failed!")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        primaryPlayer?.release()
        secondaryPlayer?.release()
        primaryPlayer = null
        secondaryPlayer = null
        cameraExecutor.shutdown()
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}