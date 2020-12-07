package com.rm.module_listen.activity

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.utilExt.getStateHeight
import com.rm.component_comm.activity.ComponentShowPlayActivity
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.databinding.ListenActivitySheetDetailBinding
import com.rm.module_listen.databinding.ListenHeaderSheetDetailBinding
import com.rm.module_listen.viewmodel.ListenSheetDetailViewModel
import kotlinx.android.synthetic.main.listen_activity_sheet_detail.*

/**
 * 听单详情
 */
class ListenMySheetDetailActivity :
    ComponentShowPlayActivity<ListenActivitySheetDetailBinding, ListenSheetDetailViewModel>() {

    companion object {
        //删除成功回调code
        const val LISTEN_SHEET_DETAIL_DELETE = 0x1001

        const val LISTEN_SHEET_DETAIL = 0x1002

        //跳转过来的code
        const val LISTEN_SHEET_DETAIL_REQUEST_CODE = 0x101

        const val SHEET_ID = "sheetId"
        const val SHEET_NAME = "sheetName"
        const val SHEET_AUDIO_NUM = "sheetAudioNum"
        fun startActivity(context: Activity, sheetId: String) {
            context.startActivityForResult(
                Intent(
                    context,
                    ListenMySheetDetailActivity::class.java
                ).putExtra(SHEET_ID, sheetId), LISTEN_SHEET_DETAIL_REQUEST_CODE
            )
        }
    }

    //当前听单id
    private var mSheetId: String? = null

    private var headerDataBind: ListenHeaderSheetDetailBinding? = null


    override fun initModelBrId() = BR.viewModel


    override fun getLayoutId() = R.layout.listen_activity_sheet_detail


    override fun initView() {
        super.initView()

        // 设置透明沉浸式
        setTransparentStatusBar()

        val layoutParams =
            (listen_sheet_detail_title_cl.layoutParams) as ConstraintLayout.LayoutParams
        layoutParams.apply {
            //动态获取状态栏的高度,并设置标题栏的topMargin
            val stateHeight = getStateHeight(this@ListenMySheetDetailActivity)
            topMargin = stateHeight
        }

        //获取其他页面传过来的听单id
        intent?.let {
            mSheetId = it.getStringExtra(SHEET_ID)
        }

        mViewModel.blockSuccess = {
            headerDataBind?.listenSheetDetailDescription?.text = it
        }
        mDataBind.listenSheetDetailRecyclerView.apply {
            bindVerticalLayout(mViewModel.mAdapter)
            createHeader()
        }
        recycleScrollListener()

    }

    /**
     * 创建头部
     */
    private fun createHeader() {
        headerDataBind = DataBindingUtil.inflate<ListenHeaderSheetDetailBinding>(
            LayoutInflater.from(this),
            R.layout.listen_header_sheet_detail,
            listen_sheet_detail_recycler_view,
            false
        ).apply {
            //头部数据绑定
            mViewModel.mAdapter.addHeaderView(root)
            setVariable(BR.headViewModel, mViewModel)
        }
    }

    override fun initData() {
        mSheetId?.let {
            mViewModel.sheetId = it
            mViewModel.getSheetInfo()
            mViewModel.getAudioList()
        }
    }

    override fun startObserve() {
    }

    /**
     * recyclerView滑动监听
     */
    private fun recycleScrollListener() {
        mDataBind.listenSheetDetailRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            private var totalDy = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val top = headerDataBind?.listenSheetDetailFrontCover?.top ?: 0
                val height = headerDataBind?.listenSheetDetailFrontCover?.height ?: 0
                totalDy += dy
                if (totalDy > 0) {
                    val alpha = if (totalDy.toFloat() / (top + height) > 1f) {
                        1f
                    } else {
                        totalDy.toFloat() / (top + height)
                    }
                    mDataBind.listenSheetDetailBlur.alpha = alpha
                }
            }
        })
    }


}