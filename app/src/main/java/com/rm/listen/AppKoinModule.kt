package com.rm.listen

import com.rm.baselisten.net.CoroutinesDispatcherProvider
import com.rm.listen.test.TestViewModel
import com.rm.module_mine.api.ListenApiService
import com.rm.module_mine.api.RetrofitClient
import com.rm.module_mine.login.LoginViewModel
import com.rm.module_mine.repository.LoginRepository
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
}

val appModule = listOf(viewModelModule, repositoryModule)