package com.rm.module_mine.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.baselisten.util.TimeUtils
import com.rm.business_lib.bean.LoginUserBean
import com.rm.module_mine.api.MineApiService
import com.rm.module_mine.bean.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
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

}