package com.example.test_camera

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.LinearGradient
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class ScanningLineView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val linePaint = Paint()
    private var lineY = 0f

    init {
        linePaint.strokeWidth = 4f

        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 3000 // 3 seconds for a slow scan
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
        }

        animator.addUpdateListener { animation ->
            lineY = height * animation.animatedValue as Float
            invalidate()
        }
        animator.start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Create a gradient for the glowing effect
        linePaint.shader = LinearGradient(
            0f, 0f, w.toFloat(), 0f,
            Color.TRANSPARENT,
            Color.CYAN,
            Shader.TileMode.MIRROR
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(0f, lineY, width.toFloat(), lineY, linePaint)
    }
}