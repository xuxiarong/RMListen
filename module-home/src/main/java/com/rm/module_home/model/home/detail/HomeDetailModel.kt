package com.rm.module_home.model.home.detail

import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * 书籍详情bean类
 */
//data class HomeDetailModel (val datas:List<HomeDetailModelList>):Serializable
data class HomeDetailModel(
    @SerializedName("list")
    val list: DetailArticle
):Serializable

data class DetailArticle(
    val anchor: MutableList<Anchor>,
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
):Serializable

data class Anchor(
    val anchor_avatar: String,
    val anchor_follows: Int,
    val anchor_name: String,
    val status: Int
):Serializable

data class Tags(
    val id: Int,
    val name: String
):Serializable
