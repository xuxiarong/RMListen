package com.rm.module_home.viewmodel

import android.content.Context
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
import com.rm.business_lib.insertpoint.BusinessInsertConstance
import com.rm.business_lib.insertpoint.BusinessInsertManager
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.utils.BannerJumpUtils
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.adapter.HomeAdapter
import com.rm.module_home.model.ad.HomeSingleImgAdResultModel
import com.rm.module_home.model.home.*
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
    var collectItemClickList: (HomeMenuModel) -> Unit = {}
    var doubleRvLeftScrollOpenDetail: () -> Unit = {}
    var errorMsg = ObservableField<String>()
    var showNetError = ObservableBoolean(false)

    var homeAllData = MutableLiveData<MutableList<MultiItemEntity>>()

    var audioClick: (HomeAudioModel) -> Unit = {}
    var blockClick: (HomeBlockModel) -> Unit = {}

    //首页弹窗广告
    var homeDialogAdModel = ObservableField<BusinessAdModel>()

    //首页列表广告数据
    var homeItemDataAdModel = ObservableField<HomeSingleImgAdResultModel>()


    /**
     * 获取首页数据
     */
    fun getHomeDataFromService() {
        refreshStatusModel.setNoHasMore(true)
        launchOnIO(block = {
            repository.getHomeData().checkResult(
                    onSuccess = {
                        showContentView()
                        refreshStatusModel.finishRefresh(true)
                        dealHomeData(it)
                    },
                    onError = {it,_->
                        refreshStatusModel.finishRefresh(false)
                        if (homeAllData.value != null) {
                            showContentView()
                        } else {
                            DLog.d("suolong","onError  homeAllData.value == null")
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
                DLog.d("suolong","netErrorBlock")
                showServiceError()
            }
            refreshStatusModel.finishRefresh(false)
        })
    }

    /**
     * 获取首页弹窗广告
     */
    fun getHomeDialogAd() {
        launchOnIO {
            repository.getHomeDialogAd(arrayOf("ad_index_alert")).checkResult(
                    onSuccess = {
                        it.ad_index_alert?.let { dialogAdList ->
                            if (dialogAdList.isNotEmpty()) {
                                val randomAdPosition = Random.nextInt(dialogAdList.size)
                                homeDialogAdModel.set(dialogAdList[randomAdPosition])
                            }
                        }
                    },
                    onError = {it,_->
                        DLog.d("suolong_home", "getHomeDialogAd error = ${it ?: "错误信息为空"}")
                    }
            )
        }
    }

    /**
     * 获取首页列表数据广告
     */
    private fun getHomeItemDataAd() {
        homeItemDataAdModel.set(HomeSingleImgAdResultModel(null, null, null))
        launchOnIO {
            repository.getHomeImgContentAd(
                    arrayOf("ad_index_banner", "ad_index_floor_streamer", "ad_index_voice")
            ).checkResult(
                    onSuccess = {
                        homeItemDataAdModel.set(it)
                    },
                    onError = {it,_->
                        homeItemDataAdModel.set(HomeSingleImgAdResultModel(null, null, null))
                        DLog.d("suolong_home", "getHomeDialogAd error = ${it ?: "错误信息为空"}")
                    }
            )
        }

    }


    /**
     * 处理首页的数据
     */
    private fun dealHomeData(homeModel: HomeModel) {

        val allData = ArrayList<MultiItemEntity>()
        //添加banner数据
        homeModel.banner_list?.let {
            allData.add(HomeBannerRvModel(it))
        }
        //添加菜单数据
        homeModel.menu_list?.let { menuList ->
            allData.add(HomeMenuRvModel(homeModel.menu_list))
            menuList.forEach { it.itemType = R.layout.home_item_menu }
        }
        //添加板块数据
        homeModel.block_list?.let { blockList ->
            if (blockList.isNotEmpty()) {
                //板块数据不为空且弹窗广告未拉取过，则去获取弹窗广告
                if (null == homeDialogAdModel.get()) {
                    getHomeDialogAd()
                }
                blockList.forEach { blockModel ->
                    if (blockModel.audio_list.list.size > 0 || "image" == blockModel.relation_to) {
                        setBlockData(allData, blockModel)
                        DLog.d("suolong", "type  = ${blockModel.block_type_id}")
                    }
                }
                homeAllData.value = allData
                //板块数据不为空,每次都获取Item的广告数据
                getHomeItemDataAd()
            }
        }
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
                allData.add(HomeAudioHorDoubleRvModel(block, doubleHorList))
            }
            BLOCK_HOR_GRID -> {
                val gridList = ArrayList<MultiItemEntity>()
                block.audio_list.list.forEach {
                    gridList.add(HomeGridAudioModel(it))
                }
                allData.add(block)
                allData.add(HomeGridAudioRvModel(block, gridList))
            }
            BLOCK_HOR_SINGLE -> {
                val horSingList = ArrayList<MultiItemEntity>()
                block.audio_list.list.forEach {
                    horSingList.add(HomeAudioHorSingleModel(it))
                }
                allData.add(block)
                allData.add(HomeAudioHorSingleRvModel(block, horSingList))
            }
            BLOCK_HOR_VER -> {
                val verSingList = ArrayList<MultiItemEntity>()
                block.audio_list.list.forEach {
                    verSingList.add(HomeAudioVerModel(it))
                }
                allData.add(block)
                allData.add(HomeAudioVerRvModel(block, verSingList))
            }
            BLOCK_SINGLE_IMG -> {
                if ("image" == block.relation_to) {
                    block.isNoMore = true
                    block.single_img_content?.let {
                        allData.add(block)
                        it.itemType = R.layout.home_item_audio_sing_img
                        allData.add(it)
                    }
                }
            }

            else -> {

            }
        }
    }

    //设置首页的广告数据
    fun setHomeItemDataAd(adResultModel: HomeSingleImgAdResultModel) {
        val homeAllData = homeAdapter.data
        homeAllData.forEach { itemData ->
            when (itemData) {
                is HomeBannerRvModel -> {
                    getBannerAd(itemData, adResultModel.ad_index_banner)
                }
                is HomeAudioHorSingleRvModel -> {
                    getHorSingAd(itemData, adResultModel.ad_index_voice)
                }
                is HomeAudioHorDoubleRvModel -> {
                    getHorDoubleAd(itemData, adResultModel.ad_index_voice)
                }
                is HomeGridAudioRvModel -> {
                    getGridAd(itemData, adResultModel.ad_index_voice)
                }
                is HomeAudioVerRvModel -> {
                    getVerAd(itemData, adResultModel.ad_index_voice)
                }
                is HomeSingleImgContentModel -> {
                    getImgContentAd(itemData, adResultModel.ad_index_floor_streamer)
                }
                else -> {

                }
            }
        }
        homeAdapter.notifyDataSetChanged()
    }

    /**
     * 获取banner的广告
     */
    private fun getBannerAd(bannerRvModel: HomeBannerRvModel, adList: MutableList<BusinessAdModel>?) {
        bannerRvModel.bannerList?.let { bannerList ->
            adList?.let {

                adList.sortedWith(Comparator { o1, o2 ->
                    return@Comparator if (o1.sort > o2.sort) {
                        1
                    } else {
                        -1
                    }
                })

                adList.forEach {
                    val adBanner = BannerInfoBean(
                        banner_img = it.image_url,
                        banner_jump = it.jump_url,
                        img_url = it.image_url,
                        isAd = true,
                        ad_id = it.ad_id
                    )
                    if (it.sort <= bannerList.size) {
                        bannerList.add(it.sort - 1, adBanner)
                    }else{
                        bannerList.add(adBanner)
                    }
                }
            }
        }
    }

    /**
     * 随机从广告列表获取一个广告
     */
    private fun getRandomAdFromList(adList: List<BusinessAdModel>?): BusinessAdModel? {
        if (adList == null || adList.isEmpty()) {
            return null
        }
        val randomAdPosition = Random.nextInt(adList.size)
        return adList[randomAdPosition]
    }

    /**
     * 获取横向单列的广告
     */
    private fun getHorSingAd(
            horSingleRvModel: HomeAudioHorSingleRvModel,
            adList: List<BusinessAdModel>?
    ) {
        if (horSingleRvModel.data.isNotEmpty() && adList != null && adList.isNotEmpty()) {
            adList.forEach {
                if (it.attach_to_block_id == horSingleRvModel.block.block_id && it.sort <= horSingleRvModel.data.size) {
                    val singItem = horSingleRvModel.data[it.sort - 1]
                    if (singItem is HomeAudioHorSingleModel) {
                        singItem.singleModel.adModel = it
                    }
                }
            }
        }
    }

    /**
     * 获取横向双列的广告
     */
    private fun getHorDoubleAd(
            horDoubleRvModel: HomeAudioHorDoubleRvModel,
            adList: List<BusinessAdModel>?
    ) {
        if (horDoubleRvModel.horDoubleList.isNotEmpty() && adList != null && adList.isNotEmpty()) {
            adList.forEach {
                if (it.attach_to_block_id == horDoubleRvModel.block.block_id && it.sort <= horDoubleRvModel.block.audio_list.list.size) {
                    val doubleItem = horDoubleRvModel.horDoubleList[(it.sort - 1) / 2]
                    if (doubleItem is HomeAudioHorDoubleModel) {
                        if (it.sort % 2 == 0) {
                            doubleItem.bottomRecommendModel.adModel = it
                        } else {
                            doubleItem.topRecommendModel.adModel = it
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取六宫格的广告
     */
    private fun getGridAd(
            gridRvModel: HomeGridAudioRvModel, adList: List<BusinessAdModel>?
    ) {
        if (gridRvModel.data.isNotEmpty() && adList != null && adList.isNotEmpty()) {
            adList.forEach {
                if (it.attach_to_block_id == gridRvModel.block.block_id && it.sort <= gridRvModel.data.size) {
                    val singItem = gridRvModel.data[it.sort - 1]
                    if (singItem is HomeGridAudioModel) {
                        singItem.gridRecommendRvModel.adModel = it
                    }
                }
            }
        }
    }

    /**
     * 获取竖直列表的广告
     */
    private fun getVerAd(
            verRvModel: HomeAudioVerRvModel, adList: List<BusinessAdModel>?
    ) {
        if (verRvModel.data.isNotEmpty() && adList != null && adList.isNotEmpty()) {
            adList.forEach {
                if (it.attach_to_block_id == verRvModel.block.block_id && it.sort <= verRvModel.data.size) {
                    val singItem = verRvModel.data[it.sort - 1]
                    if (singItem is HomeAudioVerModel) {
                        singItem.verModel.adModel = it
                    }
                }
            }
        }
    }

    /**
     * 获取单图的广告
     */
    private fun getImgContentAd(
            horSingleRvModel: HomeSingleImgContentModel,
            adList: List<BusinessAdModel>?
    ) {
        if (horSingleRvModel.jump_url.isNotEmpty() && adList != null && adList.isNotEmpty()) {
            horSingleRvModel.img_ad_model = getRandomAdFromList(adList)
        }
    }


    /**
     * 根据数据获取Adapter
     */
    fun getAdapterWithList(data: ArrayList<MultiItemEntity>): CommonMultiVMAdapter {
        return CommonMultiVMAdapter(this, data, BR.viewModel, BR.item)
    }

    fun collectClick(model: HomeMenuModel) {
        collectItemClickList(model)
        DLog.d("suolong", "model = ${model.menu_name} ")
    }

    fun onAudioClick(context: Context, audioModel: HomeAudioModel) {
        if (audioModel.audioIsAd()) {
            BannerJumpUtils.onBannerClick(context, audioModel.getJumpUrl())
        } else {
            audioClick(audioModel)
        }
    }

    fun onBlockClick(block: HomeBlockModel) {
        blockClick(block)
    }

    fun homeSingleImgAdClick(context: Context, model: HomeSingleImgContentModel) {
        model.img_ad_model?.let {
            BannerJumpUtils.onBannerClick(context, it.jump_url, it.ad_id.toString())
        }
    }

    fun homeSingleImgAdClose(model: HomeSingleImgContentModel) {
        model.img_ad_model = null
        homeAdapter.notifyDataSetChanged()
        BusinessInsertManager.doInsertKeyAndAd(BusinessInsertConstance.INSERT_TYPE_AD_CLOSE, model.img_ad_model?.ad_id.toString())
    }


    companion object {
        const val BLOCK_HOR_SINGLE = 1
        const val BLOCK_HOR_GRID = 2
        const val BLOCK_HOR_DOUBLE = 3
        const val BLOCK_HOR_VER = 4
        const val BLOCK_SINGLE_IMG = 5
    }

}