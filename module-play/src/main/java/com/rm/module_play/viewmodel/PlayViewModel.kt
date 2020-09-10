package com.rm.module_play.viewmodel

import android.widget.TextView
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.BannerInfoBean
import com.rm.module_play.model.*
import com.rm.module_play.repository.BookPlayRepository
import com.rm.module_play.test.SearchMusicData
import com.rm.module_play.test.SearchResultInfo
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.util.HashMap
import kotlin.system.measureTimeMillis

/**
 *
 * @des:
 * @data: 8/24/20 10:44 AM
 * @Version: 1.0.0
 */
class PlayViewModel(val repository: BookPlayRepository) : BaseVMViewModel() {
    val playPath = ObservableField<List<BaseAudioInfo>>()
    val pathList = ArrayList<BaseAudioInfo>()
    val process=ObservableField<Float>()//进度条
    val maxProcess=ObservableField<Float>()//最大进度
    val updateThumbText=ObservableField<String>()//更改文字

    var playControModel = MutableLiveData<MutableList<PlayControlModel>>()
    var playControAction = ObservableField<String>()
    var playControlSubModel = MutableLiveData<MutableList<PlayControlSubModel>>()
    var playCOntrolRecommentModel = MutableLiveData<MutableList<PlayControlRecommentModel>>()
    var playControlHotModel = MutableLiveData<MutableList<PlayControlHotModel>>()
    var playControlCommentTitleModel = MutableLiveData<MutableList<PlayControlCommentTitleModel>>()
    var playControlCommentListModel = MutableLiveData<MutableList<PlayControlCommentListModel>>()
    var playControlRecommentListModel =
        MutableLiveData<MutableList<PlayControlRecommentListModel>>()
    val playManger: MusicPlayerManager=musicPlayerManger
    init {
        updateThumbText.set("0/0")
    }
    companion object {
        const val ACTION_PLAY_QUEUE = "ACTION_PLAY_QUEUE"//播放列表
        const val ACTION_PLAY_OPERATING = "ACTION_PLAY_OPERATING"//播放操作
        const val ACTION_GET_PLAYINFO_LIST = "ACTION_GET_PLAYINFO_LIST"//播放列表

    }
    var mText:TextView?=null
    fun addBubbleFLViewModel(text:TextView){
        mText= text
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

    fun initPlayerAdapterModel(): MutableList<MultiItemEntity> {
        val recommentListModel = arrayListOf<PlayControlRecommentListModel>()
        for (index in 1..10) {
            recommentListModel.add(PlayControlRecommentListModel())

        }
        playControlRecommentListModel.value = recommentListModel
        return mutableListOf(
            PlayControlModel(),
            PlayControlSubModel(),
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
    fun playControlAction(action:String) {
        playControAction.set(action)
        playControAction.notifyChange()

    }


}