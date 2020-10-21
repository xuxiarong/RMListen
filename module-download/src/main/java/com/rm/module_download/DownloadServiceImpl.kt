package com.rm.module_download

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.business_lib.bean.download.DownloadChapterStatusModel
import com.rm.business_lib.bean.download.DownloadProgressUpdateBean
import com.rm.business_lib.bean.download.DownloadUIStatus
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.component_comm.base.IApplicationDelegate
import com.rm.component_comm.download.DownloadService
import com.rm.component_comm.router.ARouterModuleServicePath
import com.rm.module_download.activity.DownloadChapterSelectionActivity
import com.rm.module_download.activity.DownloadMainActivity
import com.rm.module_download.service.DownloadFileManager

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

    override fun startDownloadWithCache(chapter: DownloadChapterStatusModel) {
        DownloadFileManager.INSTANCE.download(chapter)
//        DownloadDaoUtils.INSTANCE.saveAudio(audio)
    }

    override fun startDownloadWithCache(chapterList: MutableList<DownloadChapterStatusModel>) {
        DownloadFileManager.INSTANCE.download(chapterList)
    }

    override fun pauseDownload(chapter: DownloadChapterStatusModel): Boolean {
        return DownloadFileManager.INSTANCE.stopDownload(chapter)
    }

    override fun pauseDownload(chapterList: MutableList<DownloadChapterStatusModel>) {
        DownloadFileManager.INSTANCE.stopDownload(chapterList)
    }

    override fun deleteDownload(chapter: DownloadChapterStatusModel) {
        DownloadFileManager.INSTANCE.delete(chapter)
    }

    override fun deleteDownload(chapterList: MutableList<DownloadChapterStatusModel>) {
        DownloadFileManager.INSTANCE.delete(chapterList)
    }

    override fun getDownloadStatus(chapter: DownloadChapterStatusModel): DownloadUIStatus {
        return DownloadFileManager.INSTANCE.getTaskStatus(chapter)
    }

    override fun getDownloadProgressInfo(chapter: DownloadChapterStatusModel): DownloadProgressUpdateBean? {
        return DownloadFileManager.INSTANCE.getTaskBreakpointInfo(chapter)
    }

    override fun getDownloadAudioList(): MutableList<DownloadChapterStatusModel> {
        return mutableListOf()
    }

    override fun stopAll() {
        DownloadFileManager.INSTANCE.stopAll()
    }

    override fun deleteAll() {
        DownloadFileManager.INSTANCE.deleteAll()
    }


    override fun getApplicationDelegateClass(): Class<out IApplicationDelegate?> {
        return DownloadApplicationDelegate::class.java
    }

    override fun init(context: Context?) {
    }
}