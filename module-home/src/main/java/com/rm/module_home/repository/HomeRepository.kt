package com.rm.module_home.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.module_home.api.HomeApiService
import com.rm.module_home.model.home.HomeModel

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class HomeRepository(private val service: HomeApiService) : BaseRepository() {

    /**
     * 我的听单
     */
    suspend fun getHomeData(): BaseResult<HomeModel> {
        return apiCall { service.getHomeData() }
    }
}