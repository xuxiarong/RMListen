package com.rm.music_exoplayer_lib.manager

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.os.Build
import com.rm.music_exoplayer_lib.utils.ExoplayerLogger.exoLog


/**
 *音频监听器，当音频输出焦点被其他 MediaPlayer 实例抢占，则暂停播放，重新获取到音频输出焦点，自动恢复播放
 * @des:
 * @data: 8/22/20 12:10 PM
 * @Version: 1.0.0
 */
class MusicAudioFocusManager constructor(val context: Context) {
    private var mVolumeWhenFocusLossTransientCanDuck = 0
    private var mFocusRequest: AudioFocusRequest? = null
    private val mAudioManager by lazy {
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
    private val playbackAttributes by lazy {
        AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
    }

    fun setFocusListener(focusListener: OnAudioFocusListener) {
        mFocusListener = focusListener
    }

    /**
     * 请求音频焦点
     */
    fun requestAudioFocus(): Int {
        exoLog("请求音频焦点")
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (mFocusRequest==null){
                mFocusRequest = onAudioFocusChangeListener?.let {
                    AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                        .setAudioAttributes(playbackAttributes)
                        .setWillPauseWhenDucked(true)
                        .setOnAudioFocusChangeListener(it)
                        .build()
                }
            }
            mFocusRequest?.let {
                mAudioManager.requestAudioFocus(it)
            }?:-1
        } else {
            mAudioManager.requestAudioFocus(
                onAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN
            )
        }

    }
    //重新获取
    fun cxAudioFocus(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mFocusRequest?.let {
                mAudioManager.requestAudioFocus(it)
            }?:-1
        } else {
            mAudioManager.requestAudioFocus(
                onAudioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN
            )
        }
    }

    /**
     * 停止播放释放音频焦点
     */
    fun releaseAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mFocusRequest?.let { mAudioManager.abandonAudioFocusRequest(it) }
        } else {
            mAudioManager.abandonAudioFocus(onAudioFocusChangeListener)
        }
    }

    private val onAudioFocusChangeListener: OnAudioFocusChangeListener? =
        OnAudioFocusChangeListener { focusChange ->
            val volume: Int
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK -> {
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
                    exoLog("恢复获得焦点")
                }
                AudioManager.AUDIOFOCUS_LOSS -> {
                    exoLog("被其他播放器抢占")
                    mFocusListener?.onFocusSeize(focusChange)
                    releaseAudioFocus()
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                    exoLog("暂时失去焦点")
                    mFocusListener?.onFocusOut()
                }
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                    exoLog("瞬间失去焦点")
                    if (mFocusListener?.isPlaying == true) {
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

        fun onFocusSeize(i: Int)

        /**
         * 内部播放器是否正在播放
         * @return 为true正在播放
         */
        val isPlaying: Boolean
    }

    var mFocusListener: OnAudioFocusListener? = null


}