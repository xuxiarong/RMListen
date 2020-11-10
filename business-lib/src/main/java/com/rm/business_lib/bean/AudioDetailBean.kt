package com.rm.business_lib.bean

import com.rm.business_lib.db.download.DownloadAudio
import java.io.Serializable


/**
 * 书籍详情bean类
 */

class AudioDetailBean(
    var list: DownloadAudio,
    var total: Int
)

data class DetailTags(
    val tag_id: String = "",
    val tag_name: String = ""
) : Serializable

data class Anchor(
    val anchor_name: String = "",
    val anchor_avatar: String = "",
    val anchor_follows: String = "",
    val status: Boolean = false
) : Serializable


