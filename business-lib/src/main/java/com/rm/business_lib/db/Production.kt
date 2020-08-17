package com.rm.business_lib.db

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
interface Production<out T> {
    fun  produce() :T
}