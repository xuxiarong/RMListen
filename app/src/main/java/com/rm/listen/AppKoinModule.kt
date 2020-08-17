package com.rm.listen

import com.rm.baselisten.net.CoroutinesDispatcherProvider
import com.rm.module_mine.api.ListenApiService
import com.rm.module_mine.api.RetrofitClient
import org.koin.dsl.module

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */

val viewModelModule = module {
}

val repositoryModule = module {
    single { RetrofitClient.getService(ListenApiService::class.java, ListenApiService.BASE_URL) }
    single { CoroutinesDispatcherProvider() }
}

val appModule = listOf(viewModelModule, repositoryModule)