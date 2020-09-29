package com.rm.module_download.util

import android.content.Context
import android.widget.ProgressBar
import androidx.annotation.NonNull
import com.rm.business_lib.bean.download.DownloadUIStatus
import com.rm.module_download.bean.DownloadChapterUIStatus
import java.io.File

const val URL = "https://cdn.llscdn.com/yy/files/tkzpx40x-lls-LLS-5.7-785-20171108-111118.apk"

fun calcProgressToView(progressBar: ProgressBar, offset: Long, total: Long) {
    val percent = offset.toFloat() / total
    progressBar.progress = (percent * progressBar.max).toInt()
}


fun getParentFile(@NonNull context: Context): File? {
    val externalSaveDir = context.externalCacheDir
    return externalSaveDir ?: context.cacheDir
}

fun deleteDir(path: String): Boolean {
    val file = File(path)
    if (!file.exists()) { //判断是否待删除目录是否存在
        return false
    }
    val content = file.list() //取得当前目录下所有文件和文件夹
    for (name in content) {
        val temp = File(path, name)
        if (temp.isDirectory) { //判断是否是目录
            deleteDir(temp.absolutePath) //递归调用，删除目录里的内容
            temp.delete() //删除空目录
        } else {
            temp.delete()
        }
    }
    return true
}

fun DownloadUIStatus.toDownloadChapterStatus():DownloadChapterUIStatus{
  return  when(this){
        DownloadUIStatus.DOWNLOAD_COMPLETED -> DownloadChapterUIStatus.COMPLETED
        else -> DownloadChapterUIStatus.COMPLETED
    }
}