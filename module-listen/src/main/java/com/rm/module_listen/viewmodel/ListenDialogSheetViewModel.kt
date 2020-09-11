package com.rm.module_listen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.RetrofitKotlinManager
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.module_home.repository.ListenDialogSheetRepository
import com.rm.module_listen.api.ListenApiService
import com.rm.module_listen.bean.ListenSheetBean
import com.rm.module_listen.bean.ListenSheetMyListBean

class ListenDialogSheetViewModel : BaseVMViewModel() {
    private val repository by lazy {
        ListenDialogSheetRepository(BusinessRetrofitClient().getService(ListenApiService::class.java))
    }

    val data = MutableLiveData<ListenSheetMyListBean>()

    fun getData() {
        launchOnIO {
            repository.getData().checkResult(
                onSuccess = {
                    data.value = it
                },
                onError = {
                    DLog.i("------>", "$it")
                    //Todo 测试数据
                    data.value = test()
                }
            )
        }
    }

    private fun test(): ListenSheetMyListBean? {
        val list = mutableListOf<ListenSheetBean>()
        list.add(
            ListenSheetBean(
                1,
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3984473917,238095211&fm=26&gp=0.jpg",
                "vip",
                "听妈妈的话",
                10,
                10,
                10,
                "2020/09/20"
            )
        )
        list.add(
            ListenSheetBean(
                1,
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3984473917,238095211&fm=26&gp=0.jpg",
                "vip",
                "听妈妈的话",
                10,
                10,
                10,
                "2020/09/20"
            )
        )
        list.add(
            ListenSheetBean(
                1,
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3984473917,238095211&fm=26&gp=0.jpg",
                "vip",
                "听妈妈的话",
                10,
                10,
                10,
                "2020/09/20"
            )
        )
        list.add(
            ListenSheetBean(
                1,
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3984473917,238095211&fm=26&gp=0.jpg",
                "vip",
                "听妈妈的话",
                10,
                10,
                10,
                "2020/09/20"
            )
        )
        list.add(
            ListenSheetBean(
                1,
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3984473917,238095211&fm=26&gp=0.jpg",
                "vip",
                "听妈妈的话",
                10,
                10,
                10,
                "2020/09/20"
            )
        )
        list.add(
            ListenSheetBean(
                1,
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=3984473917,238095211&fm=26&gp=0.jpg",
                "vip",
                "听妈妈的话",
                10,
                10,
                10,
                "2020/09/20"
            )
        )
        return ListenSheetMyListBean(list, 1L)

    }


    fun itemClickFun(bean: ListenSheetBean) {

    }
}