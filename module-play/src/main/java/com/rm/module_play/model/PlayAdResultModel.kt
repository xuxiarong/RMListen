package com.rm.module_play.model

import com.rm.business_lib.bean.BusinessAdModel
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import kotlin.random.Random

/**
 *
 * @author yuanfang
 * @date 12/8/20
 * @description
 *
 */
class PlayAdResultModel(
        var ad_player_comment: List<BusinessAdModel>?
)

class PlayAdChapterModel(
        val ad_player_voice: ArrayList<BusinessAdModel>?,
        var ad_player_audio_cover: MutableList<BusinessAdModel>?
)

class PlayFloorAdModel(var ad_player_streamer: MutableList<BusinessAdModel>?,
                       var ad_player_audio_cover: MutableList<BusinessAdModel>?)

