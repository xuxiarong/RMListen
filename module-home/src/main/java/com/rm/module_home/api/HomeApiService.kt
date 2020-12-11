package com.rm.module_home.api

import com.rm.baselisten.net.bean.BaseResponse
import com.rm.business_lib.bean.*
import com.rm.module_home.bean.CategoryTabListBean
import com.rm.module_home.bean.HomeTopListBean
import com.rm.module_home.bean.MenuSheetBean
import com.rm.module_home.model.ad.HomeBannerAdResultModel
import com.rm.module_home.model.ad.HomeDialogAdResultModel
import com.rm.module_home.model.ad.HomeSingleImgAdResultModel
import com.rm.module_home.model.home.HomeModel
import com.rm.module_home.model.home.detail.HomeCommentBean
import okhttp3.RequestBody
import retrofit2.http.*


/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
interface HomeApiService {


    /**
     * 获取首页数据
     */
    @GET("content/page/home")
    suspend fun getHomeData(): BaseResponse<HomeModel>

    /**
     * 获取听书详情
     */
    @GET("audio/detail")
    suspend fun homeDetail(@Query("audio_id") id: String): BaseResponse<AudioDetailBean>


    /**
     * 评论列表
     */

    @GET("comment/audio-comments")
    suspend fun homeDetailComment(
        @Query("audio_id") audio_id: String,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): BaseResponse<HomeCommentBean>

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
     * 首页-听单
     */
    @GET("content/page/sheet")
    suspend fun homeSheet(): BaseResponse<MenuSheetBean>

    /**
     * 听单列表
     *  @Query("page_id") page_id: String,
     */
    @GET("content/sheet/list")
    suspend fun homeSheetList(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): BaseResponse<SheetListBean>

    /**
     * 听单详情
     */
    @GET("content/sheet/info")
    suspend fun homeSheetInfo(@Query("sheet_id") sheet_id: String): BaseResponse<SheetDetailInfoBean>

    /**
     * 听单音频列表
     */
    @GET("content/sheet/audio-list")
    suspend fun homeAudioList(
        @Query("sheet_id") sheet_id: String,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): BaseResponse<AudioListBean>


    /**
     * 榜单
     */
    @GET("content/rank/list")
    suspend fun homeTopList(
        @Query("rank_type") rankType: String,
        @Query("rank_seg") rankSeg: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): BaseResponse<HomeTopListBean>


    /**
     * 获取专题列表
     * @param page_id Int
     * @param block_id Int
     * @param topic_id Int
     * @param page Int
     * @param page_size Int
     * @return BaseResponse<AudioListBean>
     */
    @GET("content/topic/audio-list")
    suspend fun homeTopicList(
        @Query("page_id") page_id: Int,
        @Query("block_id") block_id: Int,
        @Query("topic_id") topic_id: Int,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int = 10
    ): BaseResponse<AudioListBean>

    /**
     * 收藏听单
     * @param sheet_id 听单Id
     */
    @FormUrlEncoded
    @POST("sheet/my-favorite")
    suspend fun homeFavoritesSheet(@Field("sheet_id") sheet_id: String): BaseResponse<Any>

    /**
     * 取消收藏
     *  @param sheet_id 听单Id
     */
    @DELETE("sheet/my-favorite")
    suspend fun homeUnFavoriteSheet(@Query("sheet_id") sheet_id: String): BaseResponse<Any>


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
     * 获取精品列表tab 类别标签列表
     */
    @GET("content/page/boutique")
    suspend fun getBoutiqueTabList(): BaseResponse<CategoryTabListBean>

    /**
     * 根据分类id获取列表
     * @param classId Int
     * @return BaseResponse<AudioListBean>
     */
    @GET("content/class/audio-list")
    suspend fun getCategoryList(
        @Query("class_id") classId: Int,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): BaseResponse<AudioListBean>


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
     * 发表评论(听书详情页)
     * @param content 评论内容
     * @param audio_id 音频ID
     * @param anchor_id 音频主播ID
     */
    @FormUrlEncoded
    @POST("comment/audio-comments")
    suspend fun homeSendComment(
        @Field("content") content: String,
        @Field("audio_id") audio_id: String,
        @Field("anchor_id") anchor_id: String
    ): BaseResponse<Any>

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
     * 获取首页弹窗广告
     * @param requestBody 请求的body
     */
    @POST("adv/content/list")
    suspend fun getHomeDialogAd(@Body requestBody: RequestBody): BaseResponse<HomeDialogAdResultModel>

    /**
     * 获取首页弹窗广告
     * @param requestBody 请求的body
     */
    @POST("adv/content/list")
    suspend fun getHomeBannerAd(@Body requestBody: RequestBody): BaseResponse<HomeBannerAdResultModel>

    /**
     * 获取首页单图广告
     * @param requestBody 请求的body
     */
    @POST("adv/content/list")
    suspend fun getHomeImgContentAd(@Body requestBody: RequestBody): BaseResponse<HomeSingleImgAdResultModel>


    /**
     * 获取app包下载接口
     */
    @POST("personal/get-version-url")
    suspend fun homeGetLaseUrl(
        @Body body: RequestBody
    ): BaseResponse<BusinessVersionUrlBean>

}