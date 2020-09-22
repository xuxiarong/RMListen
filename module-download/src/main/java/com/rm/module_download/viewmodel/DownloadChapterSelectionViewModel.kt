package com.rm.module_download.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_download.bean.DownloadChapterItemBean
import com.rm.module_download.bean.DownloadChapterResponseBean
import com.rm.module_download.repository.DownloadRepository

class DownloadChapterSelectionViewModel(private val repository: DownloadRepository) : BaseVMViewModel() {
    val chapterListData = MutableLiveData<DownloadChapterResponseBean>()
    val isEdit=ObservableField<Boolean>()


    fun itemClickFun(bean: DownloadChapterItemBean) {


    }

    fun getDownloadChapterList(audioId:String) {
        launchOnIO {
            repository.getDownloadChapterList(page = 1,audioId = audioId).checkResult(
                onSuccess = {
                    chapterListData.value=it
                },
                onError = {
                    DLog.i("download", "$it")
                }
            )
        }
    }

    fun downloadChapterSelection(audioId:String,sequences:String) {
        launchOnIO {
            repository.downloadChapterSelection(audioId = audioId,sequences = sequences).checkResult(
                onSuccess = {

                },
                onError = {
                    DLog.i("download", "$it")
                }
            )
        }
    }

}