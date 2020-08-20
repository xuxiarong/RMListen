package com.rm.module_mine.api

import com.rm.baselisten.net.api.BaseRetrofitClient


/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
object RetrofitClient : BaseRetrofitClient() {

    val service by lazy { getService(ListenApiService::class.java, ListenApiService.BASE_URL) }

}