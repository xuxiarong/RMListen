package com.rm.module_download

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.component_comm.base.IApplicationDelegate
import com.rm.component_comm.download.DownloadService
import com.rm.component_comm.router.ARouterModuleServicePath
import com.rm.module_download.activity.DownloadChapterSelectionActivity
import com.rm.module_download.activity.DownloadMainActivity

/**
 * desc   : download module 路由服务实现类
 * date   : 2020/09/03
 * version: 1.0
 */
@Route(path = ARouterModuleServicePath.PATH_DOWNLOAD_SERVICE)
class DownloadServiceImpl : DownloadService {


    override fun startDownloadMainActivity(context: Context) {
        DownloadMainActivity.startActivity(context)
    }

    override fun startDownloadChapterSelectionActivity(context: Context, downloadAudio: DownloadAudio) {
        DownloadChapterSelectionActivity.startActivity(context, downloadAudio)
    }

//    override fun startDownloadWithCache(chapter: DownloadChapter) {
//        DownloadFileManager.INSTANCE.startDownloadWithCache(chapter)
////        DownloadDaoUtils.INSTANCE.saveAudio(audio)
//    }
//
//    override fun startDownloadWithCache(chapterList: MutableList<DownloadChapter>) {
//        DownloadFileManager.INSTANCE.startDownloadWithCache(chapterList)
//    }
//
//    override fun pauseDownload(chapter: DownloadChapter): Boolean {
//        return DownloadFileManager.INSTANCE.pauseDownload(chapter)
//    }
//
//    override fun pauseDownload(chapterList: MutableList<DownloadChapter>) {
//        DownloadFileManager.INSTANCE.pauseDownload(chapterList)
//    }
//
//    override fun deleteDownload(chapter: DownloadChapter) {
//        DownloadFileManager.INSTANCE.deleteDownload(chapter)
//    }
//
//    override fun deleteDownload(chapterList: MutableList<DownloadChapter>) {
//        DownloadFileManager.INSTANCE.deleteDownload(chapterList)
//    }
//
//    override fun getDownloadStatus(chapter: DownloadChapter): DownloadUIStatus {
//        return DownloadFileManager.INSTANCE.getTaskStatus(chapter)
//    }
//
//    override fun getDownloadProgressInfo(chapter: DownloadChapter): DownloadProgressUpdateBean? {
//        return DownloadFileManager.INSTANCE.getTaskBreakpointInfo(chapter)
//    }
//
//    override fun getDownloadAudioList(): MutableList<DownloadChapter> {
//        return mutableListOf()
//    }
//
//    override fun stopAll() {
//        DownloadFileManager.INSTANCE.stopAll()
//    }
//
//    override fun deleteAll() {
//        DownloadFileManager.INSTANCE.deleteAll()
//    }


    override fun getApplicationDelegateClass(): Class<out IApplicationDelegate?> {
        return DownloadApplicationDelegate::class.java
    }

    override fun init(context: Context?) {
    }
}