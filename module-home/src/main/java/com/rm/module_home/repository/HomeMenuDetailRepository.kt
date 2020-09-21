package com.rm.module_home.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.business_lib.bean.AudioListBean
import com.rm.business_lib.bean.SheetInfoBean
import com.rm.module_home.api.HomeApiService

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class HomeMenuDetailRepository(private val service: HomeApiService) : BaseRepository() {

    /**
     * 获取听单详情
     */
    suspend fun getData(sheetId: String): BaseResult<SheetInfoBean> {
        return apiCall { service.homeSheetInfo(sheetId) }
    }

    /**
     * 获取听单详情
     */
    suspend fun getAudioList(
        page_id: String,
        sheetId: String,
        page: Int,
        page_size: Int
    ): BaseResult<AudioListBean> {
        return apiCall { service.homeAudioList(page_id, sheetId, page, page_size) }
    }

    /**
     * 收藏听单
     * @param sheetId String
     */
    suspend fun favoritesSheet(sheetId: String): BaseResult<Any> {
        return apiCall { service.homeFavoritesSheet(sheetId) }
    }

    /**
     * 取消收藏
     * @param sheetId String
     */
    suspend fun unFavoritesSheet(sheetId: String): BaseResult<Any> {
        return apiCall { service.homeUnFavoriteSheet(sheetId) }
    }
}