package com.rm.module_home.fragment

import com.rm.baselisten.adapter.BaseMultiAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseNetFragment
import com.rm.baselisten.util.ToastUtil
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.activity.boutique.BoutiqueActivity
import com.rm.module_home.activity.menu.MenuActivity
import com.rm.module_home.adapter.HomeAdapter
import com.rm.module_home.databinding.HomeHomeFragmentBinding
import com.rm.module_home.model.home.banner.HomeBannerRvModel
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
class HomeHomeFragment : BaseNetFragment<HomeHomeFragmentBinding,HomeFragmentViewModel>() {

    private lateinit var mHomeAdapter: HomeAdapter

    override fun initData() {

    }

    override fun initView() {
        val baseTitleModel = BaseTitleModel()

        baseTitleModel.setLeftIcon(R.drawable.base_icon_back)
            .setTitle("主标题")
            .setSubTitle("我是副标题")
            .setLeftIconClick { activity?.finish() }
            .setLeftText("左边")
            .setLeftTextClick { ToastUtil.show(activity, "leftTextClick") }
            .setLeftIcon1(R.drawable.base_icon_back)
            .setLeftIcon1Click { ToastUtil.show(activity, "leftIcon1Click") }
            .setRightIcon(R.drawable.base_icon_back)
            .setRightIconClick { ToastUtil.show(activity, "RightIconClick") }
            .setRightText("右边")
            .setRightTextClick { ToastUtil.show(activity, " rightTextClick") }
            .setRightIcon1(R.drawable.base_icon_back)
            .setRightIcon1Click { ToastUtil.show(activity, " rightIcon1Click") }
        mViewModel.baseTitleModel.value = baseTitleModel

        mViewModel.initBannerInfo()
        mViewModel.initCollect({ startBoutique() },
            { startMenu() },
            { startMenu() },
            { startMenu() })
        mViewModel.initSingleList()
        mViewModel.initDoubleList()
        mViewModel.initGridList()
        mViewModel.initVerList()
        mHomeAdapter = HomeAdapter(mViewModel, initHomeAdapter(), BR.item)
        dataBind.homeRv.bindVerticalLayout(mHomeAdapter)

        dataBind.run {
            collectViewModel = mViewModel
        }
    }

    private fun initHomeAdapter(): List<BaseMultiAdapter.IBindItemType> {
        return listOf(
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

    private fun startBoutique() {
        context?.let {
            BoutiqueActivity.startActivity(it)
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

    }

    fun moreClick() {

    }

    override fun startObserve() {
    }

    override fun initLayoutId(): Int {
        return R.layout.home_home_fragment
    }

}