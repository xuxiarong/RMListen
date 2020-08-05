package com.rm.listen.login

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lm.mvvmcore.base.BaseViewModel
import com.rm.listen.CoroutinesDispatcherProvider
import com.rm.listen.bean.User
import com.rm.listen.checkResult
import com.rm.listen.repository.LoginRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class LoginViewModel(private val repository: LoginRepository, private val provider: CoroutinesDispatcherProvider) : BaseViewModel() {

    val userName = ObservableField<String>("")
    val passWord = ObservableField<String>("")

    private val _uiState = MutableLiveData<LoginUiState<User>>()
    val uiState: LiveData<LoginUiState<User>>
        get() = _uiState

    private fun isInputValid(userName: String, passWord: String) = userName.isNotBlank() && passWord.isNotBlank()

    private fun loginDataChanged() {
        _uiState.value = LoginUiState(enableLoginButton = isInputValid(userName.get()
                ?: "", passWord.get() ?: ""))
    }

    fun login() {

        launchOnUI {
            if (userName.get().isNullOrBlank() || passWord.get().isNullOrBlank()) {
                _uiState.value = LoginUiState(enableLoginButton = false)
                return@launchOnUI
            }

            _uiState.value = LoginUiState(isLoading = true)

            val result = repository.login(userName.get() ?: "", passWord.get() ?: "")

            result.checkResult(
                    onSuccess = {
                        _uiState.postValue(LoginUiState(isSuccess = it, enableLoginButton = true))
                    },
                    onError = {
                        _uiState.postValue(LoginUiState(isError = it, enableLoginButton = true))
                    })
        }
    }

    fun register() {
        viewModelScope.launch(provider.main) {
            if (userName.get().isNullOrBlank() || passWord.get().isNullOrBlank()) return@launch

            withContext(provider.main) { _uiState.value = LoginUiState(isLoading = true) }

            val result = repository.register(userName.get() ?: "", passWord.get() ?: "")

            result.checkResult({
                _uiState.postValue(LoginUiState(isSuccess = it, enableLoginButton = true))
            }, {
                _uiState.postValue((LoginUiState(isError = it, enableLoginButton = true)))
            })
        }
    }

    val verifyInput: (String) -> Unit = { loginDataChanged() }
}