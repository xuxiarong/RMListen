package com.rm.module_download.fragment

import androidx.lifecycle.Observer
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.util.DLog
import com.rm.module_download.BR
import com.rm.module_download.R
import com.rm.module_download.databinding.DownloadFragmentDownloadCompletedBinding
import com.rm.module_download.viewmodel.DownloadMainViewModel

class DownloadCompletedFragment : BaseVMFragment<DownloadFragmentDownloadCompletedBinding, DownloadMainViewModel>() {


    companion object {
        fun newInstance(): DownloadCompletedFragment {
            return DownloadCompletedFragment()
        }
    }


    override fun initModelBrId(): Int = BR.viewModel

    override fun startObserve() {
        mViewModel.downloadAudioList.observe(this, Observer {
            DLog.d("suolong","size = ${it.size}")
            mViewModel.downloadFinishAdapter.setList(it)
        })
    }

    override fun initLayoutId(): Int = R.layout.download_fragment_download_completed

    override fun initData() {
        mViewModel.getDownloadFromDao()
    }

}