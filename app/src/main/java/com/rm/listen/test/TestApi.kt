package com.rm.listen.test

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 *
 * @ClassName: TestApi
 * @Description:
 * @Author: 鲸鱼
 * @CreateDate: 8/12/20 3:11 PM
 * @UpdateUser: 更新者
 * @UpdateDate: 8/12/20 3:11 PM
 * @UpdateRemark: 更新说明
 * @Version: 1.0.0
 */
interface TestApi {
    @Streaming
    @GET
    suspend fun downLoadFile(@Url url:String):Response<ResponseBody>
}