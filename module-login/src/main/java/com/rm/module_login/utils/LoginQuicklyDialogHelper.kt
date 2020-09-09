package com.rm.module_login.utils

import android.view.Gravity
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.databinding.Observable
import androidx.fragment.app.FragmentActivity
import com.rm.baselisten.dialog.CommonMvFragmentDialog
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.util.spannable.ChangeItem
import com.rm.baselisten.util.spannable.SpannableHelper
import com.rm.baselisten.util.spannable.TextClickListener
import com.rm.baselisten.utilExt.Color
import com.rm.baselisten.utilExt.String
import com.rm.module_login.BR
import com.rm.module_login.R
import com.rm.module_login.databinding.LoginDialogQuicklyLoginBinding
import com.rm.module_login.viewmodel.dialog.LoginQuicklyViewModel

/**
 * desc   : 快捷登陆弹出框帮助类
 * date   : 2020/09/08
 * version: 1.0
 */
class LoginQuicklyDialogHelper(
    val mViewModel: LoginQuicklyViewModel,
    val fragmentActivity: FragmentActivity
) {
    /**
     * 快速登陆dialog
     */
    private val quicklyLoginDialog by lazy {
        CommonMvFragmentDialog().apply {
            gravity = Gravity.BOTTOM
            dialogWidthIsMatchParent = true
            dialogHasBackground = true
//            dialogHeight = BaseApplication.CONTEXT.dip(752)
            initDialog = {
                // 设置checkbox选择协议相关文本
                val dialogBinding = this.mDataBind as LoginDialogQuicklyLoginBinding
                setSpannableText(dialogBinding.loginQuicklyTips)
                dialogBinding.loginQuicklyPhoneInputLay.loginIncludePhoneInputCountryCode.setOnClickListener {
                    showCountryListDialog()
                }
                dialogBinding.loginQuicklyPhoneInputLay.loginIncludePhoneInputArrowView.setOnClickListener {
                    showCountryListDialog()
                }

                // 输入号码界面/输入验证界面 显示或隐藏的监听
                mViewModel.isShowPhoneInputLay.addOnPropertyChangedCallback(object :
                    Observable.OnPropertyChangedCallback() {
                    override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                        if (mViewModel.isShowPhoneInputLay.get()!!) {
                            // 显示手机号码输入界面
                            dialogBinding.loginQuicklyInputPhone.startAnimation(phoneShowAnimation)
                            dialogBinding.loginQuicklyInputVerifyCode.startAnimation(
                                verifyCodeHideAnimation
                            )
                        } else {
                            // 显示验证码输入界面
                            dialogBinding.loginQuicklyInputPhone.startAnimation(phoneHideAnimation)
                            dialogBinding.loginQuicklyInputVerifyCode.startAnimation(
                                verifyCodeShowAnimation
                            )
                        }
                    }
                })
            }
        }
    }

    // 手机号码输入界面显示动画
    private val phoneShowAnimation = TranslateAnimation(
        Animation.RELATIVE_TO_SELF, -1f,
        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
        0f, Animation.RELATIVE_TO_SELF, 0f
    ).apply { duration = 200 }

    // 手机号码输入界面隐藏动画
    private val phoneHideAnimation = TranslateAnimation(
        Animation.RELATIVE_TO_SELF, 0f,
        Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF,
        0f, Animation.RELATIVE_TO_SELF, 0f
    ).apply { duration = 200 }

    // 验证码输入界面显示动画
    private val verifyCodeShowAnimation = TranslateAnimation(
        Animation.RELATIVE_TO_SELF, 1f,
        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
        0f, Animation.RELATIVE_TO_SELF, 0f
    ).apply { duration = 200 }

    // 验证码输入界面隐藏动画
    private val verifyCodeHideAnimation = TranslateAnimation(
        Animation.RELATIVE_TO_SELF, 0f,
        Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF,
        0f, Animation.RELATIVE_TO_SELF, 0f
    ).apply { duration = 200 }

    fun show() {
        mViewModel.clear()

        quicklyLoginDialog.showCommonDialog(
            fragmentActivity,
            R.layout.login_dialog_quickly_login,
            mViewModel,
            BR.viewModel
        )
    }

    /**
     * 显示列表国家列表
     */
    private fun showCountryListDialog() {
        CountryListDialogHelper.show(fragmentActivity, mViewModel, mViewModel.phoneInputViewModel)
    }


    /**
     * 设置富文本
     * @param textView TextView
     */
    private fun setSpannableText(textView: TextView) {
        SpannableHelper.with(
            textView,
            fragmentActivity.resources.getString(R.string.login_login_tips_all)
        )
            .addChangeItem(
                ChangeItem(
                    fragmentActivity.String(R.string.login_login_tips_user),
                    ChangeItem.Type.COLOR,
                    fragmentActivity.Color(R.color.login_high_color),
                    object : TextClickListener {
                        override fun onTextClick(clickContent: String) {
                            ToastUtil.show(fragmentActivity, "用户协议")
                        }
                    })
            )
            .addChangeItem(
                ChangeItem(
                    fragmentActivity.String(R.string.login_login_tips_privacy),
                    ChangeItem.Type.COLOR,
                    fragmentActivity.Color(R.color.login_high_color),
                    object : TextClickListener {
                        override fun onTextClick(clickContent: String) {
                            ToastUtil.show(fragmentActivity, "隐私保护协议")
                        }
                    })
            ).build()
    }
}