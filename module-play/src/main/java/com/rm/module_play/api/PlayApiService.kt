package com.rm.module_play.api

import com.rm.baselisten.net.bean.BaseResponse
import retrofit2.http.*

/**
 *
 * @des:
 * @data: 9/3/20 5:48 PM
 * @Version: 1.0.0
 */
interface PlayApiService {
    @Headers("BaseUrlName:baidu")//静态替换
    @GET("/search/song")
    suspend fun getBookList(@QueryMap map: Map<String, String>): BaseResponse<String>
}