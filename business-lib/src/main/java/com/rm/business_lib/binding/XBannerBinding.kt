package com.rm.business_lib.binding

import android.net.Uri
import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.google.gson.Gson
import com.rm.baselisten.util.DLog
import com.rm.baselisten.utilExt.dip
import com.rm.business_lib.bean.BannerInfoBean
import com.rm.business_lib.bean.BannerJumpBean
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
                        if(param == "page"){
                            var page = mUri.getQueryParameter("page")
                        }else if(param == "param"){
                            val jumpBean = Gson().fromJson(mUri.getQueryParameter("param"), BannerJumpBean::class.java)
                            DLog.d("suolong",jumpBean.audio_id)
                        }
                    }
                }
                "h5"->{

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
            view as ImageView,
            bannerInfoList[position].img_url
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
            bannerInfoList[position].img_url
        )
        view.setPadding(
            view.dip(16f),
            0,
            view.dip(16f),
            0
        )
    }
}