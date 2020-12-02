package com.rm.module_listen.viewmodel

import androidx.databinding.ObservableInt
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.isLogin
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.module_listen.repository.ListenRepository

/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */
class ListenMyListenViewModel(val repository: ListenRepository) : BaseVMViewModel() {

    var subsNumber = ObservableInt(0)
    var downloadNumber = ObservableInt(0)
    val refreshStateModel = SmartRefreshLayoutStatusModel()
    var subsRefreshFun: () -> Unit = {}

    fun getSubsTotalNumberFromService() {
        launchOnIO {
            repository.getSubsNumber().checkResult(
                    onSuccess = {
                        subsNumber.set(it.subscription_total)
                    },
                    onError = {
                        DLog.d("suolong", "$it")
                    }
            )
        }
    }

    fun refresh() {
        if(isLogin.get()){
            launchOnIO {
                repository.getSubsNumber().checkResult(
                        onSuccess = {
                            subsNumber.set(it.subscription_total)
                            refreshStateModel.finishRefresh(true)
                        },
                        onError = {
                            it?.let {
                                showTip(it)
                            }
                            refreshStateModel.finishRefresh(false)
                        }
                )
            }
            subsRefreshFun()
        }else{
            refreshStateModel.finishRefresh(false)
        }
    }

    fun loadMore(){
        DLog.d("suolong","loadMore")
    }


}