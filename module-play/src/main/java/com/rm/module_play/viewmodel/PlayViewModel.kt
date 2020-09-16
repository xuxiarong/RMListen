package com.rm.module_play.viewmodel

import android.util.Log
import android.widget.TextView
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_play.model.*
import com.rm.module_play.repository.BookPlayRepository
import com.rm.module_play.test.SearchResultInfo
import com.rm.module_play.view.PlayButtonView
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import kotlinx.android.synthetic.main.music_paly_control_view.*
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
class PlayViewModel(val repository: BookPlayRepository) : BaseVMViewModel() {
    val playPath = ObservableField<List<BaseAudioInfo>>()
    val pathList = ArrayList<BaseAudioInfo>()
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

    fun getPlayPath(hashKey: String) {
        val params: MutableMap<String, String> = HashMap()
        params["r"] = "play/getdata"
        params["hash"] = hashKey
        launchOnUI {
            repository.getPlayPath(params).checkResult(
                onSuccess = {
                    if (it.play_url.orEmpty().isNotEmpty() && it.img.orEmpty().isNotEmpty()) {
                        pathList.add(
                            BaseAudioInfo(
                                it.play_url,
                                it.img,
                                it.audio_name,
                                it.author_name
                            )
                        )
                        playPath.notifyChange()
                        playPath.set(pathList)
                    }
                },
                onError = {
                }
            )
        }
    }

    fun zipPlayPath(searchResultInfo: List<SearchResultInfo>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                searchResultInfo.forEach {
                    getPlayPath(it.hash)
                }

            }
        }

    }


    /**
     * 初始化数据
     */
    fun initPlayerAdapterModel() {
        val recommentListModel = arrayListOf<PlayControlRecommentListModel>()
        for (index in 1..10) {
            recommentListModel.add(PlayControlRecommentListModel())

        }
        playControlRecommentListModel.value = recommentListModel
        mutableList.value = mutableListOf(
            playControlModel.get() ?: PlayControlModel(BaseAudioInfo()),
            playControlSubModel.value ?: PlayControlSubModel(),
            PlayControlRecommentModel(),
            PlayControlHotModel(),
            PlayControlCommentTitleModel(),
            PlayControlCommentListModel(),
            PlayControlCommentListModel(),
            PlayControlCommentListModel(),
            PlayControlCommentListModel(),
            PlayControlCommentListModel(),
            PlayControlCommentListModel()
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
    fun playOrPause() {
        playManger.playOrPause()
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

}