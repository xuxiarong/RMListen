package com.example.music_exoplayer_lib.manager

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import com.example.music_exoplayer_lib.utils.ExoplayerLogger.exoLog

/**
 *音频监听器，当音频输出焦点被其他 MediaPlayer 实例抢占，则暂停播放，重新获取到音频输出焦点，自动恢复播放
 * @des:
 * @data: 8/22/20 12:10 PM
 * @Version: 1.0.0
 */
class MusicAudioFocusManager constructor(val context: Context) {
    private var mVolumeWhenFocusLossTransientCanDuck = 0
    private val mAudioManager by lazy {
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
    private val playbackAttributes by lazy {
        AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build();
    }

    /**
     * 请求音频焦点
     */
    fun requestAudioFocus(focusListener: OnAudioFocusListener): Int {
        mFocusListener = focusListener
        return mAudioManager.requestAudioFocus(
            onAudioFocusChangeListener,
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK,
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
        )
    }

    /**
     * 停止播放释放音频焦点
     */
    fun releaseAudioFocus() {
        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener)
    }

    private val onAudioFocusChangeListener: OnAudioFocusChangeListener? =
        OnAudioFocusChangeListener { focusChange ->
            exoLog(
                "onAudioFocusChange:focusChange:$focusChange"
            )
            val volume: Int
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK -> {
                    exoLog("重新获取到了焦点")
                    volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                    if (mVolumeWhenFocusLossTransientCanDuck > 0 && volume == mVolumeWhenFocusLossTransientCanDuck / 2) {
                        // 恢复音量
                        mAudioManager.setStreamVolume(
                            AudioManager.STREAM_MUSIC, mVolumeWhenFocusLossTransientCanDuck,
                            AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE
                        )
                    }
                    //恢复播放
                    mFocusListener?.onFocusGet()
                }
                AudioManager.AUDIOFOCUS_LOSS -> {
                    exoLog("被其他播放器抢占")
                    mFocusListener?.onFocusOut()
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                    exoLog("暂时失去焦点")
                    mFocusListener?.onFocusOut()
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                    exoLog("瞬间失去焦点")
                    if (mFocusListener?.isPlaying==true) {
                        volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                        if (volume > 0) {
                            mVolumeWhenFocusLossTransientCanDuck = volume
                            mAudioManager.setStreamVolume(
                                AudioManager.STREAM_MUSIC,
                                mVolumeWhenFocusLossTransientCanDuck / 2,
                                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE
                            )
                        }
                    }
                }
            }
        }

    interface OnAudioFocusListener {
        /**
         * 恢复焦点
         */
        fun onFocusGet()

        /**
         * 失去焦点
         */
        fun onFocusOut()

        /**
         * 内部播放器是否正在播放
         * @return 为true正在播放
         */
        val isPlaying: Boolean
    }

    var mFocusListener: OnAudioFocusListener? = null


}