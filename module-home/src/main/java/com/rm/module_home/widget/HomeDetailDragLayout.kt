package com.rm.module_home.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.customview.widget.ViewDragHelper
import androidx.recyclerview.widget.RecyclerView
import com.rm.module_home.R

/**
 *
 * @author yuanfang
 * @date 10/13/20
 * @description
 *
 */
class HomeDetailDragLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var viewDragHelper: ViewDragHelper
    private lateinit var mDragCallback: HomeDetailDragViewCallback
    private lateinit var dragCl: HomeDetailInterceptLayout
    private lateinit var dragRv: RecyclerView


    override fun onFinishInflate() {
        super.onFinishInflate()
        mDragCallback = HomeDetailDragViewCallback(this)
        viewDragHelper = ViewDragHelper.create(this, 1.0f, mDragCallback)
        dragCl = findViewById(R.id.home_detail_draggable_cl)
        dragRv = findViewById(R.id.home_detail_draggable_rv)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (viewDragHelper.continueSettling(true)) {
            invalidate()
        }
    }

    private var mDownY: Int = 0

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {

        //recyclerView是否滑动到顶部
        val isScrollTop = !dragRv.canScrollVertically(-1)

        when (ev.action) {
            MotionEvent.ACTION_MOVE -> {
                //判断滑动的方向是否是往上滑动
                val dDownY = ev.y - mDownY


            }
        }
        mDownY = ev.y.toInt()

        return viewDragHelper.shouldInterceptTouchEvent(ev)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        viewDragHelper.processTouchEvent(event)
        return true
    }


    fun onVerticalDrag(child: View, yVel: Float) {
        if (viewDragHelper.smoothSlideViewTo(child, 0, yVel.toInt())) {
            invalidate()
        }
    }


}