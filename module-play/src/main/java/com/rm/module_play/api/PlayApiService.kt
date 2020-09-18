package com.rm.module_play.api

import com.rm.baselisten.net.bean.BaseResponse
import com.rm.module_play.model.AudioCommentsModel
import com.rm.module_play.test.ResultData
import com.rm.module_play.test.SearchMusicData
import com.rm.module_play.test.SearchResult
import com.rm.module_play.test.SearchResultInfo
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
    suspend fun getBookList(@QueryMap map: Map<String, String>): BaseResponse<SearchResult>

    @Headers(
        "BaseUrlName:play",
        "Cookie:kg_mid=8a18832e9fc0845106e1075df481c1c2;Hm_lvt_aedee6983d4cfc62f509129360d6bb3d=1557584633",
        "User-Agent:Mozilla/5.0 (Windows NT 10.0; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0"
    )//静态替换
    @GET("/")
    suspend fun getPlayPath(@QueryMap map: Map<String, String>): BaseResponse<SearchMusicData>

    /**
     * 用户评论列表
     */
    @GET("comment/member-comments")
    suspend fun getMemberComments(
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): BaseResponse<Any>

    /**
     * 评论点赞和取消点赞
     */
    @POST("comment/like")
    suspend fun likeComment(
        @Field("comment_id") comment_id: Int
    ): BaseResponse<Any>

    /**
     * 用户评论列表
     */
    @GET("comment/audio-comments")
    suspend fun commentAudioComments(
        @Query("audio_id") audioId: String,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): BaseResponse<AudioCommentsModel>


}