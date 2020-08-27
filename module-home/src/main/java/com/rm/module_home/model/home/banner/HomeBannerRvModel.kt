package com.rm.module_home.model.home.banner

import com.rm.baselisten.adapter.multi.BaseMultiAdapter
import com.rm.business_lib.bean.BannerInfo
import com.rm.module_home.R

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
 class HomeBannerRvModel(var bannerList : List<BannerInfo>?) : BaseMultiAdapter.IBindItemType{
    override fun bindType(): Int {
        return R.layout.home_item_banner
    }

}