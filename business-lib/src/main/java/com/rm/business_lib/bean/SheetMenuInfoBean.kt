package com.rm.business_lib.bean

import com.rm.business_lib.db.download.DownloadAudio

data class SheetMenuInfoBean(
    var audio_list: MutableList<DownloadAudio>?,//音频列表
    var avatar_url: String,//创建者头像URL
    var cover_url: String,//听单封面URL
    var created_at: String,//创建时间，格式：YYYY-mm-dd HH:ii:ss
    var created_from: Int,//来源，1：系统创建，2：用户手动创建，3：用户注册默认创建
    var favor: Int,//当前登录用户是否收藏，0：否，1：是。未登录则为0。
    var member_id: String,//创建者ID
    var nickname: String,//创建者昵称
    var num_audio: Int,//音频数
    var num_favor: Int,//收藏数
    var num_play: Int,//播放数
    var sheet_cover: String,//听单封面路径
    var sheet_id: String,//听单ID
    var sheet_intro: String,//听单描述
    var sheet_name: String,//听单名称
    var status: Int//有效状态，0：无效，1：有效
)




