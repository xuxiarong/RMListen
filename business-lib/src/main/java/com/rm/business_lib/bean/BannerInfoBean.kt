package com.rm.business_lib.bean

import com.rm.business_lib.xbanner.entity.BaseBannerInfo

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
    var page_id: Int,
    var img_url: String
) : BaseBannerInfo {
    override fun getXBannerUrl(): Any {
        return img_url
    }

    override fun getXBannerTitle(): String {
        return ""
    }
}