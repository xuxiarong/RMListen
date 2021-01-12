package com.rm.listen

import org.koin.dsl.module

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */

val viewModelModule = module {
}

val repositoryModule = module {
}

val appModule = listOf(viewModelModule, repositoryModule)