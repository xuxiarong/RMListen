package com.rm.baselisten.model

/**
 * desc   :
 * date   : 2020/09/03
 * version: 1.0
 */
data class BasePlayStatusModel constructor(
    var playReady : Boolean = true,
    var playStatus : Int = 2
)