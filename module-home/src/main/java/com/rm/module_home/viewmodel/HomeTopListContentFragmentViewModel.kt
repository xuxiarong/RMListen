package com.rm.module_home.viewmodel

import android.view.View
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_home.BR
import com.rm.module_home.adapter.HomeTopListContentAdapter
import com.rm.module_home.bean.HomeTopListBean
import com.rm.module_home.bean.HomeTopListDataBean
import com.rm.module_home.repository.HomeRepository

class HomeTopListContentFragmentViewModel(private val repository: HomeRepository) :
    BaseVMViewModel() {
    /**
     * 懒加载构建adapter对象
     */
    val mAdapter by lazy {
        HomeTopListContentAdapter(
            this,
            BR.itemViewModel,
            BR.item
        )
    }
    val refreshStatusModel = SmartRefreshLayoutStatusModel()

    private var mPage = 1//当前的页码

    //每页加载数据条数
    private val pageSize = 12

    private lateinit var rankType: String
    private lateinit var rankSeg: String


    /**
     * 获取榜单听单
     */
    fun getListInfo(rankType: String, rankSeg: String, page: Int) {
        this.rankType = rankType
        this.rankSeg = rankSeg
        launchOnIO {
            repository.getTopList(rankType, rankSeg, page, pageSize).checkResult(
                onSuccess = {
                    processSuccessData(it)

                },
                onError = {
                    processFailData()
                }
            )
        }
    }

    /**
     * 成功数据处理
     */
    private fun processSuccessData(bean: HomeTopListBean) {
        showContentView()
        if (mPage == 1) {
            //刷新完成
            refreshStatusModel.finishRefresh(true)
            if (bean.list.isNullOrEmpty()) {
                showDataEmpty()
            } else {
                mAdapter.setList(bean.list)
            }
        } else {
            //加载更多完成
            refreshStatusModel.finishLoadMore(true)
            bean.list?.let { list -> mAdapter.addData(list) }
        }

        //是否有更多数据
        refreshStatusModel.setHasMore(bean.list?.size ?: 0 > pageSize)
    }

    /**
     * 失败数据处理
     */
    private fun processFailData() {
        showServiceError()
        if (mPage == 1) {
            refreshStatusModel.finishRefresh(false)
        } else {
            refreshStatusModel.finishLoadMore(false)
        }
    }

    /**
     * item点击事件
     */
    fun itemClickFun(view: View, bean: HomeTopListDataBean) {
        RouterHelper.createRouter(HomeService::class.java)
            .toDetailActivity(view.context, bean.audio_id)
    }

    /**
     * 刷新数据
     */
    fun refreshData() {
        mPage = 1
        getListInfo(rankType, rankSeg, mPage)
    }

    /**
     * 加载更多
     */
    fun loadData() {
        ++mPage
        getListInfo(rankType, rankSeg, mPage)
    }


}