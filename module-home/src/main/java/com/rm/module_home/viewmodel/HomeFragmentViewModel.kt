package com.rm.module_home.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.adapter.multi.CommonMultiVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.BannerInfoBean
import com.rm.business_lib.bean.BusinessAdModel
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.adapter.HomeAdapter
import com.rm.module_home.model.home.*
import com.rm.module_home.model.home.banner.HomeBannerRvModel
import com.rm.module_home.model.home.collect.HomeMenuRvModel
import com.rm.module_home.model.home.grid.HomeGridAudioModel
import com.rm.module_home.model.home.grid.HomeGridAudioRvModel
import com.rm.module_home.model.home.hordouble.HomeAudioDoubleBlockModel
import com.rm.module_home.model.home.hordouble.HomeAudioHorDoubleFooterModel
import com.rm.module_home.model.home.hordouble.HomeAudioHorDoubleModel
import com.rm.module_home.model.home.hordouble.HomeAudioHorDoubleRvModel
import com.rm.module_home.model.home.horsingle.HomeAudioHorSingleModel
import com.rm.module_home.model.home.horsingle.HomeAudioHorSingleRvModel
import com.rm.module_home.model.home.ver.HomeAudioVerModel
import com.rm.module_home.model.home.ver.HomeAudioVerRvModel
import com.rm.module_home.repository.HomeRepository
import kotlin.random.Random

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
class HomeFragmentViewModel(var repository: HomeRepository) : BaseVMViewModel() {

    val homeAdapter: HomeAdapter by lazy {
        HomeAdapter(this, BR.viewModel, BR.item)
    }

    // 下拉刷新和加载更多控件状态控制Model
    val refreshStatusModel = SmartRefreshLayoutStatusModel()

    var homeBannerInfoList = MutableLiveData<MutableList<BannerInfoBean>>()
    var homeMenuList = MutableLiveData<MutableList<MultiItemEntity>>()
    var collectItemClickList: (HomeMenuModel) -> Unit = {}
    var doubleRvLeftScrollOpenDetail: () -> Unit = {}
    var errorMsg = ObservableField<String>()
    var showNetError = ObservableBoolean(false)

    var homeAllData = MutableLiveData<MutableList<MultiItemEntity>>()

    var audioClick: (HomeAudioModel) -> Unit = {}
    var blockClick: (HomeBlockModel) -> Unit = {}

    //首页弹窗广告
    var homeDialogAdModel = ObservableField<BusinessAdModel>()
    //首页block为5的广告
    var homeImgContentAdModel = MutableLiveData<MutableList<BusinessAdModel>>()

    fun getHomeDataFromService() {
        refreshStatusModel.setNoHasMore(true)
        launchOnIO(block = {
            repository.getHomeData().checkResult(
                    onSuccess = {
                        showContentView()
                        refreshStatusModel.finishRefresh(true)
                        dealHomeData(it)
                    },
                    onError = {
                        refreshStatusModel.finishRefresh(false)
                        if (homeAllData.value != null) {
                            showContentView()
                        } else {
                            showServiceError()
                        }
                        errorMsg.set(it)
                        DLog.d("suolong ", "error = $it")
                    }
            )
        }, netErrorBlock = {
            refreshStatusModel.finishRefresh(false)
            showNetError.set(true)
            showNetError.notifyChange()
            if (homeAllData.value != null) {
                showContentView()
            } else {
                showServiceError()
            }
            refreshStatusModel.finishRefresh(false)
        })
    }

    fun getHomeDialogAd() {
        launchOnIO {
            repository.getHomeDialogAd(arrayOf("ad_index_alert")).checkResult(
                    onSuccess = {
                        it.ad_index_alert?.let { dialogAdList ->
                            val randomAdPosition = Random.nextInt(dialogAdList.size)
                            homeDialogAdModel.set(dialogAdList[randomAdPosition])
                        }
                    },
                    onError = {
                        DLog.d("suolong_home", "getHomeDialogAd error = ${it ?: "错误信息为空"}")
                    }
            )
        }
    }

    fun getHomeImgContentAd(){
        if(homeImgContentAdModel.value ==null){
            homeImgContentAdModel.value = mutableListOf()
            launchOnIO {
                repository.getHomeImgContentAd(arrayOf("ad_index_floor_streamer")).checkResult(
                    onSuccess = {
                        it.ad_index_floor_streamer?.let { floorList ->
                            homeImgContentAdModel.value = floorList
                        }
                    },
                    onError = {
                        DLog.d("suolong_home", "getHomeDialogAd error = ${it ?: "错误信息为空"}")
                    }
                )
            }
        }
    }


    /**
     * 获取轮播广告
     */
    fun getHomeBannerAd() {
        launchOnIO {
            repository.getHomeBannerAd(arrayOf("ad_index_banner")).checkResult(
                    onSuccess = {
                        it.ad_index_banner?.let { bannerAdList ->
                            for (i in 0 until homeAdapter.data.size) {
                                val randomAdPosition = Random.nextInt(bannerAdList.size)
                                if (homeAdapter.data[i] is HomeBannerRvModel) {
                                    val currentBannerList = homeBannerInfoList.value

                                    currentBannerList?.let { bannerList ->
                                        if (bannerList.size > 0) {
                                            val adBanner = BannerInfoBean(
                                                    banner_id = bannerList[0].banner_id,
                                                    banner_img = bannerAdList[randomAdPosition].image_path,
                                                    banner_jump = bannerAdList[randomAdPosition].jump_url,
                                                    banner_seq = bannerList[0].banner_seq,
                                                    page_id = bannerList[0].page_id,
                                                    img_url = bannerAdList[randomAdPosition].image_path,
                                                    isAd = true
                                            )
                                            if (bannerList.size > i) {
                                                bannerList.add(i, adBanner)
                                            } else {
                                                bannerList.add(adBanner)
                                            }
                                            homeBannerInfoList.value = bannerList
                                            homeAdapter.notifyDataSetChanged()
                                            return@checkResult
                                        }
                                    }
                                }
                            }
                        }
                    },
                    onError = {
                        DLog.d("suolong_home", "getHomeDialogAd error = ${it ?: "错误信息为空"}")
                    }
            )
        }
    }


    private fun dealHomeData(homeModel: HomeModel) {

        val allData = ArrayList<MultiItemEntity>()

        homeModel.banner_list?.let {
            if (it.isNotEmpty()) {
                getHomeBannerAd()
            }
            homeBannerInfoList.value = it

            allData.add(HomeBannerRvModel(it))
        }

        val menuList = ArrayList<MultiItemEntity>()
        homeModel.menu_list?.forEach {
            it.itemType = R.layout.home_item_menu
        }
        homeModel.menu_list?.let {
            menuList.addAll(homeModel.menu_list)
            allData.add(HomeMenuRvModel())
        }

        homeModel.block_list?.let { blockList ->
            if (null == homeDialogAdModel.get() && blockList.isNotEmpty()) {
                getHomeDialogAd()
            }
            blockList.forEach { blockModel ->
                if (blockModel.audio_list.list.size > 0 || "image" == blockModel.relation_to) {
                    setBlockData(allData, blockModel)
                    DLog.d("suolong","type  = ${blockModel.block_type_id}")
                }
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
            block: HomeBlockModel
    ) {
        block.itemType = R.layout.home_item_block

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
                    doubleModel = if (i == topList.size - 1 && topList.size > bottomList.size) {
                        HomeAudioHorDoubleModel(topList[i], topList[i], false)
                    } else {
                        HomeAudioHorDoubleModel(topList[i], bottomList[i])
                    }
                    doubleHorList.add(doubleModel)
                }
                if (doubleHorList.size >= 3) {
                    doubleHorList.add(HomeAudioHorDoubleFooterModel())
                }
                allData.add(block)
                allData.add(HomeAudioHorDoubleRvModel(doubleHorList, HomeAudioDoubleBlockModel(page_id = block.page_id, block_id = block.block_id, block_name = block.block_name, topic_id = block.topic_id)))
            }
            BLOCK_HOR_GRID -> {
                val gridList = ArrayList<MultiItemEntity>()
                block.audio_list.list.forEach {
                    gridList.add(HomeGridAudioModel(it))
                }
                allData.add(block)
                allData.add(HomeGridAudioRvModel(gridList))
            }
            BLOCK_HOR_SINGLE -> {
                val horSingList = ArrayList<MultiItemEntity>()
                block.audio_list.list.forEach {
                    horSingList.add(HomeAudioHorSingleModel(it))
                }
                allData.add(block)
                allData.add(HomeAudioHorSingleRvModel(horSingList))
            }
            BLOCK_HOR_VER -> {
                val verSingList = ArrayList<MultiItemEntity>()
                block.audio_list.list.forEach {
                    verSingList.add(HomeAudioVerModel(it))
                }
                allData.add(block)
                allData.add(HomeAudioVerRvModel(verSingList))
            }
            BLOCK__SINGLE_IMG -> {
                if("image" == block.relation_to){
                    block.isNoMore = true
                    block.single_img_content?.let {
                        allData.add(block)
                        it.itemType = R.layout.home_item_audio_sing_img
                        allData.add(it)
                        getHomeImgContentAd()
                    }
                }
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

    fun onBlockClick(block: HomeBlockModel) {
        blockClick(block)
    }

    fun homeSingleImgAdClick(model : HomeSingleImgContentModel){

    }
    fun homeSingleImgAdClose(model : HomeSingleImgContentModel){

    }


    companion object {
        const val BLOCK_HOR_SINGLE = 1
        const val BLOCK_HOR_GRID = 2
        const val BLOCK_HOR_DOUBLE = 3
        const val BLOCK_HOR_VER = 4
        const val BLOCK__SINGLE_IMG = 5
    }

}