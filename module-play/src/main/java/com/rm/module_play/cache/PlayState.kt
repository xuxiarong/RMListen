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
data class PlayState(
    var state: Int=2, var read: Boolean=false
)