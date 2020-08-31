package com.rm.module_play.playview

import android.widget.FrameLayout
import com.rm.baselisten.util.Cxt
import com.rm.baselisten.util.dip
import com.rm.module_play.activity.PlayActivity
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger

/**
 *
 * @des:播放状态栏
 * @data: 8/28/20 10:48 AM
 * @Version: 1.0.0
 */
class GlobalplayHelp private constructor() : MusicPlayerEventListener {
    companion object {
        val instance: GlobalplayHelp by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            GlobalplayHelp()
        }
    }

    val globalView by lazy {
        GlobalPlay(Cxt.context).apply {
            setRadius(Cxt.context.dip(19).toFloat())
            setBarWidth(Cxt.context.dip(2).toFloat())
            setOnClickListener {
                PlayActivity.startActivity(context)
            }

        }
    }

    fun addOnPlayerEventListener() {
        musicPlayerManger.addOnPlayerEventListener(this)
    }

    fun addGlobalPlayHelp(viewParent: FrameLayout, layoutParams: FrameLayout.LayoutParams) {
        (instance.globalView.parent as FrameLayout).removeView(globalView)
        viewParent.addView(globalView, layoutParams)
    }

    override fun onMusicPlayerState(playerState: Int, message: String?) {
    }

    override fun onPrepared(totalDurtion: Long) {
    }

    override fun onBufferingUpdate(percent: Int) {
    }

    override fun onInfo(event: Int, extra: Int) {
    }

    override fun onPlayMusiconInfo(musicInfo: BaseAudioInfo, position: Int) {
    }

    override fun onMusicPathInvalid(musicInfo: BaseAudioInfo, position: Int) {
    }

    override fun onTaskRuntime(
        totalDurtion: Long,
        currentDurtion: Long,
        alarmResidueDurtion: Long,
        bufferProgress: Int
    ) {
        globalView.setProgress(currentDurtion.toFloat() / totalDurtion.toFloat())
    }

    override fun onPlayerConfig(playModel: Int, alarmModel: Int, isToast: Boolean) {
    }


}