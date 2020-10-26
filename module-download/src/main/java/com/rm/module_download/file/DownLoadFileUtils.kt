package com.rm.module_download.file

import android.content.Context
import androidx.annotation.NonNull
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.util.ConvertUtils
import com.rm.business_lib.DownloadConstant
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import java.io.File

/**
 * desc   :
 * date   : 2020/10/15
 * version: 1.0
 */
object DownLoadFileUtils {


    fun checkChapterIsDownload(chapter: DownloadChapter): DownloadChapter {
        val file = File(
            createFileWithAudio(chapter.audio_id.toString()).absolutePath,
            chapter.chapter_name
        )
        if (file.exists() && file.isFile) {
            if (file.length() >= chapter.size) {
                chapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_FINISH
            } else {
                chapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOADING
            }
        }
        return chapter
    }

    fun getChapterFileSize(chapter: DownloadChapter): String {
        val file = File(
            createFileWithAudio(chapter.audio_id.toString()).absolutePath,
            chapter.chapter_name
        )
        if (file.exists() && file.isFile) {
            if (file.length() >= chapter.size) {
                chapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_FINISH
            } else {
                chapter.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOADING
            }
            return ConvertUtils.byte2FitMemorySize(file.length(), 1)
        }
        return ""
    }

    fun deleteChapterFile(chapter: DownloadChapter): Boolean {
        try {
            val file = File(
                createFileWithAudio(chapter.audio_id.toString()).absolutePath,
                chapter.chapter_name
            )
            if (file.exists() && file.isFile) {
                return file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun deleteChapterFile(chapterList: List<DownloadChapter>) {
        chapterList.forEach {
            deleteChapterFile(it)
        }
    }

    fun deleteAudioFile(audio : DownloadAudio) : Boolean{
        try {
            val file = File(createFileWithAudio(audio.audio_id.toString()).absolutePath)
            if (file.exists() && file.isDirectory) {
                return file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun deleteAudioFile(audioList : List<DownloadAudio>) {
        audioList.forEach {
            deleteAudioFile(it)
        }
    }


    private fun createFileWithAudio(audioName: String): File {
        return File(getParentFile(BaseApplication.CONTEXT), audioName)
    }

    fun getParentFile(@NonNull context: Context): File? {
        val externalSaveDir = context.externalCacheDir
        return externalSaveDir ?: context.cacheDir
    }
}