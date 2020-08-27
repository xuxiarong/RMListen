package com.rm.business_lib.wedgit

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.rm.business_lib.R

/**
 * 统一虚线
 */
class DashLineView(
    context: Context?,
    attrs: AttributeSet?
) : View(context, attrs) {
    private val mPaint: Paint
    private val mPath: Path
    override fun onDraw(canvas: Canvas) {
        val centerY = height / 2
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        canvas.drawLine(0f, centerY.toFloat(), width.toFloat(), centerY.toFloat(), mPaint)
    }

    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = resources.getColor(R.color.stroke_color)
        // 需要加上这句，否则画不出东西
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 1f
        mPaint.pathEffect = DashPathEffect(floatArrayOf(15f, 5f), 0F)
        mPath = Path()
    }
}