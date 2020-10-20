package com.rm.module_download.file

import android.content.Context
import androidx.annotation.NonNull
import com.rm.baselisten.BaseApplication
import com.rm.module_download.DownloadConstant
import com.rm.module_download.model.DownloadChapterStatusModel
import java.io.File

/**
 * desc   :
 * date   : 2020/10/15
 * version: 1.0
 */
object DownLoadFileUtils {


    fun  checkChapterIsDownload(chapterStatusModel : DownloadChapterStatusModel) : DownloadChapterStatusModel{
        val file = File(
            createFileWithAudio(chapterStatusModel.chapter.audio_id.toString()).absolutePath,
            chapterStatusModel.chapter.chapter_name
        )
        if(file.exists() && file.isFile){
            if(file.length() >= chapterStatusModel.chapter.size){
                chapterStatusModel.status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_FINISH
            }else{
                chapterStatusModel.status = DownloadConstant.CHAPTER_STATUS_DOWNLOADING
            }
        }
        return chapterStatusModel
    }

    private fun createFileWithAudio(audioName :String ) : File {
        return File(getParentFile(BaseApplication.CONTEXT), audioName)
    }

    fun getParentFile(@NonNull context: Context): File? {
        val externalSaveDir = context.externalCacheDir
        return externalSaveDir ?: context.cacheDir
    }
}