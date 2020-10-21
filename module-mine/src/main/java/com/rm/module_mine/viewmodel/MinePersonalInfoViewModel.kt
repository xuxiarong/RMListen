package com.rm.module_mine.viewmodel

import android.content.Context
import android.view.View
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.TimeUtils
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.LOGIN_USER_INFO
import com.rm.business_lib.bean.LoginUserBean
import com.rm.business_lib.loginUser
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.activity.MineNicknameSettingActivity
import com.rm.module_mine.activity.MinePersonalSignatureSettingActivity
import com.rm.module_mine.bean.UpdateUserInfoBean
import com.rm.module_mine.databinding.MineDialogBottomSelectBirthdayBinding
import com.rm.module_mine.repository.MineRepository
import kotlin.math.log

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MinePersonalInfoViewModel(private val repository: MineRepository) : BaseVMViewModel() {

    val userInfo = loginUser

    val sex = ObservableField<String>(getSexStr(loginUser.get()!!))

    //当前选中的生日
    private val birthday = ObservableField<String>()

    val birthdayBlock: (String) -> Unit = { birthday.set(it) }

    //性别 dialog
    private val sexDialog by lazy { CommBottomDialog() }

    //头像dialog
    private val imageDialog by lazy { CommBottomDialog() }

    //生日
    private val birthdayDialog by lazy { CommBottomDialog() }


    fun updateUserInfo(updateUserInfo: UpdateUserInfoBean) {
        userInfo.get()?.let {
            launchOnIO {
                repository.updateUserInfo(updateUserInfo).checkResult(
                    onSuccess = { userBean ->
                        LOGIN_USER_INFO.putMMKV(userBean)
                        loginUser.set(userBean)
                        sex.set(getSexStr(userBean))
                    },
                    onError = {
                        showToast("修改失败")
                        DLog.i("------>", "$it")
                    }
                )
            }
        }
    }

    /**
     * 获取性别
     */
    private fun getSexStr(userInfo: LoginUserBean): String {
        return when (userInfo.gender) {
            0 -> {
                BaseApplication.CONTEXT.getString(R.string.mine_secret)
            }
            1 -> {
                BaseApplication.CONTEXT.getString(R.string.mine_man)
            }
            2 -> {
                BaseApplication.CONTEXT.getString(R.string.mine_woman)
            }
            else -> {
                BaseApplication.CONTEXT.getString(R.string.mine_unfilled)
            }
        }
    }

    /**
     * 头像点击事件
     */
    fun clickAvatar(view: View) {
        getActivity(view.context)?.let {
            imageDialog.showCommonDialog(
                it,
                R.layout.mine_dialog_bottom_select_image,
                this,
                BR.viewModel
            )
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
    fun clickAddress(context: Context) {
        getActivity(context)?.let {
            RouterHelper.createRouter(LoginService::class.java).startCountry(it, 100)
        }
    }

    /**
     * 生日点击事件
     */
    fun clickBirthday(view: View) {
        getActivity(view.context)?.let {
            birthdayDialog.showCommonDialog(
                it,
                R.layout.mine_dialog_bottom_select_birthday,
                this,
                BR.viewModel
            ).apply {
                birthdayDialog.mDataBind?.let { binding ->
                    val dataBinding = binding as MineDialogBottomSelectBirthdayBinding
                    val time = dataBinding.mineDialogBirthdayTimePicker.getTime()
                    birthday.set(time)
                }
            }
        }
    }

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
     * 性别点击事件
     */
    fun sexDialogClickManFun(sex: Int) {
        userInfo.get()?.let {
            updateUserInfo(
                UpdateUserInfoBean(
                    it.nickname,
                    sex,
                    it.birthday,
                    it.address,
                    it.signature
                )
            )
        }
        sexDialog.dismiss()
    }


    /**
     * 相机
     */
    fun imageDialogCameraFun() {

    }

    /**
     * 相册
     */
    fun imageDialogAlbumFun() {

    }

    /**
     * 取消
     */
    fun imageDialogCancelFun() {
        imageDialog.dismiss()
    }

    /**
     * 生日确定
     */
    fun birthdayDialogSureFun() {
        birthday.get()?.let { bir ->
            userInfo.get()?.let {
                updateUserInfo(
                    UpdateUserInfoBean(
                        it.nickname,
                        it.gender,
                        bir.trimEnd(),
                        it.address,
                        it.signature
                    )
                )
            }
        }
        birthdayDialog.dismiss()
    }

    /**
     * 生日取消
     */
    fun birthdayDialogSureCancel() {
        birthdayDialog.dismiss()
    }


}