package com.rm.module_home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.AudioBean
import com.rm.module_home.bean.MenuSheetInfoBean
import com.rm.module_home.repository.MenuDetailRepository

class MenuDetailViewModel(private var repository: MenuDetailRepository) : BaseVMViewModel() {
    //数据源
    val data = MutableLiveData<MenuSheetInfoBean>()

    val favorites = MutableLiveData<Boolean>()

    val unFavorites = MutableLiveData<Boolean>()

    //item点击事件的闭包提供外部调用
    var itemClick: (AudioBean) -> Unit = {}

    /**
     * 获取听单详情
     */
    fun getData(pageId: String, sheetId: String, memberId: String) {
        showLoading()
        launchOnIO {
            repository.getData(pageId, sheetId, memberId)
                .checkResult(
                    onSuccess = {
                        showContentView()
                        data.value = it
                    },
                    onError = {
                        DLog.i("----->", "$it")
                        showContentView()
                    }
                )
        }
    }

    /**
     * 收藏听单
     */
    fun favoritesSheet(sheetId: String) {
        showLoading()
        launchOnIO {
            repository.favoritesSheet(sheetId).checkResult(
                onSuccess = {
                    showContentView()
                    favorites.value = true
                    DLog.i("----->", "收藏听单成功")
                },
                onError = {
                    favorites.value = false
                    showContentView()
                    DLog.i("----->", "$it")
                }
            )
        }
    }


    /**
     * 取消收藏
     */
    fun unFavoritesSheet(sheetId: String) {
        showLoading()
        launchOnIO {
            repository.unFavoritesSheet(sheetId).checkResult(
                onSuccess = {
                    showContentView()
                    unFavorites.value = true
                    DLog.i("----->", "取消收藏成功")
                },
                onError = {
                    unFavorites.value = false
                    showContentView()
                    DLog.i("----->", "$it")
                }
            )
        }
    }

    /**
     * item 点击事件
     */
    fun itemClickFun(bookBean: AudioBean) {
        itemClick(bookBean)
    }


}