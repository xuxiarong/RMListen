package com.rm.module_download.fragment

import androidx.lifecycle.Observer
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.business_lib.db.download.DownloadAudio
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

    private val mAdapter by lazy {
        CommonBindVMAdapter<DownloadAudio>(
            mViewModel,
            mutableListOf(),
            R.layout.download_item_download_completed,
            BR.viewModel,
            BR.itemBean
        )
    }

    override fun initModelBrId(): Int = BR.viewModel

    override fun startObserve() {
        mViewModel.downloadAudioList.observe(this, Observer {
            mAdapter.setList(it)
        })
    }

    override fun initLayoutId(): Int = R.layout.download_fragment_download_completed

    override fun initData() {
        mViewModel.getDownloadFromDao()
    }

    override fun initView() {
        super.initView()
        download_rv_download_completed.apply {
            bindVerticalLayout(mAdapter)
        }
    }
}