package com.rm.module_download.service

import com.rm.baselisten.util.*
import com.rm.business_lib.bean.download.DownloadAudioBean

/**
 * 用于本地存储下载信息
 */
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
        var list = KEY_DOWNLOAD_URL_LIST.getListString()
        if (!list.contains(downloadAudioBean.url)) {
            list.add(downloadAudioBean.url)
            KEY_DOWNLOAD_URL_LIST.putMMKV(list)
        }
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

    fun getDownloadAudioList(): MutableList<DownloadAudioBean> {
        var beanList = mutableListOf<DownloadAudioBean>()
        KEY_DOWNLOAD_URL_LIST.getListString().forEach {
            it.getObjectMMKV(DownloadAudioBean::class.java)?.apply {
                beanList.add(this)
            }
        }
        return beanList
    }

}