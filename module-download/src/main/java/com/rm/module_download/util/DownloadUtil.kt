package com.rm.module_download.util

import android.content.Context
import android.widget.ProgressBar
import androidx.annotation.NonNull
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