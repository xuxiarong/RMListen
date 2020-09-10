package com.rm.module_listen.bean

data class ListenSheetDetailBean(
    var list: List<ListenSheetDetailDataBean>,
    var total: Long
)

data class ListenSheetDetailDataBean(
    var audio_cover: String,
    var audio_id: String,
    var audio_label: String,
    var audio_name: String,
    var chapter_id: String,
    var chapter_name: String,
    var duration: Long,
    var path: String,
    var sequence: Long,
    var sheet_id: String
)