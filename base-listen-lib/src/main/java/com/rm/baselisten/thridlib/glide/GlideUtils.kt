package com.rm.baselisten.thridlib.glide

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.rm.baselisten.utilExt.dip

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
fun loadImage(imageView: ImageView, url: String?, @DrawableRes defaultResId: Int) {
    if (imageView.context == null) return
    val options: RequestOptions = RequestOptions() //图片加载出来前，显示的图片
        .placeholder(defaultResId) //url为空的时候,显示的图片
        .fallback(defaultResId) //图片加载失败后，显示的图片
        .error(defaultResId)
    Glide.with(imageView).load(url)
        .apply(options)
        .into(imageView)
}

fun loadImageByTransform(
    imageView: ImageView,
    url: String?,
    @DrawableRes defaultResId: Int,
    transformation: BitmapTransformation
) {
    if (imageView.context == null) return
    val options: RequestOptions = RequestOptions() //图片加载出来前，显示的图片
        .placeholder(defaultResId) //url为空的时候,显示的图片
        .fallback(defaultResId) //图片加载失败后，显示的图片
        .error(defaultResId)
        .transform(transformation)
    Glide.with(imageView).load(url)
        .apply(options)
        .into(imageView)
}

fun loadImage(imageView: ImageView, url: String?) {
    loadImage(
        imageView,
        url,
        0
    )
}

fun loadCircleImage(imageView: ImageView, url: String, @DrawableRes defaultResId: Int) {
    loadImageByTransform(
        imageView,
        url,
        defaultResId,
        CircleCrop()
    )
}

fun loadCircleImage(imageView: ImageView, url: String?) {
    loadImageByTransform(
        imageView,
        url,
        0,
        CircleCrop()
    )
}

fun loadRoundCornersImage(imageView: ImageView, url: String?, @DrawableRes defaultResId: Int) {
    loadImageByTransform(
        imageView,
        url,
        defaultResId,
        RoundedCorners(6)
    )
}

fun loadRoundCornersImage(corner: Float, imageView: ImageView, url: String?) {
    if (corner <= 0) {
        loadImage(imageView, url)
        return
    }
    loadImageByTransform(
        imageView,
        url,
        0,
        RoundedCorners(imageView.dip(corner))
    )
}

fun loadBlurImage(imageView: ImageView, url: String) {
    imageView.alpha = 0.1f
    loadImageByTransform(
        imageView,
        url,
        0,
        BlurTransformation(imageView.context, 25, 6)
    )
}

