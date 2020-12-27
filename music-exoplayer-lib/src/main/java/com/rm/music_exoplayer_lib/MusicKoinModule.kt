package com.rm.music_exoplayer_lib


import com.rm.music_exoplayer_lib.viewModel.MusicLockViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
val viewModelModule = module {
    // 所有的ViewModel都需要在这里注入声明
    viewModel { MusicLockViewModel() }
}


val musicModules = listOf(viewModelModule)