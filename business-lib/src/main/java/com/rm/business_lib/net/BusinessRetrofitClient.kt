package com.rm.business_lib.net

import androidx.annotation.Nullable
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.rm.baselisten.jsondeserializer.JsonObjectAdapter
import com.rm.baselisten.net.api.BaseRetrofitClient
import com.rm.baselisten.net.api.BusinessGsonConverterFactory
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
        private const val BASE_DEVELOP_URL = "http://dev-api.ls.com:9602/api/v1_0/"
        private const val BASE_MOCK_URL = "http://192.168.13.244:9604/api/v1_0/"
        private const val BASE_RELEASE_URL = "http://tianlai.leimans.com/api/v1_0/"
        private const val BASE_STG_URL = "http://10.1.20.201:9602/api/v1_0/"

        private const val BASE_TEST_RUL = "http://10.1.9.197:9602/api/v1_0/"
        const val NEW_URL = "http://mobilecdn.kugou.com/api/v3"
        const val OLD_HOST = "http://10.1.3.12:9602"
        const val PLAY_PATH = "http://www.kugou.com/yy/index.php"

        private const val TYPE_RELEASE = 0
        private const val TYPE_TEST = 1
        private const val TYPE_MOCK = 2
        private const val TYPE_DEVELOP = 3
        private const val TYPE_STG = 4

        private var currentType = TYPE_RELEASE

        fun getBaseUrl(): String {
            when (currentType) {
                TYPE_DEVELOP -> {
                    return BASE_DEVELOP_URL
                }
                TYPE_MOCK -> {
                    return BASE_MOCK_URL
                }
                TYPE_TEST -> {
                    return BASE_TEST_RUL
                }
                TYPE_RELEASE -> {
                    return BASE_RELEASE_URL
                }
                TYPE_STG -> {
                    return BASE_STG_URL
                }
                else -> {
                    return BASE_RELEASE_URL
                }
            }
        }

        fun getUserPrivacy() : String{
            return if(currentType == TYPE_TEST){
                "http://10.1.9.197:8481/login/userPrivacy?channel=Andriod"
            }else{
                "http://tianlai.leimans.com/login/userPrivacy?channel=Andriod"
            }
        }

        fun getUserAgreement() : String{
            return if(currentType == TYPE_TEST){
                "http://10.1.9.197:8481/login/userAgreement?channel=Andriod"
            }else{
                "http://tianlai.leimans.com/login/userAgreement?channel=Andriod"
            }
        }

    }

    override fun handleBuilder(builder: OkHttpClient.Builder) {
        super.handleBuilder(builder)
        builder.addInterceptor(RefreshTokenInterceptor()).eventListenerFactory(HttpEventListener.FACTORY)
    }

    fun <S> getService(serviceClass: Class<S>): S {

        val gson = GsonBuilder()
            .serializeNulls()
            .registerTypeHierarchyAdapter(JsonObject::class.java, JsonObjectAdapter())
            .create()

        return Retrofit.Builder()
            .client(client)
            .callFactory(object : CallFactoryProxy(client) {
                override fun getNewUrl(baseUrlName: String?, request: Request?): HttpUrl? {
                    if (baseUrlName.equals("baidu")) {
                        val oldUrl = request?.url.toString()
                        val newUrl = oldUrl.replace(OLD_HOST, NEW_URL)
                        return newUrl.toHttpUrl()
                    }
                    if (baseUrlName.equals("play")) {
                        val oldUrl = request?.url.toString()
                        val newUrl = oldUrl.replace(OLD_HOST, PLAY_PATH)
                        return newUrl.toHttpUrl()
                    }
                    return null
                }
            })
//            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(BusinessGsonConverterFactory.create(gson))
            .baseUrl(getBaseUrl())
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