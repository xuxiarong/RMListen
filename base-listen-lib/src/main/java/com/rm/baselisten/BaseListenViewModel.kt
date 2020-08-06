package com.rm.baselisten

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lm.common.net.api.BaseRepository
import com.lm.mvvmcore.base.BaseViewModel

/**
 * desc   :
 * date   : 2020/08/04
 * version: 1.0
 */
class BaseListenViewModel(
    private val repository: BaseRepository
) : BaseViewModel() {
    private val _uiState = MutableLiveData<UiState<*>>()
    val uiState: LiveData<UiState<*>>
        get() = _uiState
}