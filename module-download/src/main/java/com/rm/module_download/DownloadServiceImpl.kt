package com.rm.module_download

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.rm.business_lib.bean.download.DownloadAudioBean
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

    override fun startDownloadChapterSelectionActivity(context: Context, audioId: String) {
        DownloadChapterSelectionActivity.startActivity(context, audioId)
    }

    override fun startDownloadAudio(audio: DownloadAudioBean) {

    }

    override fun startDownloadAudio(audioList: List<DownloadAudioBean>) {
    }

    override fun stopDownload(url: String) {
    }

    override fun stopDownload(urlList: List<String>) {
    }

    override fun delete(url: String) {
    }

    override fun delete(urlList: List<String>) {
    }

    override fun getDownloadAudioInfo(url: String): DownloadAudioBean {
        return DownloadFileManager.INSTANCE.getDownloadAudioInfo(url)
    }


    override fun getApplicationDelegateClass(): Class<out IApplicationDelegate?> {
        return DownloadApplicationDelegate::class.java
    }

    override fun init(context: Context?) {
    }
}