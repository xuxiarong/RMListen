package com.rm.business_lib.insertpoint

import com.rm.business_lib.loginUser

/**
 *
 * @author yuanfang
 * @date 12/12/20
 * @description
 *
 */
data class BusinessInsertBean(
    val report_key: String,
    val ext_data: Any? = null
)

data class BusinessInsertAudioDataBean(
    val audio_id: String,
    val memberId: String = loginUser.get()?.id ?: ""
)

data class BusinessInsertChapterDataBean(
    val audio_id: String,
    val chapter_id:String,
    val memberId: String = loginUser.get()?.id ?: ""
)

data class BusinessInsertAdDataBean(
    val ad_id: String,
    val memberId: String = loginUser.get()?.id ?: ""
)

data class BusinessInsertSheetDataBean(
    val sheet_id: String,
    val memberId: String = loginUser.get()?.id ?: ""
)