package com.rm.module_download.viewmodel

import android.content.Context
import android.view.View
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
import com.rm.business_lib.download.file.DownLoadFileUtils
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
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
    val refreshModel = SmartRefreshLayoutStatusModel()


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
            isSelectAll.set(false)
            selectChapterNum.set(selectChapterNum.get() - 1)
            selectChapterSize.set(selectChapterSize.get() - item.size)
        } else {
            selectChapterNum.set(selectChapterNum.get() + 1)
            selectChapterSize.set(selectChapterSize.get() + item.size)
        }
        item.chapter_edit_select = item.chapter_edit_select.not()
        mAdapter.notifyItemChanged(mAdapter.data.indexOf(item))
        mAdapter.data.forEach {
            if (!it.chapter_edit_select) {
                return
            }
        }
        isSelectAll.set(true)
    }

    fun changeAllChapterSelect() {
        val selectAll = isSelectAll.get().not()
        isSelectAll.set(selectAll)
        var selectNum = 0
        var selectSize = 0L
        mAdapter.data.forEach {
            if (it.down_status == DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD) {
                if (selectAll) {
                    selectNum++
                    selectSize+=it.size
                    it.chapter_edit_select = true
                } else {
                    it.chapter_edit_select = false
                }
            }
        }
        selectChapterNum.set(selectNum)
        selectChapterSize.set(selectSize)
        mAdapter.notifyDataSetChanged()
    }

    fun getDownloadChapterList(context: Context) {
        downloadAudio.get()?.audio_id?.let {
            launchOnIO {
                repository.downloadChapterList(page = page, pageSize = pageSize, audioId = it)
                    .checkResult(
                        onSuccess = {
                            page++
                            total = it.total
                            refreshModel.finishLoadMore(true)
                            it.list?.let { list ->
                                refreshModel.noMoreData.set(list.size < pageSize)
                                if (isSelectAll.get()) {
                                    val chapterStatusList = getChapterStatus(list)
                                    chapterStatusList.forEach { statusChapter ->
                                        if(statusChapter.down_status == DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD){
                                            selectChapterNum.set(selectChapterNum.get() + 1)
                                            selectChapterSize.set(selectChapterSize.get() + statusChapter.size)
                                            statusChapter.chapter_edit_select = true
                                        }
                                    }
                                    mAdapter.addData(chapterStatusList)
                                } else {
                                    mAdapter.addData(list)
                                }
                                if (list.size < pageSize && mAdapter.footerLayout == null) {
                                    mAdapter.addFooterView(View.inflate(context, R.layout.business_foot_view, null))
                                }
                            }
                        },
                        onError = {
                            refreshModel.finishLoadMore(false)
                            showTip("$it", R.color.business_color_ff5e5e)
                            DLog.e("download", "$it")
                        }
                    )
            }
        }
    }

    private fun getChapterStatus(chapterList: List<DownloadChapter>): MutableList<DownloadChapter> {
        val audioName = downloadAudio.get()?.audio_name
        val audioId = downloadAudio.get()?.audio_id
        chapterList.forEach {
            it.audio_name = audioName
            it.audio_id = audioId
            DownLoadFileUtils.checkChapterIsDownload(chapter = it)
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
                            mAdapter.notifyDataSetChanged()
                        }
//                        audioChapterList.addAll(chapterStatusList)
                    },
                    onError = {
                        DLog.i("download", "$it")
                        showTip("$it", R.color.business_color_ff5e5e)
                    }
                )
        }
    }

}