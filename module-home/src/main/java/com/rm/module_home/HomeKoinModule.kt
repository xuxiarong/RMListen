package com.rm.module_home

import com.rm.baselisten.net.api.BaseRetrofitClient
import com.rm.module_home.activity.boutique.BoutiqueFragmentViewModel
import com.rm.module_home.activity.boutique.BoutiqueViewModel
import com.rm.module_home.activity.list.TopListViewModel
import com.rm.module_home.activity.menu.MenuViewModel
import com.rm.module_home.api.HomeApiService
import com.rm.module_home.repository.BoutiqueRepository
import com.rm.module_home.repository.MenuRepository
import com.rm.module_home.viewmodel.HomeDetailViewModel
import com.rm.module_home.repository.TopListRepository
import com.rm.module_home.viewmodel.HomeFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */

val viewModelModule = module {
    // 所有的ViewModel都需要在这里注入声明
    viewModel { HomeFragmentViewModel() }
    viewModel { MenuViewModel(get()) }
    viewModel { BoutiqueViewModel(get()) }
    viewModel { TopListViewModel(get()) }
    viewModel { BoutiqueFragmentViewModel(get()) }
    viewModel { HomeDetailViewModel() }
}

val repositoryModule = module {
    // 所有的Repository都需要在这里声明
    single { MenuRepository(get()) }
    single { BoutiqueRepository(get()) }
    single { TopListRepository(get()) }
    single { BaseRetrofitClient().getService(HomeApiService::class.java) }
}

val homeModules = listOf(viewModelModule, repositoryModule)