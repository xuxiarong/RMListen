package com.rm.module_download.service

import com.rm.baselisten.util.getListString
import com.rm.baselisten.util.getObjectMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.util.removeValuesForKeys
import com.rm.business_lib.bean.download.DownloadAudioBean
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter

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
//        KEY_DOWNLOAD_URL_LIST.putMMKV(mutableListOf())
//        KEY_DOWNLOAD_BOOK_LIST.putMMKV(mutableListOf())
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
        var chapter = audioList[0]
        var downloadAudio = DownloadAudio()
        downloadAudio.audio_id = chapter.bookId.toLong()
        downloadAudio.audio_cover_url = chapter.audioUrl
        downloadAudio.audio_name = chapter.bookName
        downloadAudio.last_sequence = 100
        downloadAudio.status = 1
        downloadAudio.author = "suoloong"

        audioList.forEach {
            var downChapter = DownloadChapter()
            downChapter.chapter_id = chapter.chapter_id.toLong()
            downChapter.audio_id = chapter.bookId.toLong()
            downChapter.sequence = 10
            downChapter.chapter_name = chapter.audioName
            downChapter.size = 1000
            downChapter.duration = 200
            downChapter.need_pay = 1
            downChapter.amount = 2
            downChapter.play_count = "100"
            downChapter.created_at = "00.00"
            downChapter.file_path = "sss"
            downChapter.path_url = chapter.url
            DaoUtil(DownloadChapter::class.java, "").saveOrUpdate(downChapter)
        }
        DaoUtil(DownloadAudio::class.java, "").saveOrUpdate(downloadAudio)
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