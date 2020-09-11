package com.rm.module_home.repository

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
class ListenDialogSheetRepository(private val service: ListenApiService) : BaseRepository() {

    /**
     * 我的听单
     */
    suspend fun getData(): BaseResult<ListenSheetMyListBean> {
        return apiCall { service.listenSheetMyList() }
    }
}