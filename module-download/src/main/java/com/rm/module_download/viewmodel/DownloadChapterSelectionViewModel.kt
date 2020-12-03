package com.rm.module_download.viewmodel

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ObservableLong
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.download.DownloadConstant
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.module_download.BR
import com.rm.business_lib.download.DownloadMemoryCache
import com.rm.business_lib.download.file.DownLoadFileUtils
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.module_download.R
import com.rm.module_download.bean.DownloadChapterAdapterBean
import com.rm.module_download.repository.DownloadRepository
import com.tencent.bugly.proguard.s
import kotlinx.android.synthetic.main.download_dialog_select_chapter.*

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

    //选集下载的逻辑
    val selectDownChapterList = MutableLiveData<MutableList<DownloadChapter>>()
    var chapterStartSequence = "1"
    var chapterEndSequence = "1"
    var startSequence = ObservableField<String>("1")
    var endSequence = ObservableField<String>("")

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
                    selectSize += it.size
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
                                            for (i in 0 until chapterStatusList.size) {
                                                if (page == 2 && i == 0) {
                                                    chapterStartSequence = chapterStatusList[0].sequence.toString()
                                                }
                                                if (chapterStatusList[i].down_status == DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD) {
                                                    selectChapterNum.set(selectChapterNum.get() + 1)
                                                    selectChapterSize.set(selectChapterSize.get() + chapterStatusList[i].size)
                                                    chapterStatusList[i].chapter_edit_select = true
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

    private fun downloadChapterSelection(sequences: List<Int>) {
        downloadAudio.get()?.audio_id?.let { audioId ->
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

    fun showChapterSelectDialog(context: Context) {
        (context as FragmentActivity).let {
            CommBottomDialog().apply {
                initDialog = {
                    dialog?.setOnShowListener {
                        download_start_et.postDelayed({
                            download_start_et.isFocusable = true
                            download_start_et.requestFocus()
                            download_start_et.isFocusableInTouchMode = true
                            val inputManager =
                                    download_start_et.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
                            download_start_et.setSelection(download_start_et.text.length)
                        }, 50)
                        download_dialog_start_select_chapter.setOnClickListener {
                            startSelectChapter()
                            dismiss()
                        }
                    }
                }
            }.showCommonDialog(context, R.layout.download_dialog_select_chapter, this, BR.viewModel)
        }
    }

    private fun startSelectChapter() {
        var startIndex = 1
        var endIndex = 1
        try {
            startIndex = startSequence.get()?.toInt()?:1
            endIndex = endSequence.get()?.toInt()?:1
            startIndex = startIndex.coerceAtMost(endIndex)
            endIndex = startIndex.coerceAtLeast(endIndex)
        }catch (e : Exception){
            e.printStackTrace()
        }
        downloadChapterSelection((startIndex..endIndex).toList())
    }

    fun inputStartSequence() {
        startSequence.set(chapterStartSequence)
    }

    fun inputEndSequence() {
        endSequence.set(downloadAudio.get()?.last_sequence ?: chapterEndSequence)
    }
}