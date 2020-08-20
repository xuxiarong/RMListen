package com.example.music_exoplayer_lib.utils

import android.util.Log

/**
 *
 * @des:
 * @data: 8/20/20 10:44 AM
 * @Version: 1.0.0
 */
object ExoplayerLogger {

    const val TAG = "music-exoplayer-lib"
    fun exoLog(msg: Any) {
        Log.i(TAG, "${msg}")
    }
}
