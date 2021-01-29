package com.rm.baselisten.binding

import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.rm.baselisten.R
import com.rm.baselisten.model.BasePlayStatusModel
import com.rm.baselisten.thridlib.glide.loadBlurImage
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

@BindingAdapter("corner", "bindUrl", "isCircle", "defaultIcon","isCenterCrop", requireAll = false)
fun ImageView.bindUrl(
    corner: Float = 0f,
    bindUrl: String?,
    isCircle: Boolean = false,
    defaultIcon: Drawable? = null,
    isCenterCrop: Boolean? = false
) {
    val url = bindUrl ?: ""
    when {
        isCircle -> {
            if (defaultIcon != null) {
                loadCircleImage(this, url, defaultIcon)
            } else {
                loadCircleImage(this, url)
            }
        }
        corner > 0 -> {
            if (defaultIcon != null) {
                loadRoundCornersImage(corner, this, url, defaultIcon,isCenterCrop)
            } else {
                loadRoundCornersImage(corner, this, url,isCenterCrop)
            }
        }
        else -> {
            if (defaultIcon != null) {
                loadImage(this, url, defaultIcon)
            } else {
                loadImage(this, url)
            }
        }
    }
}

@BindingAdapter("blurUrl")
fun ImageView.blurUrl(blurUrl: String?) {
    val url = blurUrl ?: ""
    loadBlurImage(this, url)
}


@BindingAdapter("bindPlayRotation")
fun ImageView.bindPlayRotation(model: BasePlayStatusModel?) {
    if (model == null) {
        clearAnimation()
        return
    } else {
        if (model.playReady && model.playStatus == 3) {
            val playAnim = AnimationUtils.loadAnimation(context, R.anim.base_rotate_play)
            val interpolator = LinearInterpolator() //设置匀速旋转，在xml文件中设置会出现卡顿
            playAnim!!.interpolator = interpolator
            startAnimation(playAnim) //开始动画
        } else {
            clearAnimation()
        }
    }

}