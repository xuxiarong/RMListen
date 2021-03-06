package com.rm.baselisten.binding

import android.content.Context
import android.text.*
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.rm.baselisten.helper.KeyboardStatusDetector
import com.rm.baselisten.util.DLog
import java.util.regex.Matcher
import java.util.regex.Pattern

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
            action(context, s.toString())
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
                action(str1)
            } else {
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
        } else {
            false
        }
    }
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


/**
 * 是否能够输入表情和空格（非表情和空格以外其他都能输入）
 */
@BindingAdapter("bindCanInputEmoji")
fun EditText.bindCanInputEmoji(bindCanInputEmoji: Boolean? = true) {
    if (bindCanInputEmoji == true) {
        return
    }
    val inputFilter: InputFilter = object : InputFilter {
        var pattern: Pattern =
            Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_\\{\\}\\[\\]\\|,：.\"!@#$%^&*()_+=-「」（）！？¥:;'。，；/【】、｜…<《》>]")

        override fun filter(
            charSequence: CharSequence,
            i: Int,
            i1: Int,
            spanned: Spanned,
            i2: Int,
            i3: Int
        ): CharSequence {
            val matcher: Matcher = pattern.matcher(charSequence)
            return if (!matcher.find()) {
                charSequence
            } else {
                ""
            }
        }
    }
    val filter = arrayOf(inputFilter)
    filters = filter
}

/**
 * 是否能够输入表情（非表情以外其他都能输入）
 */
@BindingAdapter("bindCanInputEmojiAndSpace")
fun EditText.bindCanInputEmojiAndSpace(bindCanInputEmoji: Boolean? = true) {
    if (bindCanInputEmoji == true) {
        return
    }
    val inputFilter: InputFilter = object : InputFilter {
        var pattern: Pattern =
            Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_\\{\\}\\[\\]\\|,：.\\\t \"!@#$%^&*()_+=-「」（）！？¥:;'。，；/【】、｜…<《》>]")

        override fun filter(
            charSequence: CharSequence,
            i: Int,
            i1: Int,
            spanned: Spanned,
            i2: Int,
            i3: Int
        ): CharSequence {
            val matcher: Matcher = pattern.matcher(charSequence)
            return if (!matcher.find()) {
                charSequence
            } else {
                ""
            }
        }
    }
    val filter = arrayOf(inputFilter)
    filters = filter
}


/**
 * 是否能够输入表情和符号（只能输入数字，汉字，字母）
 */
@BindingAdapter("bindCanInputEmojiAndSymbol")
fun EditText.bindCanInputEmojiAndSymbol(bindCanInputEmojiAndSymbol: Boolean? = true) {
    if (bindCanInputEmojiAndSymbol == true) {
        return
    }

    val inputFilter: InputFilter = object : InputFilter {
        var pattern: Pattern =
            Pattern.compile("[^a-zA-Z0-9\\u4E00-\\u9FA5_]")

        override fun filter(
            charSequence: CharSequence,
            i: Int,
            i1: Int,
            spanned: Spanned,
            i2: Int,
            i3: Int
        ): CharSequence {
            val matcher: Matcher = pattern.matcher(charSequence)
            return if (!matcher.find()) {
                charSequence
            } else {
                ""
            }
        }
    }
    val filter = arrayOf(inputFilter)
    filters = filter
}


