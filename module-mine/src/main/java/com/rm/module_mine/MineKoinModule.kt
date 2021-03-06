package com.rm.module_mine

import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.module_mine.api.MineApiService
import com.rm.module_mine.login.LoginViewModel
import com.rm.module_mine.repository.LoginRepository
import com.rm.module_mine.repository.MineRepository
import com.rm.module_mine.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
val viewModelModule = module {
    // 所有的ViewModel都需要在这里注入声明
    viewModel { LoginViewModel(get()) }
    viewModel { MineHomeViewModel(get()) }
    viewModel { MineSettingViewModel() }
    viewModel { MinePersonalInfoViewModel(get()) }
    viewModel { MinePlaySettingViewModel() }
    viewModel { MineDownloadSettingViewModel() }
    viewModel { MineAccountSecuritySettingViewModel(get()) }
    viewModel { MineNicknameSettingViewModel(get()) }
    viewModel { MinePersonalSignatureSettingViewModel(get()) }
    viewModel { MineMemberViewModel(get()) }
    viewModel { MineFragmentMemberMainViewModel(get()) }
    viewModel { MineFragmentMemberCommentViewMode(get()) }
    viewModel { MineMemberFansViewModel(get()) }
    viewModel { MineMemberFollowsViewModel(get()) }
    viewModel { MineMemberReleaseBooksViewModel(get()) }
    viewModel { MineVersionUpdateViewModel() }
    viewModel { MineAboutViewModel(get()) }
    viewModel { MineGetBookViewModel(get()) }
    viewModel { MineFeedbackViewModel(get()) }

}

val repositoryModule = module {
    // 所有的Repository都需要在这里声明
    single { LoginRepository(get()) }
    single { MineRepository(get()) }

    single { BusinessRetrofitClient().getService(MineApiService::class.java) }

}

val mineModules = listOf(viewModelModule, repositoryModule)