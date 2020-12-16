package com.rm.module_listen.fragment

import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.utilExt.DisplayUtils
import com.rm.business_lib.HomeGlobalData
import com.rm.business_lib.LISTEN_SHEET_LIST_MY_LIST
import com.rm.business_lib.download.DownloadMemoryCache
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser
import com.rm.business_lib.wedgit.bendtablayout.BendTabLayout
import com.rm.component_comm.download.DownloadService
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

    private lateinit var mViewPagerAdapter: ListenMyListenPagerAdapter


    private val mMyListenFragmentList = mutableListOf<Fragment>(
        ListenRecentListenFragment.newInstance(),
        ListenSubscriptionUpdateFragment.newInstance()
    )

    private val tabList = mutableListOf(
        BaseApplication.CONTEXT.getString(R.string.listen_tab_recent_listen),
        BaseApplication.CONTEXT.getString(R.string.listen_tab_subscription_update)
    )

    override fun initModelBrId() = BR.viewModel
    override fun initLayoutId() = R.layout.listen_fragment_my_listen

    override fun initView() {
        super.initView()
        setDefault()
        context?.let {
            val height = DisplayUtils.getStateHeight(it)
            val params = mDataBind.listenMyListenSrl.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = height
        }

        mDataShowView = listenMyListenVp
        //用懒加载的方式切换fragment的时候会报错
        mViewPagerAdapter = ListenMyListenPagerAdapter(
            fm = activity!!.supportFragmentManager,
            tabList = tabList,
            fragmentList = mMyListenFragmentList
        )
        listenMyListenVp.offscreenPageLimit = 2
        listenMyListenVp.adapter = mViewPagerAdapter
        setClick()
        configTab()
    }

    override fun startObserve() {
        isLogin.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (isLogin.get()) {
                    mViewModel.getSubsTotalNumberFromService()
                }
            }
        })
        HomeGlobalData.isShowSubsRedPoint.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (HomeGlobalData.isShowSubsRedPoint.get()) {
                    listenMyListenRtl?.getTabAt(1)?.tabView?.setRedPointVisible(true)
                } else {
                    listenMyListenRtl?.getTabAt(1)?.tabView?.setRedPointVisible(false)
                }
            }
        })
        DownloadMemoryCache.downloadingChapterList.observe(this, Observer {
            download_chapter_num.isVisible = it.isNotEmpty()
            download_chapter_num.text = it.size.toString()
        })
    }


    override fun initData() {
        if (mMyListenFragmentList[1] is ListenSubscriptionUpdateFragment) {
            mViewModel.subsRefreshFun =
                { (mMyListenFragmentList[1] as ListenSubscriptionUpdateFragment).refreshSubsData() }
        }
    }


    private fun configTab() {
        listenMyListenRtl.setupWithViewPager(listenMyListenVp)
        listenMyListenVp.setCurrentItem(0, false)
        listenMyListenRtl.addOnTabSelectedListener(object : BendTabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: BendTabLayout.BendTab?) {
            }

            override fun onTabUnselected(tab: BendTabLayout.BendTab?) {
            }

            override fun onTabSelected(tab: BendTabLayout.BendTab?) {
                tab?.let {
                    HomeGlobalData.myListenSelectTab.set(it.position)
                }
            }
        })
    }

    private fun setClick() {
        val router = RouterHelper.createRouter(LoginService::class.java)

        listenBuyCl.setOnClickListener {
            if (!isLogin.get()) {
                activity?.let {
                    router.quicklyLogin(mViewModel, it)
                }
            } else {
                ListenBoughtActivity.startActivity(it.context)
            }
        }


        listenSubCl.setOnClickListener {
            if (!isLogin.get()) {
                router.quicklyLogin(mViewModel, activity!!)
            } else {
                (mMyListenFragmentList[1] as ListenSubscriptionUpdateFragment).checkRedPointStatus()
                ListenSubscriptionActivity.startActivity(it.context)
            }
        }
        listenListCl.setOnClickListener {
            if (!isLogin.get()) {
                router.quicklyLogin(mViewModel, activity!!)
            } else {
                ListenSheetListActivity.startActivity(
                    activity!!,
                    LISTEN_SHEET_LIST_MY_LIST,
                    ""
                )
            }
        }
        listenDownloadCl.setOnClickListener {
            val createRouter = RouterHelper.createRouter(DownloadService::class.java)
            createRouter.startDownloadMainActivity(context = it.context, startTab = 1)
        }
    }


}