package com.rm.music_exoplayer_lib.listener

import com.rm.music_exoplayer_lib.bean.BaseAudioInfo

/**
 *
 * @des:如果需要关心正在播放的对象存储播放记录等，注册此监听
 * 如果使用默认的通知栏组件，请实现此接口，返回音频文件的收藏状态
 * @data: 8/19/20 3:25 PM
 * @Version: 1.0.0
 */
interface MusicPlayerInfoListener {

    /**
     * 播放器对象发生了变化
     * @param musicInfo 播放器内部正在处理的音频对象
     * @param position 位置
     */
    fun onPlayMusiconInfo(
        musicInfo: BaseAudioInfo?,
        position: Int
    )
}