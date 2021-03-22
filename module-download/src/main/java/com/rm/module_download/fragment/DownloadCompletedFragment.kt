package com.rm.module_download.fragment

import android.view.View
import androidx.lifecycle.Observer
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadDaoUtils
import com.rm.business_lib.download.DownloadMemoryCache
import com.rm.module_download.BR
import com.rm.module_download.R
import com.rm.module_download.binding.bindDownFinishAudioSize
import com.rm.module_download.databinding.DownloadFragmentDownloadCompletedBinding
import com.rm.module_download.viewmodel.DownloadMainViewModel
import kotlinx.android.synthetic.main.download_fragment_download_completed.*

class DownloadCompletedFragment : BaseVMFragment<DownloadFragmentDownloadCompletedBinding, DownloadMainViewModel>() {


    companion object {
        fun newInstance(): DownloadCompletedFragment {
            return DownloadCompletedFragment()
        }
    }

    private var totalAudioSize = 0L

    override fun initModelBrId(): Int = BR.viewModel

    override fun startObserve() {
        DownloadMemoryCache.downloadingAudioList.observe(this, Observer {
            totalAudioSize = 0L
            val realFinishList = mutableListOf<DownloadAudio>()
            it.forEach { downloadAudio ->
                DownloadDaoUtils.queryAllFinishChapterWithAudioId(downloadAudio)
                if(downloadAudio.download_num>0){
                    realFinishList.add(downloadAudio)
                    totalAudioSize += downloadAudio.down_size
                }
            }
            if(realFinishList.size>0){
                realFinishList.sortWith(Comparator { o1, o2 ->
                    return@Comparator if (o1.updateMillis < o2.updateMillis) {
                        1
                    } else {
                        -1
                    }
                })
                showData()
                mViewModel.downloadFinishAdapter.setList(realFinishList)
                down_finish_list_space.bindDownFinishAudioSize(totalAudioSize)
                down_finish_list_size.text = String.format(getString(R.string.download_downloaded_number),realFinishList.size)
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

    private fun showData(){
        downFinishContent.visibility = View.VISIBLE
        downFinishEmpty.visibility = View.GONE
    }

    private fun showEmpty(){
        mViewModel.downloadFinishEdit.set(false)
        downFinishContent.visibility = View.GONE
        downFinishEmpty.visibility = View.VISIBLE
    }

}