package com.rm.module_mine.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.business_lib.bean.LoginUserBean
import com.rm.module_mine.api.MineApiService
import com.rm.module_mine.bean.MineInfoDetail
import com.rm.module_mine.bean.MineInfoProfile
import com.rm.module_mine.bean.UpdateUserInfoBean

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
    suspend fun memberProfile(member_id: String): BaseResult<MineInfoProfile> {
        return apiCall { service.getMemberProfile(member_id) }
    }

    /**
     * 关注主播接口
     * @param follow_id String
     */
    suspend fun attentionAnchor(follow_id: String): BaseResult<Any> {
        return apiCall { service.homeAttentionAnchor(follow_id) }
    }

    /**
     * 取消关注主播接口
     * @param follow_id String
     */
    suspend fun unAttentionAnchor(follow_id: String): BaseResult<Any> {
        return apiCall { service.homeUnAttentionAnchor(follow_id) }
    }
}