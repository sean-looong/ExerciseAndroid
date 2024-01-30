package com.seanlooong.exerciseandroid.modules.camera.ui

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable
import kotlin.math.min

class FocusPointDrawable : Drawable() {
    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        color = Color.WHITE
    }

    private var radius: Float = 0f
    private var centerX: Float = 0f
    private var centerY: Float = 0f

    fun setStrokeWidth(strokeWidth: Float): Boolean =
        if (paint.strokeWidth == strokeWidth) {
            false
        } else {
            paint.strokeWidth = strokeWidth
            true
        }

    override fun onBoundsChange(bounds: Rect) {
        val width = bounds.width()
        val height = bounds.height()
        radius = min(width, height) / 2f - paint.strokeWidth / 2f
        centerX = width / 2f
        centerY = height / 2f
    }

    override fun draw(canvas: Canvas) {
        if (radius == 0f) return

        canvas.drawCircle(centerX, centerY, radius, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
}