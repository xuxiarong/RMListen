package com.rm.module_listen.bean

import com.rm.business_lib.db.download.DownloadAudio

data class SheetFavorBean(
    var list: List<SheetFavorDataBean>,
    var total: Long
)

data class SheetFavorDataBean(
    var audio_label: String,
    var audio_list: MutableList<DownloadAudio>?,
    var audio_total: Int,
    var avatar_url: String,
    var member_id: Int,
    var member_name: String,
    var num_audio: Long,
    var num_favor: Int,
    var sheet_id: Long,
    var sheet_name: String
)
