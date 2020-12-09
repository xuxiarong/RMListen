package com.rm.module_play.model

import com.rm.business_lib.bean.BusinessAdModel

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
        var ad_player_voice: List<BusinessAdModel>?,
        var ad_player_audio_cover: MutableList<BusinessAdModel>?
)

class PlayFloorAdModel(var ad_player_streamer : MutableList<BusinessAdModel>?)

