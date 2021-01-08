package com.rm.module_download.viewmodel

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.ObservableLong
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.dialog.CommonMvFragmentDialog
import com.rm.baselisten.ktx.toIntSafe
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
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
import kotlinx.android.synthetic.main.download_dialog_select_chapter.*

class DownloadChapterSelectionViewModel(private val repository: DownloadRepository) :
        BaseVMViewModel() {
    val data = MutableLiveData<MutableList<DownloadChapterAdapterBean>>()
    private var page = 1
    private val pageSize = 12
    private var total = 0
    var isSelectAll = ObservableBoolean(false)
    var selectChapterNum = ObservableInt(0)
    var selectChapterSize = ObservableLong(0L)
    var downloadAudio = ObservableField<DownloadAudio>()
    val refreshModel = SmartRefreshLayoutStatusModel()
    val rvId = R.id.down_select_rv
    //选集下载的逻辑
    private var chapterStartSequence = "1"
    var startSequence = ObservableField<String>("")
    var endSequence = ObservableField<String>("")
    private var lastChangStartIndex = 0
    private var selectDownChapterList = mutableListOf<DownloadChapter>()
    private var lastChangeEndIndex = 0
    var dialogSelectChapterSize = ObservableLong(0L)

    val mAdapter by lazy {
        CommonBindVMAdapter<DownloadChapter>(
                this,
                mutableListOf(),
                R.layout.download_item_chapter_selection,
                BR.viewModel,
                BR.itemBean
        )
    }

    fun downloadList(context: Context) {
        val tempDownloadList = mutableListOf<DownloadChapter>()
        //筛选已选择的章节
        mAdapter.data.forEach {
            if (it.chapter_edit_select && it.down_status == DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD) {
                tempDownloadList.add(it)
            }
        }
        if (tempDownloadList.size == 0) {
            showTip(BaseApplication.CONTEXT.getString(com.rm.business_lib.R.string.business_download_all_exist))
            return
        }

//        //将音频信息存储
        downloadAudio.get()?.let { DownloadMemoryCache.addAudioToDownloadMemoryCache(it) }
        //存储已选择的下载章节
        DownloadMemoryCache.addDownloadingChapter(context,tempDownloadList)
        //调用下载服务开始下载
        mAdapter.notifyDataSetChanged()

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
                                        val chapterStatusList = getChapterStatus(list)
                                        if (isSelectAll.get()) {
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
                                        if (list.size < pageSize ) {
                                           refreshModel.noMoreData.set(true)
                                        }
                                    }
                                },
                                onError = {it,_->
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


    /**
     * 显示选集下载的dialog
     */
    fun showChapterSelectDialog(context: Context) {
        startSequence.set("")
        endSequence.set("")
        dialogSelectChapterSize.set(0L)
        (context as FragmentActivity).let {
            CommonMvFragmentDialog().apply {
                gravity = Gravity.BOTTOM
                dialogWidthIsMatchParent = true
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
                            startDownSelectChapter(context)
                            dismiss()
                        }
                    }
                }
            }.showCommonDialog(context, R.layout.download_dialog_select_chapter, this, BR.viewModel)
        }
    }

    /**
     * 检测该次请求数据是否是合法数据，也就是最后一次输入的起始集数据
     */
    private fun checkDialogSelectChapterLegal(list: MutableList<DownloadChapter>) {
        if (lastChangStartIndex.toString() == startSequence.get() && lastChangeEndIndex.toString() == endSequence.get()
                || lastChangStartIndex.toString() == endSequence.get() && lastChangeEndIndex.toString() == startSequence.get()) {
            var chapterSize = 0L
            selectDownChapterList = list
            list.forEach {
                chapterSize += it.size
            }
            dialogSelectChapterSize.set(chapterSize)
        } else {
            selectDownChapterList.clear()
        }
    }

    /**
     * 选集下载弹窗的点击事件
     */
    fun startDownSelectChapter(context: Context) {
        if (lastChangStartIndex.toString() == startSequence.get() && lastChangeEndIndex.toString() == endSequence.get()
                || lastChangStartIndex.toString() == endSequence.get() && lastChangeEndIndex.toString() == startSequence.get()) {
            if (selectDownChapterList.size > 0) {
                val chapterStatusList = getChapterStatus(selectDownChapterList)
                downloadAudio.get()?.let { DownloadMemoryCache.addAudioToDownloadMemoryCache(it) }
                DownloadMemoryCache.addDownloadingChapter(context,chapterStatusList)
                mAdapter.notifyDataSetChanged()
            } else {
                showTip("数据正在加载中，请稍后")
            }
        }
    }

    /**
     * 获取选集下载的数据
     */
    private fun getDialogStartToEndIndexChapterList(startIndex: Int, endIndex: Int) {
        lastChangStartIndex = startIndex
        lastChangeEndIndex = endIndex
        downloadAudio.get()?.audio_id?.let { audioId ->
            launchOnIO {
                repository.downloadChapterSelection(audioId = audioId, startSequence = startIndex , endSequence = endIndex)
                        .checkResult(
                                onSuccess = {
                                    it.list?.let { list ->
                                        if (list.size > 0) {
                                            DLog.i("suolong_download downloadChapterSelection ", "startIndex = $startIndex  endIndex = $endIndex chapterName = ${list[0].chapter_name}  chapterSequences = ${list[0].sequence}")
                                        }
                                        checkDialogSelectChapterLegal(list)
                                    }
                                },
                                onError = {it,_->
                                    DLog.i("download", "$it")
                                    showTip("$it", R.color.business_color_ff5e5e)
                                }
                        )
            }
        }
    }

    /**
     * 校验输入起始集和终止集，正常的话去获取数据
     */
    fun getDialogSelectChapterList() {
        if (!TextUtils.isEmpty(startSequence.get()) && !TextUtils.isEmpty(endSequence.get())) {
            var startIndex = 1
            var endIndex = 1
            try {
                startIndex = startSequence.get()?.toIntSafe() ?: 1
                endIndex = endSequence.get()?.toIntSafe() ?: 1
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (startIndex <= endIndex) {
                getDialogStartToEndIndexChapterList(startIndex, endIndex)
            }else{
                getDialogStartToEndIndexChapterList(endIndex, startIndex)
            }
        }
    }

    fun inputStartSequence() {
        startSequence.set(chapterStartSequence)
    }

    fun inputEndSequence() {
        endSequence.set(downloadAudio.get()?.last_sequence ?: "1")
    }
}