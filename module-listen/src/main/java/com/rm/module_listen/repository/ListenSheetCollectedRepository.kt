package com.rm.module_listen.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.module_listen.api.ListenApiService
import com.rm.module_listen.bean.ListenSheetCollectedBean
import com.rm.module_listen.bean.ListenSheetMyListBean
import com.rm.module_listen.bean.SubscriptionListBean

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class ListenSheetCollectedRepository(private val service: ListenApiService) : BaseRepository() {
    /**
     * 收藏听单列表
     */
    suspend fun getCollectedList(page:Int,pageSize:Int): BaseResult<ListenSheetCollectedBean> {
        return apiCall { service.listenSheetFavoriteList(page,pageSize) }
    }


}