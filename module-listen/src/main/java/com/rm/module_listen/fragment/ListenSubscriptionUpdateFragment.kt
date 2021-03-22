package com.rm.module_listen.fragment

import androidx.databinding.Observable
import com.rm.baselisten.binding.bindHorizontalLayout
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.business_lib.HomeGlobalData
import com.rm.business_lib.isLogin
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.databinding.ListenFragmentSubscriptionUpdateBinding
import com.rm.module_listen.viewmodel.ListenSubsUpdateViewModel
import kotlinx.android.synthetic.main.listen_fragment_subscription_update.*

/**
 * desc   :
 * date   : 2020/09/09
 * version: 1.0
 */
class ListenSubscriptionUpdateFragment :
    BaseVMFragment<ListenFragmentSubscriptionUpdateBinding, ListenSubsUpdateViewModel>() {
    private var isLoginChangedCallback: Observable.OnPropertyChangedCallback? = null
    private var myListenSelectTabChangedCallback: Observable.OnPropertyChangedCallback? = null
    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {

        isLoginChangedCallback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (isLogin.get()) {
                    mViewModel.refreshSubsDataFromService()
                }
            }
        }
        isLoginChangedCallback?.let {
            isLogin.addOnPropertyChangedCallback(it)
        }

        myListenSelectTabChangedCallback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (HomeGlobalData.myListenSelectTab.get() == HomeGlobalData.LISTEN_SELECT_SUBS_UPDATE && isLogin.get()) {
                    mViewModel.checkRedPointStatus()
                }
            }
        }
        myListenSelectTabChangedCallback?.let {
            HomeGlobalData.myListenSelectTab.addOnPropertyChangedCallback(it)
        }

    }

    override fun initLayoutId() = R.layout.listen_fragment_subscription_update

    override fun initData() {
        if (isLogin.get()) {
            mViewModel.refreshSubsDataFromService()
        }
    }

    fun refreshSubsData() {
        if (isLogin.get()) {
            mViewModel.refreshSubsDataFromService()
        }
    }

//    fun checkRedPointStatus() {
//        if (isLogin.get()) {
//            mViewModel.checkRedPointStatus()
//        }
//    }

    override fun initView() {
        super.initView()
        listenSubsDataRv.bindHorizontalLayout(mViewModel.subsDateAdapter)
        listenSubscriptionRv.bindVerticalLayout(mViewModel.subsAudioAdapter)
        mViewModel.init()
    }

    companion object {
        fun newInstance(): ListenSubscriptionUpdateFragment {
            return ListenSubscriptionUpdateFragment()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isLoginChangedCallback?.let {
            isLogin.removeOnPropertyChangedCallback(it)
            isLoginChangedCallback = null
        }
        myListenSelectTabChangedCallback?.let {
            HomeGlobalData.myListenSelectTab.removeOnPropertyChangedCallback(it)
            myListenSelectTabChangedCallback = null
        }
    }
}