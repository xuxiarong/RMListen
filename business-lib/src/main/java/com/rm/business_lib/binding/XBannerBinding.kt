package com.rm.business_lib.binding

import androidx.databinding.BindingAdapter
import com.rm.baselisten.utilExt.dip
import com.rm.business_lib.R
import com.rm.business_lib.bean.BannerInfoBean
import com.rm.business_lib.wedgit.BusinessAdImageView
import com.rm.business_lib.xbanner.XBanner


/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
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
        val imageView = view.findViewById(R.id.banner_img) as BusinessAdImageView
        val bannerInfoBean = bannerInfoList[position]
        com.rm.baselisten.thridlib.glide.loadRoundCornersImage(
            8f,
            imageView,
            bannerInfoBean.img_url
        )
        view.setPadding(
            view.dip(16f),
            0,
            view.dip(16f),
            0
        )
    }
}

