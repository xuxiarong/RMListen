package com.rm.module_listen

import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.module_listen.repository.ListenSheetCollectedRepository
import com.rm.module_listen.repository.ListenSheetMyListRepository
import com.rm.module_listen.api.ListenApiService
import com.rm.module_listen.repository.ListenSheetDetailRepository
import com.rm.module_listen.repository.ListenSubscriptionRepository
import com.rm.module_listen.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
val viewModelModule = module {
    // 所有的ViewModel都需要在这里注入声明
    viewModel { ListenMyListenViewModel() }
    viewModel { ListenRecentListenViewModel() }
    viewModel { ListenSubsUpdateViewModel() }
    viewModel { ListenBoughtViewModel() }
    viewModel { ListenSubscriptionViewModel(get()) }
    viewModel { ListenSheetMyListViewModel(get()) }
    viewModel { ListenSheetCollectedListViewModel(get()) }
    viewModel { ListenSheetDetailViewModel(get()) }
}

val repositoryModule = module {
    // 所有的Repository都需要在这里声明
    single { ListenSubscriptionRepository(get()) }
    single { ListenSheetMyListRepository(get()) }
    single { ListenSheetCollectedRepository(get()) }
    single { ListenSheetDetailRepository(get()) }
    single { BusinessRetrofitClient().getService(ListenApiService::class.java) }
}

val listenModules = listOf(viewModelModule, repositoryModule)