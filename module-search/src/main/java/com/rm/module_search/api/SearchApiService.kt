package com.rm.module_search.api

import com.rm.baselisten.net.bean.BaseResponse
import com.rm.module_search.bean.SearchHotRecommendBean
import com.rm.module_search.bean.SearchCommonBean
import com.rm.module_search.bean.SearchResultBean
import retrofit2.http.*

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
interface SearchApiService {

    /**
     * 推荐搜索
     */
    @GET("search/recommend")
    suspend fun searchRecommend(): BaseResponse<SearchCommonBean>

    /**
     * 热搜推荐
     */
    @GET("search/hot")
    suspend fun searchHotRecommend(): BaseResponse<MutableList<SearchHotRecommendBean>>

    /**
     * 搜索栏轮播
     */
    @GET("search/roll")
    suspend fun searchHintBanner(): BaseResponse<SearchCommonBean>

    /**
     * 搜索结果
     * @param keyword 搜索关键字
     * @param type 搜索维度，all：全部；member：主播；audio：书籍；sheet：书单
     * @param page 页码
     * @param page_size 每页展示数量
     */
    @GET("search/list")
    suspend fun searchResult(
        @Query("keyword") keyword: String,
        @Query("type") type: String,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): BaseResponse<SearchResultBean>

    /**
     * 下拉框搜索
     * @param keyword 搜索关键字
     */
    @GET("search/suggest")
    suspend fun searchSuggest(@Query("keyword") keyword: String): BaseResponse<SearchCommonBean>



    /**
     * 关注主播接口
     * @param follow_id Int
     */
    @FormUrlEncoded
    @POST("member/follow")
    suspend fun homeAttentionAnchor(@Field("follow_id") follow_id: String): BaseResponse<Any>

    /**
     * 取消关注主播接口
     * @param follow_id Int
     */
    @DELETE("member/follow")
    suspend fun homeUnAttentionAnchor(@Query("follow_id") follow_id: String): BaseResponse<Any>

}