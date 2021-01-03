package com.rm.baselisten.model

/**
 * desc   :
 * date   : 2020/09/03
 * version: 1.0
 */
data class BasePlayInfoModel constructor(
    var playUrl : String = "",
    var playAudioId : String = "",
    var playChapterId : String = "",
    var playSort : String = "asc"

)