package com.rm.baselisten.load.core

import com.rm.baselisten.load.callback.Callback

/**
 * desc   :
 * date   : 2020/08/10
 * version: 1.0
 */
interface Convertor<T> {
    fun map(t: T): Class<out Callback?>?
}