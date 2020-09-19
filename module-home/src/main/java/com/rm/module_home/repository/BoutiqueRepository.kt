package com.rm.module_home.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.business_lib.bean.AudioListBean
import com.rm.module_home.api.HomeApiService
import com.rm.module_home.bean.CategoryTabListBean

/**
 * desc   :
 * date   : 2020/08/24
 * version: 1.0
 */
class BoutiqueRepository(private val service: HomeApiService) : BaseRepository() {
    suspend fun getTabList(): BaseResult<CategoryTabListBean> {
        return apiCall { service.getBoutiqueTabList() }
    }

    suspend fun getBoutiqueRecommendInfoList(
        classId: Int,
        page: Int,
        pageSize: Int = 10
    ): BaseResult<AudioListBean> {
        return apiCall { service.getCategoryList(classId, page, pageSize) }
    }
}