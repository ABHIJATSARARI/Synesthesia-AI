package com.example.test_camera

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class IntroActivity : AppCompatActivity() {

    private var currentStep = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val btnNext = findViewById<Button>(R.id.btn_next)
        val title = findViewById<TextView>(R.id.intro_title)
        val desc = findViewById<TextView>(R.id.intro_desc)
        val icon = findViewById<TextView>(R.id.intro_icon)
        val dot1 = findViewById<View>(R.id.dot1)
        val dot2 = findViewById<View>(R.id.dot2)
        val dot3 = findViewById<View>(R.id.dot3)

        btnNext.setOnClickListener {
            currentStep++
            
            // FADE ANIMATION
            title.alpha = 0f
            desc.alpha = 0f
            icon.alpha = 0f
            title.animate().alpha(1f).duration = 500
            desc.animate().alpha(1f).duration = 500
            icon.animate().alpha(1f).duration = 500

            when (currentStep) {
                2 -> {
                    icon.text = "👁️ ➔ 🎵"
                    title.text = "Visual to Audio"
                    desc.text = "Our neural network translates visual chaos into calming audio frequencies in real-time."
                    dot1.setBackgroundColor(Color.GRAY)
                    dot2.setBackgroundColor(Color.WHITE)
                }
                3 -> {
                    icon.text = "🎧"
                    title.text = "Deep Focus"
                    desc.text = "Use 'Serenity' mode for relaxation or 'Order' mode for deep work. Offline. Secure."
                    btnNext.text = "START EXPERIENCE"
                    btnNext.setBackgroundColor(Color.parseColor("#2E8B57")) // Green
                    dot2.setBackgroundColor(Color.GRAY)
                    dot3.setBackgroundColor(Color.WHITE)
                }
                else -> {
                    // Launch Main App
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Close intro so user can't go back
                }
            }
        }
    }
}