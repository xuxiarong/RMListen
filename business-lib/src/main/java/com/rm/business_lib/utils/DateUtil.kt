package com.rm.business_lib.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @des:
 * @data: 9/25/20 2:50 PM
 * @Version: 1.0.0
 */

val mmSS = "mm:ss"

@SuppressLint("SimpleDateFormat")
fun String.time2format(time: Long): String = SimpleDateFormat(this).format(Date(time))
