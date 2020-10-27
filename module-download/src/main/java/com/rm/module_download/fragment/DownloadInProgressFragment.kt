package com.rm.module_download.fragment

import androidx.lifecycle.Observer
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.module_download.BR
import com.rm.module_download.DownloadMemoryCache
import com.rm.module_download.R
import com.rm.module_download.databinding.DownloadFragmentInProgressBinding
import com.rm.module_download.viewmodel.DownloadMainViewModel

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
            mViewModel.downloadingAdapter.setList(it)
        })
    }

    override fun initLayoutId(): Int = R.layout.download_fragment_in_progress

    override fun initData() {
        DownloadMemoryCache.downloadingChapterList.value?.let {
            mViewModel.downloadingAdapter.setList(it)
        }
    }

}