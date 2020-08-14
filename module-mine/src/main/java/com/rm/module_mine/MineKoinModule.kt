package com.rm.module_mine

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
    }
}

val repositoryModule = module {
    single { LoginRepository(get()) }
}

val mineModule = listOf(viewModelModule, repositoryModule)