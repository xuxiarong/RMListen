package com.rm.baselisten.net.api

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import com.rm.baselisten.net.bean.BaseResponse
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Converter
import java.io.IOException
import java.lang.Exception
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets

/**
 *
 * @author yuanfang
 * @date 12/23/20
 * @description
 *
 */
class BusinessGsonResponseBodyConverter<T>(
    private val gson: Gson,
    private val type: Type,
    private val adapter: TypeAdapter<T>
) : Converter<ResponseBody, T> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        val contentType = value.contentType()
        val body: ResponseBody
        val rawType = TypeToken.get(type).rawType
        val assignableFrom = rawType.isAssignableFrom(BaseResponse::class.java)
        var bodyString = bodyToString(value)
        try {
            if (assignableFrom) {
                gson.fromJson<T>(bodyString, type)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is JsonSyntaxException) {
                val msg = e.message.toString()
                when {
                    msg.contains("Expected BEGIN_ARRAY but was BEGIN_OBJECT") -> {
                        bodyString = bodyString.replace("\"data\":{}", "\"data\":[]")
                    }
                    msg.contains("Expected BEGIN_OBJECT but was BEGIN_ARRAY") -> {
                        bodyString = bodyString.replace("\"data\":[]", "\"data\":{}")
                    }
                }
            }
        }
        body = bodyString.toResponseBody(contentType)
        // 在这里通过 value 拿到 json 字符串进行解析
        // 判断状态码是失败的情况，就抛出异常
        val jsonReader = gson.newJsonReader(body.charStream())
        value.use {
            val result = adapter.read(jsonReader)
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw JsonIOException("JSON document was not fully consumed.")
            }
            return result
        }
    }

    private fun bodyToString(body: ResponseBody): String {
        val contentLength = body.contentLength()
        val source = body.source()
        source.request(Long.MAX_VALUE)
        val buffer = source.buffer
        val contentType = body.contentType()
        val charset = contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
        return if (contentLength != 0L) {
            buffer.clone().readString(charset)
        } else {
            ""
        }
    }
}