package com.rm.component_comm.home

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 *
 * @des:
 * @data: 9/17/20 5:56 PM
 * @Version: 1.0.0
 */

data class AudioChapterListModel(
    val list: List<ChapterList>,
    val total: Int
) : Serializable


data class ChapterList(
    val amount: Int,
    val audio_id: String,
    val chapter_id: String,
    val chapter_name: String,
    val created_at: String,
    val duration: Int,
    val need_pay: Int,
    val path: String,
    val path_url: String,
    val play_count: Int,
    val sequence: Int,
    val size: Int
) : Serializable