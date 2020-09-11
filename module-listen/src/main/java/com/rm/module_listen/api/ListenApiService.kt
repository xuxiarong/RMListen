package com.rm.module_listen.api

import com.rm.baselisten.net.bean.BaseResponse
import com.rm.module_listen.bean.ListenSheetCollectedBean
import com.rm.module_listen.bean.ListenSheetDetailBean
import com.rm.module_listen.bean.ListenSheetMyListBean
import com.rm.module_listen.bean.SubscriptionListBean
import retrofit2.http.*

interface ListenApiService {

    /**
     *订阅列表
     */
    @GET("/api/v1_0/subscription/list")
    suspend fun listenSubscriptionList(): BaseResponse<MutableList<SubscriptionListBean>>

    /**
     * 置顶
     */
    @FormUrlEncoded
    @POST("/api/v1_0/subscription/top")
    suspend fun listenSubscriptionTop(@Field("audio_id") audio_id: String): BaseResponse<Any>

    /**
     * 取消置顶
     */
    @DELETE("/api/v1_0/subscription/top")
    suspend fun listenSubscriptionTCancelTop(@Query("audio_id") audio_id: String): BaseResponse<Any>

    /**
     * 添加订阅
     */
    @FormUrlEncoded
    @POST("/api/v1_0/subscription/list")
    suspend fun listenAddSubscription(@Field("audio_id") audio_id: String): BaseResponse<String>

    /**
     * 取消订阅
     */
    @DELETE("/api/v1_0/subscription/list")
    suspend fun listenUnsubscribe(@Query("audio_id") audio_id: String): BaseResponse<Any>

    /**
     * 导航栏相关
     */
    @GET("/api/v1_0/listen/navigation")
    suspend fun listenNavigation(): BaseResponse<String>

    /**
     * 最新更新
     */
    @GET("/api/v1_0/listen/upgrade")
    suspend fun listenUpgrade(): BaseResponse<String>

    /**
     * 最近收听
     */
    @GET("/api/v1_0/listen/history")
    suspend fun listenHistory(): BaseResponse<String>

    /**
     * 我的听单列表
     */
    @GET("/api/v1_0/sheet/my-list")
    suspend fun listenSheetMyList(): BaseResponse<ListenSheetMyListBean>

    /**
     * 收藏听单列表
     */
    @GET("/api/v1_0/sheet/my-favorite")
    suspend fun listenSheetFavoriteList(): BaseResponse<ListenSheetCollectedBean>

    /**
     * 听单列表
     */
    @GET("/api/v1_0/sheet/list")
    suspend fun listenSheetList(
        @Query("sheet_id") sheet_id: String,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): BaseResponse<ListenSheetDetailBean>

}