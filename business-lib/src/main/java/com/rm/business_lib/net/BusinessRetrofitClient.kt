package com.rm.business_lib.net

import com.rm.baselisten.net.api.BaseRetrofitClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * desc   : 基础业务的的RetrofitClient
 * date   : 2020/09/01
 * version: 1.0
 */
class BusinessRetrofitClient : BaseRetrofitClient() {
    companion object {
        // 基础host
        const val BASE_URL = "http://10.1.3.12:9602/api/v1_0/"
    }

    override fun handleBuilder(builder: OkHttpClient.Builder) {
        super.handleBuilder(builder)
    }

    fun <S> getService(serviceClass: Class<S>): S {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build().create(serviceClass)
    }
}