package com.rm.module_home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rm.module_home.bean.BannerInfo
import com.rm.module_home.model.HomeBoutiqueModel
import com.rm.module_home.model.HomeCollectModel
import com.rm.module_home.model.HomeRecommendModel

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
class HomeFragmentViewModel : ViewModel() {

    var bannerInfoList  = MutableLiveData<List<BannerInfo>>()
    var homeCollectModel = MutableLiveData<HomeCollectModel>()
    var homeBoutiqueModel = MutableLiveData<HomeBoutiqueModel>()
    var homeRecommendModel = MutableLiveData<HomeRecommendModel>()
}