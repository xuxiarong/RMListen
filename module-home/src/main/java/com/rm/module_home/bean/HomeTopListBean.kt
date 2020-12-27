package com.rm.module_home.bean

import com.rm.module_home.R

data class HomeTopListBean(
    var list: MutableList<HomeTopListDataBean>?,
    var total: Int
)

data class HomeTopListDataBean(
    var anchor_id: String,//主播ID
    var audio_cover: String,//音频封面路径
    var audio_id: String,//音频ID
    var audio_intro: String,//音频描述
    var audio_label: String,//音频角标
    var audio_name: String,//音频名称
    var audio_type: Int,//音频类型，1：有声小说
    var author: String,//原著作者名称
    var author_intro: String,//原著作者简介
    var chapter_updated_at: Any?,
    var cover_url: String,//音频封面URL
    var created_at: String,//创建时间，格式：YYYY-mm-dd HH:ii:ss
    var original_name: String,//原著名称
    var play_count: Int,//音频播放数
    var progress: Int,//连载状态，1：未开播，2：连载中，3：已完结
    var quality: Int,//清晰度，1：标清，2：高清
    var short_intro: String,//音频简介
    var anchor_name: String,//作者名称
    var status: Int//有效状态，0：下架，1：上架
)
