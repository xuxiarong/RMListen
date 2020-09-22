package com.rm.component_comm.download

import android.content.Context
import com.rm.business_lib.bean.download.DownloadAudioBean
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
     * @param audioId 书籍id
     */
    fun startDownloadChapterSelectionActivity(context: Context, audioId: String)

    /**
     *下载单个音频文件
     */
    fun startDownloadAudio(audio: DownloadAudioBean)

    /**
     * 下载音频列表
     */
    fun startDownloadAudio(audioList: List<DownloadAudioBean>)

    /**
     * 暂停下载
     */
    fun stopDownload(url: String)

    /**
     * 暂停下载（列表）
     */
    fun stopDownload(urlList: List<String>)

    /**
     *删除下载（包括下载中,已完成）
     */
    fun delete(url: String)


    /**
     *删除下载列表（包括下载中,已完成）
     */
    fun delete(urlList: List<String>)

    /**
     * 根据url获取对应的audio下载信息bean
     */
    fun getDownloadAudioInfo(url: String): DownloadAudioBean

}