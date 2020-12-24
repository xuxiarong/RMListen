package com.rm.module_mine.repository

import android.util.Log
import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.baselisten.net.util.GsonUtils
import com.rm.baselisten.util.DLog
import com.rm.business_lib.bean.BusinessUpdateVersionBean
import com.rm.business_lib.bean.LoginUserBean
import com.rm.business_lib.bean.BusinessVersionUrlBean
import com.rm.business_lib.utils.DeviceUtils
import com.rm.module_mine.api.MineApiService
import com.rm.module_mine.bean.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Field
import java.io.File

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class MineRepository(private val service: MineApiService) : BaseRepository() {

    /**
     * 更新用户信息
     */
    suspend fun updateUserInfo(bean: UpdateUserInfoBean): BaseResult<LoginUserBean> {
        return apiCall { service.updateInfo(bean) }
    }

    /**
     * 获取用户信息
     */
    suspend fun getUserInfo(): BaseResult<LoginUserBean> {
        return apiCall { service.getUserInfo() }
    }

    // 主播/个人信息
    suspend fun memberDetail(member_id: String): BaseResult<MineInfoDetail> {
        return apiCall { service.memberDetail(member_id) }
    }

    //发布书籍/听单/收藏听单列表
    suspend fun memberProfile(member_id: String): BaseResult<MineInfoProfileBean> {
        return apiCall { service.getMemberProfile(member_id) }
    }

    /**
     * 关注主播接口
     * @param follow_id String
     */
    suspend fun attentionAnchor(follow_id: String): BaseResult<Any> {
        return apiCall { service.mineAttentionAnchor(follow_id) }
    }

    /**
     * 取消关注主播接口
     * @param follow_id String
     */
    suspend fun unAttentionAnchor(follow_id: String): BaseResult<Any> {
        return apiCall { service.mineUnAttentionAnchor(follow_id) }
    }

    /**
     * 用户详情页评论
     * @param member_id 用户ID
     */
    suspend fun mineMemberCommentList(
        member_id: String,
        page: Int,
        pageSize: Int
    ): BaseResult<MineMemberListBean> {
        return apiCall { service.mineMemberCommentList(member_id, page, pageSize) }
    }

    /**
     * 主播详情页评论
     * @param anchor_id 主播id
     */
    suspend fun mineAnchorCommentList(
        anchor_id: String,
        page: Int,
        pageSize: Int
    ): BaseResult<MineMemberListBean> {
        return apiCall { service.mineAnchorCommentList(anchor_id, page, pageSize) }
    }

    /**
     * 关注列表
     * @param member_id Int
     */
    suspend fun mineMemberFollowList(
        member_id: String,
        page: Int,
        pageSize: Int
    ): BaseResult<MineMemberFollowBean> {
        return apiCall { service.mineMemberFollowList(member_id, page, pageSize) }
    }

    /**
     * 粉丝列表
     * @param member_id Int
     */
    suspend fun mineMemberFansList(
        member_id: String,
        page: Int,
        pageSize: Int
    ): BaseResult<MineMemberFansBean> {
        return apiCall { service.mineMemberFansList(member_id, page, pageSize) }
    }

    /**
     * 发布的书籍
     * @param member_id Int
     */
    suspend fun minePublishList(
        member_id: String,
        page: Int,
        pageSize: Int
    ): BaseResult<MinePublishBean> {
        return apiCall { service.minePublishList(member_id, page, pageSize) }
    }

    /**
     * 免费求书
     */
    suspend fun mineRequestBook(
        book_name: String,
        author: String,
        anchor_name: String,
        contact: String
    ): BaseResult<Any> {
        return apiCall {
            val bean = MineGetBookBean(book_name, author, anchor_name, contact)
            val json = GsonUtils.toJson(bean)
            service.mineRequestBook(json.toRequestBody("application/json; charset=utf-8".toMediaType()))
        }
    }

    /**
     * 关于我们
     */
    suspend fun mineAboutUs(): BaseResult<MutableList<MineAboutUsBean>> {
        return apiCall { service.mineAboutUs() }
    }

    /**
     * 版本更新
     */
    suspend fun mineGetLaseUrl(): BaseResult<BusinessVersionUrlBean> {
        val json = GsonUtils.toJson(BusinessUpdateVersionBean())
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        return apiCall { service.mineGetLaseUrl(body) }
    }

    /**
     * 问题反馈
     */
    suspend fun mineFeedback(
        feedback_type: Int?,
        content: String,
        img_list: Array<String>?,
        contact: String?
    ): BaseResult<Any> {
        val json = if (feedback_type in 0..4) {
            val bean = MineUploadFeedbackBean(
                feedback_type,
                content,
                img_list,
                contact,
                DeviceUtils.uniqueDeviceId
            )
            GsonUtils.toJson(bean)
        } else {
            val bean = MineUploadFeedbackBean1(
                content,
                img_list,
                contact,
                DeviceUtils.uniqueDeviceId
            )
            GsonUtils.toJson(bean)
        }
        val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        return apiCall {
            service.mineFeedback(requestBody)
        }
    }

    /**
     * 上传头像
     * @param filePath Int
     */
    suspend fun uploadAvatar(
        filePath: String
    ): BaseResult<MineUploadPic> {
        val file = File(filePath)
        val img = file.asRequestBody("image/png".toMediaType())
        val request = MultipartBody.Builder()
            .addFormDataPart("file", filePath, img)
            .build()

        return apiCall { service.uploadAvatar(request.parts[0]) }
    }

    /**
     * 上传文件
     * @param filePath Int
     */
    suspend fun uploadCommon(
        filePath: String
    ): BaseResult<MineUploadPic> {
        val file = File(filePath)
        val img = file.asRequestBody("image/png".toMediaType())
        val request = MultipartBody.Builder()
            .addFormDataPart("file", filePath, img)
            .build()

        return apiCall { service.uploadCommon(request.parts[0]) }
    }


    /**
     * 发送登陆短信验证码
     * @param area_code String
     * @param phone String
     */
    suspend fun sendMessage(type: String, area_code: String, phone: String): BaseResult<Any> {
        DLog.i("========>sendMessage", "$type    ${Log.getStackTraceString(Throwable())}")
        return apiCall { service.sendMessage(type, subPlusChar(area_code), phone) }
    }

    /**
     * 去掉 "+"
     * @param area_code String
     */
    private fun subPlusChar(area_code: String): String {
        if (!area_code.startsWith("+")) {
            return area_code
        }
        return area_code.substring(1, area_code.length)
    }
}