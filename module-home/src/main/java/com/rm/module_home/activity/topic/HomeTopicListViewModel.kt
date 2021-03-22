package com.rm.module_home.activity.topic

import android.content.Context
import android.text.TextUtils
import androidx.databinding.ObservableField
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.AudioListBean
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.activity.detail.HomeDetailActivity
import com.rm.module_home.repository.HomeRepository

/**
 * desc   : 专题列表viewModel
 * date   : 2020/09/11
 * version: 1.0
 */
class HomeTopicListViewModel(val repository: HomeRepository) : BaseVMViewModel() {
    var topicId = -1
    private var page = 1
    private val pageSize = 12
    var blockName = ObservableField("")

    // 下拉刷新和加载更多控件状态控制Model
    val refreshStatusModel = SmartRefreshLayoutStatusModel()
    val contentRvId = R.id.home_topic_recycler_view
    val mAdapter by lazy {
        CommonBindVMAdapter<DownloadAudio>(
            viewModel = this,
            commonData = mutableListOf(),
            commonItemLayoutId = R.layout.home_adapter_topic_list_item,
            commonDataBrId = BR.audioItem,
            commonViewModelId = BR.viewModel
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
            repository.getTopicList(topicId, page, pageSize).checkResult(
                onSuccess = {
                    processSuccess(it)
                    if(TextUtils.isEmpty(blockName.get())){
                        blockName.set(it.block_name)
                    }
                },
                onError = { _, _->
                    if (page == 1) {
                        // 获取第一页数据就失败
                        // 显示错误视图
                        refreshStatusModel.finishRefresh(false)
                        showServiceError()
                    } else {
                        // 获取下一页失败，显示列表加载失败的视图
                        refreshStatusModel.finishLoadMore(false)
                    }
                }
            )
        }
    }

    private fun processSuccess(audioListBean: AudioListBean) {
        showContentView()
        if (page == 1) {
            if (audioListBean.list?.size ?: 0 > 0) {
                mAdapter.setList(audioListBean.list)
            } else {
                showDataEmpty()
            }
            refreshStatusModel.finishRefresh(true)
        } else {
            // 加载更多
            audioListBean.list?.let {
                mAdapter.addData(it)
            }
            refreshStatusModel.finishLoadMore(true)
        }
        // 设置是否还有下一页数据
        if (mAdapter.data.size >= audioListBean.total || audioListBean.list?.size ?: 0 < pageSize) {
            refreshStatusModel.setNoHasMore(true)
        } else {
            page++
        }
    }

    /**
     * 下拉刷新时调用
     */
    fun refresh() {
        refreshStatusModel.setNoHasMore(false)
        page = 1
        getTopicList()
    }

    fun loadData() {
        getTopicList()
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