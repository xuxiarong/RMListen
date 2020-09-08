package com.rm.business_lib.coroutinepermissions

/**
 *
 * @des:
 * @data: 9/8/20 2:30 PM
 * @Version: 1.0.0
 */
interface FailCallback {
    fun onFailed()
}
interface RequestResultListener {
    fun onSuccess()
    fun onFailed()
}
interface SuccessCallback {
    fun onSuccess()
}