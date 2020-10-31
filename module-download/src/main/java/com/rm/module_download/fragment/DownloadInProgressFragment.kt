package com.rm.module_download.fragment

import android.view.View
import androidx.lifecycle.Observer
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.business_lib.download.DownloadMemoryCache
import com.rm.module_download.BR
import com.rm.module_download.R
import com.rm.module_download.databinding.DownloadFragmentInProgressBinding
import com.rm.module_download.viewmodel.DownloadMainViewModel
import kotlinx.android.synthetic.main.download_fragment_in_progress.*

class DownloadInProgressFragment :
    BaseVMFragment<DownloadFragmentInProgressBinding, DownloadMainViewModel>() {

    companion object {
        fun newInstance(): DownloadInProgressFragment {
            return DownloadInProgressFragment()
        }
    }

    override fun initModelBrId(): Int = BR.viewModel

    override fun startObserve() {
        DownloadMemoryCache.downloadingChapterList.observe(this, Observer {
            if (it.size > 0) {
                showData()
                mViewModel.downloadingAdapter.setList(it)
            } else {
                showEmpty()
            }
        })
    }

    override fun initLayoutId(): Int = R.layout.download_fragment_in_progress

    override fun initData() {

        if (DownloadMemoryCache.downloadingChapterList.value == null) {
            showEmpty()
        } else {
            DownloadMemoryCache.downloadingChapterList.value?.let {
                if (it.size > 0) {
                    showData()
                    mViewModel.downloadingAdapter.setList(it)
                } else {
                    showEmpty()
                }
            }
        }
    }

    fun showData(){
        downloadEmpty.visibility = View.GONE
        downloadContent.visibility = View.VISIBLE
    }

    fun showEmpty(){
        downloadEmpty.visibility = View.VISIBLE
        downloadContent.visibility = View.GONE
    }

}