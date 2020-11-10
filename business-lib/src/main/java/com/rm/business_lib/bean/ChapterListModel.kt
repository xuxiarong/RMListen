package com.rm.business_lib.bean

import com.rm.business_lib.db.download.DownloadChapter
import java.io.Serializable


/**
 *
 * @des:
 * @data: 9/17/20 5:56 PM
 * @Version: 1.0.0
 */

data class ChapterListModel(
    var list: MutableList<DownloadChapter>?,
    var total: Int,
    var page : Int
) : Serializable

data class DataStr(
    var strData: String = "",
    var position: Int = 0
)