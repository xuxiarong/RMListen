package com.rm.business_lib.insertpoint

import com.rm.baselisten.net.util.GsonUtils
import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.business_lib.net.api.BusinessApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 *
 * @author yuanfang
 * @date 12/12/20
 * @description
 *
 */
class BusinessInsertManager {
    companion object {
        private val apiService by lazy {
            BusinessRetrofitClient().getService(
                BusinessApiService::class.java
            )
        }

        private fun doInsert(bean: BusinessInsertBean) {
            val toJson = GsonUtils.toJson(bean)
            GlobalScope.launch(Dispatchers.IO) {
                apiService.insertPoint(toJson.toRequestBody("application/json;charset=utf-8".toMediaType()))
            }
        }

        @JvmStatic
        fun doInsertKey(key: String) {
            doInsert(BusinessInsertBean(key))
        }

        @JvmStatic
        fun doInsertKeyAndAudio(key: String, audioId: String) {
            val audioBean = BusinessInsertAudioDataBean(audioId)
            val bean = BusinessInsertBean(key, audioBean)
            doInsert(bean)
        }

        @JvmStatic
        fun doInsertKeyAndChapter(key: String, audioId: String,chapterId:String) {
            val audioBean = BusinessInsertChapterDataBean(audioId,chapterId)
            val bean = BusinessInsertBean(key, audioBean)
            doInsert(bean)
        }

        @JvmStatic
        fun doInsertKeyAndAd(key: String, adId: String) {
            val adBean = BusinessInsertAdDataBean(adId)
            val bean = BusinessInsertBean(key, adBean)
            doInsert(bean)
        }

        @JvmStatic
        fun doInsertKeyAndSheet(key: String, sheetId: String) {
            val sheetBean = BusinessInsertSheetDataBean(sheetId)
            val bean = BusinessInsertBean(key, sheetBean)
            doInsert(bean)
        }

    }
}