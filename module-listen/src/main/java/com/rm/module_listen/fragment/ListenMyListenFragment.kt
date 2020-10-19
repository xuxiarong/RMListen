package com.rm.module_listen.fragment

import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.business_lib.LISTEN_SHEET_LIST_MY_LIST
import com.rm.business_lib.isLogin
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

        //用懒加载的方式切换fragment的时候会报错
        mViewPagerAdapter = ListenMyListenPagerAdapter(fm = activity!!.supportFragmentManager,tabList = tabList, fragmentList = mMyListenFragmentList)
        listenMyListenVp.offscreenPageLimit = 2

        listenMyListenVp.adapter = mViewPagerAdapter

        setClick()
        configTab()
    }

    override fun startObserve() {
        isLogin.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if(isLogin.get()){
                    mViewModel.getSubsTotalNumberFromService()
                }
            }
        })
    }

    override fun initData() {
        mViewModel.getSubsTotalNumberFromService()
    }


    private fun configTab() {
        listenMyListenRtl.setupWithViewPager(listenMyListenVp)
//        listenMyListenRtl.addOnTabSelectedListener(object :BendTabLayout.OnTabSelectedListener{
//            override fun onTabReselected(tab: BendTabLayout.BendTab?) {
//            }
//
//            override fun onTabUnselected(tab: BendTabLayout.BendTab?) {
//            }
//
//            override fun onTabSelected(tab: BendTabLayout.BendTab?) {
//                if(tab!=null){
//                    if(tab.position == 1){
//                        val subFragment =   mMyListenFragmentList[1] as ListenSubscriptionUpdateFragment
//                        subFragment.checkLogin()
//                    }
//                }
//            }
//        })
        listenMyListenVp.setCurrentItem(0, false)

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
                ListenSubscriptionActivity.startActivity(it.context)
            }
        }
        listenListCl.setOnClickListener {
            if (!isLogin.get()) {
                router.quicklyLogin(mViewModel, activity!!)
            } else {
                ListenSheetListActivity.startActivity(
                    activity!!,
                    LISTEN_SHEET_LIST_MY_LIST
                )
            }
        }
        listenDownloadCl.setOnClickListener {
//            val createRouter = RouterHelper.createRouter(DownloadService::class.java)
//            createRouter.startDownloadChapterSelectionActivity(it.context, "162163095869968384")
        }
    }


}