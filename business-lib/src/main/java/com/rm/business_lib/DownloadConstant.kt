package com.rm.business_lib

/**
 * desc   :
 * date   : 2020/10/19
 * version: 1.0
 */
object DownloadConstant {

    /**
     * 章节选集下载的状态 ：  0->未选中 1->已选中
     */
    const val CHAPTER_UN_SELECT = 0
    const val CHAPTER_SELECTED = 1

    /**
     * 章节下载的状态 ： 0->还没有加入下载队列 1->下载队列中 2->正在下载 3->暂停下载 4->已完成
     */
    const val CHAPTER_STATUS_NOT_DOWNLOAD = 0
    const val CHAPTER_STATUS_DOWNLOAD_WAIT = 1
    const val CHAPTER_STATUS_DOWNLOADING = 2
    const val CHAPTER_STATUS_DOWNLOAD_PAUSE = 3
    const val CHAPTER_STATUS_DOWNLOAD_FINISH = 4

    /**
     *  章节是否需要付费或VIP 0-> 免费 1-> 付费 2-> 会员（预留）
     */
    const val CHAPTER_PAY_STATUS_FREE = 0
    const val CHAPTER_PAY_STATUS_NEED_BUY = 1
    const val CHAPTER_PAY_STATUS_VIP = 2

}