package com.rm.module_mine.viewmodel

import android.Manifest
import android.content.Context
import android.view.View
import androidx.databinding.ObservableField
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.dialog.CommBottomDialog
import com.rm.baselisten.mvvm.BaseActivity
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.FileUtils
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.LOGIN_USER_INFO
import com.rm.business_lib.bean.LoginUserBean
import com.rm.business_lib.loginUser
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.activity.MineSettingNicknameActivity
import com.rm.module_mine.activity.MineSettingPersonalSignatureActivity
import com.rm.module_mine.bean.UpdateUserInfoBean
import com.rm.module_mine.databinding.MineDialogBottomSelectBirthdayBinding
import com.rm.module_mine.repository.MineRepository
import com.rm.module_mine.util.CommonTakePhotoHelp


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

    //用户头像路径
    val userIconUrl = ObservableField<String>(loginUser.get()?.avatar_url)

    val birthdayBlock: (String) -> Unit = { birthday.set(it) }

    //性别 dialog
    private val sexDialog by lazy { CommBottomDialog() }

    var photoHelp: CommonTakePhotoHelp? = null


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
                        showTip("修改成功")
                    },
                    onError = { it, _ ->
                        showTip("$it", R.color.business_color_ff5e5e)
                    }
                )
            }
        }
    }

    fun uploadPic(filePath: String) {
        launchOnIO {
            repository.uploadAvatar(filePath).checkResult(
                onSuccess = {
                    showTip("修改成功")
                    loginUser.get()?.let { userBean ->
                        userBean.avatar_url = it.url
                        loginUser.set(userBean)
                        LOGIN_USER_INFO.putMMKV(userBean)
                    }
                    userIconUrl.set(it.url)
                    val delete = FileUtils.delete(filePath)
                    DLog.i("=======>", "$delete")
                },
                onError = { it, _ ->
                    showTip("$it", R.color.business_color_ff5e5e)

                }
            )
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
            if (it is BaseActivity) {
                it.requestPermissionForResult(
                    mutableListOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), actionDenied = {
                        showTip(it.getString(R.string.business_listen_storage_permission_refuse))
                    }, actionGranted = {
                        photoHelp = CommonTakePhotoHelp(activity = it)
                        photoHelp?.showTakePhoto()
                    },
                    actionPermanentlyDenied = {
                        showTip(it.getString(R.string.business_listen_to_set_storage_permission))
                    })
            }
        }
    }

    /**
     * 昵称点击事件
     */
    fun clickNickname() {
        startActivityForResult(MineSettingNicknameActivity::class.java, 100)
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
            )
        }
    }

    /**
     * 个性签名点击事件
     */
    fun clickPersonalSignature() {
        startActivityForResult(MineSettingPersonalSignatureActivity::class.java, 100)
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
                    it.nickname!!,
                    sex,
                    it.birthday!!,
                    it.address!!,
                    it.signature!!
                )
            )
        }
        sexDialog.dismiss()
    }


    /**
     * 生日确定
     */
    fun birthdayDialogSureFun() {
        if (birthday.get() != null) {
            uploadUser(birthday.get()!!.trimEnd())
        } else {
            birthdayDialog.mDataBind?.let { binding ->
                val dataBinding = binding as MineDialogBottomSelectBirthdayBinding
                val time = dataBinding.mineDialogBirthdayTimePicker.getTime()
                birthday.set(time.trimEnd())
                uploadUser(time.trimEnd())
            }
        }
        birthdayDialog.dismiss()
    }

    private fun uploadUser(birthday: String) {
        userInfo.get()?.let {
            updateUserInfo(
                UpdateUserInfoBean(
                    it.nickname!!,
                    it.gender!!,
                    birthday,
                    it.address!!,
                    it.signature!!
                )
            )
        }
    }

    /**
     * 生日取消
     */
    fun birthdayDialogSureCancel() {
        birthdayDialog.dismiss()
    }

}