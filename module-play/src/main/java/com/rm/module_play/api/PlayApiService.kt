package com.rm.module_play.api

import com.rm.baselisten.net.bean.BaseResponse
import com.rm.business_lib.bean.AudioChapterListModel
import com.rm.business_lib.bean.HomeDetailBean
import com.rm.module_play.model.AudioCommentsModel
import com.rm.module_play.test.SearchMusicData
import com.rm.module_play.test.SearchResult
import retrofit2.http.*

/**
 *
 * @des:
 * @data: 9/3/20 5:48 PM
 * @Version: 1.0.0
 */
interface PlayApiService {
    @Headers("BaseUrlName:baidu")//静态替换
    @GET("/search/song")
    suspend fun getBookList(@QueryMap map: Map<String, String>): BaseResponse<SearchResult>

    @Headers(
        "BaseUrlName:play",
        "Cookie:kg_mid=8a18832e9fc0845106e1075df481c1c2;Hm_lvt_aedee6983d4cfc62f509129360d6bb3d=1557584633",
        "User-Agent:Mozilla/5.0 (Windows NT 10.0; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0"
    )//静态替换
    @GET("/")
    suspend fun getPlayPath(@QueryMap map: Map<String, String>): BaseResponse<SearchMusicData>

    /**
     * 用户评论列表
     */
    @GET("comment/member-comments")
    suspend fun getMemberComments(
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): BaseResponse<Any>

    /**
     * 评论点赞和取消点赞
     */
    @POST("comment/like")
    suspend fun likeComment(
        @Field("comment_id") comment_id: Int
    ): BaseResponse<Any>

    /**
     * 用户评论列表
     */
    @GET("comment/audio-comments")
    suspend fun commentAudioComments(
        @Query("audio_id") audioId: String,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): BaseResponse<AudioCommentsModel>

    /**
     * 播放上报
     */
    @POST("report/report")
    suspend fun playerReport(
        @Field("report_type") report_type: String,
        @Field("audio_id") audio_id: String,
        @Field("chapter_id") chapter_id: String
    ): BaseResponse<Any>


    /**
     * 章节列表
     */
    @GET("audio/chapter/list")
    suspend fun chapterList(
        @Query("audio_id") audioId: String,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int,
        @Query("sort") sort: String
    ): BaseResponse<AudioChapterListModel>


    /**
     * 章节列表获取到章节
     */
    @GET("audio/chapter/list")
    suspend fun chapterPage(
        @Query("audio_id") audioId: String,
        @Query("chapter_id") chapterId:String,
        @Query("sort") sort: String
    ): BaseResponse<AudioChapterListModel>

    /**
     * 获取听书详情
     */
    @GET("audio/detail")
    suspend fun homeDetail(@Query("audio_id") id: String): BaseResponse<HomeDetailBean>

    /**
     * 评论点赞
     * @param comment_id 评论Id
     */
    @FormUrlEncoded
    @POST("comment/like")
    suspend fun homeLikeComment(@Field("comment_id") comment_id: String): BaseResponse<Any>

    /**
     * 取消点赞
     * @param comment_id 评论Id
     */
    @DELETE("comment/like")
    suspend fun homeUnLikeComment(@Query("comment_id") comment_id: String): BaseResponse<Any>



    /**
     * 关注主播接口
     * @param follow_id Int
     */
    @FormUrlEncoded
    @POST("member/follow")
    suspend fun homeAttentionAnchor(@Field("follow_id") follow_id: String): BaseResponse<Any>

    /**
     * 取消关注主播接口
     * @param follow_id Int
     */
    @DELETE("member/follow")
    suspend fun homeUnAttentionAnchor(@Query("follow_id") follow_id: String): BaseResponse<Any>


    /**
     * 添加订阅
     * @param audio_id 音频Id
     */
    @FormUrlEncoded
    @POST("subscription/list")
    suspend fun homeAddSubscription(@Field("audio_id") audio_id: String): BaseResponse<Any>

    /**
     * 取消订阅
     * @param audio_id 音频Id
     */
    @DELETE("subscription/list")
    suspend fun homeCancelSubscription(@Query("audio_id") audio_id: String): BaseResponse<Any>


}