package com.rm.module_home.activity.boutique

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.viewmodel.BaseNetViewModel
import com.rm.business_lib.bean.BannerInfo
import com.rm.business_lib.bean.BookBean
import com.rm.module_home.repository.BoutiqueRepository

/**
 * desc   :
 * date   : 2020/08/25
 * version: 1.0
 */
class BoutiqueFragmentViewModel(private val repository: BoutiqueRepository) : BaseNetViewModel() {
    var categoryName = ""

    // banner数据列表
    val bannerInfoList = MutableLiveData<List<BannerInfo>>()

    // 列表数据集合
    val bookInfoList = MutableLiveData<MutableList<BookBean>>()

    /**
     * 获取Banner数据集合
     * @return MutableList<BannerInfo>
     */
    fun getBannerInfo() {
        bannerInfoList.value = repository.getBanner()
    }

    fun getBookInfo() {
        bookInfoList.value = repository.getBoutiqueRecommendInfoList()
    }
}