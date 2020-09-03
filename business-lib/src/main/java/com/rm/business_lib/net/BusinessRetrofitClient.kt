package com.rm.business_lib.net

import androidx.annotation.Nullable
import com.rm.baselisten.net.api.BaseRetrofitClient
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
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
        const val  NEW_URL="http://mobilecdn.kugou.com/api/v3"
        const val OLD_HOST="http://10.1.3.12:9602"

    }

    override fun handleBuilder(builder: OkHttpClient.Builder) {
        super.handleBuilder(builder)
        builder.addInterceptor(CustomInterceptor())
    }

    fun <S> getService(serviceClass: Class<S>): S {
        return Retrofit.Builder()
            .client(client)
            .callFactory(object : CallFactoryProxy(client) {
                override fun getNewUrl(baseUrlName: String?, request: Request?): HttpUrl? {
                    if (baseUrlName.equals("baidu")) {
                        val oldUrl = request?.url.toString()
                        val newUrl = NEW_URL+oldUrl.drop(OLD_HOST.length)
                        return newUrl.toHttpUrl()
                    }
                    return null
                }
            })
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build().create(serviceClass)
    }
}

abstract class CallFactoryProxy(private val delegate: Call.Factory) : Call.Factory {
    override fun newCall(request: Request): Call {
        request.header(NAME_BASE_URL)?.let { it1 ->
            getNewUrl(it1, request)?.let {
                return delegate.newCall(request.newBuilder().url(it).build())
            }
        }
        return delegate.newCall(request)
    }

    @Nullable
    protected abstract fun getNewUrl(baseUrlName: String?, request: Request?): HttpUrl?
    companion object {
        private const val NAME_BASE_URL = "BaseUrlName"
    }

}