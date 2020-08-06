package com.rm.listen

import com.rm.listen.api.ListenApiService
import com.rm.listen.api.RetrofitClient
import com.rm.listen.login.LoginViewModel
import com.rm.listen.repository.LoginRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
}

val repositoryModule = module {
    single { RetrofitClient.getService(ListenApiService::class.java, ListenApiService.BASE_URL) }
    single { CoroutinesDispatcherProvider() }
    single { LoginRepository(get()) }
}

val appModule = listOf(viewModelModule, repositoryModule)