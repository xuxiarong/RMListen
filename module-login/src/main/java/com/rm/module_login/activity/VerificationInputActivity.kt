package com.rm.module_login.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import androidx.databinding.Observable
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
        // 登录类型
        const val TYPE_LOGIN = 0

        // 重置密码类型
        const val TYPE_RESET_PWD = 1

        fun getIntent(countryCode: String, phoneNumber: String, type: Int): HashMap<String, Any> {
            return hashMapOf(
                Pair("countryCode", countryCode),
                Pair("phone", phoneNumber),
                Pair("type", type)
            )
        }

        fun startActivity(context: Context,countryCode: String, phoneNumber: String, type: Int){
            context.startActivity(Intent(context,VerificationInputActivity::class.java).apply {
                putExtra("countryCode",countryCode)
                putExtra("phone",phoneNumber)
                putExtra("type",type)
            })
        }
    }

    override fun getLayoutId(): Int = R.layout.login_activity_verification_code_input

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {
        mViewModel.reGetCodeStr.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (TextUtils.isEmpty(mViewModel.reGetCodeStr.get())) {
                    // "重新获取" 字符为空，表明又重新开始倒计时
                    countdownTimeHandler.sendEmptyMessage(0)
                }
            }
        })
    }

    override fun initView() {
        mViewModel.baseTitleModel.value = BaseTitleModel().setLeftIconClick { finish() }
        mViewModel.getCodeType = intent.getIntExtra("type", 0)
        mViewModel.countryCode = intent.getStringExtra("countryCode")
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

    override fun onDestroy() {
        super.onDestroy()
        countdownTimeHandler.removeMessages(0)
    }


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

