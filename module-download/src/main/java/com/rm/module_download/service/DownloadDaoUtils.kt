package com.rm.module_download.service

import com.rm.baselisten.util.removeValuesForKeys
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter

/**
 * 用于本地存储下载信息
 */
class DownloadDaoUtils {


    companion object {
        @JvmStatic
        val INSTANCE: DownloadDaoUtils by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { DownloadDaoUtils() }
        private const val KEY_DOWNLOAD_URL_LIST = "KEY_DOWNLOAD_URL_LIST"
        private const val KEY_DOWNLOAD_BOOK_LIST = "KEY_DOWNLOAD_BOOK_LIST"
    }

    init {
//        KEY_DOWNLOAD_URL_LIST.putMMKV(mutableListOf())
//        KEY_DOWNLOAD_BOOK_LIST.putMMKV(mutableListOf())
    }

    fun saveAudio(audio: DownloadAudio) {
        DaoUtil(DownloadAudio::class.java, "").saveOrUpdate(audio)
    }

    fun deleteAudio(url: String) {
        removeValuesForKeys(url)
    }

    fun deleteAudio(list: List<String>) {
        list.forEach {
            removeValuesForKeys(it)
        }
    }

    fun getDownloadAudioList(): MutableList<DownloadChapter> {

        return mutableListOf()
    }

}