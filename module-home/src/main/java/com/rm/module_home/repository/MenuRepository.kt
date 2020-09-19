package com.rm.module_home.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.business_lib.bean.SheetListBean
import com.rm.module_home.api.HomeApiService
import com.rm.module_home.bean.MenuSheetBean

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class MenuRepository(private val service: HomeApiService) : BaseRepository() {

    /**
     * 获取听单详情
     */
    suspend fun sheet(): BaseResult<MenuSheetBean> {
        return apiCall { service.homeSheet() }
    }

    /**
     * 获取听单列表
     */
    suspend fun getSheetList(pageId: String, page: Int, pageSize: Int): BaseResult<SheetListBean> {
        return apiCall { service.homeSheetList(pageId, page, pageSize) }
    }
}