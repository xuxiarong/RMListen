package com.rm.module_play.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.rm.module_play.R

/**
 *
 * @des:
 * @data: 9/2/20 10:30 AM
 * @Version: 1.0.0
 */
class MusicCustomTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyle: Int = 0
) : AppCompatTextView(context, attrs, defStyle) {
    private var paint1: Paint? = null
    private var paint2: Paint? = null
    private var mWidth = 0f
    private var gradient: LinearGradient? = null
    private val mMatrix by lazy {
        Matrix()
    }
    //渐变的速度
    private var deltaX = 0
    init {
        paint1 = Paint()
        paint1?.color = ContextCompat.getColor(context, R.color.holo_blue_dark)
        paint1?.style = Paint.Style.FILL
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (mWidth == 0f) {
            mWidth = getMeasuredWidth().toFloat()
            paint2 = getPaint()
            //颜色渐变器
            gradient = LinearGradient(
                0f,
                0f,
                mWidth,
                0f,
                intArrayOf(
                    Color.GRAY,
                    Color.WHITE,
                    Color.GRAY
                ),
                floatArrayOf(
                    0.3f, 0.5f, 1.0f
                ),
                Shader.TileMode.CLAMP
            )
            paint2?.shader = gradient
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //关键代码通过矩阵的平移实现
        mMatrix.setTranslate(deltaX.toFloat(), 0f)
        gradient?.setLocalMatrix(mMatrix)
        postInvalidateDelayed(100)
    }

    fun onDestroy() {
        gradient = null
        paint1 = null
        paint2 = null
    }
}