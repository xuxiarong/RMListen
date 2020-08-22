package com.rm.baselisten.binding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.rm.baselisten.thridlib.glide.loadCircleImage
import com.rm.baselisten.thridlib.glide.loadImage
import com.rm.baselisten.thridlib.glide.loadRoundCornersImage

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

@BindingAdapter("corner", "bindUrl", "isCircle", requireAll = false)
fun ImageView.bindUrl(corner: Float, bindUrl: String, isCircle: Boolean) {
    when {
        isCircle -> {
            loadCircleImage(this, bindUrl)
        }
        corner > 0 -> {
            loadRoundCornersImage(corner, this, bindUrl)
        }
        else -> {
            loadImage(this, bindUrl)
        }
    }
}