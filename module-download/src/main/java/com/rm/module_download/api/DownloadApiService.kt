package com.rm.module_download.api

import com.rm.baselisten.net.bean.BaseResponse
import com.rm.business_lib.bean.HomeDetailModel
import com.rm.module_download.bean.DownloadChapterResponseBean
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface DownloadApiService {


    /**
     * 获取下载章节列表（选集下载）
     */
    @POST("audio/chapter/download")
    suspend fun downloadChapter(@Body requestBody: RequestBody): BaseResponse<DownloadChapterResponseBean>


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

    /**
     * 获取听书详情
     */
    @GET("audio/detail")
    suspend fun homeDetail(@Query("audio_id") id: String): BaseResponse<HomeDetailModel>

}