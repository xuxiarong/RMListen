package com.rm.module_login

import org.koin.dsl.module

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
val viewModelModule = module {
    // 所有的ViewModel都需要在这里注入声明
}

val repositoryModule = module {
    // 所有的Repository都需要在这里声明
}

val loginModules = listOf(viewModelModule, repositoryModule)