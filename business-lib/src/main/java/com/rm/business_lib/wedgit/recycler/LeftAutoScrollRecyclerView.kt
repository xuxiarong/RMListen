package com.rm.business_lib.wedgit.recycler

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.util.DLog
import com.rm.baselisten.utilExt.DisplayUtils
import com.rm.baselisten.utilExt.dip
import com.rm.business_lib.R
import me.jessyan.autosize.utils.ScreenUtils

/**
 * desc   :
 * date   : 2020/09/07
 * version: 1.0
 */
class LeftAutoScrollRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    var isSlidingToLeft = false
    var firstScrollLast = false
    var isShow = false
    var blockName = ""
    var lastOpenTime = System.currentTimeMillis()


    var currentDragX = 100F
    var dragHeight = 0F
    var dragWidth = 0F
    var needDraw = false

    private val mPaint: Paint by lazy {
        Paint()
    }

    private val screenWidth by lazy {
        ScreenUtils.getScreenSize(context)[0].toFloat()
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

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        DLog.d("suolong","blockName = $blockName onDetachedFromWindow")
        isShow = false
        currentDragX = 0F
        needDraw = false
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        isShow = true
        DLog.d("suolong","blockName = $blockName onAttachedToWindow")
        val manager = layoutManager as LinearLayoutManager
        val lastItemPosition = manager.findLastCompletelyVisibleItemPosition()
        val itemCount = manager.itemCount
        firstScrollLast = (lastItemPosition == itemCount-1)
    }

//    @SuppressLint("DrawAllocation")
//    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
//        if(needDraw){
//            val rect = RectF(screenWidth-currentDragX,0F,screenWidth,dip(176).toFloat())
//            mPaint.color = ContextCompat.getColor(context, R.color.base_ff5e5e)
//            mPaint.isAntiAlias = true
//            mPaint.style = Paint.Style.FILL
//            canvas?.drawArc(rect,90F,180F,false,mPaint)
////            canvas?.drawCircle(left + (right - left)/2f,top + (bottom - top)/2f,measuredHeight/2f,mPaint)
//            DLog.d("suolong_draw","绘制圆弧 currentDragX = $currentDragX   rect : left = ${rect.left}   top = ${rect.top}  right = ${rect.right}  bottom = ${rect.bottom}")
//        }
//
//    }

//    fun showShowArc(dx : Int){
//        currentDragX += dx.toFloat()
//        if(currentDragX <= 0F){
//            currentDragX = 0F
//        }
//        if(currentDragX>=300F){
//            currentDragX = 300F
//        }
//
//        needDraw = currentDragX >0
//        if(needDraw){
//            invalidate()
//            DLog.d("suolong_draw","开始绘制 dx = $dx currentDragX = $currentDragX")
//        }
//    }
//
//    fun setCantScroll(){
//        needDraw = false
//        DLog.d("suolong_draw","停止绘制")
//    }

}