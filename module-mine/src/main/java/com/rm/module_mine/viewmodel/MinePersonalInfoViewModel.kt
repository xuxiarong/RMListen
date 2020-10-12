package com.rm.module_mine.viewmodel

import android.view.View
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.LoginUserBean
import com.rm.business_lib.loginUser
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.activity.MineNicknameSettingActivity
import com.rm.module_mine.activity.MinePersonalSignatureSettingActivity

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MinePersonalInfoViewModel : BaseVMViewModel() {
    val userInfo = loginUser

    val sex = ObservableField<String>()

    val sexDialog by lazy { CommBottomDialog() }

    init {
        userInfo.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val bean = userInfo.get()!!
                sex.set(getSexStr(bean))
            }
        })
    }


    /**
     * 获取性别
     */
    fun getSexStr(userInfo: LoginUserBean): String {
        return when (userInfo.gender) {
            "0" -> {
                BaseApplication.CONTEXT.getString(R.string.mine_secret)
            }
            "1" -> {
                BaseApplication.CONTEXT.getString(R.string.mine_man)
            }
            "2" -> {
                BaseApplication.CONTEXT.getString(R.string.mine_woman)
            }
            else -> {
                BaseApplication.CONTEXT.getString(R.string.mine_unfilled)
            }
        }
    }

    /**
     * 昵称点击事件
     */
    fun clickNickname() {
        startActivity(MineNicknameSettingActivity::class.java)
    }

    /**
     * 性别点击事件
     */
    fun clickSex(view: View) {
        getActivity(view.context)?.let {
            sexDialog.showCommonDialog(
                it,
                R.layout.mine_dialog_bottom_select_sex,
                this,
                BR.viewModel
            )
        }
    }

    /**
     * 地址点击事件
     */
    fun clickAddress() {}

    /**
     * 生日点击事件
     */
    fun clickBirthday() {}

    /**
     * 个性签名点击事件
     */
    fun clickPersonalSignature() {
        startActivity(MinePersonalSignatureSettingActivity::class.java)
    }

    /**
     * 性别取消
     */
    fun sexDialogCancelFun() {
        sexDialog.dismiss()
    }

    /**
     * 男
     */
    fun sexDialogManFun() {}

    /**
     * 女
     */
    fun sexDialogWoManFun() {}

    /**
     * 保密
     */
    fun sexDialogSecretFun() {}
}