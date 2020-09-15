package com.rm.module_listen.api

import com.rm.baselisten.net.bean.BaseResponse
import com.rm.module_listen.bean.ListenSheetCollectedBean
import com.rm.module_listen.bean.ListenSheetDetailBean
import com.rm.module_listen.bean.ListenSheetMyListBean
import com.rm.module_listen.bean.SubscriptionListBean
import com.rm.module_listen.model.ListenSubsModel
import retrofit2.http.*

interface ListenApiService {

    /**
     *订阅列表
     */
    @GET("subscription/list")
    suspend fun listenSubscriptionList(): BaseResponse<MutableList<SubscriptionListBean>>

    /**
     * 置顶
     */
    @FormUrlEncoded
    @POST("subscription/top")
    suspend fun listenSubscriptionTop(@Field("audio_id") audio_id: String): BaseResponse<Any>

    /**
     * 取消置顶
     */
    @DELETE("subscription/top")
    suspend fun listenSubscriptionTCancelTop(@Query("audio_id") audio_id: String): BaseResponse<Any>

    /**
     * 添加订阅
     */
    @FormUrlEncoded
    @POST("subscription/list")
    suspend fun listenAddSubscription(@Field("audio_id") audio_id: String): BaseResponse<String>

    /**
     * 取消订阅
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
     */
    @GET("sheet/list")
    suspend fun listenSheetList(
        @Query("sheet_id") sheet_id: String,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): BaseResponse<ListenSheetDetailBean>

}