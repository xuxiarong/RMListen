package com.rm.module_main

import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.module_main.api.MainApiService
import com.rm.module_main.repository.MainRepository
import com.rm.module_main.viewmodel.HomeMainViewModel
import com.rm.module_main.viewmodel.HomeSplashViewModel
import debug.repository.DemoMultiRepository
import debug.viewmodel.DemoMultiClickViewModel
import debug.viewmodel.DemoSingClickViewModel
import debug.viewmodel.DemoSwipeViewModel
import debug.viewmodel.DemoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
val viewModelModule = module {
    viewModel { DemoViewModel() }
    viewModel { DemoSingClickViewModel() }
    viewModel { DemoMultiClickViewModel(get()) }
    viewModel { DemoSwipeViewModel() }
    viewModel { HomeSplashViewModel(get()) }
    single { HomeMainViewModel(get()) }
}

val repositoryModule = module {
    single { DemoMultiRepository() }
    single { MainRepository(get()) }
    single { BusinessRetrofitClient().getService(MainApiService::class.java) }

}

val mainModules = listOf(viewModelModule, repositoryModule)