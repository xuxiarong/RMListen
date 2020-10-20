package com.rm.module_download.model

import com.rm.business_lib.db.download.DownloadChapter
import com.rm.module_download.DownloadConstant

/**
 * desc   :
 * date   : 2020/10/19
 * version: 1.0
 */
data class DownloadChapterStatusModel(var status : Int = DownloadConstant.CHAPTER_STATUS_UN_SELECT, var chapter : DownloadChapter)

