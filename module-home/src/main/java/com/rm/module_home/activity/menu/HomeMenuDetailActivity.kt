package com.rm.module_home.activity.menu

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.utilExt.getStateHeight
import com.rm.component_comm.activity.ComponentShowPlayActivity
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeActivityListenMenuDetailBinding
import com.rm.module_home.databinding.HomeHeaderMenuDetailBinding
import com.rm.module_home.viewmodel.HomeMenuDetailViewModel
import kotlinx.android.synthetic.main.home_activity_listen_menu_detail.*

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


    override fun getLayoutId() = R.layout.home_activity_listen_menu_detail

    override fun initModelBrId() = BR.viewModel

    override fun initView() {
        super.initView()
        val sheetId = intent.getStringExtra(SHEET_ID) ?: ""
        mViewModel.sheetId.set(sheetId)

        setTransparentStatusBar()//设置透明沉浸状态栏

        val layoutParams = (home_menu_detail_title_cl.layoutParams) as ConstraintLayout.LayoutParams
        layoutParams.apply {
            //动态获取状态栏的高度,并设置标题栏的topMargin
            val stateHeight = getStateHeight(this@HomeMenuDetailActivity)
            topMargin = stateHeight
        }

//        recycleScrollListener()

        home_menu_detail_recycler_view.apply {
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
            home_menu_detail_recycler_view,
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
    }

    /**
     * recyclerView滑动监听
     */
    private fun recycleScrollListener() {
        home_menu_detail_recycler_view.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //找到即将移出屏幕Item的position,position是移出屏幕item的数量
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val position = linearLayoutManager.findFirstVisibleItemPosition()
                //根据position找到这个Item
                val firstVisibleChildView = linearLayoutManager.findViewByPosition(position)
                //获取Item的高
                val itemHeight = firstVisibleChildView?.height ?: 0
                //算出该Item还未移出屏幕的高度
                val itemTop = firstVisibleChildView?.top ?: 0
                //position移出屏幕的数量*高度得出移动的距离
                val iPosition = position * itemHeight
                //减去该Item还未移出屏幕的部分可得出滑动的距离
                val iResult = iPosition - itemTop

//                val height = home_menu_detail_iv_bg.height

//                val fl = iResult.toFloat() / height.toFloat()
//                home_menu_detail_iv_bg.alpha = 1f - fl
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

    override fun onStart() {
        super.onStart()

    }
}