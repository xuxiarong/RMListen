package com.rm.baselisten.ktx

import java.lang.Double.parseDouble

/**
 * desc   :
 * date   : 2020/12/11
 * version: 1.0
 */

fun String.toLongSafe() : Long{
    return try {
        toLong()
    }catch (e : Exception){
        0L
    }
}

fun String.toIntSafe() : Int{
    return try {
        toInt()
    }catch (e : Exception){
        0
    }
}

fun String.toFloatSafe() : Float{
    return try {
        toFloat()
    }catch (e : Exception){
        0f
    }
}

fun String.toDoubleSafe() : Double{
    return try {
        toDouble()
    }catch (e : Exception){
        parseDouble("0")
    }
}