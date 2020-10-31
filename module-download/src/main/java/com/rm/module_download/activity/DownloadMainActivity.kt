package com.rm.module_download.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.mvvm.BaseVMActivity
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


    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, DownloadMainActivity::class.java))
        }
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

    }

    override fun initData() {
    }

    override fun getLayoutId(): Int = R.layout.download_activity_download_main

    override fun initView() {
        super.initView()

        mPagerAdapter = DownloadDetailPagerAdapter(fm = this.supportFragmentManager,tabList = tabList, fragmentList = mListTabFragment)
        download_detail_view_pager.offscreenPageLimit = 2
        download_detail_view_pager.adapter = mPagerAdapter

        download_main_tab.setupWithViewPager(download_detail_view_pager)
        download_main_tab.addOnTabSelectedListener(object : BendTabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: BendTabLayout.BendTab?) {
            }

            override fun onTabUnselected(tab: BendTabLayout.BendTab?) {
            }

            override fun onTabSelected(tab: BendTabLayout.BendTab?) {
                if(tab?.position == 0){
                    mViewModel.downloadingSelected.set(true)
                    mViewModel.downloadFinishSelected.set(false)
                }else{
                    mViewModel.downloadingSelected.set(false)
                    mViewModel.downloadFinishSelected.set(true)
                }
            }
        })
        download_iv_back.setOnClickListener {
            finish()
        }
        download_detail_view_pager.setCurrentItem(0,false)
        mViewModel.downloadingEdit.set(false)
        mViewModel.downloadFinishEdit.set(false)
        mViewModel.downloadingSelected.set(true)
        mViewModel.downloadingSelectNum.set(0)
        mViewModel.downloadFinishSelectNum.set(0)

        DownloadMemoryCache.initDownOrPauseAll()

    }
}