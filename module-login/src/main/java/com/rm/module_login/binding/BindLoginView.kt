package com.rm.module_login.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.rm.module_login.LoginConstants
import com.rm.module_login.R

/**
 * desc   :
 * date   : 2020/09/01
 * version: 1.0
 */

@BindingAdapter("bindLoginStatus")
fun ImageView.bindLoginStatus(loginStatus : Int){
    when(loginStatus){
        LoginConstants.LOGIN_LOGGING -> setImageResource(R.drawable.login_ic_close_eyes)
        LoginConstants.LOGIN_SUCCESS -> setImageResource(R.drawable.login_ic_checkbox_true)
        LoginConstants.LOGIN_FAILED -> setImageResource(R.drawable.login_ic_checkbox_false)
    }
}

@BindingAdapter("bindLoginText")
fun TextView.bindLoginText(loginStatus : Int){
    when(loginStatus){
        LoginConstants.LOGIN_LOGGING -> text = context.getString(R.string.login_login)
        LoginConstants.LOGIN_SUCCESS -> text = context.getString(R.string.login_success)
        LoginConstants.LOGIN_FAILED -> text = context.getString(R.string.login_failed)
    }
}