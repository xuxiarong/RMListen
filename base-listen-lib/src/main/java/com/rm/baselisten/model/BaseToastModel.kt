package com.rm.baselisten.model

import com.rm.baselisten.R

/**
 * desc   :
 * date   : 2020/09/03
 * version: 1.0
 */
data class BaseToastModel constructor(
    val content: String? = "",
    val contentId: Int = -1,
    val colorId: Int = R.color.base_333,
    val canAutoCancel: Boolean = true,
    val isNetError : Boolean= false
)