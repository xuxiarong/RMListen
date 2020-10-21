package com.rm.business_lib.bean.download

import com.rm.business_lib.DownloadConstant
import com.rm.business_lib.db.download.DownloadChapter

/**
 * desc   :
 * date   : 2020/10/19
 * version: 1.0
 */
data class DownloadChapterStatusModel(var downStatus : Int = 0, var select : Int= DownloadConstant.CHAPTER_UN_SELECT, var speed : Long = 0L, var chapter : DownloadChapter){
    fun isSelect() : Boolean{
        return select == DownloadConstant.CHAPTER_SELECTED
    }
}

