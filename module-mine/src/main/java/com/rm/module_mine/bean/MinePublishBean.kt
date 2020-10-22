package com.rm.module_mine.bean

/**
 *
 * @author yuanfang
 * @date 10/22/20
 * @description
 *
 */
data class MinePublishBean(
    val list: MutableList<MinePublishDetailBean>,
    val total: Int
)

data class MinePublishDetailBean(
    val audio_cover_url: String,//封面
    val audio_id: String,
    val audio_intro: String,//简介
    val audio_label: String,//标签
    val audio_name: String,//书籍名称
    val last_sequence: String,//总章节数
    val play_count: String,//播放量
    val short_intro: String//短简介
)