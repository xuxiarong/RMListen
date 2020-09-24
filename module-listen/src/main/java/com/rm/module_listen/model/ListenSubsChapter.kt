package com.rm.module_listen.model

/**
 * desc   :
 * date   : 2020/09/24
 * version: 1.0
 */
data class ListenChapterList(
    val list: List<ListenAudioChapter>,
    val msg: String,
    val total: Long
)

data class ListenAudioChapter(
    val audio_cover: String,
    val audio_id: String,
    val audio_name: String,
    val chapter_name: String,
    val chapter_id: String,
    val cover_url: String,
    val duration: String,
    val path: String,
    val play_count: String,
    val sequence: String,
    val size: String,
    val upgrade_time: String
)