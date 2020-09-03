package com.rm.module_play.activity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.PowerManager
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.view.updateLayoutParams
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.baselisten.utilExt.dip
import com.rm.baselisten.utilExt.screenWidth
import com.rm.module_play.R

import com.rm.module_play.helper.MusicClickControler
import com.rm.module_play.view.MusicSildingLayout.OnSildingFinishListener
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.constants.*
import com.rm.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import kotlinx.android.synthetic.main.music_activity_lock.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @des:
 * @data: 9/2/20 11:20 AM
 * @Version: 1.0.0
 */
class MusicLockActivity : BaseActivity(),
    MusicPlayerEventListener {
    private var mDiscObjectAnimator: ObjectAnimator? = null
    override fun getLayoutId(): Int = R.layout.music_activity_lock
    private var mHandler: Handler? = null
    private var discIsPlaying = false
    private var mClickControler: MusicClickControler? = null

    override fun initData() {
        //去除锁和在锁屏界面显示此Activity
        music_silding_root.setOnSildingFinishListener(object : OnSildingFinishListener {
            override fun onSildingFinish() {
                finish()

            }
        })
        music_silding_root.setTouchView(window.decorView)
        music_lock_time.updateLayoutParams<RelativeLayout.LayoutParams> {
            setMargins(0, navigationBarHeight() + dip(25), 0, 0)
        }

        val onClickListener =
            View.OnClickListener { v ->
                if (mClickControler?.canTrigger()==true) {
                    val id = v.id
                    if (id == R.id.music_lock_pause) {
                        musicPlayerManger.playOrPause()
                    } else if (id == R.id.music_lock_last) {
                        musicPlayerManger.playLastMusic()
                    } else if (id == R.id.music_lock_next) {
                        musicPlayerManger.playNextMusic()
                    } else if (id == R.id.music_lock_collect) {
                        if (null != music_lock_collect.tag) {
                            val (audioPath, audioCover) = music_lock_collect.tag as BaseAudioInfo
                            if (music_lock_collect.isSelected) {
                                val isSuccess =
                                    true //"   MusicPlayerManager.Companion.getMusicPlayerManger().unCollectMusic(audioInfo.getAudioId());"
                                if (isSuccess) {
                                    music_lock_collect.isSelected = false
                                }
                            } else {
                                val isSuccess =
                                    true //MusicPlayerManager.getInstance().collectMusic(audioInfo);
                                if (isSuccess) {
                                    music_lock_collect.isSelected = true
                                    //                                    MusicPlayerManager.getInstance().observerUpdata(new MusicStatus());
                                }
                            }
                        }
                    } else if (id == R.id.music_lock_model) {
//                        MusicPlayerManager.getInstance().changedPlayerPlayModel();
                    }
                }
            }
        music_lock_last.setOnClickListener(onClickListener)
        music_lock_next.setOnClickListener(onClickListener)
        music_lock_collect.setOnClickListener(onClickListener)
        music_lock_collect.setOnClickListener(onClickListener)
        music_lock_model.setOnClickListener(onClickListener)
        val simpleDateFormat =
            SimpleDateFormat("hh:mm-MM月dd日 E", Locale.CHINESE)
        val date =
            simpleDateFormat.format(Date()).split("-").toTypedArray()
        music_lock_time.text = date[0]
        music_lock_date.text = date[1]
        //闹钟模式
        val playerModel = 1 //"MusicPlayerManager.getInstance().getPlayerModel();"
//        music_lock_model.setImageResource(getResToPlayModel(playerModel, false))
        //播放对象、状态
        val audioInfo =
            musicPlayerManger.getCurrentPlayerMusic()
        music_lock_pause.setImageResource(getPauseIcon(musicPlayerManger.getPlayerState()))
        val discSize = (screenWidth * SCALE_DISC_LOCK_SIZE);
        music_lock_cover.updateLayoutParams {
            height = discSize.toInt()
            width = discSize.toInt()
        }

        updateMusicData(audioInfo)
        musicPlayerManger.addOnPlayerEventListener(this)
        mDiscObjectAnimator = getDiscObjectAnimator(music_lock_cover)
        mClickControler = MusicClickControler()
        mClickControler?.init(1, 300)
    }

    override fun initView() {
        //去除锁和在锁屏界面显示此Activity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }else{
            window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED

            )
        }


    }
    /**
     * 更新当前处理的对象
     * @param audioInfo
     */
    private fun updateMusicData(audioInfo: BaseAudioInfo?) {
        if (null != audioInfo) {
            //是否已收藏
            val isExist =
                true //SqlLiteCacheManager.getInstance().isExistToCollectByID(audioInfo.getAudioId());
//            mMusicCollect!!.isSelected = isExist
//            mMusicCollect!!.tag = audioInfo
            //            mMusicTitle.setText(audioInfo.getAudioName());
//            mMusicAnchor.setText(audioInfo.getNickname());
//            MusicUtils.getInstance().setMusicComposeFront(MusicLockActivity.this,mMusicCover,
//                    MusicUtils.getInstance().getMusicFrontPath(audioInfo),MusicConstants.SCALE_DISC_LOCK_SIZE
//                    ,MusicConstants.SCALE_MUSIC_PIC_LOCK_SIZE,R.drawable.ic_music_disc,R.drawable.ic_music_juke_default_cover);
        }
    }

    /**
     * 实例化旋转动画对象
     * @param view
     * @return
     */
    private fun getDiscObjectAnimator(view: View?): ObjectAnimator {
        val objectAnimator =
            ObjectAnimator.ofFloat(view, View.ROTATION, 0f, 360f)
        objectAnimator.repeatCount = ValueAnimator.INFINITE
        objectAnimator.duration = 20 * 1000.toLong()
        objectAnimator.interpolator = LinearInterpolator()
        return objectAnimator
    }

    /**
     * 播放按钮状态
     * @return
     * @param playerState
     */
    private fun getPauseIcon(playerState: Int): Int {
//        switch (playerState) {
//            case MusicConstants.MUSIC_PLAYER_PREPARE:
//            case MusicConstants.MUSIC_PLAYER_PLAYING:
//            case MusicConstants.MUSIC_PLAYER_BUFFER:
//                return R.drawable.music_player_pause_selector;
//            case MusicConstants.MUSIC_PLAYER_STOP:
//            case MusicConstants.MUSIC_PLAYER_ERROR:
//                return R.drawable.music_player_play_selector;
//        }
        return R.drawable.music_player_play_selector
    }

    /**
     * 获取对应播放模式ICON
     * @param playerModel
     * @param isToast 是否吐司提示
     * @return
     */
    private fun getResToPlayModel(playerModel: Int, isToast: Boolean): Int {
        //        if(isToast){
//            Toast.makeText(MusicLockActivity.this,MediaUtils.getInstance().getPlayerModelToString(MusicLockActivity.this,playerModel),Toast.LENGTH_SHORT).show();
//        }
        return 1
    }

    override fun onResume() {
        super.onResume()
       setStatusBar(R.color.color_FF8D006A)
        jukeBoxResume()
    }

    override fun onPause() {
        super.onPause()
        jukeBoxPause()
    }

    /**
     * 封面动画开始
     */
    @Synchronized
    private fun jukeBoxResume() {
        if (null != mDiscObjectAnimator && musicPlayerManger.isPlaying()) {
            if (discIsPlaying) {
                return
            }
            discIsPlaying = true
            if (mDiscObjectAnimator?.isPaused == true) {
                mDiscObjectAnimator?.resume()
            } else {
                mDiscObjectAnimator?.start()
            }
        }
    }

    /**
     * 封面动画暂停
     */
    @Synchronized
    private fun jukeBoxPause() {
        discIsPlaying = false
        mDiscObjectAnimator?.pause()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            onBackPressed()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {}
    override fun onDestroy() {
        super.onDestroy()

        lock_tip.onDestroy()
        if (null != mHandler) {
            mHandler?.removeMessages(0)
            mHandler?.removeCallbacksAndMessages(null)
            mHandler = null
        }
        mClickControler = null
        if (null != mDiscObjectAnimator) {
            discIsPlaying = false
            mDiscObjectAnimator?.cancel()
            mDiscObjectAnimator = null
            music_lock_cover.rotation = 0f
        }
        musicPlayerManger.removePlayerListener(this)
    }
    //========================================播放器内部状态==========================================
    /**
     * 播放器内部状态发生了变化
     * @param playerState 播放器内部状态
     * @param message
     */
    override fun onMusicPlayerState(playerState: Int, message: String?) {
        if (null != mHandler) {
            mHandler?.post {
                if (playerState == MUSIC_PLAYER_ERROR && !TextUtils.isEmpty(message)) {
                    Toast.makeText(this@MusicLockActivity, message, Toast.LENGTH_SHORT).show()
                }
                music_lock_pause?.setImageResource(getPauseIcon(playerState))
                if (playerState == MUSIC_PLAYER_PREPARE
                    || playerState == MUSIC_PLAYER_PLAYING
                ) {
                    jukeBoxResume()
                } else if (playerState == MUSIC_PLAYER_PAUSE || playerState == MUSIC_PLAYER_ERROR || playerState == MUSIC_PLAYER_STOP
                ) {
                    jukeBoxPause()
                }
            }
        }
    }

    override fun onPrepared(totalDurtion: Long) {}
    override fun onBufferingUpdate(percent: Int) {}
    override fun onInfo(event: Int, extra: Int) {}

    /**
     * 播放器正在处理的对象发生了变化
     * @param musicInfo 正在播放的对象
     * @param position 当前正在播放的位置
     */
    //    @Override
    //    public void onPlayMusiconInfo(final BaseAudioInfo musicInfo, int position) {
    //        if(null!=musicInfo&&null!=mHandler){
    //            mHandler.post(new Runnable() {
    //                @Override
    //                public void run() {
    //                    updateMusicData(musicInfo);
    //                }
    //            });
    //        }
    //    }
    override fun onMusicPathInvalid(musicInfo: BaseAudioInfo, position: Int) {}
    override fun onTaskRuntime(
        totalDurtion: Long,
        currentDurtion: Long,
        alarmResidueDurtion: Long,
        bufferProgress: Int
    ) {
        if (null != mHandler) {
            mHandler?.post {
                val simpleDateFormat =
                    SimpleDateFormat("hh:mm-MM月dd日 E", Locale.CHINESE)
                val date =
                    simpleDateFormat.format(Date()).split("-").toTypedArray()
                music_lock_time.text = date[0]
                music_lock_time.text = date[1]
            }
        }
    }

    override fun onPlayerConfig(
        playModel: Int,
        alarmModel: Int,
        isToast: Boolean
    ) {
        music_lock_model?.setImageResource(getResToPlayModel(playModel, isToast))

    }

    override fun onPlayMusiconInfo(musicInfo: BaseAudioInfo, position: Int) {}
    override fun onPlayerStateChanged(
        playWhenReady: Boolean,
        playbackState: Int
    ) {
    }


}