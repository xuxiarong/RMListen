package com.rm.baselisten.ktx

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

/**
 * desc   : 根据传递任何值过滤
 * date   : 2020/09/03
 * version: 1.0
 */

fun  Intent.putAnyExtras(name : String , value : Any){
    when (value) {
        is Int -> putExtra(name, value)
        is Byte -> putExtra(name, value)
        is Char -> putExtra(name, value)
        is Short -> putExtra(name, value)
        is Boolean -> putExtra(name, value)
        is Long -> putExtra(name, value)
        is Float -> putExtra(name, value)
        is Double -> putExtra(name, value)
        is String -> putExtra(name, value)
        is CharSequence -> putExtra(name, value)
        is Parcelable -> putExtra(name, value)
        is Array<*> -> putExtra(name, value)
        is ArrayList<*> -> putExtra(name, value)
        is Serializable -> putExtra(name, value)
        is BooleanArray -> putExtra(name, value)
        is ByteArray -> putExtra(name, value)
        is ShortArray -> putExtra(name, value)
        is CharArray -> putExtra(name, value)
        is IntArray -> putExtra(name, value)
        is LongArray -> putExtra(name, value)
        is FloatArray -> putExtra(name, value)
        is DoubleArray -> putExtra(name, value)
        is Bundle -> putExtra(name, value)
        is Intent -> putExtra(name, value)
    }
}