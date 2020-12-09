package com.rm.baselisten.net

import com.rm.baselisten.net.api.BaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
suspend inline fun <T : Any> BaseResult<T>.checkResult(
    crossinline onSuccess: (T) -> Unit,
    crossinline onError: (String?) -> Unit
) {
    if (this is BaseResult.Success) {
        withContext(Dispatchers.Main) {
            onSuccess(data)
        }
    } else if (this is BaseResult.Error) {
        withContext(Dispatchers.Main) {
            onError(msg)
        }
    }
}

suspend inline fun <T : Any> BaseResult<T>.checkSuccess(crossinline success: (T) -> Unit) {
    if (this is BaseResult.Success) {
        withContext(Dispatchers.Main) {
            success(data)
        }
    }
}