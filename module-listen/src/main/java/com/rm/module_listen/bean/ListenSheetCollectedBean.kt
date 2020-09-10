package com.rm.module_listen.bean

data class ListenSheetCollectedBean(
    var list: List<ListenSheetCollectedDataBean>,
    var total: Long
)

data class ListenSheetCollectedDataBean(
    var audio_label: String,
    var audio_list: List<ListenAudioBean>?,
    var audio_total: Int,
    var avatar_url: String,
    var member_id: Int,
    var member_name: String,
    var num_audio: Long,
    var num_favor: Int,
    var sheet_id: Long,
    var sheet_name: String
)
