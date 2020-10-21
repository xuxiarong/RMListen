package com.rm.module_download.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.download.DownloadChapterStatusModel
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.component_comm.download.DownloadService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_download.BR
import com.rm.module_download.R
import com.rm.module_download.repository.DownloadRepository

class DownloadMainViewModel(private val repository: DownloadRepository) : BaseVMViewModel() {

    private val downloadService by lazy { RouterHelper.createRouter(DownloadService::class.java) }

    val downloadingAdapter by lazy {
        CommonBindVMAdapter<DownloadChapterStatusModel>(
            this,
            mutableListOf(),
            R.layout.download_item_in_progress,
            BR.viewModel,
            BR.itemBean
        )
    }

    val downloadFinishAdapter by lazy {
        CommonBindVMAdapter<DownloadAudio>(
            this,
            mutableListOf(),
            R.layout.download_item_download_completed,
            BR.viewModel,
            BR.itemBean
        )
    }

     var downloadingSelected = ObservableBoolean(false)
     var downloadingEdit = ObservableBoolean(false)
     var downloadFinishSelected = ObservableBoolean(false)
     var downloadFinishEdit = ObservableBoolean(false)



    var downloadAudioList = MutableLiveData<MutableList<DownloadAudio>>()

    var isEdit=ObservableField<Boolean>(false)

//    fun getDownloadAudioList(): MutableList<DownloadAudioBean> {
//       return downloadService.getDownloadAudioList()
//    }


    fun getDownloadFromDao(){
        launchOnIO {
            val allData = DaoUtil(DownloadAudio::class.java, "").queryAll()
            DLog.d("suolong","${allData?.size}")
            downloadAudioList.postValue(allData?.toMutableList())
        }
    }

    fun editDownloading(){
        downloadingEdit.set(downloadingEdit.get().not())
    }

    fun editDownloadFinish(){
        downloadFinishEdit.set(downloadFinishEdit.get().not())
    }
}