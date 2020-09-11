package com.rm.business_lib.bean

data class AudioBean(
    var anchor: String,
    var anchor_id: String,
    var audio_cover: String,
    var audio_id: String,
    var audio_intro: String,
    var audio_label: String,
    var audio_name: String,
    var audio_type: Int,
    var author: String,
    var author_intro: String,
    var chapter_updated_at: String,
    var cover_url: String,
    var created_at: String,
    var original_name: String,
    var play_count: Int,
    var progress: Int,
    var quality: Int,
    var short_intro: String,
    var status: Int
)