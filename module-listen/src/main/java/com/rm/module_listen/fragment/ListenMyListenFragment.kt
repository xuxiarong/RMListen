package com.rm.module_listen.fragment

import android.view.View
import androidx.fragment.app.Fragment
import com.rm.baselisten.mvvm.BaseVMFragment
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
class ListenMyListenFragment : BaseVMFragment<ListenFragmentMyListenBinding,ListenMyListenViewModel>() {

    private val mViewPagerAdapter by lazy {
        ListenMyListenPagerAdapter(this.activity!!, mMyListenFragmentList)
    }



    private val mMyListenFragmentList = mutableListOf<Fragment>(ListenRecentListenFragment.newInstance(),
        ListenSubscriptionUpdateFragment.newInstance())

    override fun initModelBrId()= BR.viewModel
    override fun initLayoutId()= R.layout.listen_fragment_my_listen

    override fun initView() {
        super.initView()

        listenMyListenVp.adapter = mViewPagerAdapter


        configTab()
        listenBuyCl.setOnClickListener{
            ListenBoughtActivity.startActivity(it.context)
        }
        listenSubCl.setOnClickListener{
            ListenSubscriptionActivity.startActivity(it.context)
        }

        listenListCl.setOnClickListener{
            ListenSheetListActivity.startActivity(it.context)
        }

    }
    override fun startObserve() {

    }

    override fun initData() {
        mViewModel.getSubsDataFromService()
    }



    private fun configTab() {
        listenMyListenRtl.addTab(getString(R.string.listen_tab_recent_listen))
        listenMyListenRtl.addTab(getString(R.string.listen_tab_subscription_update))
        listenMyListenRtl.bindViewPager2(listenMyListenVp)
        listenMyListenVp.setCurrentItem(1,false)
        listenMyListenRtl.setRedPointVisibility(1, View.VISIBLE)
    }
}