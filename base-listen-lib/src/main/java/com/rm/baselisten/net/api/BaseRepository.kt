package com.rm.baselisten.net.api

import android.util.Log
import com.rm.baselisten.net.bean.BaseResponse
import com.rm.baselisten.util.DLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import java.io.IOException

/**
 * desc   :
 * date   : 2020/08/05
 * version: 1.0
 */
open class BaseRepository {

    suspend fun <T : Any> apiCall(call: suspend () -> BaseResponse<T>): BaseResult<T> {
        return try {
            return executeResponse(call())
        } catch (e: Exception) {
            DLog.e("request error ", "${e.message}")
            BaseResult.Error("",-1)
        }
    }

    private suspend fun <T : Any> executeResponse(
        response: BaseResponse<T>, successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
    ): BaseResult<T> {
        return coroutineScope {
            if (response.code != 0) {
                errorBlock?.let { it() }
                val message =
                    if (response.code == 1013 || response.code == 1204 || response.code == 1014) {
                        "登录凭证已过期，请重新登陆"
                    } else {
                        response.msg
                    }
                Log.i("=====>executeResponse", message)
                BaseResult.Error(message,response.code)
            } else {
                successBlock?.let { it() }
                BaseResult.Success(response.data)
            }
        }
    }
}