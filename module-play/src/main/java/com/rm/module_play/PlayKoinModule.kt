package com.rm.module_play

import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.module_play.api.PlayApiService
import com.rm.module_play.repository.BookPlayRepository
import com.rm.module_play.viewmodel.PlayViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
val viewModelModule = module {
    // 所有的ViewModel都需要在这里注入声明
    viewModel { PlayViewModel(get()) }
}

val repositoryModule = module {
    single { BookPlayRepository(get()) }
    // 所有的Repository都需要在这里声明
}
val apiServiceModule = module {
    // 所有的apiService都需要在这里声明
    single { BusinessRetrofitClient().getService(PlayApiService::class.java) }
}
val playModules = listOf(apiServiceModule,viewModelModule, repositoryModule)