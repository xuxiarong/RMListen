package com.rm.module_listen.player

import android.media.AudioManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rm.module_listen.R
import com.rm.music_ijkplayer_lib.player.IjkMediaPlayer

class TestPalyerActivity : AppCompatActivity() {
    /**
     * 播放器
     */
    private var mMediaPlayer:IjkMediaPlayer?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_palyer)
        initMediaPlayer()
    }
    fun initMediaPlayer(){
        mMediaPlayer = IjkMediaPlayer();
        mMediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        mMediaPlayer?.setDataSource("https://img.meidongli.net/198/2020/08/10/cnJnZzQtY3JqNDkuYWFjPzE1OTcwNDc2ODc2MjA=")
        mMediaPlayer?.prepareAsync()
        mMediaPlayer?.start()
    }
}