package com.rm.business_lib.db.listen

import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.ListenAudioEntityDao
import com.rm.business_lib.db.ListenChapterEntityDao

/**
 * desc   :
 * date   : 2020/11/18
 * version: 1.0
 */
object ListenDaoUtils {

    private val listenAudioDao by lazy {
        DaoUtil(ListenAudioEntity::class.java, "")
    }

    private val listenChapterDao by lazy {
        DaoUtil(ListenChapterEntity::class.java, "")
    }

    fun getAllAudioByRecent(): MutableList<ListenAudioEntity> {
        try {
            val queryBuilder = listenAudioDao.queryBuilder()
            if (queryBuilder != null) {
                val result =
                        queryBuilder.orderDesc(ListenAudioEntityDao.Properties.UpdateMillis).list()
                return result
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mutableListOf()
    }

    fun getAllAudioByRecentLimit10(): MutableList<ListenAudioEntity> {
        try {
            val queryBuilder = listenAudioDao.queryBuilder()
            if (queryBuilder != null) {
                val result =
                        queryBuilder.orderDesc(ListenAudioEntityDao.Properties.UpdateMillis).limit(10)
                                .list()
                return result
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mutableListOf()
    }

    fun deleteAudio(audio : ListenAudioEntity){
        listenAudioDao.delete(audio)
    }

    fun queryAudioById(audioId: Long): ListenAudioEntity? {
        try {
            val queryBuilder = listenAudioDao.queryBuilder()
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


    fun saveOrUpdateAudio(audioEntity: ListenAudioEntity) {

    }

    /**
     * 根据AudioID,chapterID，时间倒叙查询第一条章节
     */
    fun queryChapterRecentUpdate(audioId: Long, chapterID: Long): ListenChapterEntity? {
        try {
            val queryBuilder = listenChapterDao.queryBuilder()
            if (queryBuilder != null) {
                val result = queryBuilder
                        .where(ListenChapterEntityDao.Properties.Audio_id.eq(audioId))
                        .where(ListenChapterEntityDao.Properties.Chapter_id.eq(chapterID))
                        .orderDesc(ListenChapterEntityDao.Properties.UpdateMillis).list()
                return if (null != result && result.isNotEmpty()) {
                    result[0]
                } else {
                    null
                }
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
        return null

    }

    fun queryAllChapterByUpdateTimeDesc() {

    }


}