package com.rm.module_listen.viewmodel

import android.content.Context
import android.text.TextUtils
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.db.converter.BusinessConvert
import com.rm.business_lib.db.listen.ListenDaoUtils
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.BR
import com.rm.module_listen.activity.ListenHistorySearchActivity
import com.rm.module_listen.adapter.ListenRecentListenAdapter
import com.rm.module_listen.adapter.ListenRecentListenAdapter.*

/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */
class ListenRecentListenViewModel : BaseVMViewModel() {

    var allHistory = MutableLiveData<MutableList<MultiItemEntity>>()

    var dataEmpty = ObservableBoolean(false)

    private val playService by lazy {
        RouterHelper.createRouter(PlayService::class.java)
    }

    val mSwipeAdapter by lazy {
        ListenRecentListenAdapter(
            this,
            BR.viewModel,
            BR.item
        )
    }


    fun getListenHistory() {
        queryDataBaseOnIO {
            val queryPlayBookList = ListenDaoUtils.getAllAudioByRecentLimit10()
            val audioList = ArrayList<MultiItemEntity>()
            if (queryPlayBookList.isNotEmpty()) {
                dataEmpty.set(false)
                queryPlayBookList.forEach { audio ->
                    if (!TextUtils.isEmpty(audio.listenChapterId)) {
                        val recentChapter = ListenDaoUtils.queryChapterRecentUpdate(
                            audio.audio_id,
                            audio.listenChapterId.toLong()
                        )
                        recentChapter?.let { chapter ->
                            val listenHistoryModel =
                                ListenRecentListenContentItemEntity(audio, chapter)
                            audioList.add(listenHistoryModel)
                        }
                        if (recentChapter == null) {
                            DLog.d(
                                "suolong getListenHistory",
                                "${audio.audio_name} --- ${audio.listenChapterId}"
                            )
                        }
                    }
                }
                if (audioList.isNotEmpty()) {
                    audioList.add(0, ListenRecentListenOtherItemEntity())
                    allHistory.postValue(audioList)
                    dataEmpty.set(false)
                } else {
                    dataEmpty.set(true)
                }
            } else {
                dataEmpty.set(true)
            }
        }
    }


    fun startListenRecentDetail(context: Context) {
        ListenHistorySearchActivity.startListenHistorySearch(context)
    }

    fun startAudioPlay(context: Context, model: ListenRecentListenContentItemEntity) {
        playService.startPlayActivity(
            context = context,
            audioId = model.audio.audio_id.toString(),
            audioInfo = BusinessConvert.convertToDownloadAudio(model.audio),
            chapterId = model.chapter.chapter_id.toString()
        )
    }

    fun deleteItem(item: ListenRecentListenContentItemEntity) {
//        mSwipeAdapter.mItemManger.closeItem(mSwipeAdapter.data.indexOf(item))
        mSwipeAdapter.data.remove(item)
        ListenDaoUtils.deleteAudio(item.audio)
        getListenHistory()
    }

}