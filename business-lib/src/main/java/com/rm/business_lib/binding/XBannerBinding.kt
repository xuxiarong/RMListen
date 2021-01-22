package com.rm.business_lib.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.rm.baselisten.thridlib.glide.loadRoundCornersImage
import com.rm.baselisten.utilExt.dip
import com.rm.business_lib.R
import com.rm.business_lib.bean.BannerInfoBean
import com.rm.business_lib.xbanner.XBanner


/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
@BindingAdapter("paddingBindData")
fun XBanner.paddingBindData(bannerInfoList: List<BannerInfoBean>?) {
    if (bannerInfoList == null) {
        return
    }
    setBannerData(bannerInfoList)
    loadImage { _, _, view, position ->
        val imageView = view.findViewById(R.id.banner_img) as ImageView
        val bannerInfoBean = bannerInfoList[position]
        loadRoundCornersImage(
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

