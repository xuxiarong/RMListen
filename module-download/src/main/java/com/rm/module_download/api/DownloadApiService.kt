package com.rm.module_download.api

import com.rm.baselisten.net.bean.BaseResponse
import com.rm.module_download.bean.DownloadChapterItemBean
import com.rm.module_download.bean.DownloadChapterResponseBean
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface DownloadApiService {


    /**
     * 获取下载章节列表（选集下载）
     */
    @POST("audio/chapter/download")
    suspend fun downloadChapter(
        @Query("audio_id") audioId: String,
        @Query("start_sequence") startSequence: Int,
        @Query("end_sequence") endSequence: Int,
        @Query("sequences") sequences: String,
        @Query("type") type: Int
    ): BaseResponse<List<DownloadChapterItemBean>>


    /**
     * 获取章节列表
     */
    @GET("audio/chapter/list")
    suspend fun downloadGetChapterList(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,
        @Query("audio_id") audioId: String,
        @Query("sort") sort: String
    ): BaseResponse<DownloadChapterResponseBean>

}