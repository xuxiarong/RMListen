package com.rm.module_main.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.BusinessAdModel
import com.rm.module_main.repository.MainRepository
import kotlinx.coroutines.delay

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class HomeSplashViewModel(val repository: MainRepository) : BaseVMViewModel() {
    var isSkipAd = ObservableBoolean(false)
    var skipSecond = ObservableInt(3)
    var mainAdScreen = ObservableField<BusinessAdModel>()

    fun startSkipTimerCount() {
        launchOnUI {
            for (i in 0 until 3) {
                delay(1000)
                skipSecond.set(skipSecond.get() - 1)
                if (i == 2) {
                    isSkipAd.set(true)
                }
            }
        }
    }

    fun skipSplash() {
        isSkipAd.set(true)
    }

    fun getSplashAd() {
        launchOnIO {
            repository.getSplashAd(arrayOf("ad_screen")).checkResult(
                    onSuccess = { mainAdResultModel ->
                        mainAdResultModel.ad_screen?.forEach {
                            if(null == mainAdScreen.get()){
                                mainAdScreen.set(it)
                            }
                            DLog.d("suolong", "it.ad_name = ${it.ad_name}")
                        }
                    },
                    onError = {
                        DLog.d("suolong", "error = ${it ?: ""}")
                    }
            )
        }
    }

}