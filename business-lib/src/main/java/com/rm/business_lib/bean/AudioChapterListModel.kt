package com.rm.business_lib.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 *
 * @des:
 * @data: 9/17/20 5:56 PM
 * @Version: 1.0.0
 */

data class AudioChapterListModel(
    @SerializedName("list")
    val chapter_list: MutableList<ChapterList>,
    val total: Int,
    val Anthology_list :MutableList<DataStr>
) : Serializable

data class DataStr(
    var strData:String = ""
)

data class ChapterList(
    val chapter_id: String,
    val audio_id :String,
    val created_at: String,
    val duration: Int, //音频时长
    val need_pay: Int,
    val path_url: String, //音频地址
    val play_count: String,
    val amount:Int,
    val chapter_name:String,
    val sequence: Int, //章节序号
    val size: Long, //音频大小
    val path:String,
    var recentPlay:Long,
    var progress: Long=0L
) : Serializable