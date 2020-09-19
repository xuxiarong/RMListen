package com.rm.module_home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_home.bean.HomeTopListBean
import com.rm.module_home.bean.HomeTopListDataBean
import com.rm.module_home.repository.TopListRepository

class HomeTopListContentFragmentViewModel(private val repository: TopListRepository) :
    BaseVMViewModel() {
    // 榜单列表数据
    val dataList = MutableLiveData<HomeTopListBean>()

    var itemClick: (HomeTopListDataBean) -> Unit = {}


    //每页加载数据条数
    private val pageSize = 10


    /**
     * 获取榜单听单
     */
    fun getListInfo(rankType: String, rankSeg: String,page:Int) {
        showLoading()
        launchOnIO {
            repository.getTopList(rankType, rankSeg, page, pageSize).checkResult(
                onSuccess = {
                    showContentView()
                    dataList.value = it
                },
                onError = {
                    showContentView()
                    DLog.i("-------->", "$it")
                }
            )
        }
    }

    /**
     * item点击事件
     */
    fun itemClickFun(bean: HomeTopListDataBean) {
        itemClick(bean)
    }



}