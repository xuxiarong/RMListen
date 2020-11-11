package com.rm.module_download.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ObservableLong
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.ktx.addAll
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.download.DownloadConstant
import com.rm.business_lib.bean.download.DownloadUIStatus
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.module_download.BR
import com.rm.business_lib.download.DownloadMemoryCache
import com.rm.module_download.R
import com.rm.module_download.bean.DownloadChapterAdapterBean
import com.rm.module_download.bean.DownloadChapterUIStatus
import com.rm.module_download.repository.DownloadRepository

class DownloadChapterSelectionViewModel(private val repository: DownloadRepository) :
    BaseVMViewModel() {
    val data = MutableLiveData<MutableList<DownloadChapterAdapterBean>>()
    var page = 1
    private val pageSize = 12
    private var total = 0
    var isSelectAll = ObservableBoolean(false)
    var selectChapterNum = ObservableInt(0)
    var selectChapterSize = ObservableLong(0L)

    var downloadAudio = ObservableField<DownloadAudio>()
    val audioChapterList = MutableLiveData<MutableList<DownloadChapter>>()

    val mAdapter by lazy {
        CommonBindVMAdapter<DownloadChapter>(
            this,
            mutableListOf(),
            R.layout.download_item_chapter_selection,
            BR.viewModel,
            BR.itemBean
        )
    }

    fun downloadList() {
        if (audioChapterList.value != null) {
            val tempDownloadList = mutableListOf<DownloadChapter>()
            //筛选已选择的章节
            audioChapterList.value!!.forEach {
                if (it.chapter_edit_select && it.down_status == DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD) {
                    it.down_status = DownloadConstant.CHAPTER_STATUS_DOWNLOAD_WAIT
                    tempDownloadList.add(it)
                }
            }
            if (tempDownloadList.size == 0) {
                ToastUtil.show(
                    BaseApplication.CONTEXT,
                    BaseApplication.CONTEXT.getString(com.rm.business_lib.R.string.business_download_all_exist)
                )
                return
            }

            //将音频信息存储
            downloadAudio.get()?.let {
                DownloadMemoryCache.addAudioToDownloadMemoryCache(
                    it
                )
            }
            //存储已选择的下载章节
            DownloadMemoryCache.addDownloadingChapter(tempDownloadList)
            //调用下载服务开始下载
            mAdapter.notifyDataSetChanged()
        }
    }


    fun itemClickFun(item: DownloadChapter) {
        if (item.down_status != DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD) {
            return
        }
        if (item.chapter_edit_select) {
            selectChapterNum.set(selectChapterNum.get() - 1)
            selectChapterSize.set(selectChapterSize.get() - item.size)
        } else {
            selectChapterNum.set(selectChapterNum.get() + 1)
            selectChapterSize.set(selectChapterSize.get() + item.size)
        }
        item.chapter_edit_select = item.chapter_edit_select.not()
        mAdapter.notifyItemChanged(mAdapter.data.indexOf(item))
    }

    fun changeAllChapterSelect() {
        val selectAll = isSelectAll.get().not()
        isSelectAll.set(selectAll)
        mAdapter.data.forEach {
            if (it.down_status == DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD) {
                if (!selectAll && it.chapter_edit_select) {
                    selectChapterNum.set(selectChapterNum.get() - 1)
                    selectChapterSize.set(selectChapterSize.get() - it.size)
                    it.chapter_edit_select = it.chapter_edit_select.not()
                } else if (selectAll && !it.chapter_edit_select) {
                    selectChapterNum.set(selectChapterNum.get() + 1)
                    selectChapterSize.set(selectChapterSize.get() + it.size)
                    it.chapter_edit_select = it.chapter_edit_select.not()
                }
            }
        }
        mAdapter.notifyDataSetChanged()

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
            repository.downloadChapterList(page = page, pageSize = pageSize, audioId = audioId)
                .checkResult(
                    onSuccess = {
                        page++
                        total = it.total
                        it.list?.let { list ->
                            audioChapterList.addAll(getChapterStatus(list))
                        }
                    },
                    onError = {
                        showTip("$it",R.color.business_color_ff5e5e)
                        DLog.e("download", "$it")
                    }
                )
        }
    }

    private fun getChapterStatus(chapterList: List<DownloadChapter>): MutableList<DownloadChapter> {
        val audioName = downloadAudio.get()?.audio_name
        val audioId = downloadAudio.get()?.audio_id
        chapterList.forEach {
            it.audio_name = audioName
            it.audio_id = audioId
//            DownLoadFileUtils.checkChapterIsDownload(chapter = it)
        }
        return chapterList.toMutableList()
    }

    fun downloadChapterSelection(audioId: Long, sequences: List<Int>) {
        launchOnIO {
            repository.downloadChapterSelection(audioId = audioId, sequences = sequences)
                .checkResult(
                    onSuccess = {
                        it.list?.let { list ->
                            val chapterStatusList = getChapterStatus(list)
                            DownloadMemoryCache.addDownloadingChapter(chapterStatusList)
                        }
//                        audioChapterList.addAll(chapterStatusList)
                    },
                    onError = {
                        DLog.i("download", "$it")
                        showTip("$it",R.color.business_color_ff5e5e)
                    }
                )
        }
    }

}