package com.rm.module_download.fragment

import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.business_lib.bean.download.DownloadAudioBean
import com.rm.module_download.BR
import com.rm.module_download.R
import com.rm.module_download.databinding.DownloadFragmentInProgressBinding
import com.rm.module_download.viewmodel.DownloadMainViewModel
import kotlinx.android.synthetic.main.download_fragment_in_progress.*

class DownloadInProgressFragment : BaseVMFragment<DownloadFragmentInProgressBinding, DownloadMainViewModel>() {



    companion object {
        fun newInstance(): DownloadInProgressFragment {
            return DownloadInProgressFragment()
        }
    }

    private val mAdapter by lazy {
        CommonBindVMAdapter<DownloadAudioBean>(mViewModel, mutableListOf(), R.layout.download_item_in_progress, BR.viewModel, BR.itemBean)
    }

    override fun initModelBrId(): Int = BR.viewModel

    override fun startObserve() {

    }

    override fun initLayoutId(): Int = R.layout.download_fragment_in_progress

    override fun initData() {
        mAdapter.setList(mViewModel.getDownloadAudioList())
        mViewModel.getDownloadFromDao()
    }

    override fun initView() {
        super.initView()
        download_rv_download_in_progress.apply {
            bindVerticalLayout(mAdapter)
        }
    }


}