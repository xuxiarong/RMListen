package com.rm.module_home.repository

import com.rm.baselisten.net.api.BaseRepository
import com.rm.baselisten.net.api.BaseResult
import com.rm.module_listen.api.ListenApiService
import com.rm.module_listen.bean.SubscriptionListBean

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class ListenSubscriptionRepository(private val service: ListenApiService) : BaseRepository() {
    /**
     * 订阅列表
     */
    suspend fun getSubscriptionList(
        page: Int,
        pageSize: Int
    ): BaseResult<MutableList<SubscriptionListBean>> {
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