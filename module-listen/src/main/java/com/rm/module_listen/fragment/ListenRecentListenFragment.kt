package com.rm.module_listen.fragment

import android.text.TextUtils
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import com.rm.baselisten.BaseConstance
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.business_lib.HomeGlobalData
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.databinding.ListenFragmentRecentListenBinding
import com.rm.module_listen.viewmodel.ListenRecentListenViewModel

/**
 * desc   :
 * date   : 2020/09/09
 * version: 1.0
 */
class ListenRecentListenFragment :
    BaseVMFragment<ListenFragmentRecentListenBinding, ListenRecentListenViewModel>() {
    private var basePlayInfoModelChangedCallback: Observable.OnPropertyChangedCallback? = null
    private var homeGlobalSelectTabChangedCallback: Observable.OnPropertyChangedCallback? = null

    override fun initLayoutId() = R.layout.listen_fragment_recent_listen
    override fun initModelBrId() = BR.viewModel


    override fun startObserve() {
        mViewModel.allHistory.observe(this, Observer {
            mViewModel.mSwipeAdapter.setList(it)
        })
        //如果播放的书籍，章节列表有改变，则重新加载一下数据库
        basePlayInfoModelChangedCallback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                BaseConstance.basePlayInfoModel.get()?.let {
                    if (!TextUtils.isEmpty(it.playAudioId)) {
                        mViewModel.getListenHistory()
                    }
                }
            }
        }
        basePlayInfoModelChangedCallback?.let {
            BaseConstance.basePlayInfoModel.addOnPropertyChangedCallback(it)
        }
        //如果首页几个Tab切换后选择了我听，则重新加载一下数据库
        homeGlobalSelectTabChangedCallback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                HomeGlobalData.homeGlobalSelectTab.get().let {
                    if (HomeGlobalData.LISTEN_SELECT == it) {
                        mViewModel.getListenHistory()
                    }
                }
            }
        }
        homeGlobalSelectTabChangedCallback?.let {
            HomeGlobalData.homeGlobalSelectTab.addOnPropertyChangedCallback(it)
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getListenHistory()
    }

    override fun initData() {
    }

    companion object {
        fun newInstance(): ListenRecentListenFragment {
            return ListenRecentListenFragment()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        homeGlobalSelectTabChangedCallback?.let {
            HomeGlobalData.homeGlobalSelectTab.addOnPropertyChangedCallback(it)
            homeGlobalSelectTabChangedCallback = null
        }
        basePlayInfoModelChangedCallback?.let {
            BaseConstance.basePlayInfoModel.addOnPropertyChangedCallback(it)
            basePlayInfoModelChangedCallback = null
        }
    }
}