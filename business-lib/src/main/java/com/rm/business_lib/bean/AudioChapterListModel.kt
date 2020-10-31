package com.rm.business_lib.bean

import com.rm.business_lib.db.download.DownloadChapter
import java.io.Serializable


/**
 *
 * @des:
 * @data: 9/17/20 5:56 PM
 * @Version: 1.0.0
 */

data class AudioChapterListModel(
    var list: List<ChapterList>,
    val Anthology_list: MutableList<DataStr>,
    var total: Int
) : Serializable

data class ChapterList(
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
    var progress: Long = 0L,
    var recentPlay: Long,
    var size: Long,
    var audio_name : String = "",
    var current_offset : Long = 0L,
    var down_status : Int = 0
) : Serializable {
    companion object {
        fun copyFromDownload(chapter: DownloadChapter): ChapterList {
            return ChapterList(
                amount = chapter.amount,
                audio_id = chapter.audio_id.toString(),
                chapter_id = chapter.chapter_id.toString(),
                chapter_name = chapter.chapter_name,
                created_at = chapter.created_at,
                duration = chapter.duration,
                need_pay = chapter.need_pay,
                path = chapter.file_path,
                path_url = chapter.file_path,
                play_count = chapter.play_count.toInt(),
                sequence = chapter.sequence,
                progress = chapter.listen_duration.toLong(),
                size = chapter.size,
                recentPlay = chapter.listen_duration.toLong()
            )
        }

        fun toDownChapter(chapter: ChapterList) : DownloadChapter{
            val downloadChapter = DownloadChapter()
            downloadChapter.audio_id = chapter.audio_id.toLong()
            downloadChapter.chapter_id = chapter.chapter_id.toLong()
            downloadChapter.audio_name = chapter.audio_name
            downloadChapter.sequence = chapter.sequence
            downloadChapter.chapter_name = chapter.chapter_name
            downloadChapter.size = chapter.size
            downloadChapter.current_offset = chapter.current_offset
            downloadChapter.duration = chapter.duration
            downloadChapter.amount = chapter.amount
            downloadChapter.play_count = chapter.play_count.toString()
            downloadChapter.created_at = chapter.created_at
            downloadChapter.path_url = chapter.path_url
            downloadChapter.down_status = chapter.down_status
            return downloadChapter
        }

    }
}

data class DataStr(
    var strData: String = "",
    var position: Int = 0
)