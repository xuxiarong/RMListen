package com.rm.module_listen.bind

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.rm.module_listen.R

/**
 * desc   :
 * date   : 2020/09/23
 * version: 1.0
 */

@BindingAdapter("bindSubsText")
fun TextView.bindSubsText(number:Int?){
    when {
        number ==null -> {
            text = resources.getString(R.string.listen_my_listen_subs)
        }
        number <=0 -> {
            text = resources.getString(R.string.listen_my_listen_subs)
        }
        else -> {
            text = String.format(resources.getString((R.string.listen_my_listen_subs_number),number))
        }
    }
}