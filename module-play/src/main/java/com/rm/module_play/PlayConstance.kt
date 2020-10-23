package com.rm.module_play

import com.rm.baselisten.util.getObjectMMKV
import com.rm.module_play.model.PlayLastListenChapterModel

/**
 * desc   :
 * date   : 2020/10/22
 * version: 1.0
 */
object PlayConstance {
    //最后一次收听的章节
    const val PlAY_LAST_LISTEN_CHAPtER = "play_last_listen_chapter"

    fun getLastListenAudioUrl() : String{
        try {
            val lastChapter = PlAY_LAST_LISTEN_CHAPtER.getObjectMMKV(PlayLastListenChapterModel::class.java, PlayLastListenChapterModel())!!
            return lastChapter.audioCoverUrl
        }catch (e : Exception){
            e.printStackTrace()
        }
        return ""
    }

}