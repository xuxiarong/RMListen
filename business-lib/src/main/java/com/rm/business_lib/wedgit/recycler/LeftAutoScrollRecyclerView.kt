package com.rm.business_lib.wedgit.recycler

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.util.DLog
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

    var  downX = 0F
    var  downY = 0F

    var controlX = 0F
    var preControlX = ScreenUtils.getScreenSize(context)[0].toFloat()
    /**
     * 第一个的位置
     */
    /**
     * 最后一个可见的item的位置
     */
    var lastVisibleItemPosition = 0
    /**
     * 最后一个的位置
     */


    private val paint: Paint by lazy {
        Paint()
    }

    private val path : Path by lazy {
        Path()
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
//            val rect = RectF(screenWidth-currentDragX,0F,screenWidth+200,dip(176).toFloat())
//            paint.color = ContextCompat.getColor(context, R.color.base_ff5e5e)
//            paint.isAntiAlias = true
//            paint.style = Paint.Style.FILL
//            canvas?.drawArc(rect,90F,180F,false,paint)
////            canvas?.drawCircle(left + (right - left)/2f,top + (bottom - top)/2f,measuredHeight/2f,mPaint)
//            DLog.d("suolong_draw","绘制圆弧 currentDragX = $currentDragX   rect : left = ${rect.left}   top = ${rect.top}  right = ${rect.right}  bottom = ${rect.bottom}")
//        }
//
//    }

    fun showShowArc(dx : Int){
        currentDragX += dx.toFloat()
        if(currentDragX <= 0F){
            currentDragX = 0F
        }
        if(currentDragX>=300F){
            currentDragX = 300F
        }

        needDraw = currentDragX >0
        if(needDraw){
            invalidate()
            DLog.d("suolong_draw","开始绘制 dx = $dx currentDragX = $currentDragX")
        }
    }

    fun setCantScroll(){
        needDraw = false
        DLog.d("suolong_draw","停止绘制")
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        if(controlX!=0F){
            path.reset()
            paint.color = ContextCompat.getColor(context, R.color.base_ff5e5e)
            paint.isAntiAlias = true
            paint.style = Paint.Style.FILL
            path.moveTo(screenWidth,0F)
            path.quadTo(controlX,dip(88).toFloat(),screenWidth,dip(176).toFloat())
            canvas?.save()
            canvas?.drawPath(path,paint)
            canvas?.restore()
            DLog.d("suolong", "controlX = $controlX")
        }
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val layoutManager = layoutManager
        if (layoutManager != null) {
            if (layoutManager is GridLayoutManager) {
                lastVisibleItemPosition =
                    layoutManager.findLastVisibleItemPosition()
            } else if (layoutManager is LinearLayoutManager) {
                lastVisibleItemPosition =
                    layoutManager.findLastVisibleItemPosition()
            }
        } else {
            throw RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager")
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
                //如果滑动到了最底部，就允许继续向上滑动加载下一页，否者不允许
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val dx: Float = ev.x - downX
                val dy: Float = ev.y - downY
                val allowParentTouchEvent: Boolean
                allowParentTouchEvent = if (Math.abs(dx) > Math.abs(dy)) {
                    if (dx < 0) {
                        val visibleItemCount = layoutManager.childCount
                        val totalItemCount = layoutManager.itemCount
                        val b = visibleItemCount > 0
                        val b1: Boolean = lastVisibleItemPosition >= totalItemCount - 1
                        val b2 =
                            getChildAt(childCount - 1).right <= width
                        b && b1 && b2
                    } else {
                        true
                    }
                } else {
                    true
                }
                if(allowParentTouchEvent){
                    controlX = preControlX + dx
                    invalidate()
                }

                parent.requestDisallowInterceptTouchEvent(!allowParentTouchEvent)
            }
        }
        return super.dispatchTouchEvent(ev)
    }


}