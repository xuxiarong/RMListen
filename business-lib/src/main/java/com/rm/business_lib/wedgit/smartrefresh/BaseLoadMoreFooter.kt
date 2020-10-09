package com.rm.business_lib.wedgit.smartrefresh

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.rm.business_lib.R
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle

/**
 * desc   :
 * author : lm162_SuoLong
 * date   : 2020/07/10
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
class BaseLoadMoreFooter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs, 0), RefreshFooter {
    //默认上拉加载完成后，不延时消失进度条
    private val DEFAULT_DELAY_LOAD_FINISH_TIME = 0
    private fun init(context: Context) {
        View.inflate(context, R.layout.business_layout_load_more_footer, this)
//        mLottieAnim = findViewById(R.id.common_load_more_footer_lv)
//        if (ThemeHelper.queryCurrentStyle(context) === ThemeHelper.ThemeStyle.dark) {
//            mLottieAnim.setAnimation("common_pull_refresh_night.json")
//        } else {
//            mLottieAnim.setAnimation("common_pull_refresh.json")
//        }
    }

    override fun getView(): View {
        return this
    }

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate
    }

    override fun setPrimaryColors(vararg colors: Int) {}
    override fun onInitialized(
        kernel: RefreshKernel,
        height: Int,
        maxDragHeight: Int
    ) {
    }

    override fun onMoving(
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        height: Int,
        maxDragHeight: Int
    ) {
    }

    override fun onReleased(
        refreshLayout: RefreshLayout,
        height: Int,
        maxDragHeight: Int
    ) {
    }

    override fun onStartAnimator(
        refreshLayout: RefreshLayout,
        height: Int,
        maxDragHeight: Int
    ) {
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
//        if (mLottieAnim != null && mLottieAnim!!.isAnimating) {
//            postDelayed(
//                { mLottieAnim!!.cancelAnimation() },
//                DEFAULT_DELAY_LOAD_FINISH_TIME.toLong()
//            )
//        }
        return DEFAULT_DELAY_LOAD_FINISH_TIME
    }

    override fun onHorizontalDrag(
        percentX: Float,
        offsetX: Int,
        offsetMax: Int
    ) {
    }

    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
//        if (mLottieAnim != null && !mLottieAnim!!.isAnimating) {
//            mLottieAnim!!.playAnimation()
//        }
    }

    override fun setNoMoreData(noMoreData: Boolean): Boolean {
        return true
    }

    init {
        init(context)
    }
}