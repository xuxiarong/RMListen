package com.rm.listen

import com.rm.listen.api.ListenApiService
import com.rm.listen.api.RetrofitClient
import com.rm.listen.login.LoginViewModel
import com.rm.listen.repository.LoginRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by luyao
 * on 2019/11/15 15:44
 */

val viewModelModule = module {
    viewModel { LoginViewModel(get(),get()) }
}

val repositoryModule = module {
    single { RetrofitClient.getService(ListenApiService::class.java, ListenApiService.BASE_URL) }
    single { CoroutinesDispatcherProvider() }
    single { LoginRepository(get()) }
}

val appModule = listOf(viewModelModule, repositoryModule)