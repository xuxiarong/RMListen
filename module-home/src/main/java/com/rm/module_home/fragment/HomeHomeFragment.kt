package com.rm.module_home.fragment

import com.rm.baselisten.binding.layoutHorizontal
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.activity.menu.MenuActivity
import com.rm.module_home.adapter.HomeCollectAdapter
import com.rm.module_home.bean.BannerInfo
import com.rm.module_home.databinding.HomeHomeFragmentBinding
import com.rm.module_home.model.HomeCollectModel
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

        val bannerList = mutableListOf<BannerInfo>()
        bannerList.add(BannerInfo("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597921071741&di=f7334148be90918832df8165a435eab5&imgtype=0&src=http%3A%2F%2Fimg0.imgtn.bdimg.com%2Fit%2Fu%3D3807836035%2C2971917368%26fm%3D214%26gp%3D0.jpg"))
        bannerList.add(BannerInfo("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1597921071342&di=fbc56e43d75d84e151fb793b797017c4&imgtype=0&src=http%3A%2F%2Fpic.90sjimg.com%2Fback_pic%2F00%2F00%2F69%2F40%2Fs_1198_2cc8d6389629c39568e4a22b851e2b88.jpg"))
        bannerList.add(BannerInfo("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2011292736,2426988592&fm=26&gp=0.jpg"))
        bannerList.add(BannerInfo("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2843937210,358921993&fm=26&gp=0.jpg"))
        homeViewModel.bannerInfoList.value = bannerList

    }

    override fun initView() {

        val homeCollectAdapter = HomeCollectAdapter(
             listOf(
                HomeCollectModel(R.drawable.home_icon_collect_recommend, "精品推荐") { startMenu() },
                 HomeCollectModel(R.drawable.home_icon_collect_rank, "榜单") { startMenu() },
                 HomeCollectModel(R.drawable.home_icon_head_reading, "看书") { startMenu() },
                HomeCollectModel(R.drawable.home_icon_collect_listen, "听单") { startMenu() }),R.layout.home_item_collect,BR.collectViewModel
        )
        binding.mainRvCollect.layoutHorizontal(homeCollectAdapter)
        binding.mainRvCollect.adapter = homeCollectAdapter
        binding.run {
            collectViewModel = homeViewModel
        }
    }


    fun startMenu(){
        MenuActivity.startActivity(context!!)
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