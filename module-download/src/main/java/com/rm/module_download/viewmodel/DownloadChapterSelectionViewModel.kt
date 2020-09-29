package com.rm.module_download.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.download.BaseDownloadFileBean
import com.rm.business_lib.bean.download.DownloadAudioBean
import com.rm.business_lib.bean.download.DownloadFileBean
import com.rm.business_lib.bean.download.DownloadUIStatus
import com.rm.component_comm.download.DownloadService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_download.BR
import com.rm.module_download.R
import com.rm.module_download.bean.DownloadChapterAdapterBean
import com.rm.module_download.bean.DownloadChapterItemBean
import com.rm.module_download.bean.DownloadChapterUIStatus
import com.rm.module_download.repository.DownloadRepository

class DownloadChapterSelectionViewModel(private val repository: DownloadRepository) : BaseVMViewModel() {
    val data = MutableLiveData<MutableList<DownloadChapterAdapterBean>>()
    var page = 1
    private val pageSize = 12
    private var total = 0
    private val downloadService by lazy { RouterHelper.createRouter(DownloadService::class.java) }
    val itemChange = MutableLiveData<DownloadChapterAdapterBean>()
    var isCheckAll = ObservableField<Boolean>(false)


    fun downloadItem(item: DownloadChapterAdapterBean) {
        item.downloadChapterItemBean.apply {
            var downloadAudioBean =
                DownloadAudioBean(audioUrl = path_url, bookId = audio_id, audioName = chapter_name, bookName = chapter_name, fileSize = size)
            downloadService.startDownloadWithCache(downloadAudioBean)
        }
    }

    fun downloadList(list: List<DownloadChapterAdapterBean>) {
        var audioList = mutableListOf<DownloadAudioBean>()
        list.forEach {
            it.downloadChapterItemBean.run {
                var audioBean =
                    DownloadAudioBean(audioUrl = path_url, bookId = audio_id, audioName = chapter_name, bookName = chapter_name, fileSize = size)
                audioList.add(audioBean)
            }
        }
        downloadService.startDownloadWithCache(audioList)
    }

    fun clearList(list: List<DownloadChapterAdapterBean>) {
        var fileBeanList = mutableListOf<BaseDownloadFileBean>()
        list.forEach {
            it.downloadChapterItemBean.run {
                var bean = DownloadFileBean(path_url, chapter_name)
                fileBeanList.add(bean)
            }
        }
        if (fileBeanList.size > 0) {
            downloadService.deleteDownload(fileBeanList)
        }
    }

    fun clearAll() {
        downloadService.deleteAll()
    }

    fun checkAllClickFun() {
        isCheckAll.set(isCheckAll.get()?.not())
    }

    fun itemClickFun(item: DownloadChapterAdapterBean) {
        when (item.downloadChapterUIStatus) {
            DownloadChapterUIStatus.COMPLETED, DownloadChapterUIStatus.COMPLETED -> return
            DownloadChapterUIStatus.CHECKED -> item.downloadChapterUIStatus = DownloadChapterUIStatus.UNCHECK
            DownloadChapterUIStatus.UNCHECK -> item.downloadChapterUIStatus = DownloadChapterUIStatus.CHECKED
        }
        itemChange.value = item
    }

    fun hasMore(): Boolean {
        return (page * pageSize) < total
    }


    private fun wrapChapterList(chapterLis: MutableList<DownloadChapterItemBean>): MutableList<DownloadChapterAdapterBean> {
        var wrappedList = mutableListOf<DownloadChapterAdapterBean>()
        chapterLis.forEach {
            var downloadFileStatus = downloadService.getDownloadStatus(DownloadFileBean(it.path_url, it.chapter_name))
            wrappedList.add(DownloadChapterAdapterBean(it, convertDownloadStatus(downloadFileStatus)))
        }
        return wrappedList
    }


    fun convertDownloadStatus(downloadUIStatus: DownloadUIStatus): DownloadChapterUIStatus {
        return when (downloadUIStatus) {
            DownloadUIStatus.DOWNLOAD_PAUSED,
            DownloadUIStatus.DOWNLOAD_PENDING,
            DownloadUIStatus.DOWNLOAD_IN_PROGRESS -> DownloadChapterUIStatus.DOWNLOADING
            DownloadUIStatus.DOWNLOAD_COMPLETED -> DownloadChapterUIStatus.COMPLETED
            else -> DownloadChapterUIStatus.UNCHECK
        }
    }

    fun getDownloadChapterList(audioId: String) {
        launchOnIO {
            repository.getDownloadChapterList(page = page, pageSize = pageSize, audioId = audioId).checkResult(
                onSuccess = {
                    page++
                    total = it.total
                    data.value = wrapChapterList(it.list)
                },
                onError = {
                    showToast("$it")
                    DLog.e("download", "$it")
                }
            )
        }
    }

    fun downloadChapterSelection(audioId: String, sequences: List<Int>) {
        launchOnIO {
            repository.downloadChapterSelection(audioId = audioId, sequences = sequences).checkResult(
                onSuccess = {
                    var downloadList = it.list.map { chapterBean ->
                        DownloadAudioBean(
                            audioUrl = chapterBean.path_url,
                            bookId = chapterBean.audio_id,
                            audioName = chapterBean.chapter_name,
                            bookName = chapterBean.chapter_name,
                            fileSize = chapterBean.size
                        )
                    }
                    downloadService.startDownloadWithCache(downloadList.toMutableList())
                },
                onError = {
                    DLog.i("download", "$it")
                    showToast("$it")
                }
            )
        }
    }

}