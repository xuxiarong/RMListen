package com.rm.module_main.repository

import com.mei.orc.util.json.toJson
import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.module_main.api.MainApiService
import com.rm.module_main.model.MainAdRequestModel
import com.rm.module_main.model.MainAdResultModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * desc   :
 * date   : 2020/12/07
 * version: 1.0
 */
class MainRepository( val apiService: MainApiService) : BaseRepository() {

    /**
     *  获取开屏广告
     */
    suspend fun getSplashAd(ad_key : Array<String>): BaseResult<MainAdResultModel> {
        return apiCall {
            val requestBean = MainAdRequestModel(ad_key = ad_key)
            apiService.getSplashAd(requestBean.toJson().toString().toRequestBody("application/json;charset=utf-8".toMediaType()))
        }
    }



}