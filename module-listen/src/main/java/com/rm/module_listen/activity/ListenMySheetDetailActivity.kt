package com.rm.module_listen.activity

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.binding.linearBottomItemDecoration
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.thridlib.glide.loadBlurImage
import com.rm.baselisten.util.DLog
import com.rm.baselisten.utilExt.dimen
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.bean.ListenSheetBean
import com.rm.module_listen.bean.ListenSheetDetailDataBean
import com.rm.module_listen.databinding.ListenActivitySheetDetailBinding
import com.rm.module_listen.databinding.ListenHeaderSheetDetailBinding
import com.rm.module_listen.utils.ListenDialogCreateSheetHelper
import com.rm.module_listen.viewmodel.ListenSheetDetailViewModel
import kotlinx.android.synthetic.main.listen_activity_sheet_detail.*

/**
 * 听单详情
 */
class ListenMySheetDetailActivity :
    BaseVMActivity<ListenActivitySheetDetailBinding, ListenSheetDetailViewModel>() {

    private val mAdapter by lazy {
        CommonBindVMAdapter<ListenSheetDetailDataBean>(
            mViewModel,
            mutableListOf(),
            R.layout.listen_adapter_book,
            BR.viewModel,
            BR.item
        )
    }

    companion object {

        const val LISTEN_SHEET_DETAIL_DELETE = 0x1001
        const val LISTEN_SHEET_DETAIL_EDIT = 0x1002
        const val LISTEN_SHEET_DETAIL_REQUEST_CODE = 0x101

        fun startActivity(context: Activity, bean: ListenSheetBean) {
            context.startActivityForResult(
                Intent(
                    context,
                    ListenMySheetDetailActivity::class.java
                ).putExtra("bean", bean)
                , LISTEN_SHEET_DETAIL_REQUEST_CODE
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
                mViewModel.sheetBean.value = bean

                //默认听单不给操作
                if (bean.created_from == 3) {
                    listen_sheet_detail_more.visibility = View.INVISIBLE
                } else {
                    listen_sheet_detail_more.visibility = View.VISIBLE
                }

                loadBlurImage(listen_sheet_detail_iv_bg, bean.sheet_cover)
                createHeader()
            }
        }

        mViewModel.editSheetClick = {
            ListenDialogCreateSheetHelper(mViewModel, this).setTitle("编辑听单")
                .showEditDialog(it, success = { success() })
        }
    }

    private fun success() {
        DLog.i("--------->>>>", "编辑成功的回调")
    }

    /**
     * 创建头部
     */
    private fun createHeader() {
        val dataBinding = DataBindingUtil.inflate<ListenHeaderSheetDetailBinding>(
            LayoutInflater.from(this),
            R.layout.listen_header_sheet_detail,
            listen_sheet_detail_recycler_view,
            false
        )
        dataBinding.setVariable(BR.item, mSheetBean)
        mAdapter.addHeaderView(dataBinding.root)
    }

    override fun startObserve() {
        mViewModel.data.observe(this) {
            mAdapter.setList(it.list)
        }
        //删除回调
        mViewModel.deleteQuery.observe(this) {
            if (it) {
                setResult(LISTEN_SHEET_DETAIL_DELETE)
                finish()
            }
        }
    }

    override fun finish() {
        super.finish()

    }

    override fun initData() {
        mSheetBean?.let {
            mViewModel.getData("${it.sheet_id}", 1)
        }
    }

    //弹窗
    private fun showDialog() {
        mViewModel.mDialog.showCommonDialog(
            this,
            R.layout.listen_dialog_bottom_sheet_detail,
            mViewModel,
            BR.viewModel
        )
    }

}