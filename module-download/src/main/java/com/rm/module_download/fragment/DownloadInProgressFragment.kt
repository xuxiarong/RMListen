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
                downloading_list_size.text = String.format(getString(R.string.download_downloading_number),it.size)
            } else {
                mViewModel.downloadingAdapter.data.clear()
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
                    downloading_list_size.text = String.format(getString(R.string.download_downloading_number),it.size)
                    mViewModel.downloadingAdapter.setList(it)
                } else {
                    mViewModel.downloadingAdapter.data.clear()
                    showEmpty()
                }
            }
        }
    }

    private fun showData(){
        downloadEmpty.visibility = View.GONE
        downloadContent.visibility = View.VISIBLE
    }

    private fun showEmpty(){
        downloadEmpty.visibility = View.VISIBLE
        downloadContent.visibility = View.GONE
    }

}