package com.rm.baselisten.net.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 *
 * @author yuanfang
 * @date 12/23/20
 * @description
 *
 */
class BusinessGsonConverterFactory private constructor(private val gson: Gson) : Converter.Factory(){
    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return BusinessGsonResponseBodyConverter(gson,type, adapter)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return BusinessGsonRequestBodyConverter(gson, adapter)
    }

    companion object {
        @JvmStatic
        fun create(): BusinessGsonConverterFactory {
            return create(Gson())
        }

        @JvmStatic
        fun create(gson: Gson?): BusinessGsonConverterFactory {
            if (gson == null) throw NullPointerException("gson == null")
            return BusinessGsonConverterFactory(gson)
        }
    }
}