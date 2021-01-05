package com.rm.module_play.api

import com.rm.baselisten.net.bean.BaseResponse
import com.rm.business_lib.bean.AudioDetailBean
import com.rm.business_lib.bean.AudioRecommendList
import com.rm.business_lib.bean.ChapterListModel
import com.rm.module_play.model.AudioCommentsModel
import com.rm.module_play.model.PlayAdChapterModel
import com.rm.module_play.model.PlayAdResultModel
import com.rm.module_play.model.PlayFloorAdModel
import okhttp3.RequestBody
import retrofit2.http.*

/**
 *
 * @des:
 * @data: 9/3/20 5:48 PM
 * @Version: 1.0.0
 */
interface PlayApiService {

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
    ): BaseResponse<ChapterListModel>


    /**
     * 章节列表获取到章节
     */
    @GET("audio/chapter/list")
    suspend fun getChapterListWithId(
        @Query("audio_id") audioId: String,
        @Query("page") page : Int,
        @Query("chapter_id") chapterId:String,
        @Query("page_size") page_size:Int,
        @Query("sort") sort: String
    ): BaseResponse<ChapterListModel>


    /**
     * 章节列表获取到章节
     */
    @GET("audio/chapter/list")
    suspend fun getNextPage(
            @Query("audio_id") audioId: String,
            @Query("chapter_id") chapterId:String,
            @Query("page") page : Int,
            @Query("page_size") page_size:Int,
            @Query("sort") sort: String,
            @Query("direction") direction :String
    ): BaseResponse<ChapterListModel>

    /**
     * 章节列表获取到章节
     */
    @GET("audio/chapter/list")
    suspend fun getPrePage(
            @Query("audio_id") audioId: String,
            @Query("chapter_id") chapterId:String,
            @Query("page") page : Int,
            @Query("page_size") page_size:Int,
            @Query("sort") sort: String,
            @Query("direction") direction :String
    ): BaseResponse<ChapterListModel>

    /**
     * 获取听书详情
     */
    @GET("audio/detail")
    suspend fun homeDetail(@Query("audio_id") id: String): BaseResponse<AudioDetailBean>


    /**
     * 获取听书详情
     */
    @GET("audio/recommend")
    suspend fun getAudioRecommend(@Query("audio_id") audio_id: Long,@Query("page_size") page_size: Int): BaseResponse<AudioRecommendList>

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

    /**
     * 获取评论广告
     * @param requestBody 请求的body
     */
    @POST("adv/content/list")
    suspend fun getCommentAd(@Body requestBody: RequestBody): BaseResponse<PlayAdResultModel>

    /**
     * 获取音频封面广告和章节音频流广告
     */
    @POST("adv/content/list")
    suspend fun getChapterAd(@Body requestBody: RequestBody): BaseResponse<PlayAdChapterModel>

    /**
     * 获取音频楼层广告
     */
    @POST("adv/content/list")
    suspend fun getAudioFloorAd(@Body requestBody: RequestBody): BaseResponse<PlayFloorAdModel>

}