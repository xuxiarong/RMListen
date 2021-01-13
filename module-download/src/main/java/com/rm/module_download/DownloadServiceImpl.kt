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


    override fun startDownloadMainActivity(context: Context, startTab : Int) {
        DownloadMainActivity.startActivity(context,startTab)
    }

    override fun startDownloadChapterSelectionActivity(context: Context, downloadAudio: DownloadAudio) {
        DownloadChapterSelectionActivity.startActivity(context, downloadAudio)
    }

    override fun getApplicationDelegateClass(): Class<out IApplicationDelegate?> {
        return DownloadApplicationDelegate::class.java
    }

    override fun init(context: Context?) {
    }
}