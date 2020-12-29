package com.rm.module_download.viewmodel

import android.content.Context
import androidx.databinding.ObservableField
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.adapter.single.CommonPositionVMAdapter
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.PlayGlobalData
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_download.BR
import com.rm.module_download.R

class DownloadBookDetailViewModel : BaseVMViewModel() {

    var downloadAudio = ObservableField<DownloadAudio>()

    val playService = RouterHelper.createRouter(PlayService::class.java)

    val downloadingAdapter by lazy {
        CommonPositionVMAdapter<DownloadChapter>(
            this,
            mutableListOf(),
            R.layout.download_item_audio_detail,
            BR.viewModel,
            BR.itemBean,
            R.id.download_tv_sequence
        )
    }

    fun initAudioList(audio: DownloadAudio) {
        downloadAudio.set(audio)
    }

    fun downloadChapterClick(context: Context, chapter: DownloadChapter) {
        PlayGlobalData.playNeedQueryChapterProgress.set(true)
        playService.startPlayActivity(
            context,
            audioId = chapter.audio_id.toString(),
            chapterId = chapter.chapter_id.toString(),
            audioInfo = downloadAudio.get()?:DownloadAudio(),
            chapterList = downloadingAdapter.data,
            currentDuration = chapter.listen_duration
        )
    }

}