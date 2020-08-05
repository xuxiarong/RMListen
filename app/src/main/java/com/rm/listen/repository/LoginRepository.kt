package com.rm.listen.repository

import com.google.gson.Gson
import com.lm.common.net.api.BaseApplication
import com.lm.common.net.api.BaseRepository
import com.lm.common.net.api.BaseResult
import com.lm.common.util.Preference
import com.rm.listen.R
import com.rm.listen.api.ListenApiService
import com.rm.listen.bean.User

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class LoginRepository(val service: ListenApiService) : BaseRepository() {

    private var isLogin by Preference(Preference.IS_LOGIN, false)
    private var userJson by Preference(Preference.USER_GSON, "")


    suspend fun login(userName: String, passWord: String): BaseResult<User> {
        return safeApiCall(call = { requestLogin(userName, passWord) },
            errorMessage = BaseApplication.CONTEXT.getString(R.string.app_name))
    }

    // TODO Move into DataSource Layer ?
    private suspend fun requestLogin(userName: String, passWord: String): BaseResult<User> {
        val response = service.login(userName, passWord)

        return executeResponse(response, {
            val user = response.data
            isLogin = true
            userJson = Gson().toJson(user)
        })
    }

    suspend fun register(userName: String, passWord: String): BaseResult<User> {
        return safeApiCall(call = { requestRegister(userName, passWord) }, errorMessage = "注册失败")
    }

    private suspend fun requestRegister(userName: String, passWord: String): BaseResult<User> {
        val response = service.register(userName, passWord, passWord)
        return executeResponse(response, { requestLogin(userName, passWord) })
    }

}