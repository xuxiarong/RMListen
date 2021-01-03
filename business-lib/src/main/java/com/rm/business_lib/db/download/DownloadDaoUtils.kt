package com.rm.business_lib.db.download

import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.ListenAudioEntityDao
import com.rm.business_lib.db.ListenChapterEntityDao
import com.rm.business_lib.db.listen.ListenAudioEntity
import com.rm.business_lib.db.listen.ListenChapterEntity
import com.rm.business_lib.download.DownloadConstant

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
                    queryBuilder.where(ListenAudioEntityDao.Properties.Audio_id.eq(audioId)).list()
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

    fun deleteChapterByAudio(audioId: Long){
        try {
            val queryBuilder = downloadChapterDao.queryBuilder()
            if (queryBuilder != null) {
                val result = queryBuilder
                    .where(ListenChapterEntityDao.Properties.Audio_id.eq(audioId)).list()
                if(result!=null){
                    downloadChapterDao.delete(result)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun queryAllFinishChapterWithAudioId(audioId: Long) : List<DownloadChapter>?{
        try {
            val queryBuilder = downloadChapterDao.queryBuilder()
            if (queryBuilder != null) {
                val result = queryBuilder
                        .where(ListenChapterEntityDao.Properties.Audio_id.eq(audioId))
                        .where(ListenChapterEntityDao.Properties.Down_status.eq(DownloadConstant.CHAPTER_STATUS_DOWNLOAD_FINISH))
                        .list()
                if(result!=null){
                    return result
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    fun saveOrUpdateAudio(audioEntity: ListenAudioEntity) {

    }



}