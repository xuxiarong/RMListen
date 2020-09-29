package com.rm.module_download.service

import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.util.removeValuesForKeys
import com.rm.business_lib.bean.download.DownloadAudioBean

class DownloadAudioCache {


    companion object {
        @JvmStatic
        val INSTANCE: DownloadAudioCache by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { DownloadAudioCache() }
        private const val KEY_DOWNLOAD_URL_LIST = "KEY_DOWNLOAD_URL_LIST"
        private const val KEY_DOWNLOAD_BOOK_LIST = "KEY_DOWNLOAD_BOOK_LIST"
    }

    init {
        KEY_DOWNLOAD_URL_LIST.putMMKV(mutableListOf())
        KEY_DOWNLOAD_BOOK_LIST.putMMKV(mutableListOf())
    }

    fun saveAudio(downloadAudioBean: DownloadAudioBean) {
        downloadAudioBean.url.putMMKV(downloadAudioBean)
    }

    fun saveAudio(audioList: List<DownloadAudioBean>) {
        audioList.forEach { saveAudio(it) }
    }

    fun deleteAudio(url: String) {
        removeValuesForKeys(url)
    }

    fun deleteAudio(list: List<String>) {
        list.forEach {
            removeValuesForKeys(it)
        }
    }

}