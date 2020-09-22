package com.rm.module_listen.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.module_listen.api.ListenApiService
import com.rm.module_listen.bean.ListenSheetMyListBean
import com.rm.module_listen.bean.SubscriptionListBean

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class ListenSheetMyListRepository(private val service: ListenApiService) : BaseRepository() {
    /**
     * 我的听单
     */
    suspend fun getMyList(page: Int, pageSize: Int): BaseResult<ListenSheetMyListBean> {
        return apiCall { service.listenSheetMyList(page, pageSize) }
    }


}