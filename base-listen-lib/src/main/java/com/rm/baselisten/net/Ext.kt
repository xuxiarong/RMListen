package com.rm.baselisten.net

import android.util.Log
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
    crossinline onError: (String?, Int?) -> Unit
) {

    if (this is BaseResult.Success) {
        withContext(Dispatchers.Main) {
            onSuccess(data)
        }
    } else if (this is BaseResult.Error) {
        withContext(Dispatchers.Main) {
            // 用户未登陆 1013；被强制登出了(被挤下线了) 1204；刷新token失败 1014
            val message =
                if (code == 1013 || code == 1204 || code == 1014) {
                    "登录凭证已过期，请重新登陆"
                } else {
                    msg
                }
            Log.i("=====>checkResult", message)
            onError(message, errorCode)
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