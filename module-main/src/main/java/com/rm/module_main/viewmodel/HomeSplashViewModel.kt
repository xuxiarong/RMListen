package com.rm.module_main.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.BusinessAdModel
import com.rm.business_lib.bean.BusinessVersionUrlBean
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_main.R
import com.rm.module_main.repository.MainRepository
import kotlinx.coroutines.delay
import java.io.File

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class HomeSplashViewModel(val repository: MainRepository) : BaseVMViewModel() {
    companion object {
        const val INSTALL_RESULT_CODE = 1003
    }

    var isSkipAd = ObservableBoolean(false)
    var skipSecond = ObservableInt(3)
    var mainAdScreen = ObservableField<BusinessAdModel>()
    var isShowAd = ObservableBoolean(false)
    var downPath = ""

    //版本更新
    val versionInfo = ObservableField<BusinessVersionUrlBean>()

    fun startSkipTimerCount() {
        if (isShowAd.get()) {
            launchOnIO {
                for (i in 0 until 3) {
                    delay(1000)
                    skipSecond.set(skipSecond.get() - 1)
                    if (i == 2) {
                        isSkipAd.set(true)
                    }
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
                        if (null == mainAdScreen.get()) {
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

    /**
     * 版本更新
     */
    fun getLaseVersion() {
        DLog.i("=====>versionInfo", "getLaseVersion")
        launchOnIO {
            repository.homeGetLaseUrl().checkResult(
                onSuccess = {
                    versionInfo.set(it)
                }, onError = {
                    showTip("$it", R.color.business_color_ff5e5e)
                    versionInfo.set(null)
                })
        }
    }


    /**
     * 升级dialog
     */
    fun showUploadDialog(
        activity: FragmentActivity,
        enforceUpdate: Boolean,
        sureIsDismiss: Boolean,
        sureBlock: () -> Unit?,
        cancelBlock: () -> Unit? = {}
    ) {
        versionInfo.get()?.let {
            RouterHelper.createRouter(HomeService::class.java)
                .showUploadDownDialog(
                    activity,
                    it,
                    INSTALL_RESULT_CODE,
                    enforceUpdate,
                    downloadComplete = { path -> downPath = path },
                    sureIsDismiss = sureIsDismiss,
                    sureBlock = sureBlock,
                    cancelBlock = cancelBlock
                )
        }
    }

}