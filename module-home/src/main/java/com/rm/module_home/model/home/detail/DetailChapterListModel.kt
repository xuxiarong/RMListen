package com.rm.module_home.model.home.detail

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 书籍章节列表
 */
data class DetailChapterModel(
    @SerializedName("list")
    val chapterList: MutableList<ChapterList>,

    val total: Int //总数量
):Serializable

data class ChapterList(
    val created_at: String,
    val duration: Int, //音频时长
    val name: String,
    val need_pay: Int,
    val path_url: String, //音频地址
    val play_count: Int,
    val sequence: Int, //章节序号
    val size: String //音频大小
):Serializable