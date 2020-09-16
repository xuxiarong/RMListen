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

    /**
     * 获取听单详情
     */
    suspend fun getData(pageId: String, sheetId: String, memberId: String): BaseResult<MenuSheetInfoBean> {
        return apiCall { service.homeSheetInfo(pageId, sheetId, memberId) }
    }

    /**
     * 收藏听单
     * @param sheetId String
     */
    suspend fun favoritesSheet(sheetId: String):BaseResult<Any>{
      return apiCall { service.homeFavoritesSheet(sheetId) }
    }

    /**
     * 取消收藏
     * @param sheetId String
     */
    suspend fun unFavoritesSheet(sheetId: String):BaseResult<Any>{
      return apiCall { service.homeFavoritesSheet(sheetId) }
    }
}