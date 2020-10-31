package com.rm.module_listen.adapter

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
    override fun convert(holder: BaseViewHolder, item: ListenSubscriptionListBean) {
        super.convert(holder, item)
        val view = holder.getView<AppCompatTextView>(R.id.listen_subscription_adapter_num)
        if (item.unread > 9) {
            view.width = view.resources.getDimensionPixelSize(R.dimen.dp_26)
        } else {
            view.width = view.resources.getDimensionPixelSize(R.dimen.dp_18)
        }
    }
}