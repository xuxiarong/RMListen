package com.rm.baselisten.net.api

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
                BaseResult.Error(response.msg,response.code)
            } else {
                successBlock?.let { it() }
                BaseResult.Success(response.data)
            }
        }
    }
}