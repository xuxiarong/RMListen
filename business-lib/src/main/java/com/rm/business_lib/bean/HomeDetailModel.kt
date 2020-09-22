package com.rm.business_lib.bean

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
    val audio_id : String,
    val audio_type :Int,
    val audio_name: String,
    val original_name: String,
    val author: String,
    val author_intro: String,
    val anchor_id: String,
    val short_intro: String,
    val audio_intro: String,
    val audio_cover: String,
    val audio_label: String,
    val quality: String,
    val progress: Int,
    val play_count: String,
    val subscription_count: String,
    val last_sequence: String,
    val status: Int,
    val created_at: String,
    val chapter_updated_at: String,
    val audio_cover_url: String,
    @SerializedName("tags")
    val detail_tags: MutableList<DetailTags>,
    val anchor: Anchor
):Serializable

data class Anchor(
    val anchor_name: String,
    val anchor_follows: Int,
    val anchor_avatar: String,
    val status: Boolean
):Serializable

data class DetailTags(
    val tag_id: Int,
    val tag_name: String
):Serializable


