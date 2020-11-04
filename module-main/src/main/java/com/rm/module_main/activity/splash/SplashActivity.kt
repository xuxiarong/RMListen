package com.rm.module_main.activity.splash

import androidx.databinding.Observable
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_main.BR
import com.rm.module_main.R
import com.rm.module_main.activity.MainMainActivity
import com.rm.module_main.databinding.HomeActivitySplashBinding
import com.rm.module_main.viewmodel.HomeSplashViewModel

/**
 * desc   :
 * date   : 2020/11/02
 * version: 1.0
 */
class SplashActivity :BaseVMActivity<HomeActivitySplashBinding, HomeSplashViewModel>() {
    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {
        mViewModel.isSkipAd.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if(mViewModel.isSkipAd.get()){
                    MainMainActivity.startMainActivity(this@SplashActivity)
                    finish()
                }
            }
        })
    }

    override fun initData() {
        mViewModel.startSkipTimerCount()
    }

    override fun getLayoutId() = R.layout.home_activity_splash

}