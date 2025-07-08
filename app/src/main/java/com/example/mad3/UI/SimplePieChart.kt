package com.example.mad3.UI

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes

class SimplePieChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val rect = RectF()
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 36f
        textAlign = Paint.Align.CENTER
    }
    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 12f
        textAlign = Paint.Align.CENTER
    }

    private val defaultColors = listOf(
        Color.parseColor("#FFA726"),  // Orange
        Color.parseColor("#66BB6A"),  // Green
        Color.parseColor("#29B6F6"),  // Blue
        Color.parseColor("#EF5350"),  // Red
        Color.parseColor("#AB47BC")   // Purple
    )

    private var colors: List<Int> = defaultColors
    private var data: List<Pair<Float, String>> = emptyList()

    fun setData(newData: List<Pair<Float, String>>) {
        data = newData
        invalidate()
    }

    fun setColors(newColors: List<Int>) {
        colors = newColors
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (data.isEmpty()) return

        val total = data.sumOf { it.first.toDouble() }.toFloat()
        if (total == 0f) return

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (Math.min(width, height) / 2f) * 0.8f

        rect.set(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius
        )

        var startAngle = 0f

        // Draw pie segments
        for ((index, pair) in data.withIndex()) {
            val value = pair.first
            val sweepAngle = value / total * 360f

            paint.color = colors.getOrElse(index % colors.size) { defaultColors[index % defaultColors.size] }
            canvas.drawArc(rect, startAngle, sweepAngle, true, paint)

            // Draw label at the middle of each segment
            val angle = startAngle + sweepAngle / 2
            val labelRadius = radius * 0.6f
            val x = centerX + labelRadius * Math.cos(Math.toRadians(angle.toDouble())).toFloat()
            val y = centerY + labelRadius * Math.sin(Math.toRadians(angle.toDouble())).toFloat()

            canvas.drawText(pair.second, x, y, labelPaint)

            startAngle += sweepAngle
        }

        // Draw center text with total
        val totalText = "LKR ${"%.2f".format(total)}"
        canvas.drawText(totalText, centerX, centerY, textPaint)
    }
}