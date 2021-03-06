package com.rm.module_mine.login

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_mine.bean.User
import com.rm.module_mine.repository.LoginRepository

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class LoginViewModel(private val repository: LoginRepository) : BaseVMViewModel() {

    val userName = ObservableField<String>("")
    val passWord = ObservableField<String>("")

    private val _uiState = MutableLiveData<LoginUiState<User>>()
    val uiState: LiveData<LoginUiState<User>> get() = _uiState

    private fun isInputValid(userName: String, passWord: String) =
        userName.isNotBlank() && passWord.isNotBlank()

    private fun loginDataChanged() {
        _uiState.value = LoginUiState(
            enableLoginButton = isInputValid(
                userName.get()
                    ?: "", passWord.get() ?: ""
            )
        )
    }

    fun login() {
//        ARouterUtils.getHomeService().login(BaseApplication.CONTEXT)
//        ARouter.getInstance().build("/home/TestActivity").navigation()
        if (userName.get().isNullOrBlank() || passWord.get().isNullOrBlank()) {
            _uiState.value = LoginUiState(enableLoginButton = false)
            return
        }
        Log.i("llj","userName---->>${userName.get()}")
        Log.i("llj","passWord---->>${passWord.get()}")
        _uiState.value = LoginUiState(isLoading = true)
        launchOnIO {
            repository.login(userName.get() ?: "", passWord.get() ?: "").checkResult(
                onSuccess = {
                    Log.i("llj","登陆成功！！")
                    _uiState.value = LoginUiState(isSuccess = it, enableLoginButton = true)
                },
                onError = {it,_->
                    Log.e("llj","登陆失败！！--->>>$it")
                    _uiState.value = LoginUiState(isError = it, enableLoginButton = true)
                })
        }
    }

    fun register() {
        if (userName.get().isNullOrBlank() || passWord.get().isNullOrBlank()) return
        _uiState.value = LoginUiState(isLoading = true)
        launchOnIO {
            repository.register(userName.get() ?: "", passWord.get() ?: "").checkResult(
                onSuccess = {
                    Log.i("llj","注册成功！！")
                    _uiState.value = LoginUiState(isSuccess = it, enableLoginButton = true)
                },
                onError = {it,_->
                    Log.e("llj","注册失败！！----->$it")
                    _uiState.value = LoginUiState(isError = it, enableLoginButton = true)
                })
        }
    }

    val verifyInput: (String) -> Unit = { loginDataChanged() }
}