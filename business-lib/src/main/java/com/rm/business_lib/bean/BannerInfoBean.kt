package com.rm.business_lib.bean

import com.stx.xhb.androidx.entity.BaseBannerInfo

/**
 * desc   : XBanner所需数据对象
 * date   : 2020/08/20
 * version: 1.0
 */
data class BannerInfoBean (
    var banner_id: Int,
    var banner_img: String,
    var banner_jump: String,
    var banner_seq: Int,
    var page_id: Int
) : BaseBannerInfo {
    override fun getXBannerUrl(): Any {
        return banner_img
    }

    override fun getXBannerTitle(): String {
        return ""
    }
}