package com.rm.business_lib.play

import com.rm.business_lib.PlayGlobalData.STATE_ENDED
import com.rm.business_lib.PlayGlobalData.STATE_READY


/**
 *
 * @des:
 * @data: 9/28/20 6:13 PM
 * @Version: 1.0.0
 */
data class PlayState(
    var state: Int=0, var read: Boolean=false
){
    fun isStart() : Boolean{
        return state == STATE_READY && read
    }

    fun isPause() : Boolean{
        return state == STATE_ENDED || (state == STATE_READY && !read)
    }

}