package com.rm.module_download

import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.module_download.api.DownloadApiService
import com.rm.module_download.repository.DownloadRepository
import com.rm.module_download.viewmodel.DownloadBookDetailViewModel
import com.rm.module_download.viewmodel.DownloadChapterSelectionViewModel
import com.rm.module_download.viewmodel.DownloadMainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * desc   :
 * date   : 2020/09/03
 * version: 1.0
 */
val viewModelModule = module {
    // 所有的ViewModel都需要在这里注入声明
    viewModel { DownloadChapterSelectionViewModel(get()) }
    viewModel { DownloadBookDetailViewModel() }
    single { DownloadMainViewModel() }
}

val repositoryModule = module {
    // 所有的Repository都需要在这里声明
    single { DownloadRepository(get()) }
    single { BusinessRetrofitClient().getService(DownloadApiService::class.java) }
}

val downloadModules = listOf(viewModelModule, repositoryModule)