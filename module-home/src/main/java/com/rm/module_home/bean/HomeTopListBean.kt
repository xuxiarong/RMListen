package com.rm.module_home.bean

data class HomeTopListBean(
    var list: MutableList<HomeTopListDataBean>?,
    var total: Int
)

data class HomeTopListDataBean(
    var anchor_id: String,
    var audio_cover: String,
    var audio_id: String,
    var audio_intro: String,
    var audio_label: String,
    var audio_name: String,
    var audio_type: Int,
    var author: String,
    var author_intro: String,
    var chapter_updated_at: Any?,
    var cover_url: String,
    var created_at: String,
    var original_name: String,
    var play_count: Int,
    var progress: Int,
    var quality: Int,
    var short_intro: String,
    var status: Int
)