package com.rm.business_lib.download.file

import android.content.Context
import androidx.annotation.NonNull
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.util.FileUtils
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.DownloadChapterDao
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.download.DownloadConstant
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
            val start = System.currentTimeMillis()
            val qb = DaoUtil(DownloadChapter::class.java, "").queryBuilder()
            qb?.where(DownloadChapterDao.Properties.Chapter_id.eq(chapter.chapter_id))
            qb?.where(DownloadChapterDao.Properties.Audio_id.eq(chapter.audio_id))
            val list = qb?.list()
            val end = System.currentTimeMillis()
            if(list!=null && list.size>0){
                chapter.down_status = list[0].down_status
            }else{
                chapter.down_status = DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD
            }
        }else{
            if(chapter.down_status == DownloadConstant.CHAPTER_STATUS_DOWNLOAD_FINISH
                ||chapter.down_status == DownloadConstant.CHAPTER_STATUS_DOWNLOAD_PAUSE){
                chapter.down_status = DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD
            }
        }
        return chapter
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

    fun deleteAudioFile(audio: DownloadAudio): Boolean {
        try {
            val file = File(createFileWithAudio(audio.audio_id.toString()).absolutePath)
            if (file.exists() && file.isDirectory) {
                return FileUtils.delete(file)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun deleteAudioFile(audioList: List<DownloadAudio>) {
        audioList.forEach {
            deleteAudioFile(it)
        }
    }


    fun createFileWithAudio(audioName: String): File {
        return File(getParentFile(BaseApplication.CONTEXT), audioName)
    }

    fun getParentFile(@NonNull context: Context): File? {
        val externalSaveDir = context.externalCacheDir
        return externalSaveDir ?: context.cacheDir
    }
}