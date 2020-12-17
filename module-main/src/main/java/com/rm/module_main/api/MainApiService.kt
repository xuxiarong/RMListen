package com.rm.module_main.api

import com.rm.baselisten.net.bean.BaseResponse
import com.rm.business_lib.bean.BusinessVersionUrlBean
import com.rm.module_main.model.MainAdResultModel
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * desc   :
 * date   : 2020/12/07
 * version: 1.0
 */
interface MainApiService {

    /**
     * 获取开屏广告
     * @param requestBody 请求的body
     */
    @POST("adv/content/list")
    suspend fun getSplashAd(@Body requestBody: RequestBody): BaseResponse<MainAdResultModel>

    /**
     * 获取app包下载接口
     */
    @POST("personal/get-version-url")
    suspend fun homeGetLaseUrl(
        @Body body: RequestBody
    ): BaseResponse<BusinessVersionUrlBean>

}