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
    // 所有的ViewModel都需要在这里注入声明
    viewModel { LoginViewModel(get()) }
}

val repositoryModule = module {
    // 所有的Repository都需要在这里声明
    single { LoginRepository(get()) }
}

val mineModules = listOf(viewModelModule, repositoryModule)