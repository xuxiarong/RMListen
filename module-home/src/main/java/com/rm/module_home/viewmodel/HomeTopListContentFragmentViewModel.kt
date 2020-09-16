package com.rm.module_home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_home.bean.HomeTopListBean
import com.rm.module_home.repository.TopListRepository

class HomeTopListContentFragmentViewModel(private val repository: TopListRepository): BaseVMViewModel() {
    // 榜单列表数据
    val dataList = MutableLiveData<HomeTopListBean>()
    fun getListInfo(rankType: String, rankSeg: String, page: Int, pageSize: Int) {
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

}