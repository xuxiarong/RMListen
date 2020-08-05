package com.rm.listen

import android.os.Bundle
import android.util.Log
import com.lm.mvvmcore.base.BaseActivity
import com.music.player.lib.bean.BaseAudioInfo
import com.music.player.lib.constants.MusicConstants
import com.music.player.lib.manager.MusicPlayerManager
import com.music.player.lib.manager.SqlLiteCacheManager
import com.music.player.lib.model.MusicPlayerConfig
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initConfig()
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    /**
     * 完整的初始化
     */
    private fun initConfig() {


        //音乐播放器配置
        val config = MusicPlayerConfig.Build() //设置默认的闹钟定时关闭模式，优先取用户设置
            .setDefaultAlarmModel(MusicConstants.MUSIC_ALARM_MODEL_0) //设置默认的循环模式，优先取用户设置
            .setDefaultPlayModel(MusicConstants.MUSIC_MODEL_LOOP)

        //音乐播放器初始化
        MusicPlayerManager.getInstance() //内部存储初始化
            .init(applicationContext) //应用播放器配置
            .setMusicPlayerConfig(config) //通知栏交互，默认开启
            .setNotificationEnable(true) //常驻进程开关，默认开启
            .setLockForeground(true) //设置点击通知栏跳转的播放器界面,需开启常驻进程开关
//            .setPlayerActivityName(MusicPlayerActivity::class.java.getCanonicalName()) //设置锁屏界面，如果禁用，不需要设置或者设置为null
//            .setLockActivityName(MusicLockActivity::class.java.getCanonicalName()) //监听播放状态
            .setPlayInfoListener { musicInfo, position ->
                //此处自行存储播放记录
                SqlLiteCacheManager.getInstance().insertHistroyAudio(musicInfo)
            } //重载方法，初始化音频媒体服务,成功之后如果系统还在播放音乐，则创建一个悬浮窗承载播放器
            .initialize(this@MainActivity) { //如果系统正在播放音乐
                if (null != MusicPlayerManager.getInstance().currentPlayerMusic) {
                    MusicPlayerManager.getInstance().createWindowJukebox()
                }
            }
    }

    override fun initView() {
        tv_play.setOnClickListener {
            val baseAudioInfo = BaseAudioInfo()
            baseAudioInfo.audioPath =
                "https://webfs.yun.kugou.com/202008031715/cb4388c9845840640f0054c2ee2b3c6d/G189/M09/18/1D/nZQEAF5FiQaAUUnYACSiLQtae4I363.mp3"
            MusicPlayerManager.getInstance().addPlayMusicToTop(baseAudioInfo)
            MusicPlayerManager.getInstance().play()
        }
    }

    override fun initData() {
        Log.e("suolong", "initData: " )
    }


}