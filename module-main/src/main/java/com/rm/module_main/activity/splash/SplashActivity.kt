package com.rm.module_main.activity.splash

import androidx.databinding.Observable
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.constant.PermissionConstants
import com.rm.business_lib.coroutinepermissions.requestPermissionsForResult
import com.rm.business_lib.ext.PermissionExt
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

    var result = false

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
        mViewModel.getSplashAd()
//        mViewModel.launchOnIO {
//            requestPermissions()
//        }
//        PermissionExt.requestPermission(this,arrayOf(PermissionConstants.STORAGE),1)
    }



    suspend fun requestPermissions(){
        val result = requestPermissionsForResult(
            permissions = *arrayOf(PermissionConstants.STORAGE),
            title = "权限请求",
            rationale = "类漫听书需要存储权限"
        )
        DLog.d("suolong","result = $result")
        if(result && mViewModel.isSkipAd.get()){
            MainMainActivity.startMainActivity(this@SplashActivity)
            finish()
        }
    }


    override fun getLayoutId() = R.layout.home_activity_splash

}