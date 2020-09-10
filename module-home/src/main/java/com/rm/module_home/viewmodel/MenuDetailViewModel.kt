package com.rm.module_home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.AudioBean
import com.rm.business_lib.bean.BookBean
import com.rm.business_lib.bean.SheetListBean
import com.rm.module_home.bean.MenuSheetInfoBean
import com.rm.module_home.repository.MenuDetailRepository

class MenuDetailViewModel(private var repository: MenuDetailRepository) : BaseVMViewModel() {
    val data = MutableLiveData<MenuSheetInfoBean>()

    var itemClick: (AudioBean) -> Unit = {}

    fun getData(pageId: String, sheetId: String, memberId: String) {
        launchOnIO {
            repository.getData(pageId, sheetId, memberId)
                .checkResult(
                    onSuccess = {
                        data.value = it
                    },
                    onError = {
                        DLog.i("----->", "$it")
                    }
                )
        }
    }

    fun itemClickFun(bookBean: AudioBean) {
        itemClick(bookBean)
    }


}