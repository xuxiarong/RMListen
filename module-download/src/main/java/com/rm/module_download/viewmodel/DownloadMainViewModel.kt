package com.rm.module_download.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_download.bean.DownloadChapterItemBean
import com.rm.module_download.repository.DownloadRepository

class DownloadMainViewModel(private val repository: DownloadRepository) : BaseVMViewModel() {
    val data = MutableLiveData<MutableList<DownloadChapterItemBean>>()

    private val pageSize = 12

    private var page = 1


}