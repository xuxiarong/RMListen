package com.rm.baselisten.binding

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
@BindingAdapter("bindSrc")
fun ImageView.bindSrc(resourceId: Int?) {
    if (resourceId != null && resourceId > 0) {
        visibility = View.VISIBLE
        setImageResource(resourceId)
    } else {
        visibility = View.GONE
    }

}

@SuppressLint("CheckResult")
@BindingAdapter("bindUrl")
fun ImageView.bindUrl(url: String) {
    Glide.with(context).load(url)
        .into(this)
}

@SuppressLint("CheckResult")
@BindingAdapter("bindRoundUrl")
fun ImageView.bindRoundUrl(url: String) {
    Glide.with(context).load(url)
        .into(this)
}