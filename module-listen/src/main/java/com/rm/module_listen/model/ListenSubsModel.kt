package com.rm.module_listen.model

/**
 * desc   :
 * date   : 2020/09/11
 * version: 1.0
 */
data class ListenSubsModel(
    val code: Int,
    val `data`: Data,
    val msg: String
)

data class Data(
    val early: List<Early>,
    val last_unread: Int,
    val today: List<Today>,
    val total_unread: Int,
    val yesterday: List<Yesterday>
)

data class Early(
    val audio_cover: String,
    val audio_id: String,
    val audio_name: String,
    val chapter_list: List<Chapter>,
    val upgrade_time: String
)

data class Today(
    val audio_cover: String,
    val audio_id: String,
    val audio_name: String,
    val chapter_list: List<Chapter>
)

data class Yesterday(
    val audio_cover: String,
    val audio_id: String,
    val audio_name: String,
    val chapter_list: List<Chapter>
)

data class Chapter(
    val chapter_id: String,
    val chapter_name: String,
    val duration: String,
    val path: String,
    val play_count: Long,
    val sequence: String,
    val size: String,
    val upgrade_time: String
)

