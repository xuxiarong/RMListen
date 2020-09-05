package com.rm.module_home.api

import com.rm.baselisten.net.bean.BaseResponse
import com.rm.business_lib.bean.SheetListBean
import com.rm.module_home.bean.MenuSheetBean
import com.rm.module_home.bean.MenuSheetInfoBean
import com.rm.module_home.model.home.detail.HomeDetailModel
import retrofit2.http.*


/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
interface HomeApiService {

    companion object {
        const val BASE_URL = "http://10.1.3.12:9602/"
    }

    @FormUrlEncoded
    @POST("/lg/user_article/add/json")
    suspend fun test(
        @Field("title") title: String,
        @Field("link") url: String
    ): BaseResponse<String>

    /**
     * 获取听书详情
     */
    @FormUrlEncoded
    @GET("/api/v1_0/audio/detail")
    suspend fun HomeDetail(@Field("id") id: String): BaseResponse<HomeDetailModel>


    /**
     * 评论列表
     */
    @FormUrlEncoded
    @GET("/api/v1_0/comment/audio-comments")
    suspend fun HomeDetail_Comment(
        @Field("audio_id") audio_id: String,
        @Field("page") page: Int,
        @Field("page_size") page_size: Int
    ): BaseResponse<String>

    /**
     * 章节列表
     */
    @FormUrlEncoded
    @GET("/api/v1_0/chapter/list")
    suspend fun HomeChapter(
        @Field("page") page: Int,
        @Field("page_size") page_size: Int
    ): BaseResponse<String>

    /**
     * 下载音频章节
     */
    @FormUrlEncoded
    @POST("/api/v1_0/audio/chapter/download")
    suspend fun HomeChapterDown(
        @Field("audio_id") audio_id: Int,
        @Field("start_sequence") start_sequence: Int,
        @Field("end_sequence") end_sequence: Int,
        @Field("sequences") sequences: String,
        @Field("type") type: Int
    ): BaseResponse<String>

    /**
     * 首页-听单
     */
    @GET("/api/v1_0/content/page/sheet")
    suspend fun homeSheet(): BaseResponse<MenuSheetBean>

    /**
     * 听单详情
     */
    @GET("/api/v1_0/content/sheet/info")
    suspend fun homeSheetInfo(
        @Query("page_id") page_id: String,
        @Query("sheet_id") sheet_id: String,
        @Query("member_id") member_id: String
    ): BaseResponse<MenuSheetInfoBean>


}