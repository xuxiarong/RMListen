package com.rm.business_lib.wedgit.smartrefresh.model

import androidx.databinding.ObservableField

/**
 * desc   : SmartRefreshLayout的状态变化统一model
 * date   : 2020/09/17
 * version: 1.0
 */
class SmartRefreshLayoutStatusModel {
    // 标识下拉刷新是否成功
    // 注意：不要直接在代码里面更改isRefreshSuccess的值，一定要调用finishRefresh()方法，如果值本身未发生改变是更新不了UI的，例：当前本身值为true，下次再设置为true，则更新不了UI
    var isRefreshSuccess = ObservableField<Boolean>()

    // 标识加载更多(下一页)是否成功
    // 注意：不要直接在代码里面更改isLoadMoreSuccess的值，一定要调用finishLoadMore()方法，如果值本身未发生改变是更新不了UI的，例：当前本身值为true，下次再设置为true，则更新不了UI
    var isLoadMoreSuccess = ObservableField<Boolean>()

    // 标识是否还有更多(下一页)数据
    // 注意：不要直接在代码里面更改isLoadMoreSuccess的值，一定要调用setHasMore()方法，如果值本身未发生改变是更新不了UI的，例：当前本身值为true，下次再设置为true，则更新不了UI
    var noMoreData = ObservableField<Boolean>(false)

    //是否能够刷新
    var canRefresh = ObservableField<Boolean>(true)

    //是否能够加载更多
    var canCanLoadMore = ObservableField<Boolean>(true)

    //是否重置加载更多状态
    var resetNoMoreData = ObservableField<Boolean>(true)


    /**
     * 设置刷新完成
     *
     * @param isSuccess Boolean true = 刷新成功,false = 刷新失败
     */
    fun finishRefresh(isSuccess: Boolean) {
        isRefreshSuccess.set(isSuccess)
        isRefreshSuccess.notifyChange()
    }

    /**
     * 设置加载更多完成
     * @param isSuccess Boolean true = 加载更多成功，false = 加载更多失败
     */
    fun finishLoadMore(isSuccess: Boolean) {
        isLoadMoreSuccess.set(isSuccess)
        isLoadMoreSuccess.notifyChange()
    }

    /**
     * 设置是否还有更多数据
     * @param isMore Boolean
     */
    fun setNoHasMore(isMore: Boolean) {
        noMoreData.set(isMore)
        noMoreData.notifyChange()
    }

    /**
     * 是否能够下拉刷新
     */
    fun setCanRefresh(canRefresh: Boolean) {
        this.canRefresh.set(canRefresh)
        this.canRefresh.notifyChange()
    }

    /**
     * 是否重置加载更多状态
     */
    fun setResetNoMoreData(resetNoMoreData: Boolean) {
        this.resetNoMoreData.set(resetNoMoreData)
        this.resetNoMoreData.notifyChange()
    }

}