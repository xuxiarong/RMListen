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

class ListenSubscriptionActivity :
    BaseVMActivity<ListenActivitySubscriptionBinding, ListenSubscriptionViewModel>() {
    companion object {

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, ListenSubscriptionActivity::class.java))
        }
    }

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
        val baseTitleModel = BaseTitleModel().setTitle("订阅")
            .setLeftIcon(R.drawable.base_icon_back)
            .setLeftIconClick { finish() }
        mViewModel.baseTitleModel.value = baseTitleModel

        listen_subscription_recycler_view.apply {
            bindVerticalLayout(mAdapter)
            linearBottomItemDecoration(dimen(R.dimen.dp_14))
        }

        mViewModel.itemChildMoreClick = { itemChildClick(it) }
    }

    private fun itemChildClick(bean: SubscriptionListBean) {
        showBottomDialog(bean.is_top)
    }

    override fun initData() {
        mViewModel.getData()
    }

    override fun getLayoutId(): Int {
        return R.layout.listen_activity_subscription
    }


    override fun startObserve() {
        mViewModel.data.observe(this) {
            mAdapter.setList(it)
        }
    }

    private fun showBottomDialog(isTop: Int?) {
        mViewModel.mDialog.initDialog = {
            val subscriptionBinding =
                mViewModel.mDialog.mDataBind as ListenDialogBottomSubscriptionBinding
            subscriptionBinding.listenDialogBottomSubscriptionTop.text = if (isTop == 1) {
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