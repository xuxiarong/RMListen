package com.rm.business_lib.bean

import com.stx.xhb.androidx.entity.BaseBannerInfo

/**
 * desc   : XBanner所需数据对象
 * date   : 2020/08/20
 * version: 1.0
 */
data class BannerInfoBean (
    var id: Int,
    var img: String,
    var img_url: String,
    var jump: String,
    var page_id: Int,
    var seq: Int
) : BaseBannerInfo {
    override fun getXBannerUrl(): Any {
        return img_url
    }

    override fun getXBannerTitle(): String {
        return ""
    }
}