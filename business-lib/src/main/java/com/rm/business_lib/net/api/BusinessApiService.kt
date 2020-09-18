package com.rm.business_lib.net.api

import com.rm.baselisten.net.bean.BaseResponse
import com.rm.business_lib.bean.RefreshTokenBean
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * desc   :
 * date   : 2020/09/18
 * version: 1.0
 */
interface BusinessApiService {
    /**
     * 刷新token(令牌)
     * @param refreshToken String
     */
    @POST("auth/refresh")
    fun refreshToken(
        @Query("refresh_token") refreshToken: String
    ): Call<BaseResponse<RefreshTokenBean>>

    /**
     * 刷新token(令牌)
     * @param refreshToken String
     */
    @POST("auth/refresh")
    suspend fun refreshToken2(
        @Query("refresh_token") refreshToken: String
    ): BaseResponse<RefreshTokenBean>
}