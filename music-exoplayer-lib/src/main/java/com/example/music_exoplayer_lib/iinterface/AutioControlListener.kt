package com.example.music_exoplayer_lib.iinterface

/**
 *
 * @des:
 * @data: 8/19/20 12:08 PM
 * @Version: 1.0.0
 */
interface AutioControlListener {
    fun setCurPositionTime(position: Int, curPositionTime: Long)
    fun setDurationTime(position: Int, durationTime: Long)
    fun setBufferedPositionTime(position: Int, bufferedPosition: Long)
    fun setCurTimeString(position: Int, curTimeString: String?)
    fun isPlay(position: Int, isPlay: Boolean)
    fun setDurationTimeString(position: Int, durationTimeString: String?)
}