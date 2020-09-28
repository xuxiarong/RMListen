package com.rm.module_listen.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.module_listen.api.ListenApiService
import com.rm.module_listen.model.ListenChapterList
import com.rm.module_listen.model.ListenSubsNumberModel

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class ListenMyListenRepository(private val service: ListenApiService) : BaseRepository() {

    /**
     * 我的听单
     */
    suspend fun getListenSubsUpgradeList( page : Int, pageSize : Int): BaseResult<ListenChapterList> {
        return apiCall { service.listenUpgrade(page,pageSize) }
    }

    suspend fun getSubsNumber(): BaseResult<ListenSubsNumberModel> {
        return apiCall { service.getSubsNumber() }
    }
}