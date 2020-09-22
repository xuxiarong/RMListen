package com.rm.module_listen.fragment

import android.content.Intent
import androidx.lifecycle.observe
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.utilExt.dimen
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.adapter.ListenSheetCollectedListAdapter
import com.rm.module_listen.databinding.ListenFragmentSheetCollectedListBinding
import com.rm.module_listen.viewmodel.ListenSheetCollectedListViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.listen_activity_subscription.*
import kotlinx.android.synthetic.main.listen_fragment_sheet_collected_list.*

class ListenSheetCollectedListFragment :
    BaseVMFragment<ListenFragmentSheetCollectedListBinding, ListenSheetCollectedListViewModel>() {

    companion object {
        fun newInstance(): ListenSheetCollectedListFragment {
            return ListenSheetCollectedListFragment()
        }
    }

    /**
     * 懒加载创建adapter对象
     */
    private val mAdapter by lazy {
        ListenSheetCollectedListAdapter(
            mViewModel
        )
    }

    //每页加载的数据
    private val pageSize = 10

    //当前加载的页码
    private var mPage = 1


    override fun initModelBrId(): Int {
        return BR.viewModel
    }

    override fun initLayoutId(): Int {
        return R.layout.listen_fragment_sheet_collected_list
    }

    override fun initData() {
        mViewModel.getData(mPage, pageSize)
    }

    override fun initView() {
        super.initView()

        addRefreshListener()

        //初始化recyclerView
        listen_sheet_collected_list_recycler_view.apply {
            bindVerticalLayout(mAdapter)
        }
        //item点击事件
        mViewModel.itemClick = {
            RouterHelper.createRouter(HomeService::class.java)
                .startHomeSheetDetailActivity(activity!!, it.sheet_id.toString(), 100)
        }
    }

    override fun startObserve() {
        mViewModel.data.observe(this) {
            if (mPage == 1) {
                mAdapter.setList(it.list)
            } else {
                mAdapter.addData(it.list)
            }

            //没有更多数据
            if (it.list.size < pageSize) {
                listen_sheet_collected_list_refresh.finishLoadMoreWithNoMoreData()
            }
        }

        //监听加载数据是否完成
        mViewModel.isRefreshOrLoadComplete.observe(this) {
            loadDataComplete(it)
        }
    }

    /**
     * 数据加载完成
     */
    private fun loadDataComplete(it: Boolean) {
        if (it) {
            if (mPage == 1) {
                //刷新完成
                listen_subscription_refresh?.finishRefresh()
            } else {
                //加载更多完成
                listen_subscription_refresh?.finishLoadMore()
            }
        }
    }

    /**
     * 添加refresh监听
     */
    private fun addRefreshListener() {
        listen_sheet_collected_list_refresh.setOnRefreshLoadMoreListener(object :
            OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                ++mPage
                mViewModel.getData(mPage, pageSize)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                mViewModel.getData(mPage, pageSize)
            }
        })
    }

    /**
     * 跳转activity回调
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == 200) {
            val isFavorite = data?.getBooleanExtra("isFavorite", true)
            val sheetId = data?.getStringExtra("sheetId")
            if (isFavorite == false) {
                changeAdapter(sheetId)
            }
        }
    }

    /**
     * 如果当前的听单取消收藏是，将当前听单移除
     */
    private fun changeAdapter(sheetId: String?) {
        val bean = mViewModel.data.value
        bean?.let {
            val index = it.getIndex(sheetId)
            if (index != -1) {
                mAdapter.removeAt(index)
            }
        }
    }

}