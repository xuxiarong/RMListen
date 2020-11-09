package com.rm.component_comm.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.rm.baselisten.model.BasePlayControlModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper

/**
 * desc   :
 * date   : 2020/11/05
 * version: 1.0
 */
@Suppress("UNCHECKED_CAST")
abstract class ComponentShowPlayActivity<V : ViewDataBinding, VM : BaseVMViewModel> :
    BaseVMActivity<V, VM>() {

    private val playService by lazy {
        RouterHelper.createRouter(PlayService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.basePlayControlModel.set(
            BasePlayControlModel(
                showPlay = true,
                clickFun = { startPlayActivity() })
        )
    }

    open fun startPlayActivity() {
//        if (TextUtils.isEmpty(PlayConstance.getLastListenAudioUrl())) {
//            tipView.showTipView(
//                this,
//                getString(com.rm.business_lib.R.string.business_no_content)
//            )
//            return
//        }
        playService.onGlobalPlayClick(this)
    }

}