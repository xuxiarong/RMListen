package com.rm.module_home.activity.boutique

import android.content.Context
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.BR
import com.rm.business_lib.bean.AudioListBean
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.module_home.R
import com.rm.module_home.activity.detail.HomeDetailActivity
import com.rm.module_home.bean.CategoryTabBean
import com.rm.module_home.repository.HomeRepository

/**
 * desc   :
 * date   : 2020/08/25
 * version: 1.0
 */
class BoutiqueFragmentViewModel(private val repository: HomeRepository) : BaseVMViewModel() {
    private var page = 1
    private val pageSize = 12

    // 下拉刷新和加载更多控件状态控制Model
    val refreshStatusModel = SmartRefreshLayoutStatusModel()

    val contentRvId = R.id.home_boutique_fragment_recycler_view

    var categoryTabBean: CategoryTabBean? = null
    val bookAdapter by lazy {
        CommonBindVMAdapter<DownloadAudio>(
            this,
            mutableListOf(),
            R.layout.home_adapter_boutique_item,
            BR.viewModel,
            BR.audioItem
        )
    }

    fun getBookList() {
        if (page == 1) {
            showLoading()
        }
        launchOnIO {
            repository.getBoutiqueRecommendInfoList(categoryTabBean!!.class_id, page, pageSize)
                .checkResult(
                    onSuccess = {
                        processSuccess(it)
                    },
                    onError = {
                        if (page == 1) {
                            showServiceError()
                            refreshStatusModel.finishRefresh(false)
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
                bookAdapter.setList(audioListBean.list)
            } else {
                showDataEmpty()
            }
            refreshStatusModel.finishRefresh(true)
        } else {
            // 加载更多
            audioListBean.list?.let {
                bookAdapter.addData(it)
            }
            refreshStatusModel.finishLoadMore(true)
        }
        // 设置是否还有下一页数据
        if (bookAdapter.data.size >= audioListBean.total || audioListBean.list?.size ?: 0 < pageSize) {
            refreshStatusModel.setNoHasMore(true)
        } else {
            page++
        }
    }

    fun refresh() {
        refreshStatusModel.setNoHasMore(false)
        page = 1
        getBookList()
    }

    /**
     * 加载更多
     */
    fun loadData() {
        getBookList()
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