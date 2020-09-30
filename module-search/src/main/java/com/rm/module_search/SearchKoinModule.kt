package com.rm.module_search

import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.module_search.api.SearchApiService
import com.rm.module_search.repository.SearchRepository
import com.rm.module_search.viewmodel.SearchContentAllViewModel
import com.rm.module_search.viewmodel.SearchRecommendViewModel
import com.rm.module_search.viewmodel.SearchMainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
val viewModelModule = module {
    // 所有的ViewModel都需要在这里注入声明
    viewModel { SearchMainViewModel(get()) }
    viewModel { SearchRecommendViewModel() }
    viewModel { SearchContentAllViewModel(get()) }
}

val repositoryModule = module {
    // 所有的Repository都需要在这里声明
    single { SearchRepository(get()) }
    single { BusinessRetrofitClient().getService(SearchApiService::class.java) }
}

val searchModules = listOf(viewModelModule, repositoryModule)