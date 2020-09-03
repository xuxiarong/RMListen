package com.rm.module_play.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.module_play.api.PlayApiService

/**
 *
 * @des:
 * @data: 9/3/20 5:47 PM
 * @Version: 1.0.0
 */
class BookPlayRepository(val playApi:PlayApiService) : BaseRepository() {

    /**
     * 发送忘记密码短信验证码
     * @param area_code String
     * @param phone String
     */
    suspend fun getBookList(params:Map<String,String>): BaseResult<Any> {
        return apiCall { playApi.getBookList(params) }
    }
}