package com.rm.module_main.api

import com.rm.baselisten.net.bean.BaseResponse
import com.rm.business_lib.bean.BusinessVersionUrlBean
import com.rm.business_lib.bean.ChapterListModel
import com.rm.module_main.model.MainAdResultModel
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * desc   :
 * date   : 2020/12/07
 * version: 1.0
 */
interface MainApiService {

    /**
     * 获取开屏广告
     * @param requestBody 请求的body
     */
    @POST("adv/content/list")
    suspend fun getSplashAd(@Body requestBody: RequestBody): BaseResponse<MainAdResultModel>

    /**
     * 获取app包下载接口
     */
    @POST("personal/get-version-url")
    suspend fun homeGetLaseUrl(
        @Body body: RequestBody
    ): BaseResponse<BusinessVersionUrlBean>

    /**
     * 章节列表获取到章节
     */
    @GET("audio/chapter/list")
    suspend fun chapterPage(
        @Query("audio_id") audioId: String,
        @Query("chapter_id") chapterId:String,
        @Query("page_size") page_size:Int,
        @Query("sort") sort: String
    ): BaseResponse<ChapterListModel>

}