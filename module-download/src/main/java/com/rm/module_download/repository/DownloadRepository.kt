package com.rm.module_download.repository

import com.mei.orc.util.json.toJson
import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.module_download.api.DownloadApiService
import com.rm.module_download.bean.DownloadAudioResponseBean
import com.rm.module_download.bean.DownloadChapterRequestBean
import com.rm.module_download.bean.DownloadChapterResponseBean
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * download repository
 */
class DownloadRepository(private val apiService: DownloadApiService) : BaseRepository() {

    companion object {
//        const val TYPE_SEQUENCE_SELECTION = 1  // 序号方式下载
        const val TYPE_SEQUENCE_CONTINUOUS = 2  //选集下载
    }

    /**
     * 连续选集下载 [1,2,3,4,5]
     *
     * @param audioId 音频id
     * @param startSequence 开始序列 取出大于等于当前值数据
     * @param endSequence 结束序列 取出小于等于当前值数据
     */
    suspend fun downloadChapterSelection(
        audioId: Long,
        startSequence: Int,
        endSequence: Int
    ): BaseResult<DownloadChapterResponseBean> {
        return apiCall {
            val requestBean = DownloadChapterRequestBean(
                audio_id = audioId,
                start_sequence = startSequence,
                end_sequence = endSequence,
                type = TYPE_SEQUENCE_CONTINUOUS
            )
            apiService.downloadChapter(requestBean.toJson().toString().toRequestBody("application/json;charset=utf-8".toMediaType()))
        }
    }

//    /**
//     *  获取章节列表
//     */
//    suspend fun getDownloadChapterList(page: Int, pageSize: Int, audioId: Long): BaseResult<DownloadChapterResponseBean> {
//        return apiCall { apiService.downloadGetChapterList(page = page, pageSize = pageSize, audioId = audioId.toString() ,sort = "asc") }
//    }

    /**
     *  获取章节列表
     */
    suspend fun downloadChapterList(page: Int, pageSize: Int, audioId: Long): BaseResult<DownloadAudioResponseBean> {
        return apiCall { apiService.downloadChapterList(page = page, pageSize = pageSize, audioId = audioId.toString() ,sort = "asc") }
    }

}