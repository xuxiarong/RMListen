package com.rm.module_main.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.rm.baselisten.viewmodel.BaseVMViewModel
import kotlinx.coroutines.delay

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class HomeSplashViewModel : BaseVMViewModel() {
    var isSkipAd = ObservableBoolean(false)
    var skipSecond = ObservableInt(3)


    fun startSkipTimerCount(){
        launchOnUI {
            for(i in 0 until 3){
                delay(1000)
                skipSecond.set(skipSecond.get() - 1)
                if(i == 2){
                    isSkipAd.set(true)
                }
            }
        }
    }

    fun skipSplash(){
        isSkipAd.set(true)
    }
}