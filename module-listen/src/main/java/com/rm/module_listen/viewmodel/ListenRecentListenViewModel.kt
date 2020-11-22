package com.rm.module_listen.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.adapter.swipe.CommonMultiSwipeVmAdapter
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.listen.ListenAudioEntity
import com.rm.business_lib.db.listen.ListenChapterEntity
import com.rm.business_lib.db.listen.ListenDaoUtils
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.activity.ListenHistorySearchActivity
import com.rm.module_listen.model.ListenHistoryModel
import com.rm.module_listen.model.ListenRecentDateModel

/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */
class ListenRecentListenViewModel : BaseVMViewModel() {

    var allHistory = MutableLiveData<MutableList<MultiItemEntity>>()
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
            var queryPlayBookList = ListenDaoUtils.getAllAudioByRecent()
            val audioList = ArrayList<MultiItemEntity>()
            if (queryPlayBookList.isNotEmpty()) {
                showContentView()
                audioList.add(ListenRecentDateModel())
                if(queryPlayBookList.size>10){
                    queryPlayBookList = queryPlayBookList.subList(0,10)
                }
                queryPlayBookList.forEach {
                    audioList.add(ListenHistoryModel(it))
                }
                allHistory.postValue(audioList)
            } else {
                showDataEmpty()
            }
        }
    }


    fun startListenRecentDetail(context: Context) {
        ListenHistorySearchActivity.startListenHistorySearch(context)
    }

    fun startAudioPlay(context: Context, model: ListenHistoryModel) {
        playService.startPlayActivity(
            context = context,
            audioId = model.audio.audio_id.toString(),
            chapterId = model.audio.listenChapterList.last().chapter_id.toString(),
            currentDuration = model.audio.listenChapterList.last().listen_duration
        )
    }

    fun deleteItem(item: ListenHistoryModel) {
        mSwipeAdapter.mItemManger.closeItem(mSwipeAdapter.data.indexOf(item))
        mSwipeAdapter.data.remove(item)
        DaoUtil(ListenChapterEntity::class.java, "").delete(item.audio.listenChapterList)
        DaoUtil(ListenAudioEntity::class.java, "").delete(item.audio)
    }

}