package com.rm.business_lib.xbanner

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.rm.baselisten.util.DisplayUtils
import com.stx.xhb.androidx.XBanner

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
@BindingAdapter("bindData")
fun XBanner.bindData(bannerInfoList: List<BannerInfo>?) {
    if (bannerInfoList == null) {
        return
    }

    setBannerData(bannerInfoList)
    loadImage { _, _, view, position ->
        com.rm.baselisten.thridlib.glide.loadRoundCornersImage(
            8f,
            view as ImageView,
            bannerInfoList[position].imgUrl
        )
    }
}

@BindingAdapter("paddingBindData")
fun XBanner.paddingBindData(bannerInfoList: List<BannerInfo>?) {
    if (bannerInfoList == null) {
        return
    }

    setBannerData(bannerInfoList)
    loadImage { _, _, view, position ->
        com.rm.baselisten.thridlib.glide.loadRoundCornersImage(
            8f,
            view as ImageView,
            bannerInfoList[position].imgUrl
        )
        view.setPadding(
            DisplayUtils.dip2px(view.context, 16f),
            0,
            DisplayUtils.dip2px(view.context, 16f),
            0
        )
    }
}