package com.rm.module_home.activity.menu

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_home.bean.MenuDetailBean
import com.rm.module_home.repository.MenuDetailRepository

class MenuDetailViewModel(private var repository: MenuDetailRepository) : BaseVMViewModel() {
    val data = MutableLiveData<MenuDetailBean>()

    fun getData() {
        data.value = repository.getMenuItem()
    }
}