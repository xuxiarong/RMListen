package com.rm.module_home.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.module_home.api.HomeApiService
import com.rm.module_home.bean.CategoryTabBean
import com.rm.module_home.bean.HomeTopListBean

class TopListRepository(private val service: HomeApiService) : BaseRepository() {
    suspend fun getTopList(rankType: String,rankSeg: String,page: Int,pageSize: Int): BaseResult<HomeTopListBean> {
        return apiCall { service.homeTopList(rankType, rankSeg, page, pageSize) }
    }

}