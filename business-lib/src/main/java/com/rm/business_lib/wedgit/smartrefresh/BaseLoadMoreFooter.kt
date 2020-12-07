package com.rm.business_lib.wedgit.smartrefresh

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.util.DLog
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

    private lateinit var mTitleText: TextView

    /**
     * 内容是否满屏
     */
    private var recyclerView: RecyclerView? = null
    private lateinit var listener: ViewTreeObserver.OnGlobalLayoutListener
    private fun init(context: Context) {
        View.inflate(context, R.layout.business_layout_load_more_footer, this)
        mTitleText = findViewById(R.id.business_load_footer)

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

    fun bindRecyclerView(rv: RecyclerView) {
        this.recyclerView = rv
    }

    override fun setNoMoreData(noMoreData: Boolean): Boolean {
        canShowFooter(noMoreData)
        return true
    }

    private fun canShowFooter(noMoreData: Boolean) {
        if (noMoreData && recyclerView != null) {
            if (recyclerView!!.layoutManager != null && recyclerView!!.adapter != null)
                if (recyclerView!!.layoutManager is LinearLayoutManager) {
                    listener = ViewTreeObserver.OnGlobalLayoutListener {
                        val manager = recyclerView!!.layoutManager as LinearLayoutManager
                        val visibilityPosition = manager.findLastCompletelyVisibleItemPosition()
                        //如果屏幕被填满则进行现实尾部
                        val b = visibilityPosition < recyclerView!!.adapter!!.itemCount - 1
                        if (b) {
                            mTitleText.visibility = View.VISIBLE
                            recyclerView!!.viewTreeObserver.removeOnGlobalLayoutListener(listener)
                        } else {
                            mTitleText.visibility = View.GONE
                        }
                    }
                    recyclerView!!.viewTreeObserver.addOnGlobalLayoutListener(listener)
                } else {
                    mTitleText.visibility = View.GONE
                }
        } else {
            mTitleText.visibility = View.GONE
        }
    }

    init {
        init(context)
    }
}