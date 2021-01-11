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
import com.rm.business_lib.insertpoint.BusinessInsertConstance
import com.rm.business_lib.insertpoint.BusinessInsertManager
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
class HomeSplashViewModel(private val repository: MainRepository) : BaseVMViewModel() {
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
        mainAdScreen.get()?.ad_id?.let {
            BusinessInsertManager.doInsertKeyAndAd(
                BusinessInsertConstance.INSERT_TYPE_AD_CLOSE,
                it.toString()
            )
        }
    }

    fun getSplashAd() {
        launchOnIO (
           block = {repository.getSplashAd(arrayOf("ad_screen")).checkResult(
               onSuccess = { mainAdResultModel ->
                   mainAdResultModel.ad_screen?.forEach {
                       if (null == mainAdScreen.get()) {
                           mainAdScreen.set(it)
                       }
                       DLog.d("suolong", "it.ad_name = ${it.ad_name}")
                   }
               },
               onError = { it, _ ->
                   isSkipAd.set(true)
               }
           )} ,netErrorBlock = {
                isSkipAd.set(true)
                isShowAd.set(false)
            }
        )
    }

    /**
     * 版本更新
     */
    fun getLaseVersion(action : ()->Unit) {
        launchOnIO(
            block = {
                repository.homeGetLaseUrl().checkResult(
                    onSuccess = {
                        versionInfo.set(it)
                    }, onError = { it, _ ->
                        showTip("$it", R.color.business_color_ff5e5e)
                        versionInfo.set(null)
                        versionInfo.notifyChange()
                    })
            },
            netErrorBlock = {
                action()
            }
        )
    }


    /**
     * 升级dialog
     */
    fun showUploadDialog(
        activity: FragmentActivity,
        sureIsDismiss: Boolean,
        cancelIsFinish: Boolean,
        sureBlock: () -> Unit?,
        cancelBlock: () -> Unit? = {}
    ) {
        versionInfo.get()?.let {
            RouterHelper.createRouter(HomeService::class.java)
                .showUploadDownDialog(
                    activity = activity,
                    versionInfo = it,
                    installCode = INSTALL_RESULT_CODE,
                    dialogCancel = false,
                    cancelIsFinish = cancelIsFinish,
                    downloadComplete = { path -> downPath = path },
                    sureIsDismiss = sureIsDismiss,
                    sureBlock = sureBlock,
                    cancelBlock = cancelBlock
                )
        }
    }

}