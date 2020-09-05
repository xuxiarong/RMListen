package com.rm.module_play.viewmodel

import androidx.databinding.ObservableField
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_play.repository.BookPlayRepository
import com.rm.module_play.test.SearchMusicData
import java.util.HashMap

/**
 *
 * @des:
 * @data: 8/24/20 10:44 AM
 * @Version: 1.0.0
 */
class PlayViewModel (val repository: BookPlayRepository): BaseVMViewModel() {
    val  playBook= ObservableField<SearchMusicData>()
    val msg="自定义View"
    fun getPlayPath(hashKey: String) {
        val params: MutableMap<String, String> = HashMap()
        params["r"] = "play/getdata"
        params["hash"] = hashKey
        launchOnUI {
            repository.getPlayPath(params).checkResult(
                onSuccess = {
                    playBook.set(it)
                },
                onError = {
                }
            )

        }
    }
}