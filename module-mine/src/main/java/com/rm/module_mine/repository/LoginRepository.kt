package com.rm.module_mine.repository

import com.google.gson.Gson
import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.baselisten.util.Preference
import com.rm.module_mine.api.MineApiService
import com.rm.module_mine.bean.User

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class LoginRepository(val service: MineApiService) : BaseRepository() {

    private var isLogin by Preference(Preference.IS_LOGIN, false)
    private var userJson by Preference(Preference.USER_GSON, "")


    suspend fun login(userName: String, passWord: String): BaseResult<User> {
        return apiCall { service.login(userName, passWord) }.apply {
            isLogin = true
            userJson = Gson().toJson(this)
        }
    }

    suspend fun register(userName: String, passWord: String): BaseResult<User> {
        return apiCall { service.register(userName, passWord, passWord) }.apply {
            isLogin = true
            userJson = Gson().toJson(this)
        }
    }
}