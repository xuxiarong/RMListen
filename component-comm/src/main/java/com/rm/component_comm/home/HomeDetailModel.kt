package com.rm.component_comm.home

import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * 书籍详情bean类
 */
//data class HomeDetailModel (val datas:List<HomeDetailModelList>):Serializable
data class HomeDetailModel(
    @SerializedName("list")
    val detaillist: DetailArticle
):Serializable


data class DetailArticle(
    val anchor: Anchor,
    val anchor_id: String,
    val audio_id: Long,
    val author: String,
    val author_intro: String,
    val cover_url: String = "",
    val intro: String,
    val last_sequence: Int,
    val name: String,
    val play_count: String,
    val progress: Int,
    val tags: MutableList<Tags>,
    val type: Int
):Serializable

data class Anchor(
    val anchor_name: String,
    val anchor_follows: Int,
    val anchor_avatar: String,
    val status: Boolean
):Serializable

data class Tags(
    val id: Int,
    val name: String
):Serializable
