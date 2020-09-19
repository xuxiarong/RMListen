package com.rm.module_listen.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.business_lib.bean.SheetInfoBean
import com.rm.module_listen.api.ListenApiService

class ListenSheetDetailRepository(private val service: ListenApiService) : BaseRepository() {
    /**
     * 获取听单详情
     * @param sheet_id 听单Id
     */
    suspend fun getSheetDetail(sheet_id: String): BaseResult<SheetInfoBean> {
        return apiCall { service.listenSheetList(sheet_id) }
    }

    /**
     * 删除听单
     * @param sheet_id 听单Id
     */
    suspend fun deleteSheet(sheet_id: String): BaseResult<Any> {
        return apiCall { service.listenDeleteSheet(sheet_id) }
    }

    /**
     * 将音频从听单移除
     * @param sheet_id 听单Id
     * @param audioId 音频Id
     */
    suspend fun removeAudio(sheet_id: String, audioId: String): BaseResult<Any> {
        return apiCall { service.listenRemoveAudio(sheet_id, audioId) }
    }

}