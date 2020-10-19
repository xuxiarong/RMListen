package com.rm.module_home.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.Scroller
import androidx.constraintlayout.widget.ConstraintLayout
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.util.DLog
import com.rm.module_home.R

/**
 *
 * @author yuanfang
 * @date 10/14/20
 * @description
 *
 */
class HomeDetailInterceptLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var startY = 0
    private lateinit var params: LayoutParams
    private var mTopHeight: Int = CONTEXT.resources.getDimensionPixelSize(R.dimen.dp_100)
    private val mScroller: Scroller = Scroller(context, BounceInterpolator())


    override fun onTouchEvent(event: MotionEvent): Boolean {
        var intercept = false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startY = event.rawY.toInt()
                params = this.layoutParams as LayoutParams
                intercept = true
            }
            MotionEvent.ACTION_MOVE -> {
                val offsetY = (event.rawY - startY).toInt()
                changeParams(offsetY)
                startY = event.rawY.toInt()
                intercept = true
            }
            MotionEvent.ACTION_UP -> {
                intercept = false
                DLog.i("------->", "ACTION_UP  ${event.rawY}   ${params.topMargin}")
                if (params.topMargin > 600) {
                    startScrollTop()
                }
//                else {
//                    startScrollBottom()
//                }
            }
        }
        return intercept
    }

    private fun changeParams(offsetY: Int) {
        if (offsetY < 0) {
            if (params.topMargin + offsetY >= 0) {
                params.topMargin = params.topMargin + offsetY
                params.bottomMargin = params.bottomMargin - offsetY
                this.layoutParams = params
                DLog.i("------->", "changeParams     ${params.topMargin}")
            }
        } else {
            if (height > mTopHeight) {
                params.topMargin = params.topMargin + offsetY
                params.bottomMargin = params.bottomMargin - offsetY
                this.layoutParams = params
                DLog.i("------->", "changeParams  ---->     ${params.topMargin}")
            }
        }
    }

    private fun startScrollTop() {
        params.topToTop
        val animator = ObjectAnimator.ofInt()
        animator.addUpdateListener {
            val values = it.animatedValue as Int
            changeParams(values)
        }
        animator.interpolator = AccelerateInterpolator()
        animator.duration = 300
        animator.start()
    }

    private fun startScrollBottom() {

        val i = height - mTopHeight - params.topMargin
        val animator = ObjectAnimator.ofInt(i)
        animator.addUpdateListener {
            val values = it.animatedValue as Int
            changeParams(-values)
        }
        animator.interpolator = AccelerateInterpolator()
        animator.duration = 300
        animator.start()
    }

}