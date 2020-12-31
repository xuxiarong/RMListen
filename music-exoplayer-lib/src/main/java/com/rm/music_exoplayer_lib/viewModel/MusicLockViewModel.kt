package com.rm.music_exoplayer_lib.viewModel

import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.PlayGlobalData
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager

/**
 * desc   :
 * date   : 2020/12/27
 * version: 1.0
 */
class MusicLockViewModel : BaseVMViewModel(){
    var playChapter = PlayGlobalData.playChapter

    fun playPre(){
        MusicPlayerManager.musicPlayerManger.playLastMusic()
    }

    fun playNext(){
        MusicPlayerManager.musicPlayerManger.playNextMusic()
    }

    fun playOrPause(){
        if(MusicPlayerManager.musicPlayerManger.isPlaying()){
            MusicPlayerManager.musicPlayerManger.pause()
        }else{
            MusicPlayerManager.musicPlayerManger.play()
        }
    }
}