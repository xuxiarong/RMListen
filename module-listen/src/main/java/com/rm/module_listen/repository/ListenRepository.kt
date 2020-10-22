package com.rm.module_listen.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.business_lib.bean.AudioListBean
import com.rm.module_listen.api.ListenApiService
import com.rm.module_listen.bean.SheetFavorBean
import com.rm.business_lib.bean.SheetInfoBean
import com.rm.module_listen.bean.ListenSheetMyListBean
import com.rm.module_listen.bean.ListenSubscriptionListBean
import com.rm.module_listen.model.ListenChapterList
import com.rm.module_listen.model.ListenCreateSheetModel
import com.rm.module_listen.model.ListenSubsNumberModel

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class ListenRepository(private val service: ListenApiService) : BaseRepository() {
    /**
     * 我的听单
     */
    suspend fun getMyList(
        page: Int,
        pageSize: Int,
        memberId: String
    ): BaseResult<ListenSheetMyListBean> {
        return apiCall { service.listenSheetMyList(page, pageSize, memberId) }
    }

    /**
     * 我的听单
     */
    suspend fun getMyList(
        page: Int,
        pageSize: Int
    ): BaseResult<ListenSheetMyListBean> {
        return apiCall { service.listenSheetMyList(page, pageSize) }
    }

    /**
     * 收藏听单列表
     */
    suspend fun getCollectedList(
        page: Int,
        pageSize: Int,
        memberId: String
    ): BaseResult<SheetFavorBean> {
        return apiCall { service.listenSheetFavoriteList(page, pageSize, memberId) }
    }
    /**
     * 收藏听单列表
     */
    suspend fun getCollectedList(
        page: Int,
        pageSize: Int
    ): BaseResult<SheetFavorBean> {
        return apiCall { service.listenSheetFavoriteList(page, pageSize) }
    }

    /**
     * 创建听单
     *
     * @param sheet_name 听单名称
     * @param description 听单简介
     */
    suspend fun createSheet(sheet_name: String, description: String): BaseResult<ListenCreateSheetModel> {
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


    /**
     * 添加到听单列表
     */
    suspend fun addSheetList(sheet_id: String, audio_id: String): BaseResult<Any> {
        return apiCall { service.listenAddSheetList(sheet_id, audio_id) }
    }


    /**
     * 我的听单
     */
    suspend fun getListenSubsUpgradeList(page: Int, pageSize: Int): BaseResult<ListenChapterList> {
        return apiCall { service.listenUpgrade(page, pageSize) }
    }

    suspend fun getSubsNumber(): BaseResult<ListenSubsNumberModel> {
        return apiCall { service.getSubsNumber() }
    }

    /**
     * 获取听单详情
     * @param sheet_id 听单Id
     */
    suspend fun getSheetInfo(sheet_id: String): BaseResult<SheetInfoBean> {
        return apiCall { service.listenSheetInfo(sheet_id) }
    }

    /**
     * 听单音频列表
     * @param page Int
     * @param pageSize Int
     */
    suspend fun getAudioList(page: Int, pageSize: Int): BaseResult<AudioListBean> {
        return apiCall { service.listenAudioList(page, pageSize) }
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

    /**
     * 订阅列表
     */
    suspend fun getSubscriptionList(
        page: Int,
        pageSize: Int
    ): BaseResult<MutableList<ListenSubscriptionListBean>> {
        return apiCall { service.listenSubscriptionList(page, pageSize) }
    }

    /**
     * 置顶
     */
    suspend fun setTop(audioId: String): BaseResult<Any> {
        return apiCall { service.listenSubscriptionTop(audioId) }
    }

    /**
     * 取消置顶
     */
    suspend fun cancelTop(audioId: String): BaseResult<Any> {
        return apiCall { service.listenSubscriptionTCancelTop(audioId) }
    }

    /**
     * 取消订阅
     */
    suspend fun unsubscribe(audioId: String): BaseResult<Any> {
        return apiCall { service.listenUnsubscribe(audioId) }
    }

    /**
     * 添加订阅
     */
    suspend fun addSubscription(audioId: String): BaseResult<*> {
        return apiCall { service.listenAddSubscription(audioId) }
    }
}