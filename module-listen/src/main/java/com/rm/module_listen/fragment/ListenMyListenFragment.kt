package com.rm.module_listen.fragment

import android.view.View
import androidx.fragment.app.Fragment
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.component_comm.isLogin
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.activity.ListenBoughtActivity
import com.rm.module_listen.activity.ListenSheetListActivity
import com.rm.module_listen.activity.ListenSubscriptionActivity
import com.rm.module_listen.adapter.ListenMyListenPagerAdapter
import com.rm.module_listen.databinding.ListenFragmentMyListenBinding
import com.rm.module_listen.viewmodel.ListenMyListenViewModel
import kotlinx.android.synthetic.main.listen_fragment_my_listen.*

/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */
class ListenMyListenFragment :
    BaseVMFragment<ListenFragmentMyListenBinding, ListenMyListenViewModel>() {

    private lateinit var mViewPagerAdapter : ListenMyListenPagerAdapter

    private val mMyListenFragmentList = mutableListOf<Fragment>(
        ListenRecentListenFragment.newInstance(),
        ListenSubscriptionUpdateFragment.newInstance()
    )

    override fun initModelBrId() = BR.viewModel
    override fun initLayoutId() = R.layout.listen_fragment_my_listen

    override fun initView() {
        super.initView()

        //用懒加载的方式切换fragment的时候会报错
        mViewPagerAdapter = ListenMyListenPagerAdapter(this.activity!!, mMyListenFragmentList)
        listenMyListenVp.adapter = mViewPagerAdapter
        setClick()
        listenMyListenVp.adapter = mViewPagerAdapter
        configTab()

        listenListCl.setOnClickListener{
            ListenSheetListActivity.startActivity(it.context)
        }

    }

    override fun startObserve() {

    }

    override fun initData() {

        val loginService = RouterHelper.createRouter(LoginService::class.java)
        loginService.quicklyLogin(mViewModel,activity!!)

        mViewModel.getSubsDataFromService()
    }



    private fun configTab() {
        listenMyListenRtl.addTab(getString(R.string.listen_tab_recent_listen))
        listenMyListenRtl.addTab(getString(R.string.listen_tab_subscription_update))
        listenMyListenRtl.bindViewPager2(listenMyListenVp)
        listenMyListenVp.setCurrentItem(1, false)
        listenMyListenRtl.setRedPointVisibility(1, View.VISIBLE)
    }

    private fun setClick() {
        val router = RouterHelper.createRouter(LoginService::class.java)

        listenBuyCl.setOnClickListener {
            if (isLogin.value == false) {
                activity?.let {
                    router.quicklyLogin(mViewModel, it)
                }
            } else {
                ListenBoughtActivity.startActivity(it.context)
            }
        }


        listenSubCl.setOnClickListener {
            if (isLogin.value == false) {
                router.quicklyLogin(mViewModel, activity!!)
            } else {
                ListenSubscriptionActivity.startActivity(it.context)
            }
        }
        listenListCl.setOnClickListener {
            if (isLogin.value == false) {
                router.quicklyLogin(mViewModel, activity!!)
            } else {
                ListenSheetListActivity.startActivity(it.context)
            }
        }
    }


}