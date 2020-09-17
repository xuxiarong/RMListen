package com.rm.module_play.model

import androidx.databinding.ObservableField
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.module_play.adapter.BookPlayerAdapter
import com.rm.module_play.test.SearchResultInfo
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo

/**
 *播放控制器
 * @des:
 * @data: 9/9/20 5:38 PM
 * @Version: 1.0.0
 */

data class PlayControlModel(
    val baseAudioInfo: BaseAudioInfo = BaseAudioInfo(),
    override val itemType: Int = BookPlayerAdapter.ITEM_TYPE_PLAYER,
    val state: Boolean = false

) :
    MultiItemEntity {


}