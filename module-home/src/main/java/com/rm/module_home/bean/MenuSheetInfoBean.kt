package com.rm.module_home.bean

import com.rm.business_lib.bean.AudioBean

data class MenuSheetInfoBean(
    var audio_list: MutableList<AudioBean>?,
    var cover: String,
    var created_at: String,
    var created_from: Int,
    var description: String,
    var favor: Int,
    var id: String,
    var member_id: String,
    var name: String,
    var nickname: String,
    var num_audio: Int,
    var num_favor: Int,
    var num_play: Int,
    var status: Int
)
