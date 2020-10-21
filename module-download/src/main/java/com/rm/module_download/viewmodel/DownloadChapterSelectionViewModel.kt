package com.rm.module_download.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.ktx.addAll
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.DownloadConstant
import com.rm.business_lib.DownloadMemoryCache
import com.rm.business_lib.bean.download.DownloadChapterStatusModel
import com.rm.business_lib.bean.download.DownloadUIStatus
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.component_comm.download.DownloadService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_download.BR
import com.rm.module_download.R
import com.rm.module_download.bean.DownloadChapterAdapterBean
import com.rm.module_download.bean.DownloadChapterUIStatus
import com.rm.module_download.file.DownLoadFileUtils
import com.rm.module_download.repository.DownloadRepository

class DownloadChapterSelectionViewModel(private val repository: DownloadRepository) : BaseVMViewModel() {
    val data = MutableLiveData<MutableList<DownloadChapterAdapterBean>>()
    var page = 1
    private val pageSize = 12
    private var total = 0
    private val downloadService by lazy { RouterHelper.createRouter(DownloadService::class.java) }
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

    fun downloadList() {
        if(audioChapterList.value!=null){
            val tempDownloadList = mutableListOf<DownloadChapterStatusModel>()
            //筛选已选择的章节
            audioChapterList.value!!.forEach {
                if(it.isSelect()){
                    tempDownloadList.add(it)
                }
            }
            //将音频信息存储
            downloadAudio.get()?.let { DownloadMemoryCache.addAudioToDownloadMemoryCache(it) }
            //存储已选择的下载章节
            DownloadMemoryCache.downloadingChapterList.addAll(tempDownloadList)
            //调用下载服务开始下载
            downloadService.startDownloadWithCache(tempDownloadList)
        }
    }


    fun itemClickFun(item: DownloadChapterStatusModel) {
        if(item.downStatus != DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD){
            return
        }

        when (item.select) {
            DownloadConstant.CHAPTER_UN_SELECT -> {
                item.select =  DownloadConstant.CHAPTER_SELECTED
                selectChapterNum.set(selectChapterNum.get()?.plus(1))
                selectChapterSize.set(selectChapterSize.get()?.plus(item.chapter.size))
            }
            DownloadConstant.CHAPTER_SELECTED -> {
                item.select = DownloadConstant.CHAPTER_UN_SELECT
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
                if(it.downStatus == DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD){
                    if(!selectAll && it.select == DownloadConstant.CHAPTER_UN_SELECT){
                        it.select = DownloadConstant.CHAPTER_SELECTED
                        selectChapterNum.set(selectChapterNum.get()?.plus(1))
                        selectChapterSize.set(selectChapterSize.get()?.plus(it.chapter.size))
                    }else if(selectAll && it.select == DownloadConstant.CHAPTER_SELECTED){
                        it.select = DownloadConstant.CHAPTER_UN_SELECT
                        selectChapterNum.set(selectChapterNum.get()?.plus(-1))
                        selectChapterSize.set(selectChapterSize.get()?.plus(-it.chapter.size))
                    }
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
                DownLoadFileUtils.checkChapterIsDownload(
                    DownloadChapterStatusModel(
                        chapter = it
                    )
                )
            )
        }
        audioChapterList.addAll(chapterStatusList)
    }

    fun downloadChapterSelection(audioId: Long, sequences: List<Int>) {
//        launchOnIO {
//            repository.downloadChapterSelection(audioId = audioId, sequences = sequences).checkResult(
//                onSuccess = {
//
//                    downloadService.startDownloadWithCache(downloadList.toMutableList())
//                },
//                onError = {
//                    DLog.i("download", "$it")
//                    showToast("$it")
//                }
//            )
//        }
    }

}