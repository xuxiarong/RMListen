package com.rm.module_home.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.business_lib.bean.AudioListBean
import com.rm.module_home.api.HomeApiService

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class HomeTopicRepository(private val apiService: HomeApiService) : BaseRepository() {

    /**
     * 获取专题列表数据
     * @param page_id Int
     * @param block_id Int
     * @param topic_id Int
     * @param page Int
     * @param page_size Int
     * @return BaseResult<AudioListBean>
     */
    suspend fun getTopicList(
        page_id: Int,
        block_id: Int,
        topic_id: Int,
        page: Int,
        page_size: Int
    ): BaseResult<AudioListBean> {
        return apiCall { apiService.getTopicList(page_id, block_id, topic_id, page, page_size) }
    }
}