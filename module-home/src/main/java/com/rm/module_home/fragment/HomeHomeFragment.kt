package com.rm.module_home.fragment

import com.rm.baselisten.adapter.BaseMultiAdapter
import com.rm.baselisten.binding.bindHorizontalLayout
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.activity.menu.MenuActivity
import com.rm.module_home.adapter.HomeAdapter
import com.rm.module_home.adapter.HomeCollectAdapter
import com.rm.module_home.databinding.HomeHomeFragmentBinding
import com.rm.module_home.model.HomeCollectModel
import com.rm.module_home.model.HomeMoreModel
import com.rm.module_home.model.HomeRecommendModel
import com.rm.module_home.viewmodel.HomeFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
class HomeHomeFragment : BaseVMFragment<HomeHomeFragmentBinding>(R.layout.home_home_fragment) {

    private val homeViewModel by viewModel<HomeFragmentViewModel>()


    override fun initData() {

    }

    override fun initView() {

        val homeCollectAdapter = HomeCollectAdapter(
            listOf(
                HomeCollectModel(R.drawable.home_icon_collect_recommend, "精品推荐") { },
                HomeCollectModel(R.drawable.home_icon_collect_rank, "榜单") { },
                HomeCollectModel(R.drawable.home_icon_head_reading, "看书") { },
                HomeCollectModel(R.drawable.home_icon_collect_listen, "听单") { startMenu() }),
            R.layout.home_item_collect,
            BR.collectViewModel
        )

        binding.homeRvCollect.bindHorizontalLayout(homeCollectAdapter)

        val homeAdapter = HomeAdapter(generateHomeTestData(),BR.item)
        binding.homeRvRecommend.bindVerticalLayout(homeAdapter)


        binding.run {
            collectViewModel = homeViewModel
        }
    }

    fun generateHomeTestData(): List<BaseMultiAdapter.IBindItemType> {
        return listOf(
            HomeMoreModel("精品推荐") { moreClick() },
            HomeRecommendModel(
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2843937210,358921993&fm=26&gp=0.jpg",
                "Carolyn Gregory",
                "小丸子",
                "Vip"
            ) { recommendClick() }
        )
    }


    fun startMenu() {
        MenuActivity.startActivity(context!!)
    }

    fun recommendClick() {

    }

    fun moreClick() {

    }

    override fun startObserve() {
//        homeViewModel.bannerInfoList.observe(this, Observer {
//            homeViewModel.bannerInfoList.value = HomeBannerModel("")
//        })
    }

    companion object {
        fun getInstance() {
//            val transaction: FragmentTransaction = .beginTransaction()
//            transaction.add(frameId, fragment)
//            transaction.commit()
        }
    }

}