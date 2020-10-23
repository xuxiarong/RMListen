package com.rm.module_play.model

import com.rm.business_lib.bean.ChapterList

/**
 * desc   :
 * date   : 2020/10/22
 * version: 1.0
 */
data class PlayLastListenChapterModel constructor(val audioId : Long = 0L ,val audioName : String = "",val audioCoverUrl : String = "",var chapter: ChapterList? = null)
