package com.rm.module_listen.viewmodel

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.adapter.swipe.CommonMultiSwipeVmAdapter
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.HistoryPlayBook
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.activity.ListenHistorySearchActivity
import com.rm.module_listen.model.ListenHistoryModel

/**
 * desc   :
 * date   : 2020/09/30
 * version: 1.0
 */
class ListenHistoryViewModel : BaseVMViewModel() {

    var allHistory = MutableLiveData<MutableList<ListenHistoryModel>>()

    var startSearchHistory: (String) -> Unit = { searchHistory(it) }

    private val playService by lazy {
        RouterHelper.createRouter(PlayService::class.java)
    }

    val mSwipeAdapter: CommonMultiSwipeVmAdapter by lazy {
        CommonMultiSwipeVmAdapter(
            this, mutableListOf(),
            R.layout.listen_item_history_listen,
            R.id.listenRecentSl,
            BR.viewModel,
            BR.item
        )
    }

    fun getListenHistory() {
        showLoading()
        launchOnIO {
            val queryPlayBookList = playService.queryPlayBookList()
            val audioList = ArrayList<ListenHistoryModel>()
            if (queryPlayBookList != null && queryPlayBookList.isNotEmpty()) {
                showContentView()
                queryPlayBookList.forEach {
                    val listenHistoryModel = ListenHistoryModel(it)
                    listenHistoryModel.itemType = R.layout.listen_item_history_listen
                    audioList.add(listenHistoryModel)
                }
                allHistory.postValue(audioList)
            } else {
                showDataEmpty()
            }
        }
    }


    fun searchHistory(search: String) {
        val sourceList = allHistory.value
        val resultList = ArrayList<ListenHistoryModel>()

        if (TextUtils.isEmpty(search) && sourceList != null) {
            mSwipeAdapter.data.clear()
            mSwipeAdapter.addData(sourceList)
        } else {
            if (sourceList != null && sourceList.isNotEmpty()) {
                sourceList.forEach {
                    if (it.HistoryPlayBook.audio_name.contains(search)  ){
                        resultList.add(it)
                    }
                }
                if(resultList.isNotEmpty()){
                    mSwipeAdapter.data.clear()
                    mSwipeAdapter.addData(resultList)
                }else{
                    showDataEmpty()
                }
            }
        }
    }

    fun deleteAllHistory() {
        DaoUtil(HistoryPlayBook::class.java, "").deleteAll()
        allHistory.value = mutableListOf()
    }

    fun startListenRecentDetail(context: Context) {
        ListenHistorySearchActivity.startListenHistorySearch(context)
    }

    fun deleteItem(item: MultiItemEntity) {
        mSwipeAdapter.data.remove(item)
        mSwipeAdapter.notifyDataSetChanged()
    }


}