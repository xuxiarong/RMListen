package com.rm.business_lib.wedgit.smartrefresh.binding

import androidx.databinding.BindingAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * desc   : 下拉刷新控件扩展函数
 * date   : 2020/09/16
 * version: 1.0
 */
/**
 * 绑定下拉刷新事件
 * @receiver SmartRefreshLayout
 * @param action Function0<Unit>?
 */
@BindingAdapter("bindOnRefreshListener")
fun SmartRefreshLayout.bindOnRefreshListener(action: (() -> Unit)?) {
    if (action == null) return
    setOnRefreshListener {
        action()
    }
}

/**
 * 绑定加载下一页数据事件
 * @receiver SmartRefreshLayout
 * @param action Function0<Unit>?
 */
@BindingAdapter("bindOnLoadMoreListener")
fun SmartRefreshLayout.bindOnLoadMoreListener(action: (() -> Unit)?) {
    if (action == null) return
    setOnLoadMoreListener {
        action()
    }
}

/**
 * 绑定下拉刷新完成处理
 * @receiver SmartRefreshLayout
 * @param isSuccess Boolean?
 */
@BindingAdapter("bindIsRefreshSuccess")
fun SmartRefreshLayout.bindIsRefreshSuccess(isSuccess: Boolean?) {
    if (isSuccess == null) return
    finishRefresh(isSuccess)
}

/**
 * 绑定加载更多完成处理
 * @receiver SmartRefreshLayout
 * @param isSuccess Boolean?
 * @param isHasMoreData Boolean? 是否还有下一页数据
 */
@BindingAdapter("bindIsLoadMoreSuccess")
fun SmartRefreshLayout.bindIsLoadMoreSuccess(isSuccess: Boolean?) {
    if (isSuccess != null) {
        finishLoadMore(isSuccess)
    }
}

@BindingAdapter("bindIsHasMoreData")
fun SmartRefreshLayout.bindIsHasMoreData(isHasMoreData: Boolean?) {
    // 没有更多数据了
    if (isHasMoreData == true) {
        finishLoadMoreWithNoMoreData()
    }
}


@BindingAdapter("bindCanRefresh")
fun SmartRefreshLayout.bindCanRefresh(canRefresh: Boolean?) {
    setEnableRefresh(canRefresh ?: false)
}

@BindingAdapter("bindResetNoMoreData")
fun SmartRefreshLayout.bindResetNoMoreData(resetNoMoreData: Boolean?) {
    if (resetNoMoreData == null) {
        return
    }
    if (resetNoMoreData == true) {
        resetNoMoreData()
    }
}