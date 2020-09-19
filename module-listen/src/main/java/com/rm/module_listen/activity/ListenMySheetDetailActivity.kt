package com.rm.module_listen.activity

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.thridlib.glide.loadBlurImage
import com.rm.business_lib.bean.AudioBean
import com.rm.module_listen.BR
import com.rm.module_listen.R
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

    /**
     * 懒加载构建adapter对象
     */
    private val mAdapter by lazy {
        CommonBindVMAdapter<AudioBean>(
            mViewModel,
            mutableListOf(),
            R.layout.listen_adapter_book,
            BR.viewModel,
            BR.item
        )
    }

    companion object {
        //删除成功回调code
        const val LISTEN_SHEET_DETAIL_DELETE = 0x1001

        //编辑成功回调code
        const val LISTEN_SHEET_DETAIL_EDIT = 0x1002

        //跳转过来的code
        const val LISTEN_SHEET_DETAIL_REQUEST_CODE = 0x101

        const val SHEET_ID = "sheetId"
        const val SHEET_NAME = "sheetName"
        fun startActivity(context: Activity, sheetId: String) {
            context.startActivityForResult(
                Intent(
                    context,
                    ListenMySheetDetailActivity::class.java
                ).putExtra(SHEET_ID, sheetId)
                , LISTEN_SHEET_DETAIL_REQUEST_CODE
            )
        }
    }

    //当前听单id
    private var mSheetId: String? = null

    //头部dataBinding对象
    private var dataBinding: ListenHeaderSheetDetailBinding? = null

    override fun initModelBrId(): Int {
        return BR.viewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.listen_activity_sheet_detail
    }

    override fun initView() {
        super.initView()
        //返回按钮点击时间
        listen_sheet_detail_back.setOnClickListener { finish() }
        //更多按钮点击事件
        listen_sheet_detail_more.setOnClickListener { showDialog() }

        //recyclerView绑定数据
        listen_sheet_detail_recycler_view.apply {
            bindVerticalLayout(mAdapter)
        }
        //获取其他页面传过来的听单id
        intent?.let {
            mSheetId = it.getStringExtra(SHEET_ID)
        }

        //创建头部
        createHeader()

        //点击编辑听单
        mViewModel.editSheetClick = {
            ListenDialogCreateSheetHelper(mViewModel, this).setTitle("编辑听单")
                .showEditDialog(it.sheet_id, success = { sheetName -> success(sheetName) })
        }

        //移除音频成功
        mViewModel.removeAudio={
            mAdapter.remove(it)
        }
    }

    /**
     * 编辑听单回调
     */
    private fun success(sheetName: String) {
        dataBinding?.listenSheetDetailDescription?.text = sheetName
        val intent = intent
        intent.putExtra(SHEET_ID, mSheetId)
        intent.putExtra(SHEET_NAME, sheetName)
        setResult(LISTEN_SHEET_DETAIL_EDIT, intent)
    }

    /**
     * 创建头部
     */
    private fun createHeader() {
        dataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.listen_header_sheet_detail,
            listen_sheet_detail_recycler_view,
            false
        )
        mAdapter.addHeaderView(dataBinding!!.root)
    }

    override fun startObserve() {
        //数据源改变监听
        mViewModel.data.observe(this) {
            dataBinding?.setVariable(BR.item, it)
            mAdapter.setList(it.audio_list?.list)

            //默认听单不给操作
            if (it.created_from == 3) {
                listen_sheet_detail_more.visibility = View.INVISIBLE
            } else {
                listen_sheet_detail_more.visibility = View.VISIBLE
            }

            //设置高斯模糊
            loadBlurImage(listen_sheet_detail_iv_bg, it.cover_url)
        }
        //删除回调
        mViewModel.deleteQuery.observe(this) {
            if (it) {
                val intent = intent
                intent.putExtra(SHEET_ID, mSheetId)
                setResult(LISTEN_SHEET_DETAIL_DELETE, intent)
                finish()
            }
        }

    }

    override fun initData() {
        mSheetId?.let {
            mViewModel.getData(it)
        }
    }

    /**
     * 显示弹窗
     */
    private fun showDialog() {
        mViewModel.mDialog.showCommonDialog(
            this,
            R.layout.listen_dialog_bottom_sheet_detail,
            mViewModel,
            BR.viewModel
        )
    }

}