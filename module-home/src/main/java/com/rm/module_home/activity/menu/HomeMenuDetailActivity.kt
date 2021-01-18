package com.rm.module_home.activity.menu

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.utilExt.getStateHeight
import com.rm.component_comm.activity.ComponentShowPlayActivity
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeActivityListenMenuDetailBinding
import com.rm.module_home.databinding.HomeHeaderMenuDetailBinding
import com.rm.module_home.viewmodel.HomeMenuDetailViewModel

class HomeMenuDetailActivity :
    ComponentShowPlayActivity<HomeActivityListenMenuDetailBinding, HomeMenuDetailViewModel>() {

    companion object {
        const val SHEET_ID = "sheetId"

        fun startActivity(context: Activity, sheetId: String) {
            val intent = Intent(context, HomeMenuDetailActivity::class.java)
            intent.putExtra(SHEET_ID, sheetId)
            context.startActivityForResult(intent, 100)
        }
    }


    private var dataBinding: HomeHeaderMenuDetailBinding? = null

    override fun getLayoutId() = R.layout.home_activity_listen_menu_detail

    override fun initModelBrId() = BR.viewModel

    override fun initView() {
        super.initView()
        val sheetId = intent.getStringExtra(SHEET_ID) ?: ""
        mViewModel.sheetId.set(sheetId)

        setTransparentStatusBar()//设置透明沉浸状态栏
        mDataBind?.let {
            val layoutParams =
                (it.homeMenuDetailTitleCl.layoutParams) as ConstraintLayout.LayoutParams
            layoutParams.apply {
                //动态获取状态栏的高度,并设置标题栏的topMargin
                val stateHeight = getStateHeight(this@HomeMenuDetailActivity)
                topMargin = stateHeight
            }

            it.homeMenuDetailRecyclerView.apply {
                bindVerticalLayout(mViewModel.mAdapter)
                createHeader()
            }

            it.homeMenuDetailRefresh.refreshFooter
        }


        recycleScrollListener()
    }

    override fun initData() {
        mViewModel.getData()
    }

    private fun createHeader() {
        mDataBind?.let {
            dataBinding = DataBindingUtil.inflate<HomeHeaderMenuDetailBinding>(
                LayoutInflater.from(this@HomeMenuDetailActivity),
                R.layout.home_header_menu_detail,
                it.homeMenuDetailRecyclerView,
                false
            ).apply {
                mViewModel.mAdapter.addHeaderView(root)
                setVariable(BR.headerViewModel, mViewModel)
            }
        }
    }

    /**
     * 数据发生改变监听
     */
    override fun startObserve() {

    }

    /**
     * recyclerView滑动监听
     */
    private fun recycleScrollListener() {
        mDataBind?.let {
            it.homeMenuDetailRecyclerView.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                private var totalDy = 0
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val top = dataBinding?.homeMenuDetailFrontCover?.top ?: 0
                    val height = dataBinding?.homeMenuDetailFrontCover?.height ?: 0
                    totalDy += dy
                    if (totalDy > 0) {
                        val alpha = if (totalDy.toFloat() / (top + height) > 1f) {
                            1f
                        } else {
                            totalDy.toFloat() / (top + height)
                        }
                        it.homeMenuDetailBlur.alpha = alpha
                    }
                }
            })
        }
    }

    /**
     * 防止首次添加收藏进入收藏听单后再次进入详情取消收藏
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 300 && resultCode == 200) {
            val favorite = data?.getBooleanExtra("isFavorite", false)
            mViewModel.setFavorState(favorite == true)
        }
    }


}