package com.rm.component_comm.download

import android.content.Context
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.component_comm.router.ApplicationProvider

/**
 * desc   : download module路由服务接口
 * date   : 2020/08/13
 * version: 1.0
 */
interface DownloadService : ApplicationProvider {
    /**
     * 打开下载主页面
     * @param context Context
     */
    fun startDownloadMainActivity(context: Context,startTab : Int = 0)

    /**
     * @param context Context
     * @param downloadAudio 书籍的信息封装类
     */
    fun startDownloadChapterSelectionActivity(context: Context, downloadAudio: DownloadAudio)

}