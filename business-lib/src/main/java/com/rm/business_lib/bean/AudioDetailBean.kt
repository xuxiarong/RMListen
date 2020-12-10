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
    var status: Boolean = false
) : Serializable

data class AudioRecommendList(
    var list: MutableList<AudioRecommend>?

)

data class AudioRecommend(
    var anchor_id: String = "",
    var anchor_name: String = "",
    var audio_cover: String = "",
    var audio_id: String = "",
    var audio_label: String = "",
    var audio_name: String = "",
    var cover_url: String = "",
    var status_name: String = ""
)


