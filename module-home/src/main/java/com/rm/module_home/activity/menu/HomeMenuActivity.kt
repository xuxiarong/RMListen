package com.rm.module_home.activity.menu

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.observe
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.DLog
import com.rm.business_lib.binding.bindData
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.adapter.MenuListAdapter
import com.rm.module_home.bean.MenuSheetBean
import com.rm.module_home.databinding.HomeActivityListenMenuBinding
import com.rm.module_home.viewmodel.MenuViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.stx.xhb.androidx.XBanner
import kotlinx.android.synthetic.main.home_activity_listen_menu.*
import kotlin.properties.Delegates

class HomeMenuActivity : BaseVMActivity<HomeActivityListenMenuBinding, MenuViewModel>() {

    //懒加载头部banner
    private val headView by lazy {
        View.inflate(this, R.layout.home_header_banner, null)
    }

    //懒加载adapter
    private val menuAdapter by lazy { MenuListAdapter(mViewModel) }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, HomeMenuActivity::class.java))
        }
    }

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId(): Int = R.layout.home_activity_listen_menu

    //每次加载的条数
    private val pageSize = 10

    //当前的页码
    private var mPage = 1

    //当前pageId
    private var mPageId by Delegates.notNull<Int>()

    override fun startObserve() {
        mViewModel.menuList.observe(this) {
            //刷新数据完成
            home_menu_refresh.finishRefresh()
            //设置新的数据源
            menuAdapter.setList(it.sheet_list?.list)

            //设置banner数据源
            headView.findViewById<XBanner>(R.id.home_head_banner).bindData(it.banner_list)

            mPageId = it.page_id

            //如果总条数等于当前item个数则没有更多数据
            if (menuAdapter.data.size >= it.sheet_list?.total ?: 0) {
                home_menu_refresh.finishLoadMoreWithNoMoreData()
            }
        }

        mViewModel.sheetList.observe(this) {
            DLog.i("-------->", "item size = ${menuAdapter.data.size}   total = ${it.total}")

            if (it.total <= menuAdapter.data.size) {
                //没有更多数据
                home_menu_refresh.finishLoadMoreWithNoMoreData()
            } else {
                //数据加载完成
                home_menu_refresh.finishLoadMore()
            }
            //添加数据到列表中
            it.list?.let { bean -> menuAdapter.addData(bean) }
        }

        //item点击事件
        mViewModel.itemClick = {
            HomeMenuDetailActivity.startActivity(this@HomeMenuActivity, it.sheet_id, mPageId)
        }

    }

    override fun initView() {
        val baseTitleModel = BaseTitleModel()
            .setTitle(resources.getString(R.string.home_menu))
            .setLeftIconClick {
                finish()
            }
        mViewModel.baseTitleModel.value = baseTitleModel

        home_menu_recycler_view.bindVerticalLayout(menuAdapter)
        menuAdapter.addHeaderView(headView)

        //上下拉监听
        onRefresh()
    }

    override fun initData() {
        mViewModel.getMenuListInfo()
    }

    /**
     * 上拉加载/下拉刷新 监听
     */
    private fun onRefresh() {
        home_menu_refresh.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                mPage++
                mViewModel.getSheetList(mPageId.toString(), mPage, pageSize)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                mViewModel.getMenuListInfo()
            }
        })
    }
}