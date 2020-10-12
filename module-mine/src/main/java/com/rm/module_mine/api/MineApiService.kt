package com.rm.module_mine.api

import com.rm.baselisten.net.bean.BaseResponse
import com.rm.module_mine.bean.UpdateUserInfoBean
import com.rm.module_mine.bean.User
import okhttp3.MultipartBody
import retrofit2.http.*


/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
interface MineApiService {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com"
    }

    @FormUrlEncoded
    @POST("/lg/user_article/add/json")
    suspend fun shareArticle(
        @Field("title") title: String,
        @Field("link") url: String
    ): BaseResponse<String>

    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(
        @Field("username") userName: String,
        @Field("password") passWord: String
    ): BaseResponse<User>

    @GET("/user/logout/json")
    suspend fun logOut(): BaseResponse<Any>

    @FormUrlEncoded
    @POST("/user/register")
    suspend fun register(
        @Field("username") userName: String,
        @Field("password") passWord: String,
        @Field("repassword") rePassWord: String
    ): BaseResponse<User>

    /**
     * 更新用户信息
     */
    @PATCH("member/info")
    suspend fun updateInfo(@Body bean: UpdateUserInfoBean): BaseResponse<Any>


    /**
     * 上传头像
     */
    @Multipart
    @POST("member/avatars")
    suspend fun uploadAvatar(@Part body: MultipartBody.Part): BaseResponse<Any>


}