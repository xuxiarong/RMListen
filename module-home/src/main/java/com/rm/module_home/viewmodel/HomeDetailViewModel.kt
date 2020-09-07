package com.rm.module_home.viewmodel

import androidx.databinding.ObservableField
import com.bumptech.glide.Glide
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.net.checkSuccess
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_home.model.home.detail.DetailChapterModel
import com.rm.module_home.model.home.detail.HomeCommentViewModel
import com.rm.module_home.model.home.detail.HomeDetailModel
import com.rm.module_home.repository.DetailRepository


class HomeDetailViewModel(private val repository: DetailRepository) : BaseVMViewModel(){

    var detailViewModel = ObservableField<HomeDetailModel>()
    var detailCommentViewModel = ObservableField<HomeCommentViewModel>()
    var detailChapterViewModel = ObservableField<DetailChapterModel>()

    var showStatus = ObservableField<String>()
    var TimeStamp = ObservableField<String>()

    // 错误提示信息
    var errorTips = ObservableField<String>("")
    /**
     * 获取书籍详情信息
     */
    fun intDetailInfo(){
        launchOnUI {
            repository.getDetailInfo("1").checkResult(
                onSuccess = {
                    showContentView()
                    detailViewModel.set(it)
                },onError = {
                    showContentView()
                    errorTips.set(it)
                }
            )
        }
        showStatus()
        detailCommentViewModel.set(repository.getCommentInfo())

        detailChapterViewModel.set(repository.getChapterInfo())
    }


    /**
     * 书籍状态
     */
    fun showStatus(){
        when(detailViewModel.get()?.detaillist?.progress){
            0 -> showStatus.set("未开播")
            1 -> showStatus.set("已连载"+detailViewModel.get()?.detaillist?.last_sequence+"集")
            else -> showStatus.set("已完结")
        }
    }

    /**
     * 时间转化
     */
    fun showTimeStamp(){

    }
}