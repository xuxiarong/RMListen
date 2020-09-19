package com.rm.module_listen.activity

import android.content.Context
import android.content.Intent
import androidx.lifecycle.observe
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.binding.linearBottomItemDecoration
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.utilExt.dimen
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.bean.SubscriptionListBean
import com.rm.module_listen.databinding.ListenActivitySubscriptionBinding
import com.rm.module_listen.databinding.ListenDialogBottomSubscriptionBinding
import com.rm.module_listen.viewmodel.ListenSubscriptionViewModel
import kotlinx.android.synthetic.main.listen_activity_subscription.*

/**
 * 订阅界面
 */
class ListenSubscriptionActivity :
    BaseVMActivity<ListenActivitySubscriptionBinding, ListenSubscriptionViewModel>() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, ListenSubscriptionActivity::class.java))
        }
    }

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
            linearBottomItemDecoration(dimen(R.dimen.dp_14))
        }

        //更多点击事件
        mViewModel.itemChildMoreClick = { itemChildClick(it) }
    }

    /**
     * 更多点事件
     * @param bean 订阅实体bean
     */
    private fun itemChildClick(bean: SubscriptionListBean) {
        showBottomDialog(bean)
    }

    override fun initData() {
        mViewModel.getData()
    }

    override fun getLayoutId(): Int {
        return R.layout.listen_activity_subscription
    }


    override fun startObserve() {
        //监听数据的变换
        mViewModel.data.observe(this) {
            mAdapter.setList(it)
        }
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