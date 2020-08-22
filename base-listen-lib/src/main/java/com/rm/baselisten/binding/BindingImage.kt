package com.rm.baselisten.binding

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.rm.baselisten.thridlib.glide.loadImage

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
    loadImage(this,url)
}

//@BindingAdapter("bindRoundUrl")
//fun ImageView.bindRoundUrl(corner:Float,url: String) {
//    loadRoundCornersImage(corner,this,url)
//}
//
//@SuppressLint("CheckResult")
//@BindingAdapter("bindCircleUrl")
//fun ImageView.bindCircleUrl(url: String) {
//    loadCircleImage(this,url)
//}