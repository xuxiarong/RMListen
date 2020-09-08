package com.rm.module_login

import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.module_login.api.LoginApiService
import com.rm.module_login.repository.LoginRepository
import com.rm.module_login.viewmodel.*
import com.rm.module_login.viewmodel.dialog.CountryChoiceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
val viewModelModule = module {
    // 所有的ViewModel都需要在这里注入声明
    // 验证码登陆
    viewModel { LoginByVerifyViewModel(get()) }
    // 密码登陆
    viewModel { LoginByPasswordViewModel(get()) }
    // 输入验证码
    viewModel { VerificationInputViewModel(get()) }
    // 忘记密码
    viewModel { ForgetPasswordViewModel(get()) }
    // 重置密码
    viewModel { ResetPasswordViewModel(get()) }
    // 选择国家地区的ViewModel
    viewModel { CountryChoiceViewModel() }
}

val repositoryModule = module {
    // 所有的Repository都需要在这里声明
    single { LoginRepository(get()) }
}

val apiServiceModule = module {
    // 所有的apiService都需要在这里声明
    single { BusinessRetrofitClient().getService(LoginApiService::class.java) }
}

val loginModules = listOf(viewModelModule, repositoryModule, apiServiceModule)