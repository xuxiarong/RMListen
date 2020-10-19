package com.rm.module_search.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.module_search.api.SearchApiService
import com.rm.module_search.bean.SearchHotRecommendBean
import com.rm.module_search.bean.SearchCommonBean
import com.rm.module_search.bean.SearchResultBean

/**
 *
 * @author yuanfang
 * @date 9/28/20
 * @description
 *
 */
class SearchRepository(private val service: SearchApiService) : BaseRepository() {
    /**
     * 推荐搜索
     */
    suspend fun searchRecommend(): BaseResult<SearchCommonBean> {
        return apiCall { service.searchRecommend() }
    }

    /**
     * 热搜推荐
     */
    suspend fun searchHotRecommend(): BaseResult<MutableList<SearchHotRecommendBean>> {
        return apiCall { service.searchHotRecommend() }
    }

    /**
     * 搜索栏轮播
     */
    suspend fun searchHintBanner(): BaseResult<SearchCommonBean> {
        return apiCall { service.searchHintBanner() }
    }

    /**
     * 搜索结果
     * @param keyword 搜索关键字
     * @param type 搜索维度，all：全部；member：主播；audio：书籍；sheet：书单
     * @param page 页码
     * @param page_size 每页展示数量
     */
    suspend fun searchResult(
        keyword: String,
        type: String,
        page: Int,
        page_size: Int
    ): BaseResult<SearchResultBean> {
        return apiCall { service.searchResult(keyword, type, page, page_size) }
    }

    /**
     * 下拉框搜索
     * @param keyword 搜索关键字
     */
    suspend fun searchSuggest(keyword: String): BaseResult<SearchCommonBean> {
        return apiCall { service.searchSuggest(keyword) }
    }

    /**
     * 关注主播接口
     * @param follow_id String
     */
    suspend fun attentionAnchor(follow_id: String): BaseResult<Any> {
        return apiCall { service.homeAttentionAnchor(follow_id) }
    }

    /**
     * 取消关注主播接口
     * @param follow_id String
     */
    suspend fun unAttentionAnchor(follow_id: String): BaseResult<Any> {
        return apiCall { service.homeUnAttentionAnchor(follow_id) }
    }


}