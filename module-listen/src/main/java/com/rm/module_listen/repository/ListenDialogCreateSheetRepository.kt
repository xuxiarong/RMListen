package com.rm.module_listen.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.module_listen.api.ListenApiService

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class ListenDialogCreateSheetRepository(private val service: ListenApiService) : BaseRepository() {

    /**
     * 创建听单
     *
     * @param sheet_name 听单名称
     * @param description 听单简介
     */
    suspend fun createSheet(sheet_name: String, description: String): BaseResult<Any> {
        return apiCall { service.listenCreateSheetList(sheet_name, description) }
    }

    /**
     * 创建听单
     *
     * @param sheet_id 听单Id
     * @param audio_id 音频Id
     */
    suspend fun addSheet(sheet_id: String, audio_id: String): BaseResult<Any> {
        return apiCall { service.listenAddSheetList(sheet_id, audio_id) }
    }

    /**
     * 编辑听单
     *
     * @param bean 听单
     */
    suspend fun editSheet(bean: ListenPatchSheetBean): BaseResult<Any> {
        return apiCall { service.listenEditSheet(bean) }
    }
}