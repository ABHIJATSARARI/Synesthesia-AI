package com.example.test_camera

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class StarfieldView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val stars = mutableListOf<Star>()
    private val starPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val nebulaPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var currentNebulaColor = Color.TRANSPARENT
    private var colorAnimator: ValueAnimator? = null

    init {
        // It's better to initialize stars in onSizeChanged, as width/height are 0 here
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (stars.isEmpty()) {
            for (i in 0..200) {
                stars.add(Star(Random.nextInt(w), Random.nextInt(h), Random.nextFloat() * 2f + 1f))
            }
        }
    }

    fun setNebulaColor(toColor: Int) {
        colorAnimator?.cancel()
        colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), currentNebulaColor, toColor).apply {
            duration = 1500 // 1.5 seconds for a slow, ambient fade
            addUpdateListener { animator ->
                currentNebulaColor = animator.animatedValue as Int
            }
            start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.BLACK)

        // 1. Draw Nebula
        if (currentNebulaColor != Color.TRANSPARENT) {
            nebulaPaint.shader = RadialGradient(
                width / 2f,
                height / 2f,
                height / 1.5f, // Make the nebula large
                currentNebulaColor,
                Color.TRANSPARENT,
                Shader.TileMode.CLAMP
            )
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), nebulaPaint)
        }

        // 2. Draw Stars
        for (star in stars) {
            starPaint.color = Color.WHITE
            starPaint.alpha = star.alpha
            canvas.drawCircle(star.x.toFloat(), star.y.toFloat(), star.size, starPaint)

            // Animate the star's position
            star.y += star.speed.toInt()
            if (star.y > height) {
                star.y = 0
                star.x = Random.nextInt(width)
            }
        }

        // Redraw the view on the next frame
        invalidate()
    }

    data class Star(var x: Int, var y: Int, var size: Float, var alpha: Int = Random.nextInt(155) + 100, val speed: Float = Random.nextFloat() * 3f + 1f)
}