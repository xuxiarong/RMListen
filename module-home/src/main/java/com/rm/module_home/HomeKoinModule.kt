package com.rm.module_home

import com.rm.baselisten.net.api.BaseRetrofitClient
import com.rm.module_home.activity.menu.MenuViewModel
import com.rm.module_home.api.HomeApiService
import com.rm.module_home.repository.MenuRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */

val viewModelModule = module {
    viewModel {
        MenuViewModel(get())
    }
}

val repositoryModule = module {
    single { MenuRepository(get()) }
    single { BaseRetrofitClient().getService(HomeApiService::class.java) }
}

val homeModule = listOf(viewModelModule, repositoryModule)