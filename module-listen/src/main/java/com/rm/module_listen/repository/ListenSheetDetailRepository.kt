package com.rm.module_listen.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.module_listen.api.ListenApiService
import com.rm.module_listen.bean.ListenSheetDetailBean

class ListenSheetDetailRepository(private val service: ListenApiService) : BaseRepository() {
    suspend fun getSheetDetail(sheet_id: String,page:Int): BaseResult<ListenSheetDetailBean> {
        return apiCall { service.listenSheetList(sheet_id,page,10) }
    }
}