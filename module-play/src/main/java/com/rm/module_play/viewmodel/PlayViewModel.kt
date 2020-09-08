package com.rm.module_play.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
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
    fun getPlayPath(hashKey: String) {
        val params: MutableMap<String, String> = HashMap()
        params["r"] = "play/getdata"
        params["hash"] = hashKey
        launchOnUI {
            repository.getPlayPath(params).checkResult(
                onSuccess = {
                    if (it.play_url.orEmpty().isNotEmpty() && it.img.orEmpty().isNotEmpty()) {
                        pathList.add(BaseAudioInfo(it.play_url, it.img,it.audio_name,it.author_name))
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
}