package com.rm.module_play.viewmodel

import android.util.Log
import android.widget.TextView
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.AudioChapterListModel
import com.rm.business_lib.bean.HomeDetailModel
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.module_play.model.*
import com.rm.module_play.repository.BookPlayRepository
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import com.rm.music_exoplayer_lib.utils.ExoplayerLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.HashMap

/**
 *
 * @des:
 * @data: 8/24/20 10:44 AM
 * @Version: 1.0.0
 */
open class PlayViewModel(val repository: BookPlayRepository) : BaseVMViewModel() {
    val playPath = MutableLiveData<List<BaseAudioInfo>>()
    val pathList = ArrayList<BaseAudioInfo>()
    val process = ObservableField<Float>()//进度条
    val maxProcess = ObservableField<Float>()//最大进度
    val updateThumbText = ObservableField<String>()//更改文字
    var playControlModel = ObservableField<PlayControlModel>()
    var playControlAction = ObservableField<String>()
    var playControlSubModel = MutableLiveData<PlayControlSubModel>()
    val homeDetailModel = MutableLiveData<HomeDetailModel>()
    var playControlRecommentListModel =
        MutableLiveData<MutableList<PlayControlRecommentListModel>>()
    val mutableList = MutableLiveData<MutableList<MultiItemEntity>>()
    val playManger: MusicPlayerManager = musicPlayerManger
    val audioID = ObservableField<String>()

    // 下拉刷新和加载更多控件状态控制Model
    val refreshStatusModel = SmartRefreshLayoutStatusModel()
    var page = 1
    val pageSize = 10

    init {
        updateThumbText.set("0/0")
    }

    companion object {
        const val ACTION_PLAY_QUEUE = "ACTION_PLAY_QUEUE"//播放列表
        const val ACTION_PLAY_OPERATING = "ACTION_PLAY_OPERATING"//播放操作
        const val ACTION_GET_PLAYINFO_LIST = "ACTION_GET_PLAYINFO_LIST"//播放列表
        const val ACTION_JOIN_LISTEN = "ACTION_JOIN_LISTEN"//加入听单
        const val ACTION_MORE_COMMENT = "ACTION_MORE_COMMENT"//更多评论

    }

    var mText: TextView? = null
    fun addBubbleFLViewModel(text: TextView) {
        mText = text
    }


    fun zipPlayPath(searchResultInfo: AudioChapterListModel,headUrl:String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                searchResultInfo.chapter_list.forEach {
                    pathList.add(
                        BaseAudioInfo(
                            it.path_url,
                            headUrl,
                            it.chapter_name,
                            it.created_at,
                            it.audio_id,
                            it.chapter_id
                        )
                    )
                }
                playPath.postValue(pathList)

            }
        }

    }

    /**
     * 获取评论列表
     */
    fun getCommentList() {
        launchOnIO {

        }
    }

    /**
     * 点赞或者取消评论
     */
    fun likeComment() {

    }

    /**
     * 初始化数据
     */
    fun initPlayerAdapterModel() {
//        val recommentListModel = arrayListOf<PlayControlRecommentListModel>()
//        for (index in 1..10) {
//            recommentListModel.add(PlayControlRecommentListModel())
//
//        }
        //        playControlRecommentListModel.value = recommentListModel
//        playControlSubModel.value ?: PlayControlSubModel(),
//            PlayControlRecommentModel(),
        playControlModel.set(PlayControlModel())
        mutableList.value = mutableListOf(
            playControlModel.get()!!,
            PlayControlCommentTitleModel()
        )

    }

    //播放器操作行为
    fun playControlAction(action: String) {
        playControlAction.set(action)
        playControlAction.notifyChange()

    }


    /**
     * 播放或者暂停
     */
    fun playFun() {
        val playControl = playControlModel.get()
        playControl?.state = !(playControlModel.get()?.state == true)
        playControlModel.set(playControl)
        playControlModel.notifyChange()

    }


    //订阅
    fun playSubAction(model: PlayControlSubModel) {

    }

    //订阅
    fun playFollowAction(model: PlayControlHotModel) {

    }

    //点赞
    fun playLikeBook(model: PlayControlCommentListModel) {

    }

    //精品详情
    fun playBoutiqueDetails(model: PlayControlRecommentListModel) {
        Log.i("", "playBoutiqueDetails")
    }

    /**
     * 上报
     */
    fun playReport(audioID: String, chapterId: String) {
        launchOnIO {
            repository.playerReport(audioID, chapterId).checkResult(onSuccess = {
                ExoplayerLogger.exoLog(it)
            }, onError = {
                ExoplayerLogger.exoLog(it ?: "")

            })
        }
    }

    /**
     *评论列表
     */
    fun commentAudioComments(audioID: String) {
        launchOnIO {
            if (page == 1) {
                showLoading()
            }
            repository.commentAudioComments(audioID, page, pageSize)
                .checkResult(onSuccess = {
                    it.list.forEach {
                        mutableList.value?.add(PlayControlCommentListModel(comments = it))
                    }
                    mutableList.postValue(mutableList.value)
                    if (page == 1) {
                        if (it.list.isEmpty()) {
                            showDataEmpty()
                        } else {
                            showContentView()
                        }
                    } else {
                        refreshStatusModel.finishLoadMore(true)
                    }
                    refreshStatusModel.setHasMore(it.list.size > 0)
                    page++
                }, onError = {
                    if (page == 1) {
                        showNetError()
                    } else {
                        refreshStatusModel.finishLoadMore(false)
                    }

                })
        }
    }
}