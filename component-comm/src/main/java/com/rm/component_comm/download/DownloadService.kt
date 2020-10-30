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
    fun startDownloadMainActivity(context: Context)

    /**
     * @param context Context
     * @param downloadAudio 书籍的信息封装类
     */
    fun startDownloadChapterSelectionActivity(context: Context, downloadAudio: DownloadAudio)

//    /**
//     *下载单个音频文件并存储
//     */
//    fun startDownloadWithCache(chapter: DownloadChapter)
//
//    /**
//     * 下载音频列表并存储
//     */
//    fun startDownloadWithCache(chapterList: MutableList<DownloadChapter>)
//
//    /**
//     * 暂停下载
//     */
//    fun pauseDownload(chapter: DownloadChapter): Boolean
//
//    /**
//     * 暂停下载（列表）
//     */
//    fun pauseDownload(chapterList: MutableList<DownloadChapter>)
//
//
//    /**
//     *删除下载（包括下载中,已完成）
//     */
//    fun deleteDownload(chapter: DownloadChapter)
//
//    /**
//     *删除下载列表
//     */
//    fun deleteDownload(chapterList: MutableList<DownloadChapter>)
//
//    /**
//     * 获取任务状态
//     */
//    fun getDownloadStatus(chapter: DownloadChapter): DownloadUIStatus
//
//    /**
//     * 获取任务进度信息
//     */
//    fun getDownloadProgressInfo(chapter: DownloadChapter): DownloadProgressUpdateBean?
//
//    /**
//     * 获取下载缓存列表
//     */
//    fun getDownloadAudioList():MutableList<DownloadChapter>
//
//    fun stopAll()
//
//    fun deleteAll()

}