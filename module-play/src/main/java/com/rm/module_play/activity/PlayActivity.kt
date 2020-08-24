package com.rm.module_play.activity

import com.rm.baselisten.activity.BaseNetActivity
import com.rm.baselisten.util.ToastUtil
import com.rm.module_play.R
import com.rm.module_play.viewmodel.PlayViewModel

class PlayActivity :
    BaseNetActivity<com.rm.module_play.databinding.ActivityPlayBinding, PlayViewModel>() {

    override fun getLayoutId(): Int = R.layout.activity_play
    override fun initView() {
        dataBind.viewModel = mViewModel
    }

    override fun startObserve() {
    }

    override fun initData() {
        ToastUtil.show(this, mViewModel.msg)
    }
}