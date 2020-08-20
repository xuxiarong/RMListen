package debug

import com.example.music_exoplayer_lib.listener.MusicInitializeCallBack
import com.example.music_exoplayer_lib.manager.MusicPlayerManager
import com.rm.baselisten.activity.BaseActivity
import com.rm.module_play.R

/**
 * desc   :
 * date   : 2020/08/13
 * version: 1.0
 */
class PlayMainActivity : BaseActivity() {
    override fun getLayoutResId(): Int = R.layout.play_activity_main

    override fun initView() {

    }

    override fun initData() {
        MusicPlayerManager.musicPlayerManger.initialize(this,
            MusicInitializeCallBack { })
        MusicPlayerManager.musicPlayerManger.startPlayerMusic(0)
    }
}