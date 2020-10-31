package com.rm.module_download.viewmodel

import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.base.dialog.TipsFragmentDialog
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.download.DownloadMemoryCache
import com.rm.business_lib.download.file.DownLoadFileUtils
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_download.BR
import com.rm.module_download.R
import com.rm.module_download.activity.DownloadBookDetailActivity
import com.rm.module_download.repository.DownloadRepository

class DownloadMainViewModel(private val repository: DownloadRepository) : BaseVMViewModel() {

    private val homeService by lazy { RouterHelper.createRouter(HomeService::class.java) }

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


    var downloadAudioList = DownloadMemoryCache.downloadingAudioList

    fun operatingAll(){
        DownloadMemoryCache.operatingAll()
    }


    fun getDownloadFromDao() {
        if(downloadAudioList.value == null){
            DownloadMemoryCache.getDownAudioOnAppCreate()
        }else{
            val tempList = downloadAudioList.value!!
            tempList.forEach {
                it.edit_select = false
            }
            DownloadMemoryCache.downloadingAudioList.value = tempList
        }
    }

    fun editDownloading() {
        if(downloadingAdapter.data.isEmpty()){
            return
        }
        if (downloadingEdit.get()) {
            DownloadMemoryCache.resumeDownloadingChapter()
        }else{
            DownloadMemoryCache.pauseDownloadingChapter()
        }
        downloadingEdit.set(downloadingEdit.get().not())
    }

    fun editDownloadFinish() {
        if(downloadFinishAdapter.data.isEmpty()){
            return
        }
        if(!downloadFinishEdit.get()){
            downloadFinishSelectAll.set(false)
            downloadFinishDeleteListenFinish.set(false)
        }else{
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
        downloadingAdapter.data.forEach {
            if (selectAll && !it.down_edit_select) {
                downloadingSelectNum.set(downloadingSelectNum.get() + 1)
                it.down_edit_select = selectAll
            } else if (!selectAll && it.down_edit_select) {
                downloadingSelectNum.set(downloadingSelectNum.get() - 1)
                it.down_edit_select = selectAll
            }
        }
        downloadingAdapter.notifyDataSetChanged()
    }

    fun changeDownloadChapterSelect(chapter: DownloadChapter) {
        if (!chapter.down_edit_select) {
            downloadingSelectNum.set(downloadingSelectNum.get() + 1)
        } else {
            downloadingSelectNum.set(downloadingSelectNum.get() - 1)
        }
        chapter.down_edit_select = chapter.down_edit_select.not()
    }

    fun chapterClick(context: Context, chapter: DownloadChapter) {
        if (downloadingEdit.get()) {
            changeDownloadChapterSelect(chapter = chapter)
            downloadingAdapter.notifyDataSetChanged()
        } else {
            DownloadMemoryCache.downloadingChapterClick(chapter)
        }
    }

    fun deleteSelectChapter() {
        if (downloadingSelectNum.get() <= 0) {
            return
        }
        val iterator = downloadingAdapter.data.iterator()
        val tempList = mutableListOf<DownloadChapter>()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next.down_edit_select) {
                tempList.add(next)
            }
        }
        DownloadMemoryCache.deleteDownloadingChapter(tempList)
        downloadingSelectNum.set(downloadingSelectNum.get() - tempList.size)
    }


    fun changeDownloadFinishAll() {
        if(downloadFinishSelectAll.get()){
            return
        }
        val selectAll = downloadFinishSelectAll.get().not()
        downloadFinishSelectAll.set(selectAll)
        if(selectAll){
            downloadFinishSelectNum.set(0)
            downloadFinishDeleteListenFinish.set(false)
        }
        downloadFinishAdapter.data.forEach {
            if (selectAll) {
                downloadFinishSelectNum.set(downloadFinishSelectNum.get() + 1)
                it.edit_select = true
            }
        }
        downloadFinishAdapter.notifyDataSetChanged()
    }

    fun changeDownloadFinishSelect(audio: DownloadAudio) {
        audio.edit_select = audio.edit_select.not()
        if (audio.edit_select) {
            downloadFinishSelectNum.set(downloadFinishSelectNum.get() + 1)
        } else {
            downloadFinishSelectNum.set(downloadFinishSelectNum.get() - 1)
        }
    }

    fun changeSelectListenFinish() {
        if(downloadFinishDeleteListenFinish.get()){
            return
        }

        if(!downloadFinishDeleteListenFinish.get()){
            downloadFinishSelectAll.set(false)
            downloadFinishSelectNum.set(0)
            downloadFinishAdapter.data.forEach {
                if(it.listen_finish){
                    it.edit_select = true
                    downloadFinishSelectNum.set(downloadFinishSelectNum.get() + 1)
                }else{
                    it.edit_select = false
                }
            }
            downloadFinishAdapter.notifyDataSetChanged()
        }
        downloadFinishDeleteListenFinish.set(downloadFinishDeleteListenFinish.get().not())
    }

    fun selectFinishDelete(){
        if(!downloadFinishDeleteListenFinish.get()){
            downloadFinishDeleteListenFinish.set(true)
            downloadFinishSelectAll.set(false)
        }
    }

    fun audioClick(context: Context, audio: DownloadAudio) {
        if (downloadFinishEdit.get()) {
            changeDownloadFinishSelect(audio = audio)
            downloadFinishAdapter.notifyDataSetChanged()
        } else {
//            homeService.toDetailActivity(context, audio.audio_id.toString())
            DownloadBookDetailActivity.startActivity(context,audio = audio)
        }
    }


    fun deleteAudio(audio: DownloadAudio) {
        DownloadMemoryCache.deleteAudioToDownloadMemoryCache(audio)
        downloadingSelectNum.set(downloadingSelectNum.get() - 1)
        DownLoadFileUtils.deleteAudioFile(audio)
    }

    fun deleteAudio(context: Context) {
        if (downloadFinishSelectNum.get() <= 0) {
            return
        }
        TipsFragmentDialog().apply {
            titleText = "删除提醒"
            contentText = "确定要删除所选内容吗"
            leftBtnText = "取消"
            rightBtnText = "确定"
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
                    }
                }
                DownloadMemoryCache.deleteAudioToDownloadMemoryCache(tempList)
                downloadFinishSelectNum.set(downloadFinishSelectNum.get() - tempList.size)
                DownLoadFileUtils.deleteAudioFile(tempList)
                dismiss()
                editDownloadFinish()
            }
        }.show(context as FragmentActivity)
    }

}