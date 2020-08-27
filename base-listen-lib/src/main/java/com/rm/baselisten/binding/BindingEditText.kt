package com.rm.baselisten.binding

import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import androidx.databinding.BindingAdapter

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
@BindingAdapter(value = ["afterTextChanged"])
fun EditText.afterTextChanged(action: ((String) -> Unit)?){
    if(action == null){
        return
    }

    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            action(s.toString())
        }
    })
}

@BindingAdapter(value = ["isShowPasswordText"])
fun EditText.isShowPasswordText(isShow: Boolean){
    transformationMethod = if(isShow){
        // 显示密码
        HideReturnsTransformationMethod.getInstance()
    }else{
        // 隐藏密码
        PasswordTransformationMethod.getInstance()
    }
}

