package com.rm.module_listen.viewmodel

import android.content.Context
import android.text.TextUtils
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.LottieAnimationView
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.business_lib.swipe.CommonMultiSwipeVmAdapter
import com.rm.baselisten.util.DLog
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

    var dataEmpty = ObservableBoolean(false)

    private val playService by lazy {
        RouterHelper.createRouter(PlayService::class.java)
    }

    val mSwipeAdapter: CommonMultiSwipeVmAdapter by lazy {
        CommonMultiSwipeVmAdapter(
                this, mutableListOf(),
                R.layout.listen_item_recent_listen,
                R.id.listenRecentSl,
                R.id.swipe_delete,
                BR.viewModel,
                BR.item
        )
    }

    fun getListenHistory() {
        launchOnIO {
            val queryPlayBookList = ListenDaoUtils.getAllAudioByRecentLimit10()
            val audioList = ArrayList<MultiItemEntity>()
            if (queryPlayBookList.isNotEmpty()) {
                dataEmpty.set(false)
                queryPlayBookList.forEach { audio ->
                    if(!TextUtils.isEmpty(audio.listenChapterId)){
                        val recentChapter = ListenDaoUtils.queryChapterRecentUpdate(audio.audio_id,audio.listenChapterId.toLong())
                        recentChapter?.let { chapter ->
                            val listenHistoryModel = ListenHistoryModel(audio, chapter)
                            listenHistoryModel.itemType = R.layout.listen_item_recent_listen
                            audioList.add(listenHistoryModel)
                        }
                    }
                }
                if(audioList.isNotEmpty()){
                    audioList.add(0,ListenRecentDateModel())
                    allHistory.postValue(audioList)
                }else{
                    dataEmpty.set(false)
                }
            } else {
                dataEmpty.set(true)
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
                chapterId = model.chapter.chapter_id.toString(),
                currentDuration = model.chapter.listen_duration
        )
    }

    fun deleteItem(item: ListenHistoryModel) {
        mSwipeAdapter.mItemManger.closeItem(mSwipeAdapter.data.indexOf(item))
        mSwipeAdapter.data.remove(item)
        DaoUtil(ListenChapterEntity::class.java, "").delete(item.audio.listenChapterList)
        DaoUtil(ListenAudioEntity::class.java, "").delete(item.audio)
        getListenHistory()
    }

}