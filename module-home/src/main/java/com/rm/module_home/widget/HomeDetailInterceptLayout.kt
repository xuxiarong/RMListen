package com.rm.module_home.widget

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.AccelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.util.DLog
import com.rm.baselisten.utilExt.DisplayUtils.getStateHeight
import com.rm.baselisten.utilExt.screenHeight
import com.rm.business_lib.wedgit.shadow.ShadowLinearLayout
import com.rm.module_home.R
import kotlin.math.abs


/**
 *
 * @author yuanfang
 * @date 10/14/20
 * @description 书籍详情页滑动嵌套
 *
 */
class HomeDetailInterceptLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ShadowLinearLayout(context, attrs, defStyleAttr) {
    /**
     * 最高位置（往上滑动不能超过该位置）
     */
    private var mTopHeight: Int = 0

    /**
     * 中间位置
     */
    private var mCenterHeight: Int = 0


    /**
     * 最低位置（往下不能超过该位置）
     */
    private var mBottomHeight = 0

    /**
     * 当前停留的top
     */
    private var mCurType = TYPE_CENTER

    /**
     * 父容器 LayoutParams对象
     */
    private lateinit var params: ConstraintLayout.LayoutParams

    /**
     * 用来控制手指松开后滑动的方向
     */
    private var criticalPoint = 0

    /**
     * 记录手指的位置
     */
    private var mPrevMotionX = 0
    private var mPrevMotionY = 0

    /**
     * 是否能够上拉加载更多
     */
    private var canMoreData = true

    /**
     * 是否能够下拉加载
     */
    private var canRefreshData = true

    private lateinit var headerLayout: ConstraintLayout
    private lateinit var mRecyclerView: RecyclerView


    companion object {
        const val TYPE_TOP = 1
        const val TYPE_CENTER = 2
        const val TYPE_BOTTOM = 3
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mBottomHeight = headerLayout.height

        params = layoutParams as ConstraintLayout.LayoutParams
        criticalPoint = screenHeight / 2
        //根据实际情况计算
        mCenterHeight = resources.getDimensionPixelSize(R.dimen.dp_216) + getStateHeight(context)

        mTopHeight = resources.getDimensionPixelSize(R.dimen.dp_40) + getStateHeight(context)

        DLog.i("------------->", "$mTopHeight     $mCenterHeight    $screenHeight")
        params.height = screenHeight - mCenterHeight + getStateHeight(context)
        this.layoutParams = params
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        headerLayout = findViewById(R.id.home_detail_chapter_header)
        mRecyclerView = findViewById(R.id.detail_directory_recycler)
    }


    /**
     * 滑动事件拦截（决定由谁来消费此次的滑动事件）
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.rawX.toInt()
        val y = ev.rawY.toInt()
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mPrevMotionX = x
                mPrevMotionY = y
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = x - mPrevMotionX.toFloat()
                val dy = y - mPrevMotionY.toFloat()
                mPrevMotionX = x
                mPrevMotionY = y
                //横向滑动就不分发了
                if (abs(dx) > abs(dy)) {
                    return super.onInterceptTouchEvent(ev)
                }
                //滑动向上、向下
                return if (dy > 0) {
                    if (!isTopItem(-1) || canRefreshData) {
                        super.onInterceptTouchEvent(ev)
                    } else {
                        onTouchEvent(ev)
                    }
                } else {
                    if (canMoreData || !isTopItem(1)) {
                        super.onInterceptTouchEvent(ev)
                    } else {
                        onTouchEvent(ev)
                    }
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    fun setCanMoreData(hasMoreData: Boolean) {
        canMoreData = hasMoreData
    }

    fun setCanRefreshData(hasMoreData: Boolean) {
        canRefreshData = hasMoreData
    }

    /**
     * 判断recyclerView是否到达最顶端(-1   返回false表示不能往下滑动，即代表到顶部了)/低端(1  返回false表示不能往上滑动，即代表到底部了)
     */
    private fun isTopItem(direction: Int): Boolean {
        return !mRecyclerView.canScrollVertically(direction)
    }

    private var offsetY = 0
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var intercept = false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mPrevMotionY = event.rawY.toInt()
                intercept = true
            }
            MotionEvent.ACTION_MOVE -> {
                offsetY = event.rawY.toInt() - mPrevMotionY
                changeParams(offsetY)
                mPrevMotionY = event.rawY.toInt()
                intercept = true
            }
            MotionEvent.ACTION_UP -> {
                intercept = false
                moveEnd()
            }
        }
        return intercept
    }

    /**
     * 动态改变其位置（随手指）
     */
    private fun changeParams(offsetY: Int) {
        if (top + offsetY > mTopHeight && top + offsetY < bottom - mBottomHeight) {
            layout(0, top + offsetY, right, bottom)
        }
    }

    /**
     * 滑动结束
     */
    private fun moveEnd() {
        when (mCurType) {
            TYPE_TOP -> {
                if (offsetY > 0) {
                    mCurType = TYPE_CENTER
                    startScrollAnim(top, mCenterHeight)
                }
            }
            TYPE_CENTER -> {
                if (offsetY > 0) {
                    mCurType = TYPE_BOTTOM
                    startScrollAnim(top, bottom - mBottomHeight)
                } else {
                    mCurType = TYPE_TOP
                    startScrollAnim(top, mTopHeight)
                }
            }
            TYPE_BOTTOM -> {
                if (offsetY < 0) {
                    mCurType = TYPE_CENTER
                    startScrollAnim(top, mCenterHeight)
                }
            }
        }
    }

    private fun startScrollAnim(start: Int, end: Int) {
        val animator = ObjectAnimator.ofInt(start, end)
        animator.interpolator = AccelerateInterpolator()
        animator.duration = 100
        animator.addUpdateListener { valueAnimator: ValueAnimator ->
            val values = valueAnimator.animatedValue as Int
            layout(0, values, right, bottom)
        }
        animator.start()
    }

}

@BindingAdapter("bindCanLoadMore", "bindCanRefresh", requireAll = false)
fun HomeDetailInterceptLayout.bindCanLoadMore(
    canLoadMore: Boolean? = false,
    canRefresh: Boolean? = false
) {
    setCanMoreData(canLoadMore ?: true)
    setCanRefreshData(canRefresh ?: true)
}