package com.rm.module_home.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.util.DLog
import com.rm.baselisten.utilExt.dip
import com.rm.module_home.R

/**
 *
 * @author yuanfang
 * @date 10/14/20
 * @description
 *
 */
class DragArcView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    var currentDragX = 0F
    var dragHeight = 0F
    var dragWidth = 0F
    var needDraw = false


    private val mPaint: Paint by lazy {
        Paint()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if(dragHeight == 0F){
            dragHeight = measuredHeight.toFloat()
        }
        if(dragWidth == 0F){
            dragWidth = measuredWidth.toFloat()
        }
    }




    @SuppressLint("DrawAllocation", "ResourceAsColor")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(needDraw){
            val rect = RectF(this.right-currentDragX,this.top.toFloat(),this.right.toFloat(),this.bottom.toFloat())
            mPaint.color = ContextCompat.getColor(context, com.rm.business_lib.R.color.base_ff5e5e)
            mPaint.isAntiAlias = true
            mPaint.style = Paint.Style.FILL
            canvas?.drawArc(rect,90F,180F,false,mPaint)
//            canvas?.drawCircle(left + (right - left)/2f,top + (bottom - top)/2f,measuredHeight/2f,mPaint)
            DLog.d("suolong_draw","绘制圆弧 currentDragX = $currentDragX   rect : left = ${rect.left}   top = ${rect.top}  right = ${rect.right}  bottom = ${rect.bottom}")
        }

    }


    fun showShowArc(dx : Int){
        currentDragX += dx.toFloat()
        if(currentDragX <= 0F){
            currentDragX = 0F
        }
        if(currentDragX>=150F){
            currentDragX = 150F
        }

        needDraw = currentDragX >0
        if(needDraw){
            invalidate()
            DLog.d("suolong_draw","开始绘制 dx = $dx")
        }
    }

    fun setCantScroll(){
        needDraw = false
        currentDragX = 0F
        DLog.d("suolong_draw","停止绘制")
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        (parent as RecyclerView).addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //不能向右滑动了
                if(canScrollHorizontally(-1)){
                    showShowArc(dx)
                }else{
                    setCantScroll()
                }
            }
        })
    }
}