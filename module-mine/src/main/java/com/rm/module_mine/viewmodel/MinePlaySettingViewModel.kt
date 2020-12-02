package com.rm.module_mine.viewmodel

import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.PlaySettingData

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MinePlaySettingViewModel : BaseVMViewModel() {

    val continueLastPlayVal : (Boolean)-> Unit= {setContinueLastPlay(it)}
    val autoPlayNextVal : (Boolean)-> Unit= {setAutoPlayNext(it)}
    val network234GAlertVal : (Boolean)-> Unit= {setNetwork234GAlert(it)}


    private fun setContinueLastPlay(checked : Boolean){
        PlaySettingData.PLAY_CONTINUE_LAST_PLAY.putMMKV(checked)
    }

    private fun setAutoPlayNext(checked: Boolean){
        PlaySettingData.PLAY_AUTO_PLAY_NEXT.putMMKV(checked)
    }

    private fun setNetwork234GAlert(checked: Boolean){
        PlaySettingData.PLAY_NETWORK_234G_ALERT.putMMKV(checked)
    }

}