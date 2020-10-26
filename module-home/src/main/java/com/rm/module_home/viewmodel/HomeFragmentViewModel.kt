package com.rm.module_home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.adapter.multi.CommonMultiVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.BannerInfoBean
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.model.home.HomeAudioModel
import com.rm.module_home.model.home.HomeMenuModel
import com.rm.module_home.model.home.HomeModel
import com.rm.module_home.model.home.banner.HomeBannerRvModel
import com.rm.module_home.model.home.collect.HomeMenuRvModel
import com.rm.module_home.model.home.grid.HomeGridAudioModel
import com.rm.module_home.model.home.grid.HomeGridAudioRvModel
import com.rm.module_home.model.home.hordouble.HomeAudioHorDoubleFooterModel
import com.rm.module_home.model.home.hordouble.HomeAudioHorDoubleModel
import com.rm.module_home.model.home.hordouble.HomeAudioHorDoubleRvModel
import com.rm.module_home.model.home.horsingle.HomeAudioHorSingleModel
import com.rm.module_home.model.home.horsingle.HomeAudioHorSingleRvModel
import com.rm.module_home.model.home.ver.HomeAudioVerModel
import com.rm.module_home.model.home.ver.HomeAudioVerRvModel
import com.rm.module_home.repository.HomeRepository

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
class HomeFragmentViewModel(var repository: HomeRepository) : BaseVMViewModel() {

    // 下拉刷新和加载更多控件状态控制Model
    val refreshStatusModel = SmartRefreshLayoutStatusModel()

    var homeBannerInfoList = MutableLiveData<List<BannerInfoBean>>()
    var homeMenuList = MutableLiveData<MutableList<MultiItemEntity>>()
    var collectItemClickList: (HomeMenuModel) -> Unit = {}
    var doubleRvLeftScrollOpenDetail: () -> Unit = {}


    var homeAllData = MutableLiveData<MutableList<MultiItemEntity>>()

    var audioClick: (HomeAudioModel) -> Unit = {}
    var blockClick: (com.rm.module_home.model.home.HomeBlockModel) -> Unit = {}

    fun getHomeDataFromService() {
        refreshStatusModel.setHasMore(false)
        launchOnIO {
            repository.getHomeData().checkResult(
                onSuccess = {
                    showContentView()
                    refreshStatusModel.finishRefresh(true)
                    dealHomeData(it)
                }, onError = {
                    showServiceError()
                    refreshStatusModel.finishRefresh(false)
                    DLog.d("suolong ", "error = $it")
                }
            )
        }
    }

    private fun dealHomeData(homeModel: HomeModel) {

        val allData = ArrayList<MultiItemEntity>()

        homeBannerInfoList.value = homeModel.banner_list

        allData.add(HomeBannerRvModel(homeModel.banner_list))


        val menuList = ArrayList<MultiItemEntity>()
        homeModel.menu_list.forEach {
            it.itemType = R.layout.home_item_menu
        }
        menuList.addAll(homeModel.menu_list)
        allData.add(HomeMenuRvModel())

        homeModel.block_list.forEach {
            if(it.audio_list.list.size>0){
                setBlockData(allData, it)
            }
        }
        homeMenuList.value = menuList
        homeAllData.value = allData
    }

    /**
     * 处理服务器返回的板块数据
     * @param allData ArrayList<MultiItemEntity> 所有的数据集合
     * @param block HomeBlockModel 板块
     */
    private fun setBlockData(
        allData: ArrayList<MultiItemEntity>,
        block: com.rm.module_home.model.home.HomeBlockModel
    ) {
        when (block.block_type_id) {
            BLOCK_HOR_DOUBLE -> {
                val doubleHorList = ArrayList<MultiItemEntity>()
                val bottomList = ArrayList<HomeAudioModel>()
                val topList = ArrayList<HomeAudioModel>()
                //对板块1进行上下行拆分
                for (i in 0 until block.audio_list.list.size) {
                    if (i % 2 == 0) {
                        topList.add(block.audio_list.list[i])
                    } else {
                        bottomList.add(block.audio_list.list[i])
                    }
                }
                //如果板块1的数据是奇数，那么底下的那一行会少一个数据，这里做下处理
                var doubleModel: HomeAudioHorDoubleModel
                for (i in 0 until topList.size) {
                    doubleModel = if (i == topList.size - 1) {
                        HomeAudioHorDoubleModel(topList[i], topList[i], false)
                    } else {
                        HomeAudioHorDoubleModel(topList[i], bottomList[i])
                    }
                    doubleHorList.add(doubleModel)
                }
                if (doubleHorList.size >= 3) {
                    doubleHorList.add(HomeAudioHorDoubleFooterModel())
                }
                allData.add(com.rm.module_home.model.home.more.HomeBlockModel(block))
                allData.add(HomeAudioHorDoubleRvModel(doubleHorList))
            }
            BLOCK_HOR_GRID -> {
                val gridList = ArrayList<MultiItemEntity>()
                block.audio_list.list.forEach {
                    gridList.add(HomeGridAudioModel(it))
                }
                allData.add(com.rm.module_home.model.home.more.HomeBlockModel(block))
                allData.add(HomeGridAudioRvModel(gridList))
            }
            BLOCK_HOR_SINGLE -> {
                val horSingList = ArrayList<MultiItemEntity>()
                block.audio_list.list.forEach {
                    horSingList.add(HomeAudioHorSingleModel(it))
                }
                allData.add(com.rm.module_home.model.home.more.HomeBlockModel(block))
                allData.add(HomeAudioHorSingleRvModel(horSingList))
            }
            BLOCK_HOR_VEr -> {
                val verSingList = ArrayList<MultiItemEntity>()
                block.audio_list.list.forEach {
                    verSingList.add(HomeAudioVerModel(it))
                }
                allData.add(com.rm.module_home.model.home.more.HomeBlockModel(block))
                allData.add(HomeAudioVerRvModel(verSingList))
            }
            else -> {

            }
        }
    }

    fun getAdapterWithList(data: ArrayList<MultiItemEntity>): CommonMultiVMAdapter {
        return CommonMultiVMAdapter(
            this,
            data,
            BR.viewModel,
            BR.item
        )
    }

    fun collectClick(model: HomeMenuModel) {
        collectItemClickList(model)
        DLog.d("suolong", "model = ${model.menu_name} ")
    }

    fun onAudioClick(audioModel: HomeAudioModel) {
        audioClick(audioModel)
    }

    fun onBlockClick(block: com.rm.module_home.model.home.HomeBlockModel) {
        blockClick(block)
    }

    fun doubleRvOpenDetail() {
        doubleRvLeftScrollOpenDetail()
    }

    companion object {
        const val BLOCK_HOR_DOUBLE = 1
        const val BLOCK_HOR_GRID = 2
        const val BLOCK_HOR_SINGLE = 3
        const val BLOCK_HOR_VEr = 4
    }

}