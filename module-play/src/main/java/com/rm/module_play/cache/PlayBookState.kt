package com.rm.module_play.cache

import com.rm.business_lib.bean.AudioChapterListModel
import com.rm.business_lib.bean.HomeDetailModel
import java.io.Serializable

/**
 *
 * @des:
 * @data: 9/28/20 6:13 PM
 * @Version: 1.0.0
 */
data class PlayBookState(
    val homeDetailModel: HomeDetailModel?=null,
    val audioChapterListModel: AudioChapterListModel?=null,
    val index: Int=0,
    val process:Long=0L
) : Serializable {


}