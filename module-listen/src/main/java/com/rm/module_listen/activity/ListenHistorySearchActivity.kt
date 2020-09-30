package com.rm.module_listen.activity

import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.databinding.ActivityListenHistorySearchBinding
import com.rm.module_listen.viewmodel.ListenHistoryViewModel

class ListenHistorySearchActivity :
    BaseVMActivity<ActivityListenHistorySearchBinding, ListenHistoryViewModel>() {


    override fun initModelBrId() = BR.viewModel
    override fun getLayoutId() = R.layout.activity_listen_history_search

    override fun startObserve() {

    }

    override fun initData() {

    }


}