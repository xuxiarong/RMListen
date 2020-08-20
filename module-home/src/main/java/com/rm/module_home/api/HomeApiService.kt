package com.rm.module_home.api

import com.rm.baselisten.net.bean.BaseResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
interface HomeApiService {
    @FormUrlEncoded
    @POST("/lg/user_article/add/json")
    suspend fun test(@Field("title") title: String, @Field("link") url: String): BaseResponse<String>
}