package com.rm.module_home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rm.business_lib.xbanner.BannerInfo
import com.rm.module_home.model.HomeBoutiqueModel
import com.rm.module_home.model.HomeCollectModel
import com.rm.module_home.model.HomeRecommendModel

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
class HomeFragmentViewModel : ViewModel() {

    var bannerInfoList = MutableLiveData<List<BannerInfo>>()
    var homeCollectModel = MutableLiveData<HomeCollectModel>()
    var homeBoutiqueModel = MutableLiveData<HomeBoutiqueModel>()
    var homeRecommendModel = MutableLiveData<HomeRecommendModel>()


    fun getBannerInfo() {
        val bannerList = mutableListOf<BannerInfo>()
        bannerList.add(BannerInfo("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597921071741&di=f7334148be90918832df8165a435eab5&imgtype=0&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D3807836035%2C2971917368%26fm%3D214%26gp%3D0.jpg"))
        bannerList.add(BannerInfo("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597921071342&di=fbc56e43d75d84e151fb793b797017c4&imgtype=0&src=http%3A%2F%2Fpic.90sjimg.com%2Fback_pic%2F00%2F00%2F69%2F40%2Fs_1198_2cc8d6389629c39568e4a22b851e2b88.jpg"))
        bannerList.add(BannerInfo("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg"))
        bannerList.add(BannerInfo("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2843937210,358921993&fm=26&gp=0.jpg"))
        bannerInfoList.value = bannerList
    }

    fun getCollect() {

    }
}