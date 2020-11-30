package com.rm.module_listen.api

import com.rm.baselisten.net.bean.BaseResponse
import com.rm.business_lib.bean.AudioListBean
import com.rm.business_lib.bean.SheetInfoBean
import com.rm.module_listen.bean.SheetFavorBean
import com.rm.module_listen.bean.ListenSheetMyListBean
import com.rm.module_listen.bean.ListenSubscriptionListBean
import com.rm.module_listen.model.ListenChapterList
import com.rm.module_listen.model.ListenCreateSheetModel
import com.rm.module_listen.model.ListenSubsNumberModel
import com.rm.module_listen.repository.ListenPatchSheetBean
import retrofit2.http.*

interface ListenApiService {

    /**
     *订阅列表
     */
    @GET("subscription/list")
    suspend fun listenSubscriptionList(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): BaseResponse<MutableList<ListenSubscriptionListBean>>

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
    suspend fun getSubsNumber(): BaseResponse<ListenSubsNumberModel>

    /**
     * 最新更新
     */
    @GET("listen/upgrade")
    suspend fun listenUpgrade(
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): BaseResponse<ListenChapterList>


    /**
     * 订阅更新上报
     */
    @POST("report/report")
    suspend fun listenSubsReport(
        @Query("report_type") report_type: String,
        @Query("member_id") member_id: String
        ): BaseResponse<Any>

    /**
     * 最近收听
     */
    @GET("listen/history")
    suspend fun listenHistory(): BaseResponse<String>

    /**
     * 我的听单列表
     */
    @GET("sheet/my-list")
    suspend fun listenSheetMyList(
        @Query("page") page: Int,
        @Query("page_size") page_size: Int,
        @Query("member_id") member_id: String//传参表示请求他人数据，不传读取登陆态用户id
    ): BaseResponse<ListenSheetMyListBean>

    /**
     * 我的听单列表
     */
    @GET("sheet/my-list")
    suspend fun listenSheetMyList(
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): BaseResponse<ListenSheetMyListBean>

    /**
     * 收藏听单列表
     */
    @GET("sheet/my-favorite")
    suspend fun listenSheetFavoriteList(
        @Query("page") page: Int,
        @Query("page_size") page_size: Int,
        @Query("member_id") member_id: String//传参表示请求他人数据，不传读取登陆态用户id
    ): BaseResponse<SheetFavorBean>

    /**
     * 收藏听单列表
     */
    @GET("sheet/my-favorite")
    suspend fun listenSheetFavoriteList(
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): BaseResponse<SheetFavorBean>

    /**
     * 听单详情
     * @param sheet_id 听单Id
     */
    @GET("content/sheet/info")
    suspend fun listenSheetInfo(
        @Query("sheet_id") sheet_id: String
    ): BaseResponse<SheetInfoBean>

    /**
     * 听单音频列表
     * @param page Int
     * @param page_size Int
     */
    @GET("sheet/list")
    suspend fun listenAudioList(
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): BaseResponse<AudioListBean>


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
    ): BaseResponse<ListenCreateSheetModel>


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

    /**
     * 将音频从听单移除
     * @param sheetId 听单ID
     * @param audioId 音频ID
     */
    @DELETE("sheet/list")
    suspend fun listenRemoveAudio(
        @Query("sheet_id") sheetId: String,
        @Query("audio_id") audioId: String
    ): BaseResponse<Any>

}