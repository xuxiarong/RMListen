package com.rm.module_home.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.business_lib.bean.SheetListBean
import com.rm.module_home.api.HomeApiService
import com.rm.module_home.bean.MenuSheetInfoBean

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class MenuDetailRepository(private val service: HomeApiService) : BaseRepository() {

    suspend fun getData(pageId: String, sheetId: String, memberId: String): BaseResult<MenuSheetInfoBean> {
        return apiCall { service.homeSheetInfo(pageId, sheetId, memberId) }
    }
}