package com.rm.module_login.activity

import android.content.Context
import android.content.Intent
import com.rm.baselisten.dialog.CommonMvFragmentDialog
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.util.spannable.ChangeItem
import com.rm.baselisten.util.spannable.SpannableHelper
import com.rm.baselisten.util.spannable.TextClickListener
import com.rm.module_login.BR
import com.rm.module_login.R
import com.rm.module_login.databinding.LoginActivityLoginByVerifyCodeBinding
import com.rm.module_login.viewmodel.LoginByVerifyViewModel
import kotlinx.android.synthetic.main.login_activity_login_by_verify_code.*

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
    }

    override fun initView() {
        super.initView()
        mViewModel.context = this
    }

    override fun onResume() {
        super.onResume()
        CommonMvFragmentDialog(mViewModel,R.layout.login_dialong_login_status,BR.viewModel).showCommonDialog(this)
    }

    override fun initData() {
//        mViewModel.countryCode.set("+86")
        // 设置checkbox选择协议相关文本
        SpannableHelper.with(login_by_verify_code_tips,resources.getString(R.string.login_login_tips_all))
            .addChangeItem(ChangeItem(resources.getString(R.string.login_login_tips_user),ChangeItem.Type.COLOR,resources.getColor(R.color.login_high_color),object :TextClickListener{
                override fun onTextClick(clickContent: String) {
                    ToastUtil.show(this@LoginByVerifyCodeActivity,"用户协议")
                }
            }))
            .addChangeItem(ChangeItem(resources.getString(R.string.login_login_tips_privacy),ChangeItem.Type.COLOR,resources.getColor(R.color.login_high_color),object :TextClickListener{
                override fun onTextClick(clickContent: String) {
                    ToastUtil.show(this@LoginByVerifyCodeActivity,"隐私保护协议")
                }
            })).build()
    }
}