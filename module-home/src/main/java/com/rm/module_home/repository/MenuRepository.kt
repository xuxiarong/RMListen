package com.rm.module_home.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.module_home.api.HomeApiService

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class MenuRepository(val service: HomeApiService) : BaseRepository() {
    suspend fun test(){
        service.test("","")
    }
}