package com.rm.business_lib.db.download

import com.rm.baselisten.util.DLog
import com.rm.business_lib.db.*
import com.rm.business_lib.download.DownloadConstant
import com.rm.business_lib.download.file.DownLoadFileUtils

/**
 * desc   :
 * date   : 2020/11/18
 * version: 1.0
 */
object DownloadDaoUtils {

    private val downloadAudioDao by lazy {
        DaoUtil(DownloadAudio::class.java, "")
    }

    private val downloadChapterDao by lazy {
        DaoUtil(DownloadChapter::class.java, "")
    }

    fun queryAudioById(audioId: Long): DownloadAudio? {
        try {
            val queryBuilder = downloadAudioDao.queryBuilder()
            if (queryBuilder != null) {
                val result =
                    queryBuilder.where(DownloadAudioDao.Properties.Audio_id.eq(audioId)).list()
                return if (null != result && result.isNotEmpty()) {
                    result[0]
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun deleteFinishChapterByAudio(audioId: Long){
        try {
            val queryBuilder = downloadChapterDao.queryBuilder()
            if (queryBuilder != null) {
                val result = queryBuilder
                    .where(DownloadChapterDao.Properties.Audio_id.eq(audioId))
                    .where(DownloadChapterDao.Properties.Down_status.eq(DownloadConstant.CHAPTER_STATUS_DOWNLOAD_FINISH))
                    .list()
                if(result!=null){
                    downloadChapterDao.delete(result)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun queryAllFinishChapterWithAudioId(audio: DownloadAudio) : List<DownloadChapter>?{
        try {
            val queryBuilder = downloadChapterDao.queryBuilder()
            if (queryBuilder != null) {
                val result = queryBuilder
                        .where(DownloadChapterDao.Properties.Audio_id.eq(audio.audio_id))
                        .list()
                if(result!=null){
                    val iterator = result.iterator()
                    audio.download_num = 0
                    audio.down_size = 0L
                    var listenFinishNum = 0
                    while(iterator.hasNext()){
                        val next = iterator.next()
                        if(!DownLoadFileUtils.checkChapterDownFinish(next)){
                            iterator.remove()
                        }else{
                            if(next.listen_duration >= next.realDuration){
                                listenFinishNum++
                            }
                            audio.download_num +=1
                            audio.down_size += next.size
                        }
                    }
                    audio.listen_finish = listenFinishNum>0 && listenFinishNum == result.size
                    return result
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            DLog.d("suolong_DownloadDaoUtils","${e.cause}")
        }
        return null
    }

}