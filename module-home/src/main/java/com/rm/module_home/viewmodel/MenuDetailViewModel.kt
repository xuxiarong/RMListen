package com.rm.module_home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.AudioBean
import com.rm.business_lib.bean.AudioListBean
import com.rm.business_lib.bean.SheetInfoBean
import com.rm.module_home.repository.MenuDetailRepository

class MenuDetailViewModel(private var repository: MenuDetailRepository) : BaseVMViewModel() {
    //数据源
    val data = MutableLiveData<SheetInfoBean>()
    //加载更多 听单音频列表
    val audioListData = MutableLiveData<AudioListBean>()

    //收藏成功
    var favoritesSuccess: () -> Unit = {}

    //取消收藏成功
    var unFavoritesSuccess: () -> Unit = {}

    //item点击事件的闭包提供外部调用
    var itemClick: (AudioBean) -> Unit = {}

    /**
     * 获取听单详情
     */
    fun getData(sheetId: String) {
        showLoading()
        launchOnIO {
            repository.getData(sheetId)
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
     * 获取听单音频列表
     */
    fun getAudioList(
        page_id: String,
        sheetId: String,
        page: Int,
        page_size: Int
    ) {
        showLoading()
        launchOnIO {
            repository.getAudioList(page_id, sheetId, page, page_size).checkResult(
                onSuccess = {
                    showContentView()
                    audioListData.value = it
                },
                onError = {
                    showContentView()
                    DLog.i("------>", "$it")
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
                    favoritesSuccess()
                    DLog.i("----->", "收藏听单成功")
                },
                onError = {
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
                    unFavoritesSuccess()
                    DLog.i("----->", "取消收藏成功")
                },
                onError = {
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