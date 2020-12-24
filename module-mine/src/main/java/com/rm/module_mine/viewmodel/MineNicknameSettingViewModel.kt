package com.rm.module_mine.viewmodel

import androidx.databinding.ObservableField
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.EmojiUtils
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.LOGIN_USER_INFO
import com.rm.business_lib.loginUser
import com.rm.module_mine.R
import com.rm.module_mine.activity.MinePersonalInfoActivity.Companion.RESULT_CODE_NICK
import com.rm.module_mine.bean.UpdateUserInfoBean
import com.rm.module_mine.repository.MineRepository

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MineNicknameSettingViewModel(private val repository: MineRepository) : BaseVMViewModel() {
    //输入的内容信息
    val inputText = ObservableField<String>("")

    val inputAction: (String) -> Unit = { inputChange(it) }

    /**
     * 监听输入框内容变化
     */
    private fun inputChange(content: String) {
        inputText.set(content)
        val titleModel = baseTitleModel.value
        if (inputText.get()?.length ?: 0 > 0) {
            titleModel?.setRightEnabled(true)
            titleModel?.setRightTextColor(R.color.business_color_ff5e5e)
        } else {
            titleModel?.setRightEnabled(false)
            titleModel?.setRightTextColor(R.color.business_text_color_666666)
        }
        baseTitleModel.value = titleModel
    }

    /**
     * 修改用户信息
     */
    fun updateUserInfo() {
        loginUser.get()?.let {
            val updateUserInfo = UpdateUserInfoBean(
                inputText.get()!!,
                it.gender!!,
                it.birthday!!,
                it.address!!,
                it.signature!!
            )
            launchOnIO {
                repository.updateUserInfo(updateUserInfo).checkResult(
                    onSuccess = { userBean ->
                        LOGIN_USER_INFO.putMMKV(userBean)
                        loginUser.set(userBean)
                        setResultAndFinish(RESULT_CODE_NICK)
                    },
                    onError = { msg, _ ->
                        showTip("$msg", R.color.business_color_ff5e5e)
                        DLog.i("------>", "$msg")
                    }
                )
            }
        }
    }

    fun clickClear() {
        inputText.set("")
    }

    fun check(s: String): Boolean {
        var b = false
        var tmp = s
        tmp = tmp.replace("\\p{P}".toRegex(), "")
        if (s.length != tmp.length) {
            b = true
        }
        return b
    }
}