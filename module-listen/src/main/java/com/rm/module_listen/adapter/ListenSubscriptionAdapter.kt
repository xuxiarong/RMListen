package com.rm.module_listen.adapter

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.rm.baselisten.adapter.single.BaseBindVMAdapter
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_listen.R
import com.rm.module_listen.bean.ListenSubscriptionListBean

/**
 *
 * @author yuanfang
 * @date 9/17/20
 * @description
 *
 */
class ListenSubscriptionAdapter(
    mViewModel: BaseVMViewModel,
    viewModelBrId: Int,
    vmDataBrId: Int
) : BaseBindVMAdapter<ListenSubscriptionListBean>(
    mViewModel,
    mutableListOf(),
    R.layout.listen_adapter_subscription,
    viewModelBrId,
    vmDataBrId
) {
    private var topSize = 0
    override fun convert(holder: BaseViewHolder, item: ListenSubscriptionListBean) {
        super.convert(holder, item)
        val view = holder.getView<AppCompatTextView>(R.id.listen_subscription_adapter_num)
        if (item.unread > 9) {
            view.width = view.resources.getDimensionPixelSize(R.dimen.dp_30)
        } else {
            view.width = view.resources.getDimensionPixelSize(R.dimen.dp_18)
        }

        if (topSize > 0 && topSize != itemCount-headerLayoutCount-footerLayoutCount && topSize-1  == holder.adapterPosition) {
            holder.getView<View>(R.id.listen_subscription_adapter_view1).visibility = View.VISIBLE
            holder.getView<View>(R.id.listen_subscription_adapter_view2).visibility = View.VISIBLE
        } else {
            holder.getView<View>(R.id.listen_subscription_adapter_view1).visibility = View.GONE
            holder.getView<View>(R.id.listen_subscription_adapter_view2).visibility = View.GONE
        }
    }

    fun setTopSize(topSize: Int) {
        this.topSize = topSize
        notifyDataSetChanged()
    }
}