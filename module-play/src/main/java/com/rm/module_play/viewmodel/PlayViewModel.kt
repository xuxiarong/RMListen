package com.rm.module_play.viewmodel

import android.util.Log
import android.widget.TextView
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.AudioChapterListModel
import com.rm.business_lib.bean.ChapterList
import com.rm.business_lib.bean.HomeDetailModel
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.HistoryPlayBook
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.module_play.activity.BookPlayerActivity
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
    val audioChapterModel = ObservableField<AudioChapterListModel>()
    val process = ObservableField<Float>()//进度条
    val maxProcess = ObservableField<Float>()//最大进度
    val updateThumbText = ObservableField<String>()//更改文字
    var playControlModel = ObservableField<PlayControlModel>()
    var playControlAction = ObservableField<String>()
    var playControlSubModel = MutableLiveData<PlayControlSubModel>()
    var playControlRecommentListModel =
        MutableLiveData<MutableList<PlayControlRecommentListModel>>()
    val mutableList = MutableLiveData<MutableList<MultiItemEntity>>()
    val playManger: MusicPlayerManager = musicPlayerManger
    val audioID = ObservableField<String>()

    //播放状态进度条，0是播放2是加载中1是暂停
    val playSate = ObservableField<Int>()
    val lastState = ObservableField<Boolean>()

    // 下拉刷新和加载更多控件状态控制Model
    val refreshStatusModel = SmartRefreshLayoutStatusModel()
    var page = 1
    val pageSize = 10
    val mHistoryPlayBook: HistoryPlayBook = HistoryPlayBook()

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


    fun zipPlayPath(searchResultInfo: AudioChapterListModel, headUrl: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                searchResultInfo.chapter_list.forEach {
                    pathList.add(
                        BaseAudioInfo(
                            audioPath = it.path_url,
                            audioCover = headUrl,
                            audioName = it.chapter_name,
                            filename = it.created_at,
                            audioId = it.audio_id,
                            chapterId = it.chapter_id,
                            duration = it.duration
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
     * 章节列表
     */
    fun chapterList(audioId: String, page: Int, page_size: Int, sort: String, anchorURL: String) {
        launchOnUI {
            repository.chapterList(audioId, page, page_size, sort).checkResult(onSuccess = {
                audioChapterModel.set(it)
                zipPlayPath(it, anchorURL)
                showContentView()
            }, onError = {
                showContentView()
            })
        }
    }

    //设置书籍
    fun setHistoryPlayBook(homeDetail: HomeDetailModel) {
        mHistoryPlayBook.anchor_id = homeDetail.detaillist.anchor_id
        mHistoryPlayBook.audio_cover = homeDetail.detaillist.audio_cover
        mHistoryPlayBook.audio_cover_url = homeDetail.detaillist.audio_cover_url
        mHistoryPlayBook.audio_id = homeDetail.detaillist.audio_id.toLong()
        mHistoryPlayBook.audio_intro = homeDetail.detaillist.audio_intro
        mHistoryPlayBook.audio_label = homeDetail.detaillist.audio_label
        mHistoryPlayBook.audio_name = homeDetail.detaillist.audio_name
        mHistoryPlayBook.audio_type = homeDetail.detaillist.audio_type
        mHistoryPlayBook.author = homeDetail.detaillist.author
        mHistoryPlayBook.chapter_updated_at = homeDetail.detaillist.chapter_updated_at
        mHistoryPlayBook.created_at = homeDetail.detaillist.created_at
        mHistoryPlayBook.subscription_count = homeDetail.detaillist.subscription_count
        mHistoryPlayBook.status = homeDetail.detaillist.status
        mHistoryPlayBook.quality = homeDetail.detaillist.quality
        mHistoryPlayBook.short_intro = homeDetail.detaillist.short_intro
        mHistoryPlayBook.progress = homeDetail.detaillist.progress
        mHistoryPlayBook.play_count = homeDetail.detaillist.play_count
        mHistoryPlayBook.last_sequence = homeDetail.detaillist.last_sequence
        mHistoryPlayBook.listBean = arrayListOf()
        repository.insertPlayBook(mHistoryPlayBook)

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


    /**
     * 记录播放的章节
     */
    fun updatePlayBook(chapter: ChapterList?) {
        repository.updatePlayBook(mHistoryPlayBook.audio_id.toLong(), chapter)
    }

    /**
     * 查询
     */
    fun queryPlayBookList(): List<HistoryPlayBook>? =
        DaoUtil(HistoryPlayBook::class.java, "").queryAll()
}