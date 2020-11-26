package com.rm.module_play.view

import android.content.Context
import android.util.AttributeSet
import com.airbnb.lottie.LottieAnimationView
import com.rm.baselisten.BaseConstance
import com.rm.baselisten.model.BasePlayStatusModel
import com.rm.baselisten.util.setPlayOnClickNotDoubleListener
import com.rm.business_lib.PlayGlobalData


/**
 * desc   :
 * date   : 2020/11/03
 * version: 1.0
 */
class PlayControlView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        LottieAnimationView(context, attrs, defStyleAttr) {
    var startPlayVar: (() -> Unit)? = {}
    var pausePlayVar: (() -> Unit)? = {}
    var resetPlayVar: (() -> Unit)? = {}
    var initFinish = false
    var isStart = false

    fun processPlayStatus(playState: BasePlayStatusModel) {
        if(playState.isBuffering()){
            return
        }
        if (!initFinish) {
            init()
            return
        }
        if (playState.isStart() && !isStart) {
            startAnim()
        } else if (playState.isPause() && isStart) {
            pauseAnim()
        }
    }

    fun init() {
        postDelayed({
            startAnim()
            BaseConstance.basePlayStatusModel.get()?.let {
                if (it.isPause()) {
                    startPlayVar?.let { startPlay ->
                        startPlay()
                    }
                }
            }
            setPlayOnClickNotDoubleListener {
                if(isStart){
                    pauseAnim()
                    pausePlayVar?.let { pausePlay ->
                        pausePlay()
                    }
                }else{
                    startAnim()
                    startPlayVar?.let { startPlay ->
                        val statusModel = BaseConstance.basePlayStatusModel.get()
                        if(statusModel!=null){
                            if(statusModel.playEnd()){
                                resetPlayVar?.let { resetPlay ->
                                    resetPlay()
                                }
                            }else{
                                startPlay()
                            }
                        }
                    }
                }
            }

        },300)
        initFinish = true
    }

    private fun startAnim() {
        isStart = true
        setAnimation("play_start.json")
        if (isAnimating) {
            clearAnimation()
        }
        playAnimation()
    }

    private fun pauseAnim() {
        isStart = false
        setAnimation("play_pause.json")
        if (isAnimating) {
            clearAnimation()
        }
        playAnimation()
    }
}