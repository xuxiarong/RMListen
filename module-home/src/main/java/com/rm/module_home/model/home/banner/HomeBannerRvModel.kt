package com.rm.module_home.model.home.banner

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.business_lib.bean.BannerInfoBean
import com.rm.module_home.R

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
class HomeBannerRvModel(var bannerList: List<BannerInfoBean>?) : MultiItemEntity {

    override val itemType = R.layout.home_item_banner


}