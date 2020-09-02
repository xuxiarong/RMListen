package com.rm.module_home.activity.menu

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.BookBean
import com.rm.module_home.bean.MenuDetailBean
import com.rm.module_home.bean.MenuItemBean
import com.rm.module_home.repository.MenuDetailRepository

class MenuDetailViewModel(private var repository: MenuDetailRepository) : BaseVMViewModel() {
    val data = MutableLiveData<MenuDetailBean>()
    var dialogData = MutableLiveData<MenuItemBean>()

    var itemClick: (BookBean) -> Unit = {}

    fun getData() {
        data.value = repository.getMenuItem()
    }

    fun itemClickFun(bookBean: BookBean) {
        itemClick(bookBean)
    }

}