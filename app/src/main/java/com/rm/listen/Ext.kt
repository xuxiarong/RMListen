package com.rm.listen

import com.lm.common.net.api.BaseResult
/**
 * Created by luyao
 * on 2020/3/30 16:19
 */
inline fun <T : Any> BaseResult<T>.checkResult(crossinline onSuccess: (T) -> Unit, crossinline onError: (String?) -> Unit) {
    if (this is BaseResult.Success) {
        onSuccess(data)
    } else if (this is BaseResult.Error) {
        onError(exception.message)
    }
}

inline fun <T : Any> BaseResult<T>.checkSuccess(success: (T) -> Unit) {
    if (this is BaseResult.Success) {
        success(data)
    }
}