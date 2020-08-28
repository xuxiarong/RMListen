package com.rm.module_main

import debug.viewmodel.DemoSingClickViewModel
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
}

val repositoryModule = module {
    // 所有的Repository都需要在这里声明
}

val mainModules = listOf(viewModelModule, repositoryModule)