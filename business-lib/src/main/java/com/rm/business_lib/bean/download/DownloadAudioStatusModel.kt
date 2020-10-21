package com.rm.business_lib.bean.download

import com.rm.business_lib.DownloadConstant
import com.rm.business_lib.db.download.DownloadAudio

/**
 * desc   :
 * date   : 2020/10/19
 * version: 1.0
 */
data class DownloadAudioStatusModel(var downStatus : Int = 0, var select : Int= DownloadConstant.AUDIO_UN_SELECT, var audio : DownloadAudio){
    fun isSelect() : Boolean{
        return select == DownloadConstant.AUDIO_SELECTED
    }
}

