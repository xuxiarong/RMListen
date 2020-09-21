package com.rm.module_home.fragment

import android.os.Bundle
import androidx.annotation.IntDef
import androidx.lifecycle.observe
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.adapter.HomeTopListContentAdapter
import com.rm.module_home.databinding.HomeFragmentTopListContentBinding
import com.rm.module_home.viewmodel.HomeTopListContentFragmentViewModel
import kotlinx.android.synthetic.main.home_fragment_top_list_content.*

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
    private var mPage = 1//当前的页码

    //每页加载数据条数
    private val pageSize = 10


    /**
     * 懒加载构建adapter对象
     */
    private val mAdapter by lazy {
        HomeTopListContentAdapter(
            mViewModel,
            BR.itemViewModel,
            BR.item
        )
    }

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

        home_top_list_recycler_content.apply {
            bindVerticalLayout(mAdapter)
        }

        mViewModel.itemClick = {
            context?.let { cox ->
                RouterHelper.createRouter(HomeService::class.java)
                    .toDetailActivity(cox, it.audio_id)
            }
        }

        refresh()
        loadMore()

    }

    /**
     * 榜单类型发生改变   由activity通知
     */
    fun changRankSeg(rankSeg: String) {
        canRefreshData = rankSeg != this.rankSeg
        this.rankSeg = rankSeg
        mPage = 1
        if (mVisible) {
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
        mViewModel.getListInfo("$rankType", rankSeg, mPage, pageSize)
    }

    /**
     * 下拉刷新
     */
    private fun refresh() {
        home_top_list_refresh.setOnRefreshListener {
            canRefreshData = true
            mPage = 1
            getData()
        }
    }

    /**
     * 上拉加载
     */
    private fun loadMore() {
        home_top_list_refresh.setOnLoadMoreListener {
            canRefreshData = true
            ++mPage
            getData()
        }
    }

    override fun startObserve() {
        mViewModel.dataList.observe(this) {
            if (mPage == 1) {
                home_top_list_refresh?.finishRefresh()
                mAdapter.setList(it.list)
            } else {
                home_top_list_refresh?.finishLoadMore()
                it.list?.let { data -> mAdapter.addData(data) }
            }
            //没有更多数据
            if (it.list?.size ?: 0 < pageSize) {
                home_top_list_refresh?.finishLoadMoreWithNoMoreData()
            }
        }
    }

}