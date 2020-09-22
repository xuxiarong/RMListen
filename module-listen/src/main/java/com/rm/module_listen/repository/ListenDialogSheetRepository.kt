package com.rm.module_listen.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.module_listen.api.ListenApiService
import com.rm.module_listen.bean.ListenSheetMyListBean

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class ListenDialogSheetRepository(private val service: ListenApiService) : BaseRepository() {

    /**
     * 我的听单
     */
    suspend fun getData(page: Int, pageSize: Int): BaseResult<ListenSheetMyListBean> {
        return apiCall { service.listenSheetMyList(page,pageSize) }
    }

    /**
     * 添加到听单列表
     */
    suspend fun addSheetList(sheet_id: String, audio_id: String): BaseResult<Any> {
        return apiCall { service.listenAddSheetList(sheet_id, audio_id) }
    }
}