package com.rm.module_download.viewmodel

import android.Manifest
import android.content.Context
import android.text.TextUtils
import android.view.ContextThemeWrapper
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
import com.rm.baselisten.mvvm.BaseActivity
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
    //?????????????????????
    var chapterStartSequence = "1"
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
        //????????????????????????
        mAdapter.data.forEach {
            if (it.chapter_edit_select && it.down_status == DownloadConstant.CHAPTER_STATUS_NOT_DOWNLOAD) {
                tempDownloadList.add(it)
            }
        }
        if (tempDownloadList.size == 0) {
            showTip(BaseApplication.CONTEXT.getString(com.rm.business_lib.R.string.business_download_all_exist))
            return
        }

        checkContext(context){
//        //?????????????????????
            downloadAudio.get()?.let { DownloadMemoryCache.addAudioToDownloadMemoryCache(it) }
            //??????????????????????????????
            DownloadMemoryCache.addDownloadingChapter(context,tempDownloadList)
            //??????????????????????????????
            isSelectAll.set(false)
            selectChapterNum.set(0)
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

    fun getDownloadChapterList() {
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
     * ?????????????????????dialog
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
                            checkContext(context){
                                startDownSelectChapter(context)
                            }
                            dismiss()
                        }
                    }
                }
            }.showCommonDialog(context, R.layout.download_dialog_select_chapter, this, BR.viewModel)
        }
    }

    /**
     * ?????????????????????????????????????????????????????????????????????????????????????????????
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
            dialogSelectChapterSize.set(0)
            selectDownChapterList.clear()
        }
    }

    /**
     * ?????????????????????????????????
     */
    fun startDownSelectChapter(context: Context) {
        if (lastChangStartIndex.toString() == startSequence.get() && lastChangeEndIndex.toString() == endSequence.get()
                || lastChangStartIndex.toString() == endSequence.get() && lastChangeEndIndex.toString() == startSequence.get()) {
            if (selectDownChapterList.size > 0) {
                mAdapter.data.forEach {
                    it.chapter_edit_select = false
                }
                isSelectAll.set(false)
                isSelectAll.notifyChange()
                selectChapterNum.set(0)
                mAdapter.notifyDataSetChanged()
                val chapterStatusList = getChapterStatus(selectDownChapterList)
                downloadAudio.get()?.let { DownloadMemoryCache.addAudioToDownloadMemoryCache(it) }
                DownloadMemoryCache.addDownloadingChapter(context,chapterStatusList)
            } else {
                showTip("?????????????????????????????????")
            }
        }
    }

    /**
     * ???????????????????????????
     */
    private fun getDialogStartToEndIndexChapterList(startIndex: Int, endIndex: Int) {
        lastChangStartIndex = startIndex
        lastChangeEndIndex = endIndex
        if(startIndex==0 || endIndex==0){
            return
        }
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
                                    checkDialogSelectChapterLegal(mutableListOf())
                                    DLog.i("download", "$it")
                                }
                        )
            }
        }
    }

    /**
     * ???????????????????????????????????????????????????????????????
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

    private fun checkContext(context: Context,actionGranted: () -> Unit) {
        if(context is BaseActivity){
            requestPermission(context,actionGranted)

        }else{
            //???Dialog???????????????context?????????ContextThemeWrapper???
            if(context is ContextThemeWrapper){
                if(context.baseContext is BaseActivity){
                    requestPermission(context.baseContext as BaseActivity,actionGranted)
                }
            }
            else if(context is androidx.appcompat.view.ContextThemeWrapper){
                if(context.baseContext is BaseActivity){
                    requestPermission(context.baseContext as BaseActivity,actionGranted)
                }
            }
        }
    }

    private fun requestPermission(baseActivity : BaseActivity?, actionGranted: () -> Unit) {
        baseActivity?.let {
            baseActivity.requestPermissionForResult(permission = mutableListOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE),
                actionDenied = {
                   ToastUtil.showTopToast(
                        baseActivity,
                        baseActivity.getString(com.rm.business_lib.R.string.business_listen_storage_permission_refuse)
                    )
                },
                actionGranted = {
                    actionGranted()
                },
                actionPermanentlyDenied = {
                    ToastUtil.showTopToast(
                        baseActivity,
                        baseActivity.getString(com.rm.business_lib.R.string.business_listen_to_set_storage_permission)
                    )
                })
        }
    }

}