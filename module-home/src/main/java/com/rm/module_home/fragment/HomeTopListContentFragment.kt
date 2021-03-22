package com.rm.module_home.fragment

import android.os.Bundle
import androidx.annotation.IntDef
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeFragmentTopListContentBinding
import com.rm.module_home.viewmodel.HomeTopListContentFragmentViewModel

class HomeTopListContentFragment :
    BaseVMFragment<HomeFragmentTopListContentBinding, HomeTopListContentFragmentViewModel>() {

    @RankType
    private var rankType = RANK_TYPE_POPULAR

    //当前按照什么排行
    private var rankSeg = "week"

    @IntDef(
        RANK_TYPE_POPULAR,
        RANK_TYPE_HOT,
        RANK_TYPE_NEW_BOOK,
        RANK_TYPE_SEARCH,
        RANK_TYPE_PRAISE
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class RankType(val type: Int = RANK_TYPE_POPULAR)


    companion object {
        const val RANK_TYPE_POPULAR = 1//热门榜
        const val RANK_TYPE_HOT = 3//热搜榜
        const val RANK_TYPE_NEW_BOOK = 2//新书榜
        const val RANK_TYPE_SEARCH = 4//搜索榜
        const val RANK_TYPE_PRAISE = 5//好评榜
        const val RANK_TYPE = "rankType"


        fun newInstance(@RankType rankType: Int): HomeTopListContentFragment {
            val homeTopListContentFragment = HomeTopListContentFragment()
            val bundle = Bundle()
            bundle.putInt(RANK_TYPE, rankType)
            homeTopListContentFragment.arguments = bundle
            return homeTopListContentFragment
        }
    }

    private var mVisible = false//是否可见
    private var canRefreshData = false//是否能够刷新数据

    override fun initLayoutId() = R.layout.home_fragment_top_list_content

    override fun initModelBrId(): Int = BR.viewModel


    /**
     * 初始化数据
     */
    override fun initData() {
        canRefreshData = true
        getData()
    }


    override fun initView() {
        super.initView()
        arguments?.let {
            rankType = it.getInt(RANK_TYPE)
        }
    }

    /**
     * 榜单类型发生改变   由activity通知
     */
    fun changRankSeg(rankSeg: String) {
        canRefreshData = rankSeg != this.rankSeg
        this.rankSeg = rankSeg
        if (mVisible) {
            mViewModel.mAdapter.setList(null)
            mViewModel.refreshStatusModel.setResetNoMoreData(true)
            getData()
        }
    }

    /**
     * 判断当前的fragment是否可见
     */
    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        mVisible = menuVisible
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    /**
     * 数据请求
     */
    private fun getData() {
        if (!canRefreshData) {
            return
        }
        canRefreshData = false
        mViewModel.showLoading()
        mViewModel.mPage = 1
        mViewModel.getListInfo("$rankType", rankSeg)
    }


    override fun startObserve() {
    }

}