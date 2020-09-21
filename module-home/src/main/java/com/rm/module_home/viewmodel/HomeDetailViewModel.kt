package com.rm.module_home.viewmodel

import android.util.Log
import androidx.databinding.ObservableField
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.AudioChapterListModel
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_home.model.home.detail.DetailChapterModel
import com.rm.module_home.model.home.detail.HomeCommentViewModel
import com.rm.business_lib.bean.HomeDetailModel
import com.rm.module_home.repository.DetailRepository


class HomeDetailViewModel(private val repository: DetailRepository) : BaseVMViewModel() {

    var detailViewModel = ObservableField<HomeDetailModel>()
    var detailCommentViewModel = ObservableField<HomeCommentViewModel>()
    var detailChapterViewModel = ObservableField<DetailChapterModel>()
    val audioList = ObservableField<AudioChapterListModel>()
    var showStatus = ObservableField<String>()
    var TimeStamp = ObservableField<String>()

    val action = ObservableField<String>()

    // 错误提示信息
    var errorTips = ObservableField<String>("")

    //收藏点击事件闭包
    var clickCollected: () -> Unit = {}
    //订阅点击事件闭包
    var clickSubscribe: () -> Unit = {}


    /**
     * 获取书籍详情信息
     */
    fun intDetailInfo(audioID: String) {
        launchOnUI {
            repository.getDetailInfo(audioID).checkResult(
                onSuccess = {
                    showContentView()
                    detailViewModel.set(it)
                }, onError = {
                    showContentView()
                    errorTips.set(it)
                }
            )
        }
        showStatus()
        chapterList(audioID)
        detailCommentViewModel.set(repository.getCommentInfo())
        detailChapterViewModel.set(repository.getChapterInfo())
    }

    /**
     * 订阅
     */
    fun subscribe(audioID: String){
        showLoading()
        launchOnIO {
            repository.subscribe(audioID).checkResult(
                onSuccess = {
                    showContentView()
                    DLog.i("------->","订阅成功")
                    showToast("订阅成功")
                },
                onError = {
                    showContentView()
                    DLog.i("------->","订阅失败  $it")
                }
            )
        }
    }

    /**
     * 跳转到播放器页面
     */
    fun toPlayPage() {
        action.set("toPlayPage")
        action.notifyChange()
    }

    /**
     * 书籍状态
     */
    fun showStatus() {
        when (detailViewModel.get()?.detaillist?.progress) {
            0 -> showStatus.set("未开播")
            1 -> showStatus.set("已连载" + detailViewModel.get()?.detaillist?.last_sequence + "集")
            else -> showStatus.set("已完结")
        }
    }

    /**
     * 章节列表
     */
    fun chapterList(audioId: String) {
        launchOnUI {
            repository.chapterList(audioId).checkResult(onSuccess = {
                audioList.set(it)
                audioList.notifyChange()
                Log.i("AudioChapterListModel", it.toString())
            }, onError = {
                Log.i("AudioChapterListModel", it.toString())
            })
        }
    }

    /**
     * 时间转化
     */
    fun showTimeStamp() {

    }

    /**
     * 收藏点击事件
     */
    fun clickCollectionFun() {
        clickCollected()
    }

    /**
     * 订阅点击事件
     */
    fun clickSubscribeFun() {
        clickSubscribe()
    }
}