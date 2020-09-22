package com.rm.module_listen.fragment

import android.content.Intent
import androidx.lifecycle.observe
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.utilExt.dimen
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.activity.ListenMySheetDetailActivity
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.LISTEN_SHEET_DETAIL_DELETE
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.LISTEN_SHEET_DETAIL_EDIT
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.LISTEN_SHEET_DETAIL_REQUEST_CODE
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.SHEET_ID
import com.rm.module_listen.activity.ListenMySheetDetailActivity.Companion.SHEET_NAME
import com.rm.module_listen.bean.ListenSheetBean
import com.rm.module_listen.databinding.ListenFragmentSheetMyListBinding
import com.rm.module_listen.viewmodel.ListenSheetMyListViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.listen_fragment_sheet_my_list.*

class ListenSheetMyListFragment :
    BaseVMFragment<ListenFragmentSheetMyListBinding, ListenSheetMyListViewModel>() {

    companion object {
        fun newInstance(): ListenSheetMyListFragment {
            return ListenSheetMyListFragment()
        }
    }

    //懒加载构建adapter对象
    private val mAdapter by lazy {
        CommonBindVMAdapter<ListenSheetBean>(
            mViewModel,
            mutableListOf(),
            R.layout.listen_adapter_sheet_my_list,
            BR.click,
            BR.item
        )
    }

    //记录点击item对应的实体对象
    private var clickBean: ListenSheetBean? = null


    private val pageSize = 10

    private var page = 1

    override fun initModelBrId() = BR.viewModel

    override fun initLayoutId() = R.layout.listen_fragment_sheet_my_list

    override fun initView() {
        super.initView()
        //recyclerView初始化操作
        listen_sheet_my_list_recycler_view.apply {
            bindVerticalLayout(mAdapter)
        }

        //item点击事件
        mViewModel.itemClick = { startDetail(it) }

        addRefreshListener()
    }

    override fun initData() {
        mViewModel.showLoading()
        mViewModel.getData(page, pageSize)
    }

    override fun startObserve() {
        mViewModel.data.observe(this) {
            if (page == 1) {
                //设置新的数据
                mAdapter.setList(it.list)
            } else {
                //添加数据
                it.list?.let { data -> mAdapter.addData(data) }
            }

            //没有更多数据
            if (it.list?.size ?: 0 < pageSize) {
                listen_sheet_my_list_refresh.finishLoadMoreWithNoMoreData()
            }
        }

        mViewModel.isRefreshOrLoadComplete.observe(this) {
            if (page == 1) {
                //刷新完成
                listen_sheet_my_list_refresh.finishRefresh()
            } else {
                //加载完成
                listen_sheet_my_list_refresh.finishLoadMore()
            }
        }
    }

    /**
     * 跳转到详情页面
     */
    private fun startDetail(bean: ListenSheetBean) {
        activity?.let {
            clickBean = bean
            ListenMySheetDetailActivity.startActivity(it, bean.sheet_id.toString())
        }
    }

    /**
     * 添加上下拉监听
     */
    private fun addRefreshListener() {
        listen_sheet_my_list_refresh.setOnRefreshLoadMoreListener(object :
            OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                ++page
                mViewModel.getData(page, pageSize)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 1
                mViewModel.getData(page, pageSize)
            }
        })
    }

    /**
     * activity回调监听
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LISTEN_SHEET_DETAIL_REQUEST_CODE) {
            val sheetId = data?.getStringExtra(SHEET_ID)
            if (clickBean?.sheet_id.toString() == sheetId) {
                when (resultCode) {
                    //删除
                    LISTEN_SHEET_DETAIL_DELETE -> {
                        mAdapter.remove(clickBean!!)
                    }

                    //编辑成功
                    LISTEN_SHEET_DETAIL_EDIT -> {
                        val sheetName = data.getStringExtra(SHEET_NAME)!!
                        mViewModel.data.value?.list?.let {
                            val indexOf = it.indexOf(clickBean!!)
                            it[indexOf].sheet_name = sheetName
                            mAdapter.notifyItemChanged(indexOf)
                        }
                    }
                }
            }
        }
    }
}