package com.rm.baselisten.model

import android.app.Activity

/**
 * desc   :
 * date   : 2020/09/03
 * version: 1.0
 */
data class BaseResultModel constructor(
    var resultCode: Int = Activity.RESULT_CANCELED,
    val dataMap: HashMap<String,Any>? = null
)