package com.rm.module_listen.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.module_listen.api.ListenApiService
import com.rm.module_listen.model.ListenSubsModel

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class ListenSubsUpdateRepository(private val service: ListenApiService) : BaseRepository() {

    /**
     * 我的听单
     */
    suspend fun getListenSubsUpgradeList(): BaseResult<ListenSubsModel> {
        return apiCall { service.listenUpgrade() }
    }
}