package com.rm.module_download.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser
import com.rm.module_download.BR
import com.rm.module_download.R
import com.rm.module_download.adapter.DownloadDetailPagerAdapter
import com.rm.module_download.bean.DownloadChapterItemBean
import com.rm.module_download.databinding.DownloadActivityDownloadMainBinding
import com.rm.module_download.fragment.DownloadCompletedFragment
import com.rm.module_download.fragment.DownloadInProgressFragment
import com.rm.module_download.viewmodel.DownloadChapterSelectionViewModel
import com.rm.module_download.viewmodel.DownloadMainViewModel
import kotlinx.android.synthetic.main.download_activity_download_main.*

class DownloadMainActivity : BaseVMActivity<DownloadActivityDownloadMainBinding, DownloadMainViewModel>() {


    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, DownloadMainActivity::class.java))
        }
    }

    private val mPagerAdapter by lazy {
        DownloadDetailPagerAdapter(this, mListTabFragment)
    }

    private val mListTabFragment = mutableListOf<Fragment>(
        DownloadInProgressFragment.newInstance(),
        DownloadCompletedFragment.newInstance()
    )


    override fun initModelBrId(): Int = BR.viewModel

    override fun startObserve() {

    }

    override fun initData() {
    }

    override fun getLayoutId(): Int = R.layout.download_activity_download_main

    override fun initView() {
        super.initView()
//        mViewModel.baseTitleModel.value = BaseTitleModel().setNoTitle(true)
        download_detail_view_pager2.offscreenPageLimit = 2
        download_detail_view_pager2.adapter = mPagerAdapter

        download_radio_tab_layout.apply {
            addTab(getString(R.string.download_downloading))
            addTab(getString(R.string.download_downloaded))
        }.bindViewPager2(download_detail_view_pager2)

    }
}