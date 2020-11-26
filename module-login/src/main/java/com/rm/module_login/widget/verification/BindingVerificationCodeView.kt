package com.rm.module_login.widget.verification

import android.view.View
import androidx.databinding.BindingAdapter
import com.rm.baselisten.util.DLog

/**
 * desc   :
 * date   : 2020/08/27
 * version: 1.0
 */
@BindingAdapter(value = ["inputTextChange"])
fun VerificationCodeView.inputTextChange(action: ((String) -> Unit)?) {
    if (action == null) {
        return
    }
    onCodeFinishListener = object : VerificationCodeView.OnCodeFinishListener {
        override fun onComplete(view: View?, content: String) {
            DLog.i("llj", "输入完成！！！---content------>>>$content")
            action(content)
        }

        override fun onTextChange(view: View?, content: String) {
            DLog.d("llj", "输入改变---content------>>>$content")
        }
    }
}

@BindingAdapter(value = ["inputTextClear"])
fun VerificationCodeView.inputTextClear(isClear: Boolean?) {
    if (isClear == true) {
        setEmpty()
    }
}