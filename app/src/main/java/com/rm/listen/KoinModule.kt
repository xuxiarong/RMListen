package com.rm.listen

import com.rm.baselisten.net.CoroutinesDispatcherProvider
import com.rm.listen.api.ListenApiService
import com.rm.listen.api.RetrofitClient
import com.rm.listen.login.LoginViewModel
import com.rm.listen.repository.LoginRepository
import com.rm.listen.test.TestRepository
import com.rm.listen.test.TestViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */

val viewModelModule = module {
    viewModel {
        LoginViewModel(get())
        TestViewModel()
    }
}

val repositoryModule = module {
    single { RetrofitClient.getService(ListenApiService::class.java, ListenApiService.BASE_URL) }
    single { CoroutinesDispatcherProvider() }
    single { LoginRepository(get()) }
    single { TestRepository() }
}

val appModule = listOf(viewModelModule, repositoryModule)