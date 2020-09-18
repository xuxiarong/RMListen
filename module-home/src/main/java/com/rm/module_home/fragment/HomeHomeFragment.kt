package com.rm.module_home.fragment

import androidx.lifecycle.Observer
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.util.DLog
import com.rm.business_lib.isLogin
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.activity.HomeTopListActivity
import com.rm.module_home.activity.boutique.BoutiqueActivity
import com.rm.module_home.activity.detail.HomeDetailActivity
import com.rm.module_home.activity.menu.HomeMenuActivity
import com.rm.module_home.activity.topic.HomeTopicListActivity
import com.rm.module_home.adapter.HomeAdapter
import com.rm.module_home.databinding.HomeHomeFragmentBinding
import com.rm.module_home.model.home.HomeMenuModel
import com.rm.module_home.model.home.banner.HomeBannerRvModel
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
        mViewModel.getHomeDataFromService()
    }

    override fun initView() {

        setStatusBar(R.color.base_activity_bg_color)

        mViewModel.collectItemClickList = {initCollectClick(it)}
//        mViewModel.initBannerInfo()
//        mViewModel.initCollect()
//        mViewModel.initSingleList()
//        mViewModel.initDoubleList()
//        mViewModel.initGridList()
//        mViewModel.initVerList()
        mViewModel.doubleRvLeftScrollOpenDetail = {startBoutique()}
        mDataBind.homeRv.bindVerticalLayout(mHomeAdapter)

    }

    fun initCollectClick(model: HomeMenuModel) {
        when (model.menu_name) {
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
        mViewModel.homeAllData.observe(this, Observer {
            mHomeAdapter.setList(it)
        })

        isLogin.observe(this, Observer {
            DLog.d("suolong","isLogin = $it")
        })
    }


    var count = 0
    override fun onResume() {
        super.onResume()
        isLogin.value = (!isLogin.value!!)
        count++
        mViewModel.homeAllData.value = mutableListOf(
            HomeMoreModel("标题$count",{}))

    }

    private fun startBoutique() {
        context?.let {
            BoutiqueActivity.startActivity(it)
        }
    }

    private fun startRank() {
        context?.let {
            HomeTopListActivity.startActivity(it)
        }
    }

    private fun startMenu() {
        HomeMenuActivity.startActivity(context!!)
    }

    private fun startHorDoubleMore() {
        this.context?.let { HomeTopicListActivity.startActivity(it,1,1,1,"精品推荐Double") }
    }

    private fun startHorSingleMore() {
        this.context?.let { HomeTopicListActivity.startActivity(it,1,1,1,"精品推荐Grid") }
    }


    fun recommendClick() {
        HomeDetailActivity.startActivity(context!!,"1")
    }

    fun moreClick() {

    }




}