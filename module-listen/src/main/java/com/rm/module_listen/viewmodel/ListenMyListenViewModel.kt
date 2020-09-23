package com.rm.module_listen.viewmodel

import androidx.databinding.ObservableInt
import com.rm.baselisten.adapter.swipe.CommonMultiSwipeVmAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.repository.ListenMyListenRepository

/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */
class ListenMyListenViewModel(val repository: ListenMyListenRepository) : BaseVMViewModel() {

    var subsNumber = ObservableInt(0)

    val mSwipeAdapter : CommonMultiSwipeVmAdapter by lazy {
        CommonMultiSwipeVmAdapter(this, mutableListOf(),
            R.layout.listen_item_recent_listen,
            R.id.listenRecentSl,
            BR.viewModel,
            BR.item)
    }


    fun getSubsTotalNumberFromService(){
        launchOnIO {
            repository.getSubsNumber().checkResult(
                onSuccess = {
                    subsNumber.set(it.subNumber)
                },
                onError = {
                    DLog.d("suolong","$it")
                }
            )
        }
    }

}