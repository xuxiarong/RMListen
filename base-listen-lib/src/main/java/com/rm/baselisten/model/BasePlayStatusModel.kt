package com.rm.baselisten.model

/**
 * desc   :
 * date   : 2020/09/03
 * version: 1.0
 */
data class BasePlayStatusModel constructor(
    var playReady : Boolean = false,
    var playStatus : Int = 3
){
    fun isStart() : Boolean{
        return playStatus == STATE_READY && playReady
    }

    fun isPause() : Boolean{
        return playStatus == STATE_ENDED || (playStatus == STATE_READY && !playReady)
    }

    var STATE_IDLE = 1

    /**
     * The player is not able to immediately play from its current position. This state typically
     * occurs when more data needs to be loaded.
     */
    var STATE_BUFFERING = 2

    /**
     * The player is able to immediately play from its current position. The player will be playing if
     * [.getPlayWhenReady] is true, and paused otherwise.
     */
    var STATE_READY = 3

    /**
     * The player has finished playing the media.
     */
    var STATE_ENDED = 4

}