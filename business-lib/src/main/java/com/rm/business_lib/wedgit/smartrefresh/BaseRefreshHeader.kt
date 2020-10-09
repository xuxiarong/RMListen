package com.rm.business_lib.wedgit.smartrefresh

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.rm.business_lib.R
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import kotlinx.android.synthetic.main.business_layout_refrsh_header.view.*

/**
 * desc   :
 * author : lm162_SuoLong
 * date   : 2020/07/10
 * version: 1.0
 * 版权所有:雷漫网络科技
 */
class BaseRefreshHeader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs, 0), RefreshHeader {
    //默认下拉刷新完成后，不延时消失进度条
    private val DEFAULT_DELAY_LOAD_REFRESH_TIME = 0
    private fun init(context: Context) {
        View.inflate(context, R.layout.business_layout_refrsh_header, this);
//        mLottieAnim = findViewById(R.id.common_refresh_header_lv);
//        if(ThemeHelper.queryCurrentStyle(context) == ThemeHelper.ThemeStyle.dark){
//            mLottieAnim.setAnimation("common_pull_refresh_night.json");
//        }else {
//            mLottieAnim.setAnimation("common_pull_refresh.json");
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
        if (businessPullRefresh != null && businessPullRefresh.isAnimating) {
            postDelayed(
                { businessPullRefresh.cancelAnimation() },
                DEFAULT_DELAY_LOAD_REFRESH_TIME.toLong()
            )
        }
        return DEFAULT_DELAY_LOAD_REFRESH_TIME
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
        if (businessPullRefresh != null && !businessPullRefresh.isAnimating) {
            businessPullRefresh.playAnimation()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        businessPullRefresh?.cancelAnimation()
    }

    init {
        init(context)
    }
}