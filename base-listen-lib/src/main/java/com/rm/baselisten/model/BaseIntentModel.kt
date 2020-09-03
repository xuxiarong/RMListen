package com.rm.baselisten.model

/**
 * desc   :
 * date   : 2020/09/03
 * version: 1.0
 */
data class BaseIntentModel constructor(
    val clazz: Class<*>,
    val dataMap: HashMap<String,Any>? = null,
    val requestCode : Int = -1
)