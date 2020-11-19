package com.rm.baselisten.binding

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.rm.baselisten.helper.KeyboardStatusDetector

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
@BindingAdapter(value = ["afterTextChanged"])
fun EditText.afterTextChanged(action: ((String) -> Unit)?) {
    if (action == null) {
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

@BindingAdapter(value = ["afterTextChanged"])
fun EditText.afterTextChanged(action: ((Context, String) -> Unit)?) {
    if (action == null) {
        return
    }

    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            action(context,s.toString())
        }
    })
}


/**
 * 不允许空格输入
 */
@BindingAdapter(value = ["afterTextChangedNoSpace"])
fun EditText.afterTextChangedNoSpace(action: ((String) -> Unit)?) {
    if (action == null) {
        return
    }

    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.toString().contains(" ")) {
                // 这里处理的是，允许输入空格
                val str: List<String> = s.toString().split(" ")
                var str1 = ""
                for (i in str.indices) {
                    str1 += str[i]
                }
                setText(str1)
                setSelection(start)
                action(str1.toString())
            }else{
                action(s.toString())
            }

        }
    })
}

@BindingAdapter("bindBlock", "bindAction", requireAll = true)
fun EditText.bindBlock(block: ((View) -> Unit)?, action: Int) {
    if (block == null) {
        return
    }
    setOnEditorActionListener { v, actionId, _ ->
        if (actionId == action) {
            block(v)
            true
        }
        false
    }
}

@BindingAdapter("bindKeyboardVisibility")
fun EditText.bindKeyboardVisibilityListener(block: ((Boolean) -> Unit)?) {
    if (block == null) {
        return
    }
    KeyboardStatusDetector()
        .registerVisibilityListener(this)
        .setVisibilityListener(object : KeyboardStatusDetector.KeyboardVisibilityListener {
            override fun onVisibilityChanged(keyboardVisible: Boolean) {
                block(keyboardVisible)
            }
        })
}

@BindingAdapter(value = ["isShowPasswordText"])
fun EditText.isShowPasswordText(isShow: Boolean) {
    transformationMethod = if (isShow) {
        // 显示密码
        HideReturnsTransformationMethod.getInstance()
    } else {
        // 隐藏密码
        PasswordTransformationMethod.getInstance()
    }
}

