package com.rm.baselisten.model

import android.os.Bundle

/**
 * desc   :
 * date   : 2020/09/03
 * version: 1.0
 */
data class BaseIntentModel constructor(
    val clazz: Class<*>,
    val bundle: Bundle = Bundle(),
    val requestCode : Int = -1
)