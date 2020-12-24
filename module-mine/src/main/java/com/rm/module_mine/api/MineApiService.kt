package com.rm.module_mine.api

import com.rm.baselisten.net.bean.BaseResponse
import com.rm.business_lib.bean.LoginUserBean
import com.rm.business_lib.bean.BusinessVersionUrlBean
import com.rm.module_mine.bean.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
interface MineApiService {

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
    suspend fun updateInfo(@Body bean: UpdateUserInfoBean): BaseResponse<LoginUserBean>

    /**
     * 获取用户信息
     */
    @GET("member/info")
    suspend fun getUserInfo(): BaseResponse<LoginUserBean>

    /**
     * 上传头像
     */
    @Multipart
    @POST("member/avatars")
    suspend fun uploadAvatar(@Part body: MultipartBody.Part): BaseResponse<MineUploadPic>

    /**
     * 文件上传
     */
    @Multipart
    @POST("file/upload/common")
    suspend fun uploadCommon(@Part body: MultipartBody.Part): BaseResponse<MineUploadPic>

    /**
     * 用户/主播详情
     */
    @GET("member/detail")
    suspend fun memberDetail(@Query("member_id") member_id: String): BaseResponse<MineInfoDetail>

    /**
     * 主页 ->发布书籍/听单/收藏听单列表 : 请求他人数据，不传读取登陆态用户id
     */
    @GET("member/profile")
    suspend fun getMemberProfile(@Query("member_id") member_id: String): BaseResponse<MineInfoProfileBean>

    /**
     * 主播详情页评论
     * @param anchor_id 主播id
     */
    @GET("comment/anchor-comments")
    suspend fun mineAnchorCommentList(
        @Query("anchor_id") anchor_id: String,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): BaseResponse<MineMemberListBean>

    /**
     * 用户详情页评论
     * @param member_id 用户ID
     */
    @GET("comment/member-comments")
    suspend fun mineMemberCommentList(
        @Query("member_id") member_id: String,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): BaseResponse<MineMemberListBean>


    /**
     * 关注主播接口
     * @param follow_id Int
     */
    @FormUrlEncoded
    @POST("member/follow")
    suspend fun mineAttentionAnchor(@Field("follow_id") follow_id: String): BaseResponse<Any>

    /**
     * 取消关注主播接口
     * @param follow_id Int
     */
    @DELETE("member/follow")
    suspend fun mineUnAttentionAnchor(@Query("follow_id") follow_id: String): BaseResponse<Any>


    /**
     * 关注列表
     * @param member_id Int
     */
    @GET("member/follow-list")
    suspend fun mineMemberFollowList(
        @Query("member_id") member_id: String,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): BaseResponse<MineMemberFollowBean>


    /**
     * 粉丝列表
     * @param member_id Int
     */
    @GET("member/fans-list")
    suspend fun mineMemberFansList(
        @Query("member_id") member_id: String,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): BaseResponse<MineMemberFansBean>


    /**
     * 发布的书籍
     * @param member_id Int
     */
    @GET("audio/publish")
    suspend fun minePublishList(
        @Query("member_id") member_id: String,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): BaseResponse<MinePublishBean>

    /**
     * 关于我们
     */
    @GET("others/about-us")
    suspend fun mineAboutUs(): BaseResponse<MutableList<MineAboutUsBean>>

    /**
     *获取app最新版本接口
     */
    @GET("personal/get-latest-version")
    suspend fun mineGetLaseVersion(@Query("platform") platform: String): BaseResponse<MineGetLastVersionBean>

    /**
     * 免费求书
     *
     */
    @POST("personal/request-book")
    suspend fun mineRequestBook(
        @Body body: RequestBody
    ): BaseResponse<Any>

    /**
     * 获取app包下载接口
     * @param version 书名
     * @param platform 作者名
     */
    @POST("personal/get-version-url")
    suspend fun mineGetLaseUrl(
        @Body body: RequestBody
    ): BaseResponse<BusinessVersionUrlBean>

    /**
     * 反馈意见
     * @param content 反馈内容
     * @param feedback_type 反馈类型（0：其它，1：功能异常，2：体验不佳，3：产品建议）
     * @param contact 联系方式

     */
    @POST("member/feedback")
    suspend fun mineFeedback(
        @Body body: RequestBody
    ): BaseResponse<Any>

    /**
     * 发送短信验证码
     * @param type String  发送短信类型，"login" = 登陆，"forget_pwd" = 忘记密码,"rebind_phone" = 重新绑定手机
     * @param area_code String 手机所在区号(例：+86)
     * @param phone String 接收短信验证码的手机号码
     * @return BaseResponse<*>
     */
    @POST("sms/code")
    suspend fun sendMessage(
        @Query("type") type: String,
        @Query("area_code") area_code: String,
        @Query("phone") phone: String
    ): BaseResponse<Any>

}