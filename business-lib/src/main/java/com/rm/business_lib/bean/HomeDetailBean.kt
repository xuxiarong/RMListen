package com.rm.business_lib.bean

import java.io.Serializable


/**
 * 书籍详情bean类
 */

class HomeDetailBean(
    var list: BaseAudioModel
)

data class BaseAudioModel(
    var audio_id: String = "",               // 音频id
    var audio_type: String = "",                // 1=有声小说
    var audio_name: String = "",             // 音频名称
    var original_name: String = "",          // 原著名称
    var status: String = "",                    // 有效状态，0=下架，1=上架
    var author_intro: String = "",           // 原著作者简介
    var anchor_id: String = "",           // 主播id
    var short_intro: String = "",           // 音频简介
    var audio_intro: String = "",          // 音频描述
    var audio_cover: String = "",       // 音频封面路径
    var cover_url: String = "",           // 音频封面url
    var audio_label: String = "",           // 音频角标
    var quality: String = "",                // 清晰度，1=标清，2=高清
    var progress: String = "",                  // 连载状态，1=未开播，2=连载中，3=已完结
    var play_count: String = "",                // 音频播放数
    var created_at: String = "",             // 创建时间，格式：YYYY-mm-dd HH:ii:ss
    var chapter_updated_at: String = "",     // 章节更新时间，格式：YYYY-mm-dd HH:ii:ss
    var author: String = "",                  // 主播昵称
    var member_id: String = "",                      // 直播用户id
    var nickname: String = "",                      // 用户名
    val subscription_count: String = "",//订阅数
    val last_sequence: String = "",
    var audio_cover_url: String = "",
    val anchor: Anchor = Anchor(),
    val tags: MutableList<DetailTags> = mutableListOf(),
    val is_subscribe: Boolean = false,//是否订阅
    val is_fav: Boolean = false//是否收藏
)

data class DetailTags(
    val tag_id: String = "",
    val tag_name: String = ""
) : Serializable

data class Anchor(
    val anchor_name: String = "",
    val anchor_avatar: String = "",
    val anchor_follows: String = "",
    val status: Boolean = false
) : Serializable


