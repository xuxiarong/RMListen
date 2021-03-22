package com.rm.module_home.widget

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.annotation.IntDef
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.utilExt.dip
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
) : ConstraintLayout(context, attrs, defStyleAttr) {
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

    /**
     * 滚动到顶部监听
     */
    private var changeTypeListener: ScrollChangeTypeListener? = null

    private lateinit var headerLayout: ConstraintLayout

    //章节RecyclerView
    private lateinit var mRecyclerView: RecyclerView

    //章节分页RecyclerView
    private lateinit var mRecyclerView1: RecyclerView


    companion object {
        const val TYPE_TOP = 1
        const val TYPE_CENTER = 2
        const val TYPE_BOTTOM = 3
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mBottomHeight = headerLayout.height + dip(20)
        //根据实际情况计算
        mCenterHeight = resources.getDimensionPixelSize(R.dimen.dp_160)

        mTopHeight = -55

        if (mCurType == TYPE_CENTER) {
            translationY = mCenterHeight.toFloat()
        }
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        headerLayout = findViewById(R.id.home_detail_chapter_header)
        mRecyclerView = findViewById(R.id.home_detail_chapter_rv)
        mRecyclerView1 = findViewById(R.id.detail_anthology_recycler)
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
                downY = ev.rawY.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = x - mPrevMotionX.toFloat()
                val dy = y - mPrevMotionY.toFloat()
                mPrevMotionX = x
                mPrevMotionY = y
                //横向滑动就不分发了
                if (abs(dx) > abs(dy) || abs(dy) < 10) {
                    return super.onInterceptTouchEvent(ev)
                }

                //滑动向上、向下
                return if (chapterPageRvIsVisible()) {
                    interceptChapterPageRv(dy, ev)
                } else {
                    interceptChapterRv(dy, ev)
                }

            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    /**
     * 对章节Rv进行拦截
     */
    private fun interceptChapterRv(dy: Float, ev: MotionEvent): Boolean {
        return if (dy > 0) {
            if (!isTopItem(-1, mRecyclerView) || canRefreshData) {
                super.onInterceptTouchEvent(ev)
            } else {
                onTouchEvent(ev)
            }
        } else {
            if (canMoreData || !isTopItem(1, mRecyclerView)) {
                super.onInterceptTouchEvent(ev)
            } else {
                if (mCurType == TYPE_TOP) {
                    super.onInterceptTouchEvent(ev)
                } else {
                    onTouchEvent(ev)
                }
            }
        }
    }

    /**
     * 对章节分页进行拦截
     */
    private fun interceptChapterPageRv(dy: Float, ev: MotionEvent): Boolean {
        return if (dy > 0) {
            if (!isTopItem(-1, mRecyclerView1)) {
                super.onInterceptTouchEvent(ev)
            } else {
                onTouchEvent(ev)
            }
        } else {
            if (!isTopItem(1, mRecyclerView1)) {
                super.onInterceptTouchEvent(ev)
            } else {
                onTouchEvent(ev)
            }
        }
    }

    fun setCanMoreData(hasMoreData: Boolean) {
        canMoreData = hasMoreData
    }

    fun setCanRefreshData(hasMoreData: Boolean) {
        canRefreshData = hasMoreData
    }

    /**
     * 判断章节分页列表是否现实
     */
    private fun chapterPageRvIsVisible(): Boolean {
        return mRecyclerView1.visibility == View.VISIBLE
    }

    /**
     * 判断recyclerView是否到达最顶端(-1   返回false表示不能往下滑动，即代表到顶部了)/低端(1  返回false表示不能往上滑动，即代表到底部了)
     */
    private fun isTopItem(direction: Int, recyclerView: RecyclerView): Boolean {
        return !recyclerView.canScrollVertically(direction)
    }

    private var downY = 0
    private var upY = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var intercept = false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mPrevMotionY = event.rawY.toInt()
                downY = event.rawY.toInt()
                intercept = true
            }
            MotionEvent.ACTION_MOVE -> {
                val offsetY = event.rawY.toInt() - mPrevMotionY
                changeParams(offsetY)
                mPrevMotionY = event.rawY.toInt()
                intercept = true
            }
            MotionEvent.ACTION_UP -> {
                intercept = false
                upY = event.rawY.toInt()
                moveEnd()
            }
            MotionEvent.ACTION_CANCEL -> {
                startScrollAnim(top, mCenterHeight)
            }
        }
        return intercept
    }

    /**
     * 动态改变其位置（随手指）
     */
    private fun changeParams(offsetY: Int) {
        if (translationY + offsetY > mTopHeight && translationY + offsetY < height - mBottomHeight) {
            translationY += offsetY
        }
    }

    /**
     * 滑动结束
     */
    private fun moveEnd() {
        val offsetY = upY - downY
        //如果滑动不超过10像素就不滑动
        if (abs(offsetY) < 10) {
            return
        }
        when (mCurType) {
            TYPE_TOP -> {
                if (offsetY >= 0) {
                    mCurType = TYPE_CENTER
                    startScrollAnim(translationY.toInt(), mCenterHeight)
                }
            }
            TYPE_CENTER -> {
                if (offsetY >= 0) {
                    mCurType = TYPE_BOTTOM
                    startScrollAnim(translationY.toInt(), height - mBottomHeight)
                } else {
                    // 615.0
                    mCurType = TYPE_TOP
                    startScrollAnim(translationY.toInt(), mTopHeight)
                }
            }
            TYPE_BOTTOM -> {
                if (offsetY <= 0) {
                    mCurType = TYPE_CENTER
                    startScrollAnim(translationY.toInt(), mCenterHeight)
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
            translationY = values.toFloat()
        }
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                changeTypeListener?.changeType(mCurType)
            }

            override fun onAnimationCancel(animation: Animator?) {
                changeTypeListener?.changeType(mCurType)
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })
        animator.start()
    }

    fun setScrollChangeTypeListener(changeTypeListener: ScrollChangeTypeListener) {
        this.changeTypeListener = changeTypeListener
    }

    interface ScrollChangeTypeListener {
        fun changeType(@HomeDetailInterceptChangeType nowType: Int)
    }

    @IntDef(TYPE_TOP, TYPE_CENTER, TYPE_BOTTOM)
    annotation class HomeDetailInterceptChangeType(val type: Int = TYPE_CENTER)

}

@BindingAdapter("bindCanLoadMore", "bindCanRefresh", requireAll = false)
fun HomeDetailInterceptLayout.bindCanLoadMore(
    canLoadMore: Boolean? = false,
    canRefresh: Boolean? = false
) {
    setCanMoreData(canLoadMore ?: true)
    setCanRefreshData(canRefresh ?: true)
}