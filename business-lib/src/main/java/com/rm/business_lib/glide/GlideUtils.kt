package com.rm.business_lib.glide

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.rm.business_lib.R

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
fun loadImage(imageView: ImageView, url: String, @DrawableRes defaultResId: Int) {
    if (imageView.context == null) return
    val options: RequestOptions = RequestOptions() //图片加载出来前，显示的图片
        .placeholder(defaultResId) //url为空的时候,显示的图片
        .fallback(defaultResId) //图片加载失败后，显示的图片
        .error(defaultResId)
    Glide.with(imageView).load(url)
        .apply(options)
        .into(imageView)
}
fun loadImageByTransform(imageView: ImageView,url: String, @DrawableRes defaultResId: Int,transformation: BitmapTransformation){
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

fun loadImage(imageView: ImageView, url: String){
    loadImage(
        imageView,
        url,
        R.drawable.business_defualt_img
    )
}

fun loadCircleImage(imageView: ImageView,url: String, @DrawableRes defaultResId: Int){
    loadImageByTransform(
        imageView,
        url,
        defaultResId,
        CircleCrop()
    )
}

