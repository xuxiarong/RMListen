package com.rm.module_main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rm.module_main.model.HomeBannerModel
import com.rm.module_main.model.HomeBoutiqueModel
import com.rm.module_main.model.HomeCollectModel
import com.rm.module_main.model.HomeRecommendModel

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
class HomeFragmentViewModel : ViewModel() {

    var homeBannerModel = MutableLiveData<HomeBannerModel>()
    var homeCollectModel = MutableLiveData<HomeCollectModel>()
    var homeBoutiqueModel = MutableLiveData<HomeBoutiqueModel>()
    var homeRecommendModel = MutableLiveData<HomeRecommendModel>()
}