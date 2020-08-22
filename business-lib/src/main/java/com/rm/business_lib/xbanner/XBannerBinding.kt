package com.rm.business_lib.xbanner

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.stx.xhb.androidx.XBanner

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
@BindingAdapter("bindData")
fun XBanner.bindData(bannerInfoList: List<BannerInfo>?) {
    if(bannerInfoList == null){
        return
    }

    setBannerData(bannerInfoList)
    loadImage { _, _, view, position ->
        com.rm.baselisten.thridlib.glide.loadRoundCornersImage(8f,view as ImageView, bannerInfoList[position].imgUrl)
    }
}