package com.rm.module_home.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.rm.module_home.bean.BannerInfo
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
        com.rm.business_lib.glide.loadImage(view as ImageView, bannerInfoList[position].imgUrl)
    }
}