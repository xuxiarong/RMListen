package com.rm.module_listen.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.module_listen.api.ListenApiService
import com.rm.module_listen.bean.ListenSheetDetailBean

class ListenSheetDetailRepository(private val service: ListenApiService) : BaseRepository() {
    /**
     * 获取听单详情
     * @param sheet_id 听单Id
     * @param page 当前加载的页码
     */
    suspend fun getSheetDetail(sheet_id: String, page: Int): BaseResult<ListenSheetDetailBean> {
        return apiCall { service.listenSheetList(sheet_id, page, 10) }
    }

    /**
     * 删除听单
     * @param sheet_id 听单Id
     */
    suspend fun deleteSheet(sheet_id: String): BaseResult<Any> {
        return apiCall { service.listenDeleteSheet(sheet_id) }
    }

}