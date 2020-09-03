package com.rm.module_home.fragment

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.util.ToastUtil
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.activity.boutique.BoutiqueActivity
import com.rm.module_home.activity.detail.HomeDetailActivity
import com.rm.module_home.activity.list.TopListActivity
import com.rm.module_home.activity.menu.MenuActivity
import com.rm.module_home.adapter.HomeAdapter
import com.rm.module_home.databinding.HomeHomeFragmentBinding
import com.rm.module_home.model.home.banner.HomeBannerRvModel
import com.rm.module_home.model.home.collect.HomeCollectModel
import com.rm.module_home.model.home.collect.HomeCollectRvModel
import com.rm.module_home.model.home.grid.HomeGridRecommendRvModel
import com.rm.module_home.model.home.hordouble.HomeRecommendHorDoubleRvModel
import com.rm.module_home.model.home.horsingle.HomeRecommendHorSingleRvModel
import com.rm.module_home.model.home.more.HomeMoreModel
import com.rm.module_home.model.home.ver.HomeRecommendVerRvModel
import com.rm.module_home.viewmodel.HomeFragmentViewModel

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
class HomeHomeFragment : BaseVMFragment<HomeHomeFragmentBinding, HomeFragmentViewModel>() {

    private  val mHomeAdapter: HomeAdapter by lazy {
        HomeAdapter(mViewModel,BR.viewModel,BR.item)
    }

    override fun initLayoutId() = R.layout.home_home_fragment

    override fun initModelBrId(): Int = BR.viewModel

    override fun initData() {

    }

    override fun initView() {

        setStatusBar(R.color.base_activity_bg_color)

        val baseTitleModel = BaseTitleModel()

        baseTitleModel.setLeftIcon(R.drawable.base_icon_back)
            .setTitle("主标题")
            .setLeftIconClick { activity?.finish() }
            .setRightText("周榜")
            .setRightIcon1(R.drawable.base_icon_back)
            .setRightBackground(R.drawable.business_shape_stroke_rect_b1b1b1)
            .setRightContainerClick { ToastUtil.show(activity, " rightContainerClick ") }
        mViewModel.baseTitleModel.value = baseTitleModel
        mViewModel.collectItemClickList = {initCollectClick(it)}
        mViewModel.initBannerInfo()
        mViewModel.initCollect()
        mViewModel.initSingleList()
        mViewModel.initDoubleList()
        mViewModel.initGridList()
        mViewModel.initVerList()
        mViewModel.doubleRvLeftScrollOpenDetail = {startBoutique()}
        mDataBind.homeRv.bindVerticalLayout(mHomeAdapter)
        mHomeAdapter.setList(initHomeAdapter())

    }

    fun initCollectClick(model: HomeCollectModel) {
        when (model.collectName) {
            "精品推荐" -> {
                startBoutique()
            }
            "榜单" -> {
                startRank()
            }
            "看书" -> {
                recommendClick()
            }
            "听单" -> {
                startMenu()
            }
        }
    }

    private fun initHomeAdapter(): MutableList<MultiItemEntity> {
        return mutableListOf(
            HomeBannerRvModel(mViewModel.homeBannerInfoList.value),
            HomeCollectRvModel(),
            HomeMoreModel("精品推荐Double") { startHorDoubleMore() },
            HomeRecommendHorDoubleRvModel(),
            HomeMoreModel("精品推荐Grid") { startHorSingleMore() },
            HomeGridRecommendRvModel(),
            HomeMoreModel("精品推荐Single") { startHorSingleMore() },
            HomeRecommendHorSingleRvModel(),
            HomeMoreModel("新书推荐") { startHorSingleMore() },
            HomeRecommendVerRvModel()
        )

    }

    override fun startObserve() {

    }

    private fun startBoutique() {
        context?.let {
            BoutiqueActivity.startActivity(it)
        }
    }

    private fun startRank() {
        context?.let {
            TopListActivity.startActivity(it)
        }
    }

    private fun startMenu() {
        MenuActivity.startActivity(context!!)
    }

    private fun startHorDoubleMore() {

    }

    private fun startHorSingleMore() {

    }


    fun recommendClick() {
        HomeDetailActivity.startActivity(context!!)
    }

    fun moreClick() {

    }




}