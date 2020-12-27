package com.rm.business_lib.wedgit.smartrefresh.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.rm.baselisten.util.DLog
import com.rm.business_lib.wedgit.smartrefresh.BaseLoadMoreFooter
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
 */
@BindingAdapter("bindIsLoadMoreSuccess")
fun SmartRefreshLayout.bindIsLoadMoreSuccess(isSuccess: Boolean?) {
    if (isSuccess != null) {
        finishLoadMore(isSuccess)
    }
}

/**
 * 是否有更多数据
 * @param noMoreData Boolean? 是否还有下一页数据
 * @param contentRvId Int 填充内容的recyclerview
 */
@BindingAdapter("bindNoMoreData", "bindContentRvId", requireAll = false)
fun SmartRefreshLayout.bindNoMoreData(noMoreData: Boolean?, contentRvId: Int? = -1) {
    // 没有更多数据了
    if (noMoreData == true) {
        if (contentRvId != null && refreshFooter is BaseLoadMoreFooter) {
            val rv = findViewById<RecyclerView>(contentRvId)
            val footer = refreshFooter as BaseLoadMoreFooter
                if(contentRvId>0){
                    footer.bindRecyclerView(rv)
                }
        }
        finishLoadMoreWithNoMoreData()
    }
}


@BindingAdapter("bindCanRefresh")
fun SmartRefreshLayout.bindCanRefresh(canRefresh: Boolean?) {
    setEnableRefresh(canRefresh ?: false)
}

@BindingAdapter("bindCanLoadMore")
fun SmartRefreshLayout.bindCanLoadMore(canLoadMore: Boolean?) {
    setEnableLoadMore(canLoadMore ?: false)
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


