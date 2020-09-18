package com.rm.business_lib.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.rm.baselisten.utilExt.dip
import com.rm.business_lib.bean.BannerInfoBean
import com.stx.xhb.androidx.XBanner

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
            view as ImageView,
            bannerInfoList[position].banner_img
        )
    }
}

@BindingAdapter("paddingBindData")
fun XBanner.paddingBindData(bannerInfoList: List<BannerInfoBean>?) {
    if (bannerInfoList == null) {
        return
    }

    setBannerData(bannerInfoList)
    loadImage { _, _, view, position ->
        com.rm.baselisten.thridlib.glide.loadRoundCornersImage(
            8f,
            view as ImageView,
            bannerInfoList[position].banner_img
        )
        view.setPadding(
            view.dip(16f),
            0,
            view.dip(16f),
            0
        )
    }
}