package com.rm.module_listen.activity

import android.content.Context
import android.content.Intent
import androidx.lifecycle.observe
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.utilExt.dimen
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.bean.SubscriptionListBean
import com.rm.module_listen.databinding.ListenActivitySubscriptionBinding
import com.rm.module_listen.databinding.ListenDialogBottomSubscriptionBinding
import com.rm.module_listen.viewmodel.ListenSubscriptionViewModel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.android.synthetic.main.listen_activity_subscription.*

/**
 * 订阅界面
 */
class ListenSubscriptionActivity :
    BaseVMActivity<ListenActivitySubscriptionBinding, ListenSubscriptionViewModel>() {


    //当前请求的页码
    private var mPage = 1

    //每次请求数据的条数
    private val pageSize = 10


    /**
     * 懒加载创建adapter对象
     */
    private val mAdapter by lazy {
        CommonBindVMAdapter<SubscriptionListBean>(
            mViewModel,
            mutableListOf(),
            R.layout.listen_adapter_subscription,
            BR.click,
            BR.item
        )
    }


    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, ListenSubscriptionActivity::class.java))
        }
    }

    override fun initModelBrId(): Int {
        return BR.viewModel
    }

    override fun initView() {
        super.initView()
        //初始化title信息
        val baseTitleModel = BaseTitleModel().setTitle("订阅")
            .setLeftIcon(R.drawable.base_icon_back)
            .setLeftIconClick { finish() }
        mViewModel.baseTitleModel.value = baseTitleModel

        //初始化recyclerView
        listen_subscription_recycler_view.apply {
            bindVerticalLayout(mAdapter)
        }

        //更多点击事件
        mViewModel.itemChildMoreClick = { itemChildClick(it) }

        //取消订阅成功
        mViewModel.dialogUnsubscribe = {
            mViewModel.subscriptionData.get()?.let {
                mAdapter.remove(it)
            }
        }

        //置顶操作成功
        mViewModel.dialogSetTop = { setTopSuccess(it) }

        //取消置顶操作成功
        mViewModel.dialogCancelTop = { cancelTopSuccess(it) }

        mViewModel.itemClick={
            RouterHelper.createRouter(HomeService::class.java).toDetailActivity(this,it.audio_id.toString())
        }

        addRefreshListener()
    }

    /**
     * 数据加载完成
     */
    private fun loadDataComplete(it: Boolean) {
        if (it) {
            if (mPage == 1) {
                //刷新完成
                listen_subscription_refresh?.finishRefresh()
            } else {
                //加载更多完成
                listen_subscription_refresh?.finishLoadMore()
            }
        }
    }

    /**
     * 置顶成功
     */
    private fun setTopSuccess(bean: SubscriptionListBean) {
        mAdapter.remove(bean)
        mAdapter.data.add(0, bean)
        mAdapter.notifyDataSetChanged()
    }

    /**
     * 取消置顶成功
     */
    private fun cancelTopSuccess(bean: SubscriptionListBean) {
        mAdapter.remove(bean)
        mAdapter.data.add(mAdapter.data.lastIndex + 1, bean)
        mAdapter.notifyDataSetChanged()
    }

    /**
     * 更多点事件
     * @param bean 订阅实体bean
     */
    private fun itemChildClick(bean: SubscriptionListBean) {
        showBottomDialog(bean)
    }

    override fun initData() {
        mViewModel.showLoading()
        mViewModel.getData(mPage, pageSize)
    }

    override fun getLayoutId(): Int {
        return R.layout.listen_activity_subscription
    }

    override fun startObserve() {
        //监听数据的变换
        mViewModel.data.observe(this) {
            if (mPage == 1) {
                mAdapter.setList(it)
            } else {
                mAdapter.addData(it)
            }

            if (it.size < pageSize) {
                //没有更多数据
                listen_subscription_refresh.finishLoadMoreWithNoMoreData()
            }
        }


        //监听网络请求完成，成功失败都会执行。防止请求失败，动画不消失
        mViewModel.isRefreshOrLoadComplete.observe(this) {
            loadDataComplete(it)
        }
    }

    /**
     * 添加上下拉监听
     */
    private fun addRefreshListener() {
        listen_subscription_refresh.setOnRefreshLoadMoreListener(object :
            OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                ++mPage
                mViewModel.getData(mPage, pageSize)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                mPage = 1
                mViewModel.getData(mPage, pageSize)
            }
        })
    }

    /**
     * 现实底部弹窗
     */
    private fun showBottomDialog(bean: SubscriptionListBean) {
        mViewModel.subscriptionData.set(bean)
        mViewModel.mDialog.initDialog = {
            val subscriptionBinding =
                mViewModel.mDialog.mDataBind as ListenDialogBottomSubscriptionBinding

            subscriptionBinding.listenDialogBottomSubscriptionTop.text = if (bean.is_top == 0) {
                getString(R.string.listen_set_top)
            } else {
                getString(R.string.listen_cancel_top)
            }
        }
        mViewModel.mDialog.showCommonDialog(
            this,
            R.layout.listen_dialog_bottom_subscription,
            mViewModel,
            BR.viewModel
        )
    }
}