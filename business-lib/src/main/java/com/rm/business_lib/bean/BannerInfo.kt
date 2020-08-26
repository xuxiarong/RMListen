package com.rm.business_lib.bean

import com.stx.xhb.androidx.entity.BaseBannerInfo

/**
 * desc   : XBanner所需数据对象
 * date   : 2020/08/20
 * version: 1.0
 */
class BannerInfo(val imgUrl: String) : BaseBannerInfo {
    override fun getXBannerUrl(): Any {
        return imgUrl
    }

    override fun getXBannerTitle(): String {
        return ""
    }
}