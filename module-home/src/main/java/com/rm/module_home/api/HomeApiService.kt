package com.rm.module_home.api

import com.rm.baselisten.net.bean.BaseResponse
import com.rm.module_home.model.home.detail.HomeDetailModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
interface HomeApiService {

    companion object {
        const val BASE_URL = "http://192.168.11.217:3000/mock/154/api/v1_0"
    }

    @FormUrlEncoded
    @POST("/lg/user_article/add/json")
    suspend fun test(@Field("title") title: String, @Field("link") url: String): BaseResponse<String>

    /**
     * 获取听书详情
     */
/*    @FormUrlEncoded
    @GET("/api/v1_0/audio/detail")
    suspend fun HomeDetail(@Field ("id") id:String) :BaseResponse<HomeDetailModel>*/

    @FormUrlEncoded
    @GET("/audio/detail")
    suspend fun HomeDetail() :BaseResponse<HomeDetailModel>

    /**
     * 评论列表
     */
    @FormUrlEncoded
    @GET("/api/v1_0/comment/audio-comments")
    suspend fun  HomeDetail_Comment(@Field("audio_id") audio_id:String ,
                                    @Field("page") page:Int ,
                                    @Field("page_size") page_size : Int) :BaseResponse<String>
    /**
     * 章节列表
     */
    @FormUrlEncoded
    @GET("/api/v1_0/chapter/list")
    suspend fun HomeChapter(@Field("page") page : Int ,
                            @Field("page_size") page_size:Int):BaseResponse<String>
    /**
     * 下载音频章节
     */
    @FormUrlEncoded
    @POST("/api/v1_0/audio/chapter/download")
    suspend fun HomeChapterDown(@Field("audio_id") audio_id : Int,
                                @Field("start_sequence") start_sequence:Int,
                                @Field("end_sequence") end_sequence:Int,
                                @Field("sequences") sequences:String ,
                                @Field("type") type : Int):BaseResponse<String>
}