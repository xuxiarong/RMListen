package com.rm.module_home.activity.topic

import android.content.Context
import com.rm.baselisten.adapter.single.CommonBindAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.BR
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.module_home.R
import com.rm.module_home.activity.detail.HomeDetailActivity
import com.rm.module_home.repository.HomeRepository

/**
 * desc   : 专题列表viewModel
 * date   : 2020/09/11
 * version: 1.0
 */
class HomeTopicListViewModel(val repository: HomeRepository) : BaseVMViewModel() {
    var pageId = -1
    var blockId = -1
    var topicId = -1
    var page = 1
    val pageSize = 12

    // 下拉刷新和加载更多控件状态控制Model
    val refreshStatusModel = SmartRefreshLayoutStatusModel()

    val mAdapter by lazy {
        CommonBindAdapter<DownloadAudio>(
            mutableListOf(),
            R.layout.home_adapter_topic_list_item,
            BR.audioItem
        )
    }

    /**
     * 获取专题列表
     */
    fun getTopicList() {
        if (page == 1) {
            showLoading()
        }
        launchOnIO {
            repository.getTopicList(pageId, blockId, topicId, page, pageSize).checkResult(
                onSuccess = {
                    mAdapter.data.addAll(it.list)
                    mAdapter.notifyDataSetChanged()
                    if (page == 1) {
                        if (it.list.isEmpty()) {
                            showDataEmpty()
                        } else {
                            showContentView()
                        }
                    } else {
                        // 加载更多
                        refreshStatusModel.finishLoadMore(true)
                    }
                    // 设置是否还有下一页数据
                    refreshStatusModel.setNoHasMore(mAdapter.data.size >= it.total)
                    page++
                },
                onError = {
                    if (page == 1) {
                        // 获取第一页数据就失败
                        // 显示错误视图
                        showServiceError()
                    } else {
                        // 获取下一页失败，显示列表加载失败的视图
                        refreshStatusModel.finishLoadMore(false)
                    }
                }
            )
        }
    }

    /**
     * 下拉刷新时调用
     */
    fun refresh() {
        refreshStatusModel.setNoHasMore(false)
        launchOnIO {
            page = 1
            repository.getTopicList(pageId, blockId, topicId, page, pageSize).checkResult(
                onSuccess = {
                    // 清空所有数据
                    mAdapter.data.clear()
                    it.list.let { it1 -> mAdapter.data.addAll(it1) }
                    mAdapter.notifyDataSetChanged()
                    page++
                    refreshStatusModel.finishRefresh(true)
                    // 设置是否还有下一页数据
                    refreshStatusModel.setNoHasMore(mAdapter.data.size >= it.total)
                },
                onError = {
                    // 下拉刷新失败
                    refreshStatusModel.finishRefresh(false)
                }
            )
        }
    }

    /**
     * item点击事件
     */
    fun itemClick(context: Context, bean: DownloadAudio) {
        HomeDetailActivity.startActivity(
            context, bean.audio_id.toString()
        )
    }
}