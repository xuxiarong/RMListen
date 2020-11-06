package com.rm.baselisten.model

/**
 * desc   :
 * date   : 2020/09/03
 * version: 1.0
 */
data class BaseTipModel constructor(
    val content: String = "",
    val contentColor: Int = 0,
    val isProgress: Boolean = false,
    val isNetError: Boolean = false
)