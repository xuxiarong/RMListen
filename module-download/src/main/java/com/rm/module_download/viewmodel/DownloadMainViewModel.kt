package com.rm.module_download.viewmodel

import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.DownloadMemoryCache
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.component_comm.download.DownloadService
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_download.BR
import com.rm.module_download.R
import com.rm.module_download.repository.DownloadRepository

class DownloadMainViewModel(private val repository: DownloadRepository) : BaseVMViewModel() {

    private val downloadService by lazy { RouterHelper.createRouter(DownloadService::class.java) }
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


    fun getDownloadFromDao() {
//        launchOnIO {
//            val allData = DaoUtil(DownloadAudio::class.java, "").queryAll()
//            DLog.d("suolong", "${allData?.size}")
//            allData?.let {
//                if (allData.isNotEmpty()) {
//                    downloadAudioList.postValue(allData.toMutableList())
//                }
//            }
//        }
    }

    fun editDownloading() {
        downloadingEdit.set(downloadingEdit.get().not())
    }

    fun editDownloadFinish() {
        downloadFinishEdit.set(downloadFinishEdit.get().not())
    }

    fun changeDownloadingAll() {
        val selectAll = downloadingSelectAll.get().not()
        downloadingSelectAll.set(selectAll)
        downloadingAdapter.data.forEach {
            if (selectAll && !it.down_edit_select) {
                downloadingSelectNum.set(downloadingSelectNum.get()+1)
                it.down_edit_select = selectAll
            } else if (!selectAll && it.down_edit_select) {
                downloadingSelectNum.set(downloadingSelectNum.get()-1)
                it.down_edit_select = selectAll
            }
        }
        downloadingAdapter.notifyDataSetChanged()
    }

    fun changeDownloadChapterSelect(chapter: DownloadChapter) {
        if (!chapter.down_edit_select) {
            downloadingSelectNum.set(downloadingSelectNum.get()+1)
        } else {
            downloadingSelectNum.set(downloadingSelectNum.get()-1)
        }
        chapter.down_edit_select = chapter.down_edit_select.not()
    }

    fun chapterClick(context: Context, chapter: DownloadChapter) {
        if (downloadingEdit.get()) {
            changeDownloadChapterSelect(chapter = chapter)
            downloadingAdapter.notifyItemChanged(downloadingAdapter.data.indexOf(chapter))
        } else {

        }
    }


    fun changeDownloadFinishAll() {
        val selectAll = downloadFinishSelectAll.get().not()
        downloadFinishSelectAll.set(selectAll)
        downloadFinishAdapter.data.forEach {
            if (selectAll && !it.edit_select) {
                downloadFinishSelectNum.set(downloadFinishSelectNum.get()+1)
                it.edit_select = it.edit_select.not()
            } else if (!selectAll && it.edit_select) {
                downloadFinishSelectNum.set(downloadFinishSelectNum.get()-1)
                it.edit_select = it.edit_select.not()
            }
        }
        downloadFinishAdapter.notifyDataSetChanged()
    }

    fun changeDownloadFinishSelect(audio: DownloadAudio) {
        audio.edit_select = audio.edit_select.not()
        if (audio.edit_select) {
            downloadFinishSelectNum.set(downloadFinishSelectNum.get()+1)
        } else {
            downloadFinishSelectNum.set(downloadFinishSelectNum.get()-1)
        }
    }

    fun changeSelectListenFinish() {
        downloadFinishDeleteListenFinish.set(downloadFinishDeleteListenFinish.get().not())
    }

    fun audioClick(context: Context, audio: DownloadAudio) {
        if (downloadFinishEdit.get()) {
            changeDownloadFinishSelect(audio = audio)
            downloadFinishAdapter.notifyItemChanged(downloadFinishAdapter.data.indexOf(audio))
        } else {
            homeService.toDetailActivity(context, audio.audio_id.toString())
        }
    }

    fun deleteAudio() {

    }

}