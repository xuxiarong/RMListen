package com.rm.module_listen.activity

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.thridlib.glide.loadBlurImage
import com.rm.baselisten.utilExt.getStateHeight
import com.rm.business_lib.bean.AudioBean
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.databinding.ListenActivitySheetDetailBinding
import com.rm.module_listen.databinding.ListenHeaderSheetDetailBinding
import com.rm.module_listen.utils.ListenDialogCreateSheetHelper
import com.rm.module_listen.viewmodel.ListenSheetDetailViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.listen_activity_sheet_detail.*
import kotlinx.android.synthetic.main.listen_fragment_sheet_my_list.*

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

    //每页加载的条数
    private val pageSize = 10

    //当前请求数据的页码
    private var mPage = 1

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.listen_activity_sheet_detail

    override fun initView() {
        super.initView()

        // 设置透明沉浸式
        setD()

        val layoutParams =
            (listen_sheet_detail_title_cl.layoutParams) as ConstraintLayout.LayoutParams
        layoutParams.apply {
            //动态获取状态栏的高度,并设置标题栏的topMargin
            val stateHeight = getStateHeight(this@ListenMySheetDetailActivity)
            topMargin = stateHeight
        }

        //返回按钮点击时间
        listen_sheet_detail_back.setOnClickListener { finish() }
        //更多按钮点击事件
        listen_sheet_detail_more.setOnClickListener { showDialog() }

        //recyclerView绑定数据
        listen_sheet_detail_recycler_view.apply {
            bindVerticalLayout(mAdapter)
        }

        createHeader()

        //获取其他页面传过来的听单id
        intent?.let {
            mSheetId = it.getStringExtra(SHEET_ID)
        }

        addRefreshListener()

        //点击编辑听单
        mViewModel.editSheetClick = {
            ListenDialogCreateSheetHelper(mViewModel, this).setTitle("编辑听单")
                .showEditDialog(it.sheet_id, success = { sheetName -> success(sheetName) })
        }

        //移除音频成功
        mViewModel.removeAudio = {
            mAdapter.remove(it)
        }

        //item点击事件
        mViewModel.itemClick={
            RouterHelper.createRouter(HomeService::class.java).toDetailActivity(this,it.audio_id)
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

            //默认听单不给操作
            if (it.created_from == 3) {
                listen_sheet_detail_more.visibility = View.INVISIBLE
            } else {
                listen_sheet_detail_more.visibility = View.VISIBLE
            }
            //设置高斯模糊
            loadBlurImage(listen_sheet_detail_iv_bg, it.cover_url)

            //刷新完成
            if (listen_sheet_detail_refresh.isRefreshing) {
                listen_sheet_detail_refresh.finishRefresh()
            }

            //设置新数据
            mAdapter.setList(it.audio_list?.list)

            //没有更多数据
            if (pageSize > it.audio_list?.list?.size ?: 0) {
                listen_sheet_detail_refresh.finishLoadMoreWithNoMoreData()
            }
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

        mViewModel.audioList.observe(this) {
            //加载更多完成
            listen_sheet_detail_refresh.finishLoadMore()
            //添加数据
            it?.list?.let { mAdapter.addData(it) }

            //没有更多数据
            if (pageSize > it.list.size) {
                listen_sheet_detail_refresh.finishLoadMoreWithNoMoreData()
            }
        }

    }

    override fun initData() {
        mSheetId?.let {
            mViewModel.showLoading()
            mViewModel.getSheetInfo(it)
        }
    }

    /**
     * 上下拉监听
     */
    private fun addRefreshListener() {
        listen_sheet_detail_refresh.setOnRefreshLoadMoreListener(object :
            OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                ++mPage
                mViewModel.getAudioList(mPage, pageSize)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                mViewModel.getSheetInfo(mSheetId!!)
            }
        })
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