package com.rm.module_home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.BannerInfoBean
import com.rm.module_home.R
import com.rm.module_home.model.home.HomeAudioModel
import com.rm.module_home.model.home.HomeBlockModel
import com.rm.module_home.model.home.HomeMenuModel
import com.rm.module_home.model.home.HomeModel
import com.rm.module_home.model.home.banner.HomeBannerRvModel
import com.rm.module_home.model.home.collect.HomeCollectRvModel
import com.rm.module_home.model.home.grid.HomeGridRecommendModel
import com.rm.module_home.model.home.grid.HomeGridRecommendRvModel
import com.rm.module_home.model.home.hordouble.HomeRecommendHorDoubleModel
import com.rm.module_home.model.home.hordouble.HomeRecommendHorDoubleRvModel
import com.rm.module_home.model.home.horsingle.HomeRecommendHorSingleModel
import com.rm.module_home.model.home.horsingle.HomeRecommendHorSingleRvModel
import com.rm.module_home.model.home.more.HomeMoreModel
import com.rm.module_home.model.home.ver.HomeRecommendVerRvModel
import com.rm.module_home.repository.HomeRepository

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
class HomeFragmentViewModel(var repository: HomeRepository) : BaseVMViewModel() {

    var homeBannerInfoList = MutableLiveData<List<BannerInfoBean>>()
    var homeMenuList = MutableLiveData<MutableList<MultiItemEntity>>()

    var homeHorSingleList = MutableLiveData<MutableList<HomeRecommendHorSingleModel>>()
    var homeHorDoubleList = MutableLiveData<MutableList<MultiItemEntity>>()
    var homeGridList = MutableLiveData<MutableList<HomeGridRecommendModel>>()
    var collectItemClickList: (HomeMenuModel) -> Unit = {}

    var homeVerList = MutableLiveData<MutableList<MultiItemEntity>>()
    var doubleRvLeftScrollOpenDetail: () -> Unit = {}


    var homeAllData = MutableLiveData<MutableList<MultiItemEntity>>()


    fun getHomeDataFromService() {
        launchOnIO {
            repository.getHomeData().checkResult(
                onSuccess = {
                    dealHomeData(it)
                }, onError = {
                    DLog.d("suolong ", "error = $it")
                }
            )
        }
    }

    fun dealHomeData(homeModel: HomeModel) {

        val allData = ArrayList<MultiItemEntity>()

        homeBannerInfoList.value = homeModel.banner_list

        allData.add(HomeBannerRvModel(homeModel.banner_list))


        val menuList = ArrayList<MultiItemEntity>()
        homeModel.menu_list.forEach {
            it.itemType = R.layout.home_item_menu
        }
        menuList.addAll(homeModel.menu_list)
        allData.add(HomeCollectRvModel())

        homeModel.block_list.forEach {
            setBlockData(allData,it)
        }
        homeMenuList.value = menuList
        homeAllData.value = allData
    }


    fun setBlockData(allData: ArrayList<MultiItemEntity>, block: HomeBlockModel) {
        when (block.block_type_id) {
            1 -> {
                val doubleHorList = ArrayList<MultiItemEntity>()
                val bottomList = ArrayList<HomeAudioModel>()
                val topList = ArrayList<HomeAudioModel>()

                for (i in 0 until block.audio_list.list.size) {
                    if(i%2==0){
                        topList.add(block.audio_list.list[i])
                    }else{
                        bottomList.add(block.audio_list.list[i])
                    }
                }

                var doubleModel : HomeRecommendHorDoubleModel
                for(i in 0 until  topList.size) {
                    if (i == topList.size-1) {
                        doubleModel = HomeRecommendHorDoubleModel(topList[i], topList[i], false)
                    } else {
                        doubleModel = HomeRecommendHorDoubleModel(topList[i], bottomList[i])
                    }
                    doubleHorList.add(doubleModel)
                }
                homeHorDoubleList.value = doubleHorList
                allData.add(HomeMoreModel(block.block_name, {}))
                allData.add(HomeRecommendHorDoubleRvModel())
            }
            2 -> {
                allData.add(HomeMoreModel(block.block_name,{}))
                allData.add(HomeGridRecommendRvModel())
            }
            3 -> {
                allData.add(HomeMoreModel(block.block_name,{}))
                allData.add(HomeRecommendHorSingleRvModel())
            }
            4 -> {
                allData.add(HomeMoreModel(block.block_name,{}))
                allData.add(HomeRecommendVerRvModel())
            }
            else -> {

            }
        }
    }


    fun collectClick(model: HomeMenuModel) {
        collectItemClickList(model)
        DLog.d("suolong", "model = ${model.menu_name} ")
    }

    fun initCollectClick(vararg clickList: () -> Unit) {

    }

    fun doubleRvOpenDetail() {
        doubleRvLeftScrollOpenDetail()
    }


}