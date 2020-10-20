package com.rm.module_download.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.ktx.addValue
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.download.BaseDownloadFileBean
import com.rm.business_lib.bean.download.DownloadAudioBean
import com.rm.business_lib.bean.download.DownloadFileBean
import com.rm.business_lib.bean.download.DownloadUIStatus
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.component_comm.download.DownloadService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_download.BR
import com.rm.module_download.DownloadConstant
import com.rm.module_download.R
import com.rm.module_download.bean.DownloadChapterAdapterBean
import com.rm.module_download.bean.DownloadChapterItemBean
import com.rm.module_download.bean.DownloadChapterUIStatus
import com.rm.module_download.file.DownLoadFileUtils
import com.rm.module_download.model.DownloadChapterStatusModel
import com.rm.module_download.repository.DownloadRepository

class DownloadChapterSelectionViewModel(private val repository: DownloadRepository) : BaseVMViewModel() {
    val data = MutableLiveData<MutableList<DownloadChapterAdapterBean>>()
    var page = 1
    private val pageSize = 12
    private var total = 0
    private val downloadService by lazy { RouterHelper.createRouter(DownloadService::class.java) }
    val itemChange = MutableLiveData<DownloadChapterAdapterBean>()
    var isSelectAll = ObservableField<Boolean>(false)
    var selectChapterNum = ObservableField<Int>(0)
    var selectChapterSize = ObservableField<Long>(0L)

    var downloadAudio = ObservableField<DownloadAudio>()
    val audioChapterList = MutableLiveData<MutableList<DownloadChapterStatusModel>>()

     val mAdapter by lazy {
        CommonBindVMAdapter<DownloadChapterStatusModel>(
            this,
            mutableListOf(),
            R.layout.download_item_chapter_selection,
            BR.viewModel,
            BR.itemBean
        )
    }


    fun downloadList(list: List<DownloadChapterAdapterBean>) {
        var audioList = mutableListOf<DownloadAudioBean>()
        list.forEach {
            it.downloadChapterItemBean.run {
                var audioBean =
                    DownloadAudioBean(audioUrl = path_url, bookId = audio_id, audioName = chapter_name, bookName = chapter_name, fileSize = size,chapter_id = chapter_id.toString())
                audioList.add(audioBean)
            }
        }
        downloadService.startDownloadWithCache(audioList)
    }

    fun clearList(list: List<DownloadChapterAdapterBean>) {
        var fileBeanList = mutableListOf<BaseDownloadFileBean>()
        list.forEach {
            it.downloadChapterItemBean.run {
                var bean = DownloadFileBean(pathUrl = path_url, chapterName = chapter_name,audioName = audio_id)
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

//    fun checkAllClickFun() {
//        isSelectAll.set(isSelectAll.get()?.not())
//    }

    fun itemClickFun(item: DownloadChapterStatusModel) {
        when (item.status) {
            DownloadConstant.CHAPTER_STATUS_DOWNLOAD_FINISH -> return
            DownloadConstant.CHAPTER_STATUS_UN_SELECT -> {
                item.status =  DownloadConstant.CHAPTER_STATUS_SELECTED
                selectChapterNum.set(selectChapterNum.get()?.plus(1))
                selectChapterSize.set(selectChapterSize.get()?.plus(item.chapter.size))
            }
            DownloadConstant.CHAPTER_STATUS_SELECTED -> {
                item.status = DownloadConstant.CHAPTER_STATUS_UN_SELECT
                selectChapterNum.set(selectChapterNum.get()?.plus(-1))
                selectChapterSize.set(selectChapterSize.get()?.plus(-item.chapter.size))
            }
        }
        mAdapter.notifyItemChanged(mAdapter.data.indexOf(item))
    }

    fun changeAllChapterSelect(){
        val selectAll = isSelectAll.get()
        if(selectAll!=null){
            mAdapter.data.forEach {
                if(!selectAll && it.status == DownloadConstant.CHAPTER_STATUS_UN_SELECT){
                    it.status = DownloadConstant.CHAPTER_STATUS_SELECTED
                    selectChapterNum.set(selectChapterNum.get()?.plus(1))
                    selectChapterSize.set(selectChapterSize.get()?.plus(it.chapter.size))
                }else if(selectAll && it.status == DownloadConstant.CHAPTER_STATUS_SELECTED){
                    it.status = DownloadConstant.CHAPTER_STATUS_UN_SELECT
                    selectChapterNum.set(selectChapterNum.get()?.plus(-1))
                    selectChapterSize.set(selectChapterSize.get()?.plus(-it.chapter.size))
                }
            }
            isSelectAll.set(selectAll.not())
            mAdapter.notifyDataSetChanged()
        }else{
            isSelectAll.set(false)
        }
    }

    fun hasMore(): Boolean {
        return (page * pageSize) < total
    }


    private fun wrapChapterList(chapterLis: MutableList<DownloadChapterItemBean>): MutableList<DownloadChapterAdapterBean> {
        var wrappedList = mutableListOf<DownloadChapterAdapterBean>()
        chapterLis.forEach {
            var downloadFileStatus = downloadService.getDownloadStatus(DownloadFileBean(it.path_url, it.chapter_name,it.audio_id))
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

    fun getDownloadChapterList(audioId: Long) {
        launchOnIO {
            repository.downloadChapterList(page = page, pageSize = pageSize, audioId = audioId).checkResult(
                onSuccess = {
                    page++
                    total = it.total
                    getChapterStatus(it.list)
                },
                onError = {
                    showToast("$it")
                    DLog.e("download", "$it")
                }
            )
        }
    }

    fun getChapterStatus(chapterList : List<DownloadChapter>){
        val chapterStatusList = mutableListOf<DownloadChapterStatusModel>()
        chapterList.forEach {
            chapterStatusList.add(
                DownLoadFileUtils.checkChapterIsDownload(DownloadChapterStatusModel(chapter = it ))
            )
        }
        audioChapterList.addValue(chapterStatusList)
    }

    fun downloadChapterSelection(audioId: Long, sequences: List<Int>) {
        launchOnIO {
            repository.downloadChapterSelection(audioId = audioId, sequences = sequences).checkResult(
                onSuccess = {
                    var downloadList = it.list.map { chapterBean ->
                        DownloadAudioBean(
                            audioUrl = chapterBean.path_url,
                            bookId = chapterBean.audio_id,
                            audioName = chapterBean.chapter_name,
                            bookName = chapterBean.chapter_name,
                            fileSize = chapterBean.size,
                            chapter_id = chapterBean.chapter_id.toString()
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