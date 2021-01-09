package com.rm.module_listen.bean

data class SheetFavorBean(
    var list: MutableList<SheetFavorDataBean>,
    var total: Long
)

data class SheetFavorDataBean(
    var audio_cover: String? = "",
    var audio_label: String? = "",
    var audio_list: MutableList<SheetFavorAudioBean>? = null,
    var audio_total: Int? = 0,
    var avatar_url: String? = "",
    var created_at: Long? = 0,
    var member_name: String? = "",
    var num_audio: Int? = 0,
    var num_favor: Int? = 0,
    var pre_deleted_from: Int? = 0,//预删除来源；0:不打算删除；1:后管删除；2:创建者删除
    var sheet_cover: String? = "",
    var sheet_id: String? = "",
    var sheet_name: String? = "",
    var status: Int? = 0
)


data class SheetFavorAudioBean(
    var anchor_id: Long? = 0,
    var anchor_name: String? = "",
    var audio_cover: String? = "",
    var audio_id: String? = "",
    var audio_intro: String? = "",
    var audio_label: String? = "",
    var audio_name: String? = "",
    var audio_status: Int? = 0,
    var audio_tags: String? = "",
    var audio_type: Int? = 0,
    var author: String? = "",
    var author_intro: String? = "",
    var chapter_updated_at: String? = "",
    var count_sequence: Int? = 0,
    var cover_url: String? = "",
    var created_at: Int? = 0,
    var last_sequence: Int? = 0,
    var latest_chapter_name: String? = "",
    var original_name: String? = "",
    var play_count: Int? = 0,
    var progress: Int? = 0,
    var quality: Int? = 0,
    var sheet_id: String? = "",
    var short_intro: String? = "",
    var status: Int? = 0,
    var subscription_count: Int? = 0,
    var updated_at: Int? = 0
)