package com.rm.baselisten.model

/**
 * desc   :
 * date   : 2020/09/03
 * version: 1.0
 */
data class BasePlayControlModel constructor(
    var showPlay : Boolean = false,
    var clickFun : ()->Unit ={}
)