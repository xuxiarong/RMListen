package com.rm.module_home

import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.module_home.activity.boutique.BoutiqueFragmentViewModel
import com.rm.module_home.activity.boutique.BoutiqueViewModel
import com.rm.module_home.activity.topic.HomeTopicListViewModel
import com.rm.module_home.api.HomeApiService
import com.rm.module_home.repository.*
import com.rm.module_home.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */

val viewModelModule = module {
    // 所有的ViewModel都需要在这里注入声明
    viewModel { HomeFragmentViewModel(get()) }
    viewModel { HomeMenuViewModel(get()) }
    viewModel { HomeMenuDetailViewModel(get()) }
    viewModel { BoutiqueViewModel(get()) }
    viewModel { TopListViewModel() }
    viewModel { BoutiqueFragmentViewModel(get()) }
    viewModel { HomeDetailViewModel(get()) }
    viewModel { HomeTopListContentFragmentViewModel(get()) }
    // 专题列表ViewModel
    viewModel { HomeTopicListViewModel(get()) }
}

val repositoryModule = module {
    // 所有的Repository都需要在这里声明
    single { HomeRepository(get()) }
    single { BusinessRetrofitClient().getService(HomeApiService::class.java) }
}

val homeModules = listOf(viewModelModule, repositoryModule)