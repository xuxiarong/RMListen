package com.rm.music_exoplayer_lib.activity

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.*
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.view.updateLayoutParams
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.utilExt.dip
import com.rm.baselisten.utilExt.screenWidth
import com.rm.music_exoplayer_lib.BR

import com.rm.music_exoplayer_lib.R
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.constants.*
import com.rm.music_exoplayer_lib.databinding.MusicActivityLockBinding
import com.rm.music_exoplayer_lib.helper.MusicClickControler
import com.rm.music_exoplayer_lib.listener.MusicPlayerEventListener
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import com.rm.music_exoplayer_lib.musicModules
import com.rm.music_exoplayer_lib.view.MusicSildingLayout
import com.rm.music_exoplayer_lib.viewModel.MusicLockViewModel
import kotlinx.android.synthetic.main.music_activity_lock.*
import org.koin.core.context.loadKoinModules
import java.text.SimpleDateFormat
import java.util.*


/**
 * @des:
 * @data: 9/2/20 11:20 AM
 * @Version: 1.0.0
 */
class MusicLockActivity : BaseVMActivity<MusicActivityLockBinding, MusicLockViewModel>() {
    private var mDiscObjectAnimator: ObjectAnimator? = null
    override fun getLayoutId(): Int = R.layout.music_activity_lock
    private var mHandler: Handler? = null
    private var discIsPlaying = false
    private var mClickControler: MusicClickControler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        loadKoinModules(musicModules)
        super.onCreate(savedInstanceState)
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
//        fullScreen(this)
//    }
//
//    fun fullScreen(activity: Activity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
//                val window: Window = activity.window
//                val decorView: View = window.getDecorView()
//                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
//                val option = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
//                decorView.systemUiVisibility = option
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//                window.setStatusBarColor(Color.TRANSPARENT)
//            } else {
//                val window: Window = activity.window
//                val attributes: WindowManager.LayoutParams = window.getAttributes()
//                val flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                attributes.flags = attributes.flags or flagTranslucentStatus
//                window.setAttributes(attributes)
//            }
//        }
//    }


    override fun initData() {
        //去除锁和在锁屏界面显示此Activity
        music_silding_root.setOnSildingFinishListener(object :
                MusicSildingLayout.OnSildingFinishListener {
            override fun onSildingFinish() {
                finish()

            }
        })
        music_silding_root.setTouchView(window.decorView)

        val simpleDateFormat =
                SimpleDateFormat("hh:mm-MM月dd日 E", Locale.CHINESE)
        val date =
                simpleDateFormat.format(Date()).split("-").toTypedArray()
        music_lock_time.text = date[0]
        music_lock_date.text = date[1]

        //播放对象、状态
        val audioInfo = musicPlayerManger.getCurrentPlayerMusic()
        mClickControler = MusicClickControler()
        mClickControler?.init(1, 300)
    }

    override fun initView() {
        //去除锁和在锁屏界面显示此Activity
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                            or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED

            )
        }
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
        }
    }

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {

    }

}