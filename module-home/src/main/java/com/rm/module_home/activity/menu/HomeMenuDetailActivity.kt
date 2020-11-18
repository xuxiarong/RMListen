package com.rm.module_home.activity.menu

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
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
    private val footView by lazy {
        LayoutInflater.from(this).inflate(R.layout.business_foot_view, null)
    }

    override fun getLayoutId() = R.layout.home_activity_listen_menu_detail

    override fun initModelBrId() = BR.viewModel

    override fun initView() {
        super.initView()
        val sheetId = intent.getStringExtra(SHEET_ID) ?: ""
        mViewModel.sheetId.set(sheetId)

        setTransparentStatusBar()//设置透明沉浸状态栏

        val layoutParams = (mDataBind.homeMenuDetailTitleCl.layoutParams) as ConstraintLayout.LayoutParams
        layoutParams.apply {
            //动态获取状态栏的高度,并设置标题栏的topMargin
            val stateHeight = getStateHeight(this@HomeMenuDetailActivity)
            topMargin = stateHeight
        }

        recycleScrollListener()

        mDataBind.homeMenuDetailRecyclerView.apply {
            bindVerticalLayout(mViewModel.mAdapter)
            createHeader()
        }
    }

    override fun initData() {
        mViewModel.showLoading()
        mViewModel.getData()
    }

    private fun createHeader() {
        mViewModel.dataBinding = DataBindingUtil.inflate<HomeHeaderMenuDetailBinding>(
            LayoutInflater.from(this@HomeMenuDetailActivity),
            R.layout.home_header_menu_detail,
            mDataBind.homeMenuDetailRecyclerView,
            false
        ).apply {
            this.root.visibility = View.GONE
            mViewModel.mAdapter.addHeaderView(this.root)
        }
    }

    /**
     * 数据发生改变监听
     */
    override fun startObserve() {
        mViewModel.refreshStatusModel.isHasMore.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val hasMore = mViewModel.refreshStatusModel.isHasMore.get()
                if (hasMore == true) {
                    mViewModel.mAdapter.removeAllFooterView()
                    mViewModel.mAdapter.addFooterView(footView)
                } else {
                    mViewModel.mAdapter.removeAllFooterView()
                }
            }
        })
    }

    /**
     * recyclerView滑动监听
     */
    private fun recycleScrollListener() {
        mDataBind.homeMenuDetailRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            private var totalDy = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val top = mViewModel.dataBinding?.homeMenuDetailFrontCover?.top ?: 0
                val height = mViewModel.dataBinding?.homeMenuDetailFrontCover?.height ?: 0
                totalDy += dy
                if (totalDy > 0) {
                    val alpha = if (totalDy.toFloat() / (top + height) > 1f) {
                        1f
                    } else {
                        totalDy.toFloat() / (top + height)
                    }
                    mDataBind.homeMenuDetailBlur.alpha = alpha
                }
            }
        })
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