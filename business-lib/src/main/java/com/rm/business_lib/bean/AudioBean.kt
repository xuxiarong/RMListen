package com.rm.business_lib.bean

data class AudioBean(
    var anchor: String?,
    var anchor_id: String,
    var author: String,
    var author_intro: String,
    var chapter_updated_at: String,
    var cover: String,
    var created_at: String,
    var id: String,
    var intro: String,
    var label: String,
    var name: String,
    var original_name: String,
    var play_count: Int,
    var progress: Int,
    var quality: Int,
    var short_intro: String,
    var status: Int,
    var type: Int
) {
    var iconCorner = 4f
}