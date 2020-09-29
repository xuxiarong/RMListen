package com.rm.module_play.cache

import com.rm.business_lib.bean.AudioChapterListModel
import com.rm.business_lib.bean.DetailBookBean
import java.io.Serializable

/**
 *
 * @des:
 * @data: 9/28/20 6:13 PM
 * @Version: 1.0.0
 */
data class PlayBookState(
    var homeDetailModel: DetailBookBean?=null,
    var audioChapterListModel: AudioChapterListModel?=null,
    var index: Int=0,
    var process:Float=0f
) : Serializable {


}