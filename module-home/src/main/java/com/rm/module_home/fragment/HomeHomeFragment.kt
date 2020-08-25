package com.rm.module_home.fragment

import com.rm.baselisten.adapter.BaseMultiAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMFragment
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
import com.rm.module_home.viewmodel.HomeFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
class HomeHomeFragment : BaseVMFragment<HomeHomeFragmentBinding>(R.layout.home_home_fragment) {

    private val homeViewModel by viewModel<HomeFragmentViewModel>()
    private lateinit var mHomeAdapter: HomeAdapter

    override fun initData() {

    }

    override fun initView() {
        homeViewModel.initBannerInfo()
        homeViewModel.initCollect({ startBoutique() },
            { startMenu() },
            { startMenu() },
            { startMenu() })
        homeViewModel.initSingleList()
        homeViewModel.initDoubleList()
        homeViewModel.initGridList()
        mHomeAdapter = HomeAdapter(homeViewModel, initHomeAdapter(), BR.item)
        binding.homeRv.bindVerticalLayout(mHomeAdapter)

        binding.run {
            collectViewModel = homeViewModel
        }
    }

    private fun initHomeAdapter(): List<BaseMultiAdapter.IBindItemType> {
        return listOf(
            HomeBannerRvModel(homeViewModel.homeBannerInfoList.value),
            HomeCollectRvModel(),
            HomeMoreModel("精品推荐Double") { startHorDoubleMore() },
            HomeRecommendHorDoubleRvModel(),
            HomeMoreModel("精品推荐Grid") { startHorSingleMore() },
            HomeGridRecommendRvModel(),
            HomeMoreModel("精品推荐Single") { startHorSingleMore() },
            HomeRecommendHorSingleRvModel()
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

}