package com.rm.module_download.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.download.DownloadAudioBean
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.component_comm.download.DownloadService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_download.repository.DownloadRepository

class DownloadMainViewModel(private val repository: DownloadRepository) : BaseVMViewModel() {

    private val downloadService by lazy { RouterHelper.createRouter(DownloadService::class.java) }

    var downloadAudioList = MutableLiveData<MutableList<DownloadAudio>>()

    var isEdit=ObservableField<Boolean>(false)

    fun getDownloadAudioList(): MutableList<DownloadAudioBean> {
       return downloadService.getDownloadAudioList()
    }


    fun getDownloadFromDao(){
        launchOnIO {
            val allData = DaoUtil(DownloadAudio::class.java, "").queryAll()
            DLog.d("suolong","${allData?.size}")
            downloadAudioList.postValue(allData?.toMutableList())
        }
    }

}