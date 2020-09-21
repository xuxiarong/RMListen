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

    override fun initModelBrId(): Int {
        return BR.viewModel
    }

    //记录点击item对应的实体对象
    private var clickBean: ListenSheetBean? = null

    override fun initView() {
        super.initView()
        //recyclerView初始化操作
        listen_sheet_my_list_recycler_view.apply {
            bindVerticalLayout(mAdapter)
        }

        //item点击事件
        mViewModel.itemClick = { startDetail(it) }
    }

    override fun startObserve() {
        mViewModel.data.observe(this) {
            mAdapter.setList(it.list)
        }
    }

    override fun initLayoutId(): Int {
        return R.layout.listen_fragment_sheet_my_list
    }

    override fun initData() {
        mViewModel.getData()
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