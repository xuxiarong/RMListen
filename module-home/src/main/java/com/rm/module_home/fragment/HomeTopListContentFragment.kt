package com.rm.module_home.fragment

import android.os.Bundle
import androidx.annotation.IntDef
import androidx.lifecycle.observe
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.binding.linearBottomItemDecoration
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.util.ToastUtil
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.adapter.HomeTopListContentAdapter
import com.rm.module_home.databinding.HomeFragmentTopListContentBinding
import com.rm.module_home.viewmodel.HomeTopListContentFragmentViewModel
import kotlinx.android.synthetic.main.home_fragment_top_list_content.*

class HomeTopListContentFragment :
    BaseVMFragment<HomeFragmentTopListContentBinding, HomeTopListContentFragmentViewModel>() {
    private val listAdapter by lazy { HomeTopListContentAdapter(BR.item) }

    @RankType
    private var rankType = RANK_TYPE_POPULAR

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
        const val RANK_TYPE_POPULAR = 1
        const val RANK_TYPE_HOT = 3
        const val RANK_TYPE_NEW_BOOK = 2
        const val RANK_TYPE_SEARCH = 4
        const val RANK_TYPE_PRAISE = 5
        const val RANK_TYPE = "rankType"


        fun newInstance(@RankType rankType: Int): HomeTopListContentFragment {
            val homeTopListContentFragment = HomeTopListContentFragment()
            val bundle = Bundle()
            bundle.putInt(RANK_TYPE, rankType)
            homeTopListContentFragment.arguments = bundle
            return homeTopListContentFragment
        }
    }

    override fun startObserve() {
        mViewModel.dataList.observe(this) {
            listAdapter.setList(it.list)
        }
    }

    private var mVisible = false//是否可见
    private var canRefreshData = false//是否能够刷新数据

    override fun initLayoutId() = R.layout.home_fragment_top_list_content

    override fun initModelBrId(): Int = BR.viewModel


    override fun initData() {
        canRefreshData = true
        getData()
    }

    override fun initView() {
        super.initView()
        arguments?.let {
            rankType = it.getInt(RANK_TYPE)
        }
        home_list_recycler_content.apply {
            bindVerticalLayout(listAdapter)
            linearBottomItemDecoration(resources.getDimensionPixelOffset(R.dimen.dp_14))
        }
        listAdapter.setOnItemClickListener { adapter, view, position ->
            ToastUtil.show(context, "item_$position")
        }
    }

    /**
     * 榜单类型发生改变   由activity通知
     */
    fun changRankSeg(rankSeg: String) {
        canRefreshData = rankSeg != this.rankSeg
        this.rankSeg = rankSeg
        if (mVisible) {
            getData()
        }
    }


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
        mViewModel.getListInfo(
            "$rankType",
            rankSeg,
            1,
            10
        )
    }
}