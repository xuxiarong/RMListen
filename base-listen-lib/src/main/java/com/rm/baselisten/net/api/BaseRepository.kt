package com.rm.baselisten.net.api

import com.rm.baselisten.net.bean.BaseResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import java.io.IOException

/**
 * desc   :
 * date   : 2020/08/05
 * version: 1.0
 */
open class BaseRepository {
//    suspend fun <T : Any> apiCall(
//        call: suspend () -> BaseResult<T>,
//        errorMessage: String
//    ): BaseResult<T> {
//        return try {
//            call()
//        } catch (e: Exception) {
//            // An exception was thrown when calling the API so we're converting this to an IOException
//            BaseResult.Error(IOException(errorMessage, e))
//        }
//    }

    suspend fun <T : Any> apiCall(call: suspend () -> BaseResponse<T>): BaseResult<T> {
        return try {
            return executeResponse(call())
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    private suspend fun <T : Any> executeResponse(
        response: BaseResponse<T>, successBlock: (suspend CoroutineScope.() -> Unit)? = null,
        errorBlock: (suspend CoroutineScope.() -> Unit)? = null
    ): BaseResult<T> {
        return coroutineScope {
            if (response.code != 0) {
                errorBlock?.let { it() }
                BaseResult.Error(IOException(response.msg))
            } else {
                successBlock?.let { it() }
                BaseResult.Success(response.data)
            }
        }
    }
}