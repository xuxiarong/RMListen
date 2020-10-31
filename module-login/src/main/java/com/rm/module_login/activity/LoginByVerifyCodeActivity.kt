package com.rm.module_login.activity

import android.content.Context
import android.content.Intent
import androidx.databinding.Observable
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.util.spannable.ChangeItem
import com.rm.baselisten.util.spannable.SpannableHelper
import com.rm.baselisten.util.spannable.TextClickListener
import com.rm.business_lib.isLogin
import com.rm.module_login.BR
import com.rm.module_login.R
import com.rm.module_login.databinding.LoginActivityLoginByVerifyCodeBinding
import com.rm.module_login.utils.CountryListDialogHelper
import com.rm.module_login.viewmodel.LoginByVerifyViewModel
import kotlinx.android.synthetic.main.login_activity_login_by_verify_code.*
import kotlinx.android.synthetic.main.login_include_layout_phone_input.*

/**
 * desc   : 验证码登陆界面
 * date   : 2020/08/26
 * version: 1.0
 */
class LoginByVerifyCodeActivity :
    BaseVMActivity<LoginActivityLoginByVerifyCodeBinding, LoginByVerifyViewModel>() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, LoginByVerifyCodeActivity::class.java))
        }
    }

    override fun getLayoutId(): Int = R.layout.login_activity_login_by_verify_code

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {
//        mViewModel.testDialogData.observe(this, Observer {
////            dialogAdapter.setList(mViewModel.testDialogData.value!!)
//        })

        // 监听登陆状态
        isLogin.addOnPropertyChangedCallback(object :Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if(isLogin.get()){
                    // 登陆成功，关闭当前界面
                    finish()
                }
            }
        })
    }

    override fun initView() {
        super.initView()
        setTransparentStatusBar()
        login_include_phone_input_country_code.setOnClickListener {
            CountryListDialogHelper.show(this, mViewModel, mViewModel.phoneInputViewModel)
        }

        login_include_phone_input_arrow_view.setOnClickListener {
            CountryListDialogHelper.show(this, mViewModel, mViewModel.phoneInputViewModel)
        }
    }


//    private lateinit var commonDialog: CommonFragmentDialog

//    override fun onResume() {
//        super.onResume()
//        /**
//         *  mViewModel ：与Dialog相关的ViewModel，建议使用该dialog依赖的Activity的ViewModel
//         *  R.layout.login_dialong_login_status ： dialog的布局文件
//         *  BR.viewModel ： 布局文件中使用的viewModel变量的名字
//         */
//        loginDialog = CommonMvFragmentDialog().apply {
//            this.dialogBackgroundColor = R.color.businessColorPrimary
//            this.gravity = Gravity.BOTTOM
//            this.dialogHasBackground = true
//            this.dialogWidthIsMatchParent = true
//            this.initDialog = {initDialogView()}
//        }
//
//
//        loginDialog.showCommonDialog(this,R.layout.login_dialong_login_status,mViewModel,BR.viewModel)
//
//    }
//
//    fun initDialogView() {
//        val bind = loginDialog.mDataBind as LoginDialongLoginStatusBinding?
//        bind?.loginDialogRv?.bindVerticalLayout(dialogAdapter)
//        mViewModel.getDialogData()
//    }

    override fun initData() {
        // 设置checkbox选择协议相关文本
        SpannableHelper.with(
            login_by_verify_code_tips,
            resources.getString(R.string.login_login_tips_all)
        )
            .addChangeItem(
                ChangeItem(
                    resources.getString(R.string.login_login_tips_user),
                    ChangeItem.Type.COLOR,
                    resources.getColor(R.color.login_high_color),
                    object : TextClickListener {
                        override fun onTextClick(clickContent: String) {
                            ToastUtil.show(this@LoginByVerifyCodeActivity, "用户协议")
                        }
                    })
            )
            .addChangeItem(
                ChangeItem(
                    resources.getString(R.string.login_login_tips_privacy),
                    ChangeItem.Type.COLOR,
                    resources.getColor(R.color.login_high_color),
                    object : TextClickListener {
                        override fun onTextClick(clickContent: String) {
                            ToastUtil.show(this@LoginByVerifyCodeActivity, "隐私保护协议")
                        }
                    })
            ).build()
    }
}