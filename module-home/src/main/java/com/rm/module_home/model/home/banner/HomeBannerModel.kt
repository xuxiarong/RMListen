package com.rm.module_home.model.home.banner

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.stx.xhb.androidx.entity.BaseBannerInfo

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
data class HomeBannerModel constructor(val bannerUrl:String)  : BaseBannerInfo,MultiItemEntity {
    override val itemType = 0

    override fun getXBannerUrl(): Any {
        return bannerUrl
    }

    override fun getXBannerTitle(): String {
        return ""
    }
}

