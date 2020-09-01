package com.rm.baselisten.net.bean

/**
 * Created by luyao
 * on 2018/3/13 14:38
 */
data class BaseResponse<out T>(val code: Int, val msg: String, val data: T)