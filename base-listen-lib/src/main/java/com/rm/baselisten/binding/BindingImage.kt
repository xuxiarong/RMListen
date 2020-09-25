package com.rm.baselisten.binding

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.rm.baselisten.R
import com.rm.baselisten.thridlib.glide.loadBlurImage
import com.rm.baselisten.thridlib.glide.loadCircleImage
import com.rm.baselisten.thridlib.glide.loadImage
import com.rm.baselisten.thridlib.glide.loadRoundCornersImage
import com.rm.baselisten.util.DLog

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

@BindingAdapter("bindSrc")
fun ImageView.bindSrc(resourceId: Int) {
    if (resourceId > 0) {
        visibility = View.VISIBLE
        setImageResource(resourceId)
    } else {
        visibility = View.GONE
    }
}

@BindingAdapter("bindVisible")
fun ImageView.bindVisible(content: String? = "") {
    val text = content ?: ""
    visibility = if (TextUtils.isEmpty(text)) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("corner", "bindUrl", "isCircle", requireAll = false)
fun ImageView.bindUrl(corner: Float = 0f, bindUrl: String?, isCircle: Boolean = false) {
    val url = bindUrl ?: ""
    when {
        isCircle -> {
            loadCircleImage(this, url)
        }
        corner > 0 -> {
            loadRoundCornersImage(corner, this, url)
        }
        else -> {
            loadImage(this, url)
        }
    }
}

@BindingAdapter("blurUrl")
fun ImageView.blurUrl(blurUrl: String?) {
    DLog.i("------->blurUrl","$blurUrl")
    val url = blurUrl ?: ""
    loadBlurImage(this, url)
}