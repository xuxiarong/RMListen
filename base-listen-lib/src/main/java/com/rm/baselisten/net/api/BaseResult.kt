package com.rm.baselisten.net.api

/**
 * desc   :
 * date   : 2020/08/18
 * version: 1.0
 */
sealed class BaseResult<out T : Any> {


    val code : Int = 0
    val message : String = ""
    data class Success<out T : Any>(val data: T) : BaseResult<T>()
    data class Error(val msg: String,val errorCode:Int) : BaseResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[msg=$msg]"
        }
    }
}