package com.rm.module_download.viewmodel

import android.content.Context
import androidx.databinding.ObservableField
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_download.BR
import com.rm.module_download.R
import com.rm.module_play.enum.Jump

class DownloadBookDetailViewModel:BaseVMViewModel() {

    var downloadAudio = ObservableField<DownloadAudio>()

    val playService = RouterHelper.createRouter(PlayService::class.java)

    val downloadingAdapter by lazy {
        CommonBindVMAdapter<DownloadChapter>(
            this,
            mutableListOf(),
            R.layout.download_item_audio_detail,
            BR.viewModel,
            BR.itemBean
        )
    }

    fun initAudioList(audio: DownloadAudio){
        downloadAudio.set(audio)
    }

    fun downloadChapterClick(context : Context,chapter: DownloadChapter){
        playService.toPlayPage(context,audio = downloadAudio.get()!!,chapterId = chapter.chapter_id.toString(),from = Jump.DOWNLOAD.from)
    }

}