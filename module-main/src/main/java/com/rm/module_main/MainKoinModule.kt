package com.rm.module_main

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
}

val repositoryModule = module {
    single { DemoMultiRepository() }
}

val mainModules = listOf(viewModelModule, repositoryModule)