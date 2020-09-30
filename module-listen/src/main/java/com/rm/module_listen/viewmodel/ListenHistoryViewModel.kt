package com.rm.module_listen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.adapter.swipe.CommonMultiSwipeVmAdapter
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.model.ListenHistoryModel
import com.rm.module_listen.model.ListenRecentDateModel

/**
 * desc   :
 * date   : 2020/09/30
 * version: 1.0
 */
class ListenHistoryViewModel : BaseVMViewModel() {

    var allHistory = MutableLiveData<MutableList<MultiItemEntity>>()
    var searchList = MutableLiveData<MutableList<MultiItemEntity>>()

    private val playService by lazy {
        RouterHelper.createRouter(PlayService::class.java)
    }

    val mSwipeAdapter: CommonMultiSwipeVmAdapter by lazy {
        CommonMultiSwipeVmAdapter(
            this, mutableListOf(),
            R.layout.listen_item_recent_listen,
            R.id.listenRecentSl,
            BR.viewModel,
            BR.item
        )
    }

    fun getListenHistory() {
        showLoading()
        launchOnIO {
            val queryPlayBookList = playService.queryPlayBookList()
            val audioList = ArrayList<MultiItemEntity>()

            if (queryPlayBookList != null && queryPlayBookList.isNotEmpty()) {
                showContentView()
                audioList.add(ListenRecentDateModel())
                queryPlayBookList.forEach {
                    audioList.add(ListenHistoryModel(it))
                }
                allHistory.postValue(audioList)
            } else {
                showDataEmpty()
            }
        }
    }

    fun searchHistory(search : String){
        val sourceList = allHistory.value
        if(sourceList!=null && sourceList.isNotEmpty()){
            sourceList.forEach {

            }
        }
    }


}