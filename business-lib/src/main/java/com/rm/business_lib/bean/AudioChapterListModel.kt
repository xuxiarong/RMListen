package com.rm.business_lib.bean

import java.io.Serializable


/**
 *
 * @des:
 * @data: 9/17/20 5:56 PM
 * @Version: 1.0.0
 */

data class AudioChapterListModel(
    var list: List<ChapterList>,
    val Anthology_list :MutableList<DataStr>,
    var total: Int
): Serializable

data class ChapterList (
    var amount: Int,
    var audio_id: String,
    var chapter_id: String,
    var chapter_name: String,
    var created_at: String,
    var duration: Int,
    var need_pay: Int,
    var path: String,
    var path_url: String,
    var play_count: Int,
    var sequence: Int,
    var progress: Long=0L,
    var recentPlay:Long,
    var size: Int


): Serializable

data class DataStr(
    var strData:String = "",
    var position : Int = 0
)