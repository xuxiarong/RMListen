package com.rm.module_home.model.home.detail

/**
 * 书籍章节列表
 */
data class DetailChapterModel(
    val list: List<ChapterList>,
    val total: Int
)

data class ChapterList(
    val created_at: Int,
    val duration: Int,
    val name: String,
    val need_pay: Int,
    val path_url: String,
    val play_count: Int,
    val sequence: Int,
    val size: Int
)