package com.rm.baselisten.binding

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.KeyboardShortcutGroup
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
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

