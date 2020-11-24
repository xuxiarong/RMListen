package com.rm.business_lib.binding

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.gson.Gson
import com.rm.baselisten.util.DLog
import com.rm.baselisten.utilExt.dip
import com.rm.business_lib.R
import com.rm.business_lib.bean.BannerInfoBean
import com.rm.business_lib.bean.BannerJumpBean
import com.rm.business_lib.xbanner.XBanner


/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
@BindingAdapter("bindData")
fun XBanner.bindData(bannerInfoList: List<BannerInfoBean>?) {
    if (bannerInfoList == null) {
        return
    }

    setBannerData(bannerInfoList)
    loadImage { _, _, view, position ->
        com.rm.baselisten.thridlib.glide.loadRoundCornersImage(
            8f,
            view as ImageView,
            bannerInfoList[position].img_url
        )
    }
    setOnItemClickListener { banner, model, view, position ->
        val bannerInfoBean = bannerInfoList[position]
        DLog.d("suolong", bannerInfoBean.banner_jump)
        val mUri: Uri = Uri.parse(bannerInfoBean.banner_jump)
        val authority = mUri.authority
        if (!TextUtils.isEmpty(authority)) {
            when (authority) {
                "native" -> {
                    val params = mUri.queryParameterNames
                    for (param in params) {
                        if (param == "page") {
                            var page = mUri.getQueryParameter("page")
                        } else if (param == "param") {
                            val jumpBean = Gson().fromJson(
                                mUri.getQueryParameter("param"),
                                BannerJumpBean::class.java
                            )
                            DLog.d("suolong", jumpBean.audio_id)
                        }
                    }
                }
                "h5" -> {

                }
                else -> {

                }
            }
        }
    }
}

@BindingAdapter("bindBanner")
fun XBanner.bindBanner(bannerInfoList: List<BannerInfoBean>?) {
    if (bannerInfoList == null) {
        return
    }

    setBannerData(bannerInfoList)
    loadImage { _, _, view, position ->
        com.rm.baselisten.thridlib.glide.loadRoundCornersImage(
            8f,
                view.findViewById(R.id.banner_img) as ImageView,
            bannerInfoList[position].img_url
        )
    }
}

@BindingAdapter("paddingBindData")
fun XBanner.paddingBindData(bannerInfoList: List<BannerInfoBean>?) {
    paddingBindData(bannerInfoList, false)
}

@BindingAdapter("paddingBindData", "bindBannerIsShadow", requireAll = false)
fun XBanner.paddingBindData(bannerInfoList: List<BannerInfoBean>?, isShadow: Boolean?) {
    if (bannerInfoList == null) {
        return
    }
    setBannerData(bannerInfoList)
    loadImage { _, _, view, position ->
        com.rm.baselisten.thridlib.glide.loadRoundCornersImage(
            8f,
            view.findViewById(R.id.banner_img) as ImageView,
            bannerInfoList[position].img_url
        )
//        Glide.with(view).asBitmap()
//            .load(bannerInfoList[position].img_url)
//            .error(R.drawable.base_ic_default)
//            .placeholder(R.drawable.base_ic_default)
//            .into(object : CustomTarget<Bitmap>(view.width, view.height) {
//                override fun onLoadCleared(placeholder: Drawable?) {
//                }
//
//                override fun onLoadFailed(errorDrawable: Drawable?) {
//                    super.onLoadFailed(errorDrawable)
//                    errorDrawable?.let {
//                    }
//                }
//
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//
//                }
//            })
        view.setPadding(
            view.dip(16f),
            0,
            view.dip(16f),
            0
        )
        /* if (isShadow == true) {
             view.bindShadow(
                 ShadowDrawableUtil.SHAPE_ROUND_PART,
                 ShadowDrawableUtil.TypeEnum.BOTTOM,
                 Color(R.color.business_color_ff5e5e),
                 0,
                 Color(R.color.business_text_color_999999),
                 10
             )
         }*/
    }
}

fun addShadow(bm: Bitmap): Bitmap {
    val mBackShadowColors = intArrayOf(0x00000000, -0x4f555556)
    val mBackShadowDrawableLR = GradientDrawable(
        GradientDrawable.Orientation.LEFT_RIGHT, mBackShadowColors
    )
    mBackShadowDrawableLR.gradientType = GradientDrawable.LINEAR_GRADIENT
    mBackShadowDrawableLR.setBounds(0, 0, 20, bm.height)
    val canvas = Canvas(bm)
    mBackShadowDrawableLR.draw(canvas)
    return bm
}
