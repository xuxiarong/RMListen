package com.rm.module_play.viewmodel

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

    var playControModel = MutableLiveData<MutableList<PlayControlModel>>()
    var playControlSubModel = MutableLiveData<MutableList<PlayControlSubModel>>()
    var playCOntrolRecommentModel = MutableLiveData<MutableList<PlayControlRecommentModel>>()
    var playControlHotModel = MutableLiveData<MutableList<PlayControlHotModel>>()
    var playControlCommentTitleModel = MutableLiveData<MutableList<PlayControlCommentTitleModel>>()
    var playControlCommentListModel = MutableLiveData<MutableList<PlayControlCommentListModel>>()

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
        return mutableListOf(
            PlayControlModel(),
            PlayControlSubModel(),
            PlayControlRecommentModel(),
            PlayControlHotModel(),
            PlayControlCommentTitleModel(),
            PlayControlCommentListModel()
        )

    }
}