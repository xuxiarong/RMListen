package com.rm.module_home.viewmodel

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.AudioChapterListModel
import com.rm.business_lib.bean.ChapterList
import com.rm.module_home.model.home.detail.HomeCommentViewModel
import com.rm.business_lib.bean.HomeDetailModel
import com.rm.business_lib.utils.time2format
import com.rm.module_home.repository.DetailRepository


class HomeDetailViewModel(private val repository: DetailRepository) : BaseVMViewModel() {

    var detailViewModel = ObservableField<HomeDetailModel>()
    var detailCommentViewModel = MutableLiveData<HomeCommentViewModel>()
    var total = ObservableField<String>("")
    var detailChapterViewModel = MutableLiveData<MutableList<ChapterList>>()

    //val audioList = ObservableField<AudioChapterListModel>()
    var showStatus = ObservableField<String>()

    val actionControl = MutableLiveData<String>()

    // 错误提示信息
    var errorTips = ObservableField<String>("")

    //收藏点击事件闭包
    var clickCollected: () -> Unit = {}
    //订阅点击事件闭包
    var clickSubscribe: () -> Unit = {}

    val test="dfas豆腐口感马拉喀什的风格穆沙拉卡的父母过来；四大发明；，公司的分公司的奉公守法公司反而你我i片分为发票金额为皮肤饥饿我"


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
        actionControl.postValue("toPlayPage")

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
    fun chapterList(audioId: String ,page :Int,page_size:Int,sort:String) {
        launchOnIO {
            repository.chapterList(audioId,page,page_size,sort).checkResult(onSuccess = {
                //detailChapterViewModel.notifyChange()
                showContentView()
                detailChapterViewModel.postValue(it.chapter_list)
                total.set(it.total)
            }, onError = {
                showContentView()
                errorTips.set(it)
            })
        }
    }


    /**
     * 评论列表
     */
    fun commentList(audio_id: String,page: Int,page_size: Int){
        launchOnUI {
            repository.getCommentInfo(audio_id,page,page_size).checkResult(
                onSuccess = {
                    showContentView()
                    detailCommentViewModel.postValue(it)
                    Log.i("commentList", it.toString())
                },onError = {
                    showContentView()
                    errorTips.set(it)
                    Log.i("commentList", it.toString())
                }
            )
        }
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