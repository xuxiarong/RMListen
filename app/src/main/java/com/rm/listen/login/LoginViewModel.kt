package com.rm.listen.login

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lm.mvvmcore.base.BaseViewModel
import com.rm.listen.CoroutinesDispatcherProvider
import com.rm.listen.bean.User
import com.rm.listen.checkResult
import com.rm.listen.repository.LoginRepository

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class LoginViewModel(
    private val repository: LoginRepository,
    private val provider: CoroutinesDispatcherProvider
) : BaseViewModel() {

    val userName = ObservableField<String>("")
    val passWord = ObservableField<String>("")

    private val _uiState = MutableLiveData<LoginUiState<User>>()
    val uiState: LiveData<LoginUiState<User>>
        get() = _uiState

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
        if (userName.get().isNullOrBlank() || passWord.get().isNullOrBlank()) {
            _uiState.value = LoginUiState(enableLoginButton = false)
            return
        }
        _uiState.value = LoginUiState(isLoading = true)
        launchOnIO {
            val result = repository.login(userName.get() ?: "", passWord.get() ?: "")
            result.checkResult(
                onSuccess = {
                    _uiState.value = LoginUiState(isSuccess = it, enableLoginButton = true)
                },
                onError = {
                    _uiState.value = LoginUiState(isError = it, enableLoginButton = true)
                })
        }
    }

    fun register() {
        if (userName.get().isNullOrBlank() || passWord.get().isNullOrBlank()) return
        _uiState.value = LoginUiState(isLoading = true)
        launchOnIO {
            val result = repository.register(userName.get() ?: "", passWord.get() ?: "")
            result.checkResult({
                _uiState.value = LoginUiState(isSuccess = it, enableLoginButton = true)
            }, {
                _uiState.value = LoginUiState(isError = it, enableLoginButton = true)
            })
        }
    }

    val verifyInput: (String) -> Unit = { loginDataChanged() }
}