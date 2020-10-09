package com.rm.module_home.viewmodel

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.ChapterList
import com.rm.business_lib.bean.DetailBookBean
import com.rm.business_lib.bean.HomeDetailModel
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.model.home.detail.HomeCommentViewModel
import com.rm.module_home.repository.DetailRepository


class HomeDetailViewModel(private val repository: DetailRepository) : BaseVMViewModel() {

    // 下拉刷新和加载更多控件状态控制Model
    val refreshStatusModel = SmartRefreshLayoutStatusModel()

    var audioId = ObservableField<String>()
    var sort = ObservableField<String>()

    //当前加载的页码
    var mPage = 2
    //每次加载数据的条数
    val pageSize = 5

    //上一页页码
    private var upTrackPage = 1
    //下一页页码
    private var curTrackPage = 2
    /**
     * 加载上一页
     */
    fun refreshData() {
        getTrackList(true)
    }
    /**
     * 加载更多
     */
    fun loadData() {
        getTrackList(false)
    }

    //场景，加载上一页，mPage 由 2变成1 ，用curTrackPage 记录变之后的值，加载更多时候，用curTrackPage++去加载数据,
    //如果加载上一页再进行加载上一页数据时候，需要mPage

    var detailViewModel = ObservableField<HomeDetailModel>()
    var detailCommentViewModel = MutableLiveData<HomeCommentViewModel>()
    var total = ObservableField<String>("")
    var totalcount = ObservableField<Int>()
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
                    finish()
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
     * page:第几页
     * page_size:每页数量
     * audio_id:音频id
     * sort：排序(asc,desc)
     */

    //独立刷新事件
     fun onRefresh(){
        launchOnIO {
            repository.chapterList(audioId.get()!!,mPage,pageSize,sort.get()!!).checkResult(
                onSuccess = {
                    showContentView()
                    total.set("共"+it.total+"集")
                    setPager(it.total)
                    it.chapter_list.let { list ->  chapterAdapter.setList(list)}
                }, onError = {
                    showContentView()
                    errorTips.set(it)
                })
        }
    }

    /**
     * 2 上拉，下拉更多
     */
    fun getTrackList(isUp: Boolean){
        var page :Int
        if(isUp){
            page = upTrackPage
            if(0 == page){
                refreshStatusModel.finishRefresh(false)
            }
        }else{
            curTrackPage ++
            page = curTrackPage
        }
        launchOnIO {
            repository.chapterList(audioId.get()!!,page,pageSize,sort.get()!!).checkResult(
                onSuccess = {
                    showContentView()
                    if(isUp){
                        upTrackPage --
                        it.chapter_list.let { list ->  chapterAdapter.addData(upTrackPage,list)}
                        refreshStatusModel.finishRefresh(true)
                    }else{
                        curTrackPage ++
                        it.chapter_list.let { list ->  chapterAdapter.addData(curTrackPage,list)}
                        refreshStatusModel.finishLoadMore(true)
                    }
                    refreshStatusModel.setHasMore(it.chapter_list.size>= pageSize)
                }, onError = {
                    showContentView()
                    refreshStatusModel.finishRefresh(false)
                    refreshStatusModel.finishLoadMore(false)
                    errorTips.set(it)

                })
        }
    }


    /**
     * 分页查询
     */
    fun getTrackList(page: Int){

    }


    val ChapterAnthologyAdapter by lazy {
        /*CommonBindVMAdapter(
            this, mutableListOf<AudioChapterListModel>(),
            R.layout.home_chapter_item_anthology,
            BR.click,
            BR.item
        )*/
    }
    fun click(){

    }
    /**
     * 章节的适配器
     */
    val chapterAdapter by lazy {
        CommonBindVMAdapter(
            this,
            mutableListOf<ChapterList>(),
            R.layout.home_item_detail_chapter,
            BR.chapterclick,
            BR.DetailChapterViewModel

        )
    }

    fun itemClick(context: Context,bean :ChapterList){
        val detailbean = detailViewModel.get()?.detaillist
        if (detailbean != null) {
            playService.toPlayPage(context,DetailBookBean(
                audio_id = detailbean.audio_id,
                audio_name = detailbean.audio_name,
                original_name = detailbean.original_name,
                author = detailbean.author,
                audio_cover_url = detailbean.audio_cover_url),
                chapterAdapter.data.indexOf(bean))
        }
    }

    //播放器路由
    private val playService by lazy {
        RouterHelper.createRouter(PlayService::class.java)
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

    /**
     * 设置分页
     */
    fun setPager(totalcount:Int){

        var anthologyList: MutableList<String> = ArrayList()
        if (sort.equals("desc")) {
            for (i in 0 until totalcount / pageSize) {
                anthologyList.add((totalcount - i * pageSize).toString() + "~" + (totalcount - (i + 1) * pageSize + 1))
            }
            if (totalcount % pageSize != 0) {
                anthologyList.add("${totalcount - totalcount / pageSize * pageSize}-1")
            }
        } else {
            for (i in 0 until totalcount / pageSize) {
                anthologyList.add("${i * pageSize + 1}-"+ (i + 1) * pageSize)
            }
            if (totalcount % pageSize !== 0) {
                anthologyList.add("${totalcount / pageSize * pageSize + 1}")
            }
        }
        //添加数据 : anthologyList

    }
    // adapter 添加
}