package com.rm.module_download.viewmodel

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.baselisten.dialog.TipsFragmentDialog
import com.rm.baselisten.util.ToastUtil
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.download.DownloadMemoryCache
import com.rm.business_lib.download.file.DownLoadFileUtils
import com.rm.module_download.BR
import com.rm.module_download.R
import com.rm.module_download.activity.DownloadBookDetailActivity

class DownloadMainViewModel : BaseVMViewModel() {

    val downloadingAdapter by lazy {
        CommonBindVMAdapter<DownloadChapter>(
            this,
            mutableListOf(),
            R.layout.download_item_in_progress,
            BR.viewModel,
            BR.itemBean
        )
    }

    val downloadFinishAdapter by lazy {
        CommonBindVMAdapter<DownloadAudio>(
            this,
            mutableListOf(),
            R.layout.download_item_download_completed,
            BR.viewModel,
            BR.itemBean
        )
    }

    var downloadingSelected = ObservableBoolean(false)
    var downloadingEdit = ObservableBoolean(false)
    var downloadingSelectAll = ObservableBoolean(false)
    var downloadingSelectNum = ObservableInt(0)

    var downloadFinishSelected = ObservableBoolean(false)
    var downloadFinishEdit = ObservableBoolean(false)
    var downloadFinishSelectAll = ObservableBoolean(false)
    var downloadFinishSelectNum = ObservableInt(0)
    var downloadFinishDeleteListenFinish = ObservableBoolean(false)

    fun operatingAll() {
        DownloadMemoryCache.operatingAll()
    }


    fun getDownloadFromDao() {
        if (DownloadMemoryCache.downloadingAudioList.value == null) {
            DownloadMemoryCache.getDownAudioOnAppCreate()
        } else {
            val tempList = DownloadMemoryCache.downloadingAudioList.value!!
            tempList.forEach {
                it.edit_select = false
            }
            DownloadMemoryCache.downloadingAudioList.value = tempList
        }
    }

    fun editDownloading() {
        if (downloadingAdapter.data.isEmpty()) {
            return
        }
        if (downloadingEdit.get() && DownloadMemoryCache.isDownAll.get()) {
            DownloadMemoryCache.resumeDownloadingChapter()
        } else {
            downloadingSelectNum.set(0)
            downloadingSelectAll.set(false)
            downloadingAdapter.data.forEach {
                it.down_edit_select = false
            }
            downloadingAdapter.notifyDataSetChanged()
            DownloadMemoryCache.pauseDownloadingChapter()
        }
        downloadingEdit.set(downloadingEdit.get().not())
    }

    fun editDownloadFinish() {
        if (downloadFinishAdapter.data.isEmpty()) {
            return
        }
        if (!downloadFinishEdit.get()) {
            downloadFinishSelectAll.set(false)
            downloadFinishDeleteListenFinish.set(false)
        } else {
            downloadFinishAdapter.data.forEach {
                it.edit_select = false
            }
            downloadFinishAdapter.notifyDataSetChanged()
        }
        downloadFinishSelectNum.set(0)
        downloadFinishEdit.set(downloadFinishEdit.get().not())

    }

    //全选事件
    fun changeDownloadingAll() {
        val selectAll = downloadingSelectAll.get().not()
        downloadingSelectAll.set(selectAll)
        var selectNum = 0
        downloadingAdapter.data.forEach {
            if (selectAll) {
                selectNum += 1
                it.down_edit_select = true
            } else {
                it.down_edit_select = false
            }
        }
        downloadingSelectNum.set(selectNum)
        downloadingAdapter.notifyDataSetChanged()
    }

    //下载中的章节列表点击事件
    private fun changeDownloadChapterSelect(chapter: DownloadChapter) {
        chapter.down_edit_select = chapter.down_edit_select.not()
        val selectNum = downloadingSelectNum.get()
        if (chapter.down_edit_select) {
            downloadingSelectNum.set(selectNum + 1)
            if ((selectNum + 1) >= downloadingAdapter.data.size) {
                downloadingSelectAll.set(true)
            }
        } else {
            downloadingSelectAll.set(false)
            downloadingSelectNum.set(selectNum - 1)
        }
    }

    fun chapterClick( chapter: DownloadChapter) {
        if (downloadingEdit.get()) {
            changeDownloadChapterSelect(chapter = chapter)
            downloadingAdapter.notifyDataSetChanged()
        } else {
            DownloadMemoryCache.downloadingChapterClick(chapter)
        }
    }

    fun deleteSelectChapter(context: Context) {
        if (downloadingSelectNum.get() <= 0) {
            return
        }
        TipsFragmentDialog().apply {
            titleText = context.getString(R.string.business_delete_tip_title)
            contentText = context.getString(R.string.business_delete_tip_content)
            leftBtnText = context.getString(R.string.business_cancel)
            rightBtnText = context.getString(R.string.business_sure)
            leftBtnTextColor = R.color.business_text_color_333333
            rightBtnTextColor = R.color.business_color_ff5e5e
            leftBtnClick = {
                dismiss()
            }
            rightBtnClick = {
                val iterator = downloadingAdapter.data.iterator()
                val tempList = mutableListOf<DownloadChapter>()
                var deleteDownloading = false
                val downloadingChapter = DownloadMemoryCache.downloadingChapter.get()
                val isDownAll = DownloadMemoryCache.isDownAll.get()
                var delayDeleteChapter : DownloadChapter? = null
                while (iterator.hasNext()) {
                    val next = iterator.next()
                    if (next.down_edit_select) {
                        if(!deleteDownloading && isDownAll && downloadingChapter!=null && downloadingChapter.chapter_id == next.chapter_id){
                            deleteDownloading = true
                            delayDeleteChapter = next
                        }else{
                            tempList.add(next)
                            iterator.remove()
                        }
                    }
                }
                dismiss()
                downloadingEdit.set(false)
                var lifecycleOwner: LifecycleOwner? = null
                if (context is ComponentActivity) {
                    lifecycleOwner = context
                }
                ToastUtil.showTopToast(
                    context = context,
                    tipText = context.getString(R.string.business_delete_success),
                    lifecycleOwner = lifecycleOwner
                )
                DownloadMemoryCache.deleteDownloadingChapter(tempList)
                if(isDownAll){
                    if(deleteDownloading){
                        downloadingChapter?.let { downloadChapter ->
                            DownloadMemoryCache.downloadNextWaitChapter(downloadChapter)
                            delayDeleteChapter?.let {
                                DownloadMemoryCache.deleteDownloadingChapter(mutableListOf(it))
                            }
                        }
                    }else{
                        DownloadMemoryCache.resumeDownloadingChapter()
                    }
                }
            }
        }.show(context as FragmentActivity)
    }


    fun changeDownloadFinishAll() {
        val selectAll = downloadFinishSelectAll.get().not()
        downloadFinishSelectAll.set(selectAll)
        var selectNum = 0
        downloadFinishDeleteListenFinish.set(false)

        downloadFinishAdapter.data.forEach {
            if (selectAll) {
                selectNum += 1
                it.edit_select = true
            } else {
                it.edit_select = false
            }
        }
        downloadFinishSelectNum.set(selectNum)
        downloadFinishAdapter.notifyDataSetChanged()
    }

    private fun changeDownloadFinishSelect(audio: DownloadAudio) {
        audio.edit_select = audio.edit_select.not()
        //该次Item是选中事件
        if (audio.edit_select) {
            //先设置选中数量+1
            downloadFinishSelectNum.set(downloadFinishSelectNum.get() + 1)
            //如果选中数量等于了列表的长度，则说明全选了
            if (downloadFinishSelectNum.get() == downloadFinishAdapter.data.size) {
                downloadFinishSelectAll.set(true)
                downloadFinishDeleteListenFinish.set(false)
                return
            }
            //检测是否所有选中的都是已听完
            if (audio.listen_finish && !downloadFinishDeleteListenFinish.get()) {
                downloadFinishAdapter.data.forEach {
                    //只要有一个已听完的书籍没被选择，则跳出for循环，说明该次选中至少有一个已听完的没被选择
                    if (it.listen_finish && !it.edit_select) {
                        return
                    } else if (!it.listen_finish && it.edit_select) {
                        return
                    }
                }
                downloadFinishDeleteListenFinish.set(true)
            } else {
                downloadFinishDeleteListenFinish.set(false)
            }
        } else {
            if (downloadFinishSelectAll.get()) {
                downloadFinishSelectAll.set(false)
            }
            if (downloadFinishDeleteListenFinish.get() && audio.listen_finish) {
                downloadFinishDeleteListenFinish.set(false)
            }
            downloadFinishSelectNum.set(downloadFinishSelectNum.get() - 1)
        }
    }

    fun changeSelectListenFinish() {
        val selectListenFinish = downloadFinishDeleteListenFinish.get().not()
        downloadFinishDeleteListenFinish.set(selectListenFinish)
        var selectNum = 0
        downloadFinishSelectAll.set(false)
        downloadFinishAdapter.data.forEach {
            if (selectListenFinish && it.listen_finish) {
                it.edit_select = true
                selectNum += 1
            } else {
                it.edit_select = false
            }
        }
        downloadFinishSelectNum.set(selectNum)
        downloadFinishAdapter.notifyDataSetChanged()
    }

    fun audioClick(context: Context, audio: DownloadAudio) {
        if (downloadFinishEdit.get()) {
            changeDownloadFinishSelect(audio = audio)
            downloadFinishAdapter.notifyDataSetChanged()
        } else {
            DownloadBookDetailActivity.startActivity(context, audio = audio)
        }
    }

    fun deleteAudio(context: Context) {
        if (downloadFinishSelectNum.get() <= 0) {
            return
        }
        TipsFragmentDialog().apply {
            titleText = context.getString(R.string.business_delete_tip_title)
            contentText = context.getString(R.string.business_delete_tip_content)
            leftBtnText = context.getString(R.string.business_cancel)
            rightBtnText = context.getString(R.string.business_sure)
            leftBtnTextColor = R.color.business_text_color_333333
            rightBtnTextColor = R.color.business_color_ff5e5e
            leftBtnClick = {
                dismiss()
            }
            rightBtnClick = {
                val iterator = downloadFinishAdapter.data.iterator()
                val tempList = mutableListOf<DownloadAudio>()
                while (iterator.hasNext()) {
                    val next = iterator.next()
                    if (next.edit_select) {
                        tempList.add(next)
                        iterator.remove()
                    }
                }
                DownLoadFileUtils.deleteAudioFile(tempList)
                DownloadMemoryCache.deleteAudioToDownloadMemoryCache(tempList)
                var lifecycleOwner: LifecycleOwner? = null
                if (context is ComponentActivity) {
                    lifecycleOwner = context
                }
                ToastUtil.showTopToast(
                    context = context,
                    tipText = context.getString(R.string.business_delete_success),
                    lifecycleOwner = lifecycleOwner
                )
                downloadFinishSelectNum.set(downloadFinishSelectNum.get() - tempList.size)
                dismiss()
                editDownloadFinish()
            }
        }.show(context as FragmentActivity)
    }

}