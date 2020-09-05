package com.rm.business_lib.bean

data class SheetBean(
    var audio_list: MutableList<AudioBean>?,
    var avatar_url: String,
    var cover_url: String,
    var created_at: String,
    var created_from: Int,
    var member_id: String,
    var nickname: String,
    var num_audio: Int,
    var num_favor: Int,
    var num_play: Int,
    var sheet_cover: String,
    var sheet_id: Long,
    var sheet_intro: String,
    var sheet_name: String,
    var status: Int
) {
    val isAuthorCircle = true

    companion object {
        const val CORNER = 14f
        const val MIN_CORNER = 4f
    }
}


