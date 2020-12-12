package com.rm.business_lib.bean

import com.rm.business_lib.xbanner.entity.BaseBannerInfo

/**
 * desc   : XBanner所需数据对象
 * date   : 2020/08/20
 * version: 1.0
 */
data class BannerInfoBean(
    var banner_id: Int = 0,
    var banner_img: String,
    var banner_jump: String = "",
    var banner_seq: Int = 0,
    var page_id: Int = 0,
    var img_url: String = "",
    var isAd: Boolean = false,
    var ad_id: Long = -1
) : BaseBannerInfo {
    override fun getXBannerUrl(): Any {
        return img_url
    }

    override fun getXBannerTitle(): String {
        return ""
    }

    override fun isAdBanner(): Boolean {
        return isAd
    }

    override fun getAdId(): String {
        return if (ad_id > 0) {
            ad_id.toString()
        } else {
            ""
        }
    }


}