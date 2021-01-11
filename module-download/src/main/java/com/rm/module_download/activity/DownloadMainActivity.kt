package com.rm.module_download.activity

import android.content.Context
import android.content.Intent
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.receiver.NetworkChangeReceiver
import com.rm.business_lib.aria.AriaDownloadManager
import com.rm.business_lib.download.DownloadMemoryCache
import com.rm.business_lib.wedgit.bendtablayout.BendTabLayout
import com.rm.module_download.BR
import com.rm.module_download.R
import com.rm.module_download.adapter.DownloadDetailPagerAdapter
import com.rm.module_download.databinding.DownloadActivityDownloadMainBinding
import com.rm.module_download.fragment.DownloadCompletedFragment
import com.rm.module_download.fragment.DownloadInProgressFragment
import com.rm.module_download.viewmodel.DownloadMainViewModel
import kotlinx.android.synthetic.main.download_activity_download_main.*

class DownloadMainActivity :
    BaseVMActivity<DownloadActivityDownloadMainBinding, DownloadMainViewModel>() {

    private var isAvailableChangeCallback: Observable.OnPropertyChangedCallback? = null
    private var needShowNetErrorChangeCallback: Observable.OnPropertyChangedCallback? = null

    companion object {
        fun startActivity(context: Context, startTab: Int = 0) {
            context.startActivity(Intent(context, DownloadMainActivity::class.java))
            this.startTab = startTab
        }

        var startTab = 0
    }

    private lateinit var mPagerAdapter: DownloadDetailPagerAdapter


    private val mListTabFragment = mutableListOf<Fragment>(
        DownloadInProgressFragment.newInstance(),
        DownloadCompletedFragment.newInstance()
    )

    private val tabList = mutableListOf(
        BaseApplication.CONTEXT.getString(R.string.download_downloading),
        BaseApplication.CONTEXT.getString(R.string.download_downloaded)
    )

    override fun initModelBrId(): Int = BR.viewModel

    override fun startObserve() {
        isAvailableChangeCallback = object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                NetworkChangeReceiver.isAvailable.get().let {
                    try {
                        if (it) {
                            if (DownloadMemoryCache.isDownAll.get()) {
                                DownloadMemoryCache.downloadingChapter.get()
                                    ?.let { downloadChapter ->
                                        AriaDownloadManager.startDownload(downloadChapter)
                                    }
                            }
                        } else {
//                            DownloadMemoryCache.pauseDownloadingChapter()
                            tipView.showNetError(this@DownloadMainActivity)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        isAvailableChangeCallback?.let {
            NetworkChangeReceiver.isAvailable.addOnPropertyChangedCallback(it)
        }

        needShowNetErrorChangeCallback = object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                AriaDownloadManager.needShowNetError.get().let {
                    if (it) {
                        DownloadMemoryCache.pauseDownloadingChapter()
                        tipView.showNetError(this@DownloadMainActivity)
                    }
                }
            }
        }
        needShowNetErrorChangeCallback?.let {
            AriaDownloadManager.needShowNetError.addOnPropertyChangedCallback(it)
        }
    }

    override fun initData() {
    }

    override fun getLayoutId(): Int = R.layout.download_activity_download_main

    override fun initView() {
        super.initView()

        mPagerAdapter = DownloadDetailPagerAdapter(
            fm = this.supportFragmentManager,
            tabList = tabList,
            fragmentList = mListTabFragment
        )
        download_detail_view_pager.offscreenPageLimit = 2
        download_detail_view_pager.adapter = mPagerAdapter

        download_main_tab.setupWithViewPager(download_detail_view_pager)
        download_main_tab.addOnTabSelectedListener(object : BendTabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: BendTabLayout.BendTab?) {
            }

            override fun onTabUnselected(tab: BendTabLayout.BendTab?) {
            }

            override fun onTabSelected(tab: BendTabLayout.BendTab?) {
                if (tab?.position == 0) {
                    mViewModel.downloadingSelected.set(true)
                    mViewModel.downloadFinishSelected.set(false)
                } else {
                    mViewModel.downloadingSelected.set(false)
                    mViewModel.downloadFinishSelected.set(true)
                }
            }
        })
        download_iv_back.setOnClickListener {
            finish()
        }
        download_detail_view_pager.setCurrentItem(startTab, false)
        mViewModel.downloadingSelected.set(startTab == 0)
        mViewModel.downloadingEdit.set(false)
        mViewModel.downloadingSelectAll.set(false)
        mViewModel.downloadingSelectNum.set(0)

        mViewModel.downloadFinishSelected.set(startTab == 1)
        mViewModel.downloadFinishEdit.set(false)
        mViewModel.downloadFinishSelectAll.set(false)
        mViewModel.downloadFinishSelectNum.set(0)

        DownloadMemoryCache.initData()

    }

    override fun onDestroy() {
        super.onDestroy()
        needShowNetErrorChangeCallback?.let {
            AriaDownloadManager.needShowNetError.removeOnPropertyChangedCallback(it)
            needShowNetErrorChangeCallback = null
        }

        isAvailableChangeCallback?.let {
            NetworkChangeReceiver.isAvailable.removeOnPropertyChangedCallback(it)
            isAvailableChangeCallback = null
        }

    }
}