package com.rm.business_lib.bean

/**
 * 单个音频bean
 * @property audio_id String
 * @property audio_type Int
 * @property audio_name String
 * @property original_name String
 * @property status Int
 * @property anchor String
 * @property author_intro String
 * @property anchor_id String
 * @property short_intro String
 * @property audio_intro String
 * @property audio_cover String
 * @property cover_url String
 * @property audio_label String
 * @property quality Int
 * @property progress Int
 * @property play_count Int
 * @property created_at String
 * @property chapter_updated_at String
 * @property author String
 * @constructor
 */
data class AudioBean(
    var audio_id: String,               // 音频id
    var audio_type: Int,                // 1=有声小说
    var audio_name: String,             // 音频名称
    var original_name: String,          // 原著名称
    var status: Int,                    // 有效状态，0=下架，1=上架
    var anchor: String,                 // 原著作者名称
    var author_intro: String,           // 原著作者简介
    var anchor_id: String,              // 主播id
    var short_intro: String,            // 音频简介
    var audio_intro: String,            // 音频描述
    var audio_cover: String,            // 音频封面路径
    var cover_url: String,              // 音频封面url
    var audio_label: String,            // 音频角标
    var quality: Int,                   // 清晰度，1=标清，2=高清
    var progress: Int,                  // 连载状态，1=未开播，2=连载中，3=已完结
    var play_count: Int,                // 音频播放数
    var created_at: String,             // 创建时间，格式：YYYY-mm-dd HH:ii:ss
    var chapter_updated_at: String,     // 章节更新时间，格式：YYYY-mm-dd HH:ii:ss
    var author: String,                  // 主播昵称
    var member_id: String,                  // 直播用户id
    var nickname: String                  // 用户名
)


