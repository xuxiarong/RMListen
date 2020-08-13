package com.rm.module_mine.login

import com.rm.baselisten.mvvm.BaseViewModel


/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class LoginUiState<T>(
        isLoading: Boolean = false,
        isSuccess: T? = null,
        isError: String? = null,
        val enableLoginButton: Boolean = false,
        val needLogin: Boolean = false
) : BaseViewModel.UiState<T>(isLoading, false, isSuccess, isError)
