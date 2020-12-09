package com.rm.module_home.model.ad

import com.rm.business_lib.bean.BusinessAdModel

/**
 * desc   :
 * date   : 2020/12/08
 * version: 1.0
 */
data class HomeSingleImgAdResultModel(
    var ad_index_banner : MutableList<BusinessAdModel>?,
    var ad_index_floor_streamer: MutableList<BusinessAdModel>?,
    var ad_index_voice: MutableList<BusinessAdModel>?
)

