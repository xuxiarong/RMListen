package com.rm.module_listen.api

import com.rm.baselisten.net.bean.BaseResponse
import com.rm.module_listen.bean.*
import com.rm.module_listen.model.ListenSubsModel
import com.rm.module_listen.repository.ListenPatchSheetBean
import retrofit2.http.*

interface ListenApiService {

    /**
     *订阅列表
     */
    @GET("subscription/list")
    suspend fun listenSubscriptionList(): BaseResponse<MutableList<SubscriptionListBean>>

    /**
     * 置顶
     * @param audio_id 音频Id
     */
    @FormUrlEncoded
    @POST("subscription/top")
    suspend fun listenSubscriptionTop(@Field("audio_id") audio_id: String): BaseResponse<Any>

    /**
     * 取消置顶
     * @param audio_id 音频Id
     */
    @DELETE("subscription/top")
    suspend fun listenSubscriptionTCancelTop(@Query("audio_id") audio_id: String): BaseResponse<Any>

    /**
     * 添加订阅
     * @param audio_id 音频Id
     */
    @FormUrlEncoded
    @POST("subscription/list")
    suspend fun listenAddSubscription(@Field("audio_id") audio_id: String): BaseResponse<String>

    /**
     * 取消订阅
     * @param audio_id 音频Id
     */
    @DELETE("subscription/list")
    suspend fun listenUnsubscribe(@Query("audio_id") audio_id: String): BaseResponse<Any>

    /**
     * 导航栏相关
     */
    @GET("listen/navigation")
    suspend fun listenNavigation(): BaseResponse<String>

    /**
     * 最新更新
     */
    @GET("listen/upgrade")
    suspend fun listenUpgrade(): BaseResponse<ListenSubsModel>

    /**
     * 最近收听
     */
    @GET("listen/history")
    suspend fun listenHistory(): BaseResponse<String>

    /**
     * 我的听单列表
     */
    @GET("sheet/my-list")
    suspend fun listenSheetMyList(): BaseResponse<ListenSheetMyListBean>

    /**
     * 收藏听单列表
     */
    @GET("sheet/my-favorite")
    suspend fun listenSheetFavoriteList(): BaseResponse<ListenSheetCollectedBean>

    /**
     * 听单列表
     * @param sheet_id 听单Id
     * @param page 加载页码
     * @param page_size 加载多少条数据
     */
    @GET("sheet/list")
    suspend fun listenSheetList(
        @Query("sheet_id") sheet_id: String,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): BaseResponse<ListenSheetDetailBean>

    /**
     * 添加到听单
     * @param sheet_id 听单Id
     * @param audio_id 音频Id
     */
    @FormUrlEncoded
    @POST("sheet/list")
    suspend fun listenAddSheetList(
        @Field("sheet_id") sheet_id: String,
        @Field("audio_id") audio_id: String
    ): BaseResponse<Any>

    /**
     * 创建听单
     * @param sheet_name 听单名称
     * @param description 听单简介
     */
    @FormUrlEncoded
    @POST("sheet/my-list")
    suspend fun listenCreateSheetList(
        @Field("sheet_name") sheet_name: String,
        @Field("description") description: String
    ): BaseResponse<Any>


    /**
     * 删除听单
     * @param sheet_id 听单ID
     */
    @DELETE("sheet/my-list")
    suspend fun listenDeleteSheet(@Query("sheet_id") sheet_id: String): BaseResponse<Any>

    /**
     * 编辑听单
     * @param bean 需要上传的json
     */
    @PATCH("sheet/my-list")
    suspend fun listenEditSheet(@Body bean: ListenPatchSheetBean): BaseResponse<Any>
}