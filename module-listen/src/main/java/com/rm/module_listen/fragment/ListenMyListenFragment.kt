package com.rm.module_listen.fragment

import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.activity.ListenBoughtActivity
import com.rm.module_listen.activity.ListenSheetListActivity
import com.rm.module_listen.activity.ListenSubscriptionActivity
import com.rm.module_listen.databinding.ListenFragmentMyListenBinding
import com.rm.module_listen.viewmodel.ListenMyListenViewModel
import kotlinx.android.synthetic.main.listen_fragment_my_listen.*

/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */
class ListenMyListenFragment : BaseVMFragment<ListenFragmentMyListenBinding,ListenMyListenViewModel>() {
    override fun initModelBrId()= BR.viewModel
    override fun initLayoutId()= R.layout.listen_fragment_my_listen
    override fun initView() {
        super.initView()
        boughView.setOnClickListener{
            ListenBoughtActivity.startActivity(it.context)
        }
        subscription.setOnClickListener{
            ListenSubscriptionActivity.startActivity(it.context)
        }

        sheetList.setOnClickListener{
            ListenSheetListActivity.startActivity(it.context)
        }
    }
    override fun startObserve() {

    }

    override fun initData() {
    }
}