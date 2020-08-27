package com.rm.module_login.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.module_login.BR
import com.rm.module_login.R
import com.rm.module_login.databinding.LoginActivityVerificationCodeInputBinding
import com.rm.module_login.viewmodel.VerificationInputViewModel
import java.lang.ref.WeakReference

/**
 * desc   : 验证码输入界面
 * date   : 2020/08/27
 * version: 1.0
 */
class VerificationInputActivity :
    BaseVMActivity<LoginActivityVerificationCodeInputBinding, VerificationInputViewModel>() {

    private val countdownTimeHandler by lazy { CountdownTimeHandler(this) }

    companion object {
        fun startActivity(context: Context, phoneNumber: String) {
            context.startActivity(
                Intent(
                    context,
                    VerificationInputActivity::class.java
                ).apply { putExtra("phone", phoneNumber) })
        }
    }

    override fun getLayoutId(): Int = R.layout.login_activity_verification_code_input

    override fun startObserve() {
    }

    override fun initView() {
        val baseTitleModel = BaseTitleModel()
            .setLeftIconClick {
                finish()
            }
        mViewModel.baseTitleModel.value = baseTitleModel
    }

    override fun initData() {
        mViewModel.phone = intent.getStringExtra("phone") as String
        mViewModel.phoneStr.set(
            resources.getString(
                R.string.login_format_verification_phone_tips,
                mViewModel.phone.replace(mViewModel.phone.substring(3, 7), "****")
            )
        )

        countdownTimeHandler.sendEmptyMessageDelayed(0, 1000)
    }

    override fun initModelBrId() = BR.viewModel


    /**
     * 倒计时handler 内部类
     * @property mWeakReference WeakReference<VerificationInputActivity?>
     * @constructor
     */
    @SuppressLint("HandlerLeak")
    inner class CountdownTimeHandler internal constructor(activity: VerificationInputActivity?) :
        Handler() {
        private val mWeakReference: WeakReference<VerificationInputActivity?> =
            WeakReference(activity)

        // 子类必须重写此方法，接收数据
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            //获取MainActivity
            if (msg.what == 0) {
                // 倒计时数量 -1
                mViewModel.countDownTime--
                if (mViewModel.countDownTime > 0) {
                    // 继续倒计时
                    sendEmptyMessageDelayed(0, 1000)
                    mViewModel.countDownTimeStr.set(
                        resources.getString(
                            R.string.login_format_verification_count_down_tips,
                            "${mViewModel.countDownTime}s"
                        )
                    )
                } else {
                    // 倒计时完成
                    mViewModel.countDownTimeStr.set("")
                    mViewModel.reGetCodeStr.set(resources.getString(R.string.login_re_get_verify_code))
                }
            }
        }

    }
}

