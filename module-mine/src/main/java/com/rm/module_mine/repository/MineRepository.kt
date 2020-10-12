package com.rm.module_mine.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.module_mine.api.MineApiService
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
    suspend fun updateUserInfo(bean: UpdateUserInfoBean): BaseResult<Any> {
        return apiCall { service.updateInfo(bean) }
    }

}