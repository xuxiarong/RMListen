package com.rm.component_comm.download

import android.content.Context
import com.rm.business_lib.bean.download.BaseDownloadFileBean
import com.rm.business_lib.bean.download.DownloadAudioBean
import com.rm.business_lib.bean.download.DownloadProgressUpdateBean
import com.rm.business_lib.bean.download.DownloadUIStatus
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
    fun startDownloadMainActivity(context: Context)

    /**
     * @param context Context
     * @param audioId 书籍的信息封装类
     */
    fun startDownloadChapterSelectionActivity(context: Context, downloadAudio: DownloadAudio)

    /**
     *下载单个音频文件并存储
     */
    fun startDownloadWithCache(audio: DownloadAudioBean)

    /**
     * 下载音频列表并存储
     */
    fun startDownloadWithCache(audioList: MutableList<DownloadAudioBean>)

    /**
     * 暂停下载
     */
    fun stopDownload(baseBean: BaseDownloadFileBean): Boolean

    /**
     * 暂停下载（列表）
     */
    fun stopDownload(list: MutableList<BaseDownloadFileBean>)


    /**
     *删除下载（包括下载中,已完成）
     */
    fun deleteDownload(baseBean: BaseDownloadFileBean)

    /**
     *删除下载列表
     */
    fun deleteDownload(list: MutableList<BaseDownloadFileBean>)

    /**
     * 获取任务状态
     */
    fun getDownloadStatus(baseBean: BaseDownloadFileBean): DownloadUIStatus

    /**
     * 获取任务进度信息
     */
    fun getDownloadProgressInfo(baseBean: BaseDownloadFileBean): DownloadProgressUpdateBean?

    /**
     * 获取下载缓存列表
     */
    fun getDownloadAudioList():MutableList<DownloadAudioBean>

    fun stopAll()

    fun deleteAll()

}