package com.rm.module_download.fragment

import android.view.View
import androidx.lifecycle.Observer
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.business_lib.download.DownloadMemoryCache
import com.rm.module_download.BR
import com.rm.module_download.R
import com.rm.module_download.databinding.DownloadFragmentDownloadCompletedBinding
import com.rm.module_download.viewmodel.DownloadMainViewModel
import kotlinx.android.synthetic.main.download_fragment_download_completed.*

class DownloadCompletedFragment : BaseVMFragment<DownloadFragmentDownloadCompletedBinding, DownloadMainViewModel>() {


    companion object {
        fun newInstance(): DownloadCompletedFragment {
            return DownloadCompletedFragment()
        }
    }


    override fun initModelBrId(): Int = BR.viewModel

    override fun startObserve() {
        mViewModel.downloadAudioList.observe(this, Observer {
            if(it.size>0){
                showData()
                it.reverse()
                mViewModel.downloadFinishAdapter.setList(it)
            }else{
                mViewModel.downloadFinishAdapter.data.clear()
                showEmpty()
            }
        })
    }

    override fun initLayoutId(): Int = R.layout.download_fragment_download_completed

    override fun initData() {
        if (DownloadMemoryCache.downloadingAudioList.value == null) {
            mViewModel.downloadFinishAdapter.data.clear()
            showEmpty()
        }
        mViewModel.getDownloadFromDao()
    }

    fun showData(){
        downFinishContent.visibility = View.VISIBLE
        downFinishEmpty.visibility = View.GONE
    }

    fun showEmpty(){
        downFinishContent.visibility = View.GONE
        downFinishEmpty.visibility = View.VISIBLE
    }

}