package com.rm.module_download

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.business_lib.bean.download.BaseDownloadFileBean
import com.rm.business_lib.bean.download.DownloadAudioBean
import com.rm.business_lib.bean.download.DownloadProgressUpdateBean
import com.rm.business_lib.bean.download.DownloadUIStatus
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.component_comm.base.IApplicationDelegate
import com.rm.component_comm.download.DownloadService
import com.rm.component_comm.router.ARouterModuleServicePath
import com.rm.module_download.activity.DownloadChapterSelectionActivity
import com.rm.module_download.activity.DownloadMainActivity
import com.rm.module_download.service.DownloadAudioCache
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

    override fun startDownloadWithCache(audio: DownloadAudioBean) {
        DownloadFileManager.INSTANCE.download(audio)
        DownloadAudioCache.INSTANCE.saveAudio(audio)
    }

    override fun startDownloadWithCache(audioList: MutableList<DownloadAudioBean>) {
        DownloadFileManager.INSTANCE.download(audioList)
        DownloadAudioCache.INSTANCE.saveAudio(audioList)
    }

    override fun stopDownload(baseBean: BaseDownloadFileBean): Boolean {
        return DownloadFileManager.INSTANCE.stopDownload(baseBean)
    }

    override fun stopDownload(list: MutableList<BaseDownloadFileBean>) {
        DownloadFileManager.INSTANCE.stopDownload(list)
    }

    override fun deleteDownload(baseBean: BaseDownloadFileBean) {
        DownloadFileManager.INSTANCE.delete(baseBean)
        DownloadAudioCache.INSTANCE.deleteAudio(baseBean.url)
    }

    override fun deleteDownload(list: MutableList<BaseDownloadFileBean>) {
        DownloadFileManager.INSTANCE.delete(list)
        DownloadAudioCache.INSTANCE.deleteAudio(list.map { it.url })
    }

    override fun getDownloadStatus(baseBean: BaseDownloadFileBean): DownloadUIStatus {
        return DownloadFileManager.INSTANCE.getTaskStatus(baseBean)
    }

    override fun getDownloadProgressInfo(baseBean: BaseDownloadFileBean): DownloadProgressUpdateBean? {
        return DownloadFileManager.INSTANCE.getTaskBreakpointInfo(baseBean)
    }

    override fun getDownloadAudioList(): MutableList<DownloadAudioBean> {
        return DownloadAudioCache.INSTANCE.getDownloadAudioList()
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