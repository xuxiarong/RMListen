package com.rm.module_mine.viewmodel

import androidx.databinding.ObservableField
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.LOGIN_USER_INFO
import com.rm.business_lib.loginUser
import com.rm.module_mine.R
import com.rm.module_mine.activity.MinePersonalInfoActivity.Companion.RESULT_CODE_SIGNATURE
import com.rm.module_mine.bean.UpdateUserInfoBean
import com.rm.module_mine.repository.MineRepository
import kotlinx.coroutines.delay
import java.util.logging.Handler

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MinePersonalSignatureSettingViewModel(private val repository: MineRepository) :
    BaseVMViewModel() {

    //输入的内容信息
    val inputText = ObservableField<String>("")

    val inputAction: (String) -> Unit = { inputChange(it) }

    /**
     * 监听输入框内容变化
     */
    private fun inputChange(content: String) {
        inputText.set(content)
    }

    /**
     * 修改用户信息
     */
    fun updateUserInfo() {
        loginUser.get()?.let {
            val updateUserInfo = UpdateUserInfoBean(
                it.nickname!!,
                it.gender!!,
                it.birthday!!,
                it.address!!,
                inputText.get()!!
            )
            launchOnIO {
                repository.updateUserInfo(updateUserInfo).checkResult(
                    onSuccess = { userBean ->
                        LOGIN_USER_INFO.putMMKV(userBean)
                        loginUser.set(userBean)
                        setResultAndFinish(RESULT_CODE_SIGNATURE)
                    },
                    onError = {
                        showTip("$it", R.color.business_color_ff5e5e)
                        DLog.i("------>", "$it")
                    }
                )
            }
        }
    }
}