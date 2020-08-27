package com.rm.module_home.model.home.detail



/**
 * 书籍详情bean类
 */
//data class HomeDetailModel (val datas:List<HomeDetailModelList>):Serializable
data class HomeDetailModel(
    val list: List<Any?>
)

data class List<T>(
    val anchor: Anchor,
    val anchor_id: Int,
    val audio_id: Long,
    val author: String,
    val author_intro: String,
    val cover_url: String,
    val intro: String,
    val last_sequence: Int,
    val name: String,
    val play_count: Int,
    val progress: Int,
    val tags: List<Tags>,
    val type: Int
)

data class Anchor(
    val anchor_avatar: String,
    val anchor_follows: Int,
    val anchor_name: String,
    val status: Int
)

data class Tags(
    val id: Int,
    val name: String
)
