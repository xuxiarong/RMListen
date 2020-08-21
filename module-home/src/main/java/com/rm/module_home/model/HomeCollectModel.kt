package com.rm.module_home.model

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
data class HomeCollectModel constructor(val imageId : Int, val collectName:String,var itemClick : () -> Unit)