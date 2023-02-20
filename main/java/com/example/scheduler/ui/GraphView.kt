package com.example.scheduler.ui

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.scheduler.model.DayWithPoints

class GraphView @JvmOverloads constructor(
    context: Context, attributes: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attributes, defStyleAttr) {

    private var listDayWithPoints = mutableListOf<DayWithPoints>()
    private val axisXLength = (Resources.getSystem().displayMetrics.widthPixels - 160).toFloat()
    private val axisYLength = (Resources.getSystem().displayMetrics.heightPixels - 320).toFloat()
    private val xStrokePadding = axisXLength / 7
    private val yStrokePadding = axisYLength / 10
    private val strokeHalfLength = 10f

    private val axisPaint = Paint().apply {
        color = Color.RED
        strokeWidth = 10f
    }
    private val yStrokePaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 3f
        style = Paint.Style.STROKE
    }
    private val xStrokePaint = Paint().apply {
        color = Color.RED
        strokeWidth = 3f
    }
    private val dataPointPaint = Paint().apply { color = Color.BLUE }
    private val textPaint = Paint(Paint.LINEAR_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 30f
    }
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GREEN
        strokeWidth = 7f
    }

    fun setData(list: List<DayWithPoints>) {
        listDayWithPoints.clear()
        listDayWithPoints.addAll(list)
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawLine(80f, 100f + axisYLength, 80f + axisXLength, 100f + axisYLength,
        axisPaint)
        canvas?.drawLine(80f, 100f, 80f, 100f + axisYLength, axisPaint)
        var yValue = 100f + axisYLength
        for (point in 2..20 step 2) {
            yValue -= yStrokePadding
            canvas?.drawLine(80f - strokeHalfLength, yValue, 80f + axisXLength, yValue,
            yStrokePaint)
            canvas?.drawText(point.toString(), 20f, yValue + 16f, textPaint)
        }
        var xValue = 80f
        listDayWithPoints.forEachIndexed { index, day ->
            canvas?.drawLine(xValue, 100f + axisYLength - strokeHalfLength,
            xValue, 100f + axisYLength + strokeHalfLength, xStrokePaint)
            canvas?.drawText(day.date, xValue - 30f, 100f + axisYLength + 46f, textPaint)
            if (index < listDayWithPoints.size - 1) {
                val nextDay = listDayWithPoints[index + 1]
                canvas?.drawLine(xValue, 100f + axisYLength - day.points * yStrokePadding / 2,
                xValue + xStrokePadding, 100f + axisYLength - nextDay.points * yStrokePadding / 2,
                linePaint)
            }
            canvas?.drawCircle(xValue, 100f + axisYLength - day.points * yStrokePadding / 2,
            12f, dataPointPaint)
            xValue += xStrokePadding
        }
    }
}