package com.rm.business_lib.pinyin

import java.io.Serializable

/**
 * desc   :
 * date   : 2020/09/08
 * version: 1.0
 */
interface CN : Serializable {
    fun chinese():String
    fun phoneCode():String
}