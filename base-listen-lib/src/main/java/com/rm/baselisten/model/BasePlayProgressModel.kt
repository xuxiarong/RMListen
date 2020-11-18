package com.rm.baselisten.model

/**
 * desc   :
 * date   : 2020/09/03
 * version: 1.0
 */
data class BasePlayProgressModel constructor(
    var totalDuration : Long = 0L,
    var currentDuration : Long = 0L
)