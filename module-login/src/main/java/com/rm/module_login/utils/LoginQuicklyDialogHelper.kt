package com.rm.module_login.utils

import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.databinding.Observable
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.observe
import com.rm.baselisten.dialog.CommonMvFragmentDialog
import com.rm.baselisten.util.spannable.ChangeItem
import com.rm.baselisten.util.spannable.SpannableBuilder
import com.rm.baselisten.util.spannable.SpannableHelper
import com.rm.baselisten.util.spannable.TextClickListener
import com.rm.baselisten.utilExt.Color
import com.rm.baselisten.utilExt.String
import com.rm.baselisten.web.BaseWebActivity
import com.rm.business_lib.LoginPhoneReminder
import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.module_login.BR
import com.rm.module_login.R
import com.rm.module_login.databinding.LoginDialogQuicklyLoginBinding
import com.rm.module_login.viewmodel.dialog.LoginQuicklyViewModel
import kotlinx.android.synthetic.main.login_include_layout_phone_input.*

/**
 * desc   : 快捷登陆弹出框帮助类
 * date   : 2020/09/08
 * version: 1.0
 */
class LoginQuicklyDialogHelper constructor(private val fragmentActivity: FragmentActivity) {

    private val loginQuicklyModel by lazy {
        LoginQuicklyViewModel()
    }

    var loginSuccess: () -> Unit = {}

    // 手机号码输入界面显示动画
    private val phoneShowAnimation by lazy {
        TranslateAnimation(
            Animation.RELATIVE_TO_SELF, -1f,
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
            0f, Animation.RELATIVE_TO_SELF, 0f
        ).apply { duration = 200 }
    }

    // 手机号码输入界面隐藏动画
    private val phoneHideAnimation by lazy {
        TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF,
            0f, Animation.RELATIVE_TO_SELF, 0f
        ).apply { duration = 200 }
    }

    // 验证码输入界面显示动画
    private val verifyCodeShowAnimation by lazy {
        TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 1f,
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
            0f, Animation.RELATIVE_TO_SELF, 0f
        ).apply { duration = 200 }
    }

    // 验证码输入界面隐藏动画
    private val verifyCodeHideAnimation by lazy {
        TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF,
            0f, Animation.RELATIVE_TO_SELF, 0f
        ).apply { duration = 200 }
    }

    private var isShowPhoneInputLayChangedCallback: Observable.OnPropertyChangedCallback? = null
    private var loginQuicklyTips: TextView? = null
    private var spannableHelper: SpannableBuilder? = null

    /**
     * 快速登陆dialog
     */
    private val quicklyLoginDialog by lazy {
        CommonMvFragmentDialog().apply {
            gravity = Gravity.BOTTOM
            dialogWidthIsMatchParent = true
            dialogHasBackground = true
            themeResId = R.style.BottomToTopAnim
            initDialog = {
                // 设置checkbox选择协议相关文本
                val dialogBinding = mDataBind as LoginDialogQuicklyLoginBinding
                loginQuicklyTips = dialogBinding.loginQuicklyTips
                setSpannableText()
                dialogBinding.loginQuicklyPhoneInputLay.loginIncludePhoneInputCountryCode.setOnClickListener {
                    showCountryListDialog()
                }
                dialogBinding.loginQuicklyPhoneInputLay.loginIncludePhoneInputArrowView.setOnClickListener {
                    showCountryListDialog()
                }
                dialog?.setOnShowListener {
                    login_by_verify_code_input?.setSelection(
                        login_by_verify_code_input?.text?.length ?: 0
                    )
                    login_by_verify_code_input?.postDelayed({
                        login_by_verify_code_input.requestFocus()
                        val inputManager =
                            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
                    }, 50)
                }

                // 输入号码界面/输入验证界面 显示或隐藏的监听
                isShowPhoneInputLayChangedCallback =
                    object : Observable.OnPropertyChangedCallback() {
                        override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                            if (loginQuicklyModel.isShowPhoneInputLay.get()!!) {
                                // 显示手机号码输入界面
                                dialogBinding.loginQuicklyInputPhone.startAnimation(
                                    phoneShowAnimation
                                )
                                dialogBinding.loginQuicklyInputVerifyCode.startAnimation(
                                    verifyCodeHideAnimation
                                )
                                // 电话输入框请求输入法的焦点
                                dialogBinding.loginQuicklyPhoneInputLay.loginByVerifyCodeInput.requestFocus()
                            } else {
                                // 显示验证码输入界面
                                dialogBinding.loginQuicklyInputPhone.startAnimation(
                                    phoneHideAnimation
                                )
                                dialogBinding.loginQuicklyInputVerifyCode.startAnimation(
                                    verifyCodeShowAnimation
                                )
                                // 验证码输入框请求输入法的焦点
                                dialogBinding.loginQuicklyVerifyCodeInputEt.requestFocus()

                            }
                        }
                    }
                isShowPhoneInputLayChangedCallback?.let {
                    loginQuicklyModel.isShowPhoneInputLay.addOnPropertyChangedCallback(it)
                }

                loginQuicklyModel.isLoginSuccess.observe(this) {
                    if (it) {
                        // 登陆成功,弹窗消失
                        this.dismiss()
                        loginSuccess()
                    }
                }

                destroyDialog = {
                    spannableHelper?.removeSpan()
                    loginQuicklyTips = null
                    phoneHideAnimation.cancel()
                    phoneShowAnimation.cancel()
                    verifyCodeShowAnimation.cancel()
                    verifyCodeHideAnimation.cancel()

                    isShowPhoneInputLayChangedCallback?.let {
                        loginQuicklyModel.isShowPhoneInputLay.removeOnPropertyChangedCallback(it)
                        isShowPhoneInputLayChangedCallback = null
                    }
                }
            }
        }
    }

    fun show() {
        loginQuicklyModel.clear()
        loginQuicklyModel.phoneInputViewModel.phone.set(
            LoginPhoneReminder.getCurrentActivityInputPhone(context = fragmentActivity)
        )
        quicklyLoginDialog.showCommonDialog(
            fragmentActivity,
            R.layout.login_dialog_quickly_login,
            loginQuicklyModel,
            BR.viewModel
        )
    }

    /**
     * 显示列表国家列表
     */
    private fun showCountryListDialog() {
        CountryListDialogHelper.show(
            fragmentActivity,
            loginQuicklyModel,
            loginQuicklyModel.phoneInputViewModel
        )
    }


    /**
     * 设置富文本
     * @param textView TextView
     */
    private fun setSpannableText() {
        if (loginQuicklyTips == null) {
            return
        }
        spannableHelper = SpannableHelper.with(
            loginQuicklyTips!!,
            fragmentActivity.resources.getString(R.string.login_login_tips_all)
        ).addChangeItem(
            ChangeItem(
                fragmentActivity.String(R.string.login_login_tips_user),
                ChangeItem.Type.COLOR,
                fragmentActivity.Color(R.color.login_high_color),
                object : TextClickListener {
                    override fun onTextClick(clickContent: String) {
                        BaseWebActivity.startBaseWebActivity(
                            loginQuicklyTips!!.context,
                            BusinessRetrofitClient.getUserAgreement()
                        )
                    }
                })
        ).addChangeItem(
            ChangeItem(
                fragmentActivity.String(R.string.login_login_tips_privacy),
                ChangeItem.Type.COLOR,
                fragmentActivity.Color(R.color.login_high_color),
                object : TextClickListener {
                    override fun onTextClick(clickContent: String) {
                        BaseWebActivity.startBaseWebActivity(
                            loginQuicklyTips!!.context,
                            BusinessRetrofitClient.getUserPrivacy()
                        )
                    }
                })
        ).build()
    }
}