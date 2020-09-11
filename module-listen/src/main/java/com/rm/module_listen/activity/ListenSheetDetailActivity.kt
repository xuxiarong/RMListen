package com.rm.module_listen.activity

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.binding.linearBottomItemDecoration
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.thridlib.glide.loadBlurImage
import com.rm.baselisten.utilExt.dimen
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.bean.ListenSheetBean
import com.rm.module_listen.bean.ListenSheetDetailDataBean
import com.rm.module_listen.databinding.ListenActivitySheetDetailBinding
import com.rm.module_listen.databinding.ListenHeaderSheetDetailBinding
import com.rm.module_listen.viewmodel.ListenSheetDetailViewModel
import kotlinx.android.synthetic.main.listen_activity_sheet_detail.*

class ListenSheetDetailActivity :
    BaseVMActivity<ListenActivitySheetDetailBinding, ListenSheetDetailViewModel>() {

    private val mAdapter by lazy {
        CommonBindVMAdapter<ListenSheetDetailDataBean>(
            mViewModel,
            mutableListOf(),
            R.layout.listen_adapter_book,
            BR.item,
            BR.viewModel
        )
    }

    companion object {
        fun startActivity(
            context: Context,
            bean: ListenSheetBean
        ) {
            context.startActivity(
                Intent(
                    context,
                    ListenSheetDetailActivity::class.java
                ).putExtra("bean", bean)
            )
        }
    }

    private var mSheetBean: ListenSheetBean? = null

    override fun initModelBrId(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.listen_activity_sheet_detail
    }

    override fun initView() {
        super.initView()
        listen_sheet_detail_back.setOnClickListener { finish() }
        listen_sheet_detail_more.setOnClickListener { showDialog() }

        listen_sheet_detail_recycler_view.apply {
            bindVerticalLayout(mAdapter)
            linearBottomItemDecoration(dimen(R.dimen.dp_14))
        }

        intent?.let {
            mSheetBean = it.getParcelableExtra("bean")
            mSheetBean?.let { bean ->
                loadBlurImage(listen_sheet_detail_iv_bg, bean.sheet_cover)
                createHeader()
            }
        }
    }

    private fun createHeader() {
        val dataBinding = DataBindingUtil.inflate<ListenHeaderSheetDetailBinding>(
            LayoutInflater.from(this),
            R.layout.listen_header_sheet_detail,
            listen_sheet_detail_recycler_view,
            false
        )
        dataBinding.setVariable(BR.item,mSheetBean)
        mAdapter.addHeaderView(dataBinding.root)
    }

    override fun startObserve() {
        mViewModel.data.observe(this) {
            mAdapter.setList(it.list)
        }
    }

    override fun initData() {
        mSheetBean?.let {
            mViewModel.getData("${it.sheet_id}", 1)
        }
    }

    private fun showDialog() {
        mViewModel.mDialog.showCommonDialog(
            this,
            R.layout.listen_dialog_bottom_sheet_detail,
            mViewModel,
            BR.viewModel
        )
    }

}