package com.rm.module_download.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.module_download.api.DownloadApiService
import com.rm.module_download.bean.DownloadChapterItemBean
import com.rm.module_download.bean.DownloadChapterResponseBean

/**
 * download repository
 */
class DownloadRepository(private val apiService: DownloadApiService) : BaseRepository() {

    companion object {
        const val TYPE_SEQUENCE_SELECTION = 1  //选集下载类型
        const val TYPE_SEQUENCE_CONTINUOUS = 2  //连续选集类型
    }

    /**
     * 选集下载 [1.3,5]
     *
     * @param audioId 音频id
     * @param sequences (序号方式下载)对应的序号使用数组传递  [1.3,5]
     */
    suspend fun downloadChapterSelection(
        audioId: String,
        sequences: String
    ): BaseResult<List<DownloadChapterItemBean>> {
        return apiCall {
            apiService.downloadChapter(
                audioId = audioId,
                startSequence = 0,
                endSequence = 0,
                sequences = sequences,
                type = TYPE_SEQUENCE_SELECTION
            )
        }
    }

    /**
     * 连续选集下载 [1,2,3,4,5]
     *
     * @param audioId 音频id
     * @param startSequence 开始序列 取出大于等于当前值数据
     * @param endSequence 结束序列 取出小于等于当前值数据
     */
    suspend fun downloadChapterContinuous(
        audioId: String,
        startSequence: Int,
        endSequence: Int
    ): BaseResult<List<DownloadChapterItemBean>> {
        return apiCall {
            apiService.downloadChapter(
                audioId = audioId,
                startSequence = startSequence,
                endSequence = endSequence,
                sequences = "",
                type = TYPE_SEQUENCE_CONTINUOUS
            )
        }
    }

    /**
     *  获取章节列表
     */
    suspend fun getDownloadChapterList(page: Int, audioId: String): BaseResult<DownloadChapterResponseBean> {
        return apiCall { apiService.downloadGetChapterList(page = page, pageSize = 20, audioId = audioId, sort = "asc") }
    }

}