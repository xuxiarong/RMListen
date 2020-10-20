package com.rm.module_download

/**
 * desc   :
 * date   : 2020/10/19
 * version: 1.0
 */
object DownloadConstant {

    /**
     * 章节选集下载的状态 ：  0->未下载且未选中 1->未下载且已选中 2->已下载到本地
     */
    const val CHAPTER_STATUS_UN_SELECT = 0
    const val CHAPTER_STATUS_SELECTED = 1
    const val CHAPTER_STATUS_DOWNLOAD_FINISH = 2
    const val CHAPTER_STATUS_DOWNLOADING = 3
    const val CHAPTER_STATUS_DOWNLOAD_PAUSE = 4

    /**
     *  章节是否需要付费或VIP 0-> 免费 1-> 付费 2-> 会员（预留）
     */
    const val CHAPTER_PAY_STATUS_FREE = 0
    const val CHAPTER_PAY_STATUS_NEED_BUY = 1
    const val CHAPTER_PAY_STATUS_VIP = 2

}