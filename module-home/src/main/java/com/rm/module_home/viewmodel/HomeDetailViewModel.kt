package com.rm.module_home.viewmodel

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.*
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.model.home.detail.HomeCommentViewModel
import com.rm.module_home.repository.DetailRepository
import com.rm.module_play.enum.Jump


class HomeDetailViewModel(private val repository: DetailRepository) : BaseVMViewModel() {


    //当前加载的页码
    var mPage = 1
    //每次加载数据的条数
    val pageSize = 2
    //上一页页码
    private var upTrackPage = 0
    //下一页页码
    private var curTrackPage = 1

    //章节分页显示与隐藏
    var HideOr = ObservableBoolean(false)
    //主播关注与已关注
    var MemberAnthor = ObservableBoolean(false)
    var MemberStr = ObservableField<String>()


    var audioId = ObservableField<String>()
    var sort = ObservableField<String>()
    var total = ObservableField<String>()
    var showStatus = ObservableField<String>()
    val actionControl = MutableLiveData<String>()
    var errorTips = ObservableField<String>()
    var detailViewModel = ObservableField<HomeDetailModel>()
    var detailCommentViewModel = MutableLiveData<HomeCommentViewModel>()
    val refreshStatusModel = SmartRefreshLayoutStatusModel()

    //收藏点击事件闭包
    var clickCollected: () -> Unit = {}
    //订阅点击事件闭包
    var clickSubscribe: () -> Unit = {}
    //关注主播
    var clickSubMember: () -> Unit = {}

    fun MemberState(MemberState : Boolean){
        if(MemberState){
            MemberStr.set("已关注")
        }else{
            MemberStr.set("关注")
        }
    }

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

    /**
     * 获取书籍详情信息
     */
    fun intDetailInfo(audioID: String) {
        launchOnUI {
            repository.getDetailInfo(audioID).checkResult(
                onSuccess = {
                    showContentView()
                    detailViewModel.set(it)
                    MemberAnthor.set(it.detaillist.anchor.status)
                    MemberState(MemberAnthor.get())
                }, onError = {
                    showContentView()
                    errorTips.set(it)
                    showToast(it.toString())
                }
            )
        }
        showStatus()
    }

    /**
     * 订阅
     */
    fun subscribe(audioID: String) {
        showLoading()
        launchOnIO {
            repository.subscribe(audioID).checkResult(
                onSuccess = {
                    showContentView()
                    DLog.i("------->", "订阅成功")
                },
                onError = {
                    showContentView()
                    DLog.i("------->", "订阅失败  $it")
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
    fun onRefresh() {
        launchOnIO {
            repository.chapterList(audioId.get()!!, mPage, pageSize, sort.get()!!).checkResult(
                onSuccess = {
                    showContentView()
                    total.set("共" + it.total + "集")
                    setPager(it.total)
                    it.chapter_list.let { list -> chapterAdapter.setList(list) }
                }, onError = {
                    showContentView()
                    errorTips.set(it)
                })
        }
    }

    /**
     * 排序
     */
    fun getTrackList(mSort :String){
        if(!mSort.equals(sort.get())){
            //curTrackPage = 1
            sort.set(mSort)
        }
        launchOnIO {
            repository.chapterList(audioId.get()!!, mPage, pageSize, sort.get()!!).checkResult(
                onSuccess = {
                    showContentView()
                    setPager(it.total)
                    //upTrackPage = 0
                    //curTrackPage ++
                    it.chapter_list.let { list -> chapterAdapter.setList(list) }
                }, onError = {
                    showContentView()
                    errorTips.set(it)
                })
        }
    }

    /**
     * 2 上拉，下拉更多
     */
    fun getTrackList(isUp: Boolean) {
        var page: Int
        if (isUp) {
            page = upTrackPage
            if (0 == page) {
                refreshStatusModel.finishRefresh(false)
            }
        } else {
            page = curTrackPage
        }
        launchOnIO {
            repository.chapterList(audioId.get()!!, page, pageSize, sort.get()!!).checkResult(
                onSuccess = {
                    showContentView()
                    if (isUp) {
                        upTrackPage--
                        it.chapter_list.let { list -> chapterAdapter.addData(0, list) }
                        refreshStatusModel.finishRefresh(true)
                    } else {
                        curTrackPage++
                        it.chapter_list.let { list -> chapterAdapter.addData(list) }
                        refreshStatusModel.finishLoadMore(true)
                    }
                    refreshStatusModel.setHasMore(it.chapter_list.size >= pageSize)
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
    fun getTrackList(mPage: Int) {
        DLog.e("mPage",""+mPage)

        launchOnIO {
            repository.chapterList(audioId.get()!!, mPage, pageSize, sort.get()!!).checkResult(
                onSuccess = {
                    showContentView()
                    //当前为1，上一页为0，下一页为2
                    upTrackPage = mPage
                    curTrackPage = mPage

                    upTrackPage --
                    curTrackPage ++

                    it.chapter_list.let { list -> chapterAdapter.setList( list) }
                    refreshStatusModel.setHasMore(it.chapter_list.size >= pageSize)
                    HideOr.set(false)

                }, onError = {
                    showContentView()
                    errorTips.set(it)
                })
        }

    }

    /**
     * 分页适配器
     */
    val ChapterAnthologyAdapter by lazy {
        CommonBindVMAdapter(
            this, mutableListOf<DataStr>(),
            R.layout.home_chapter_item_anthology,
            BR.click,
            BR.item
        )
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

    fun itemClick(context: Context, bean: ChapterList) {
        playService.toPlayPage(
            context, bean,Jump.CHAPTER.from
        )
    }

    //播放器路由
    private val playService by lazy {
        RouterHelper.createRouter(PlayService::class.java)
    }

    /**
     * 评论列表
     */
    fun commentList(audio_id: String, page: Int, page_size: Int) {
        launchOnUI {
            repository.getCommentInfo(audio_id, page, page_size).checkResult(
                onSuccess = {
                    showContentView()
                    detailCommentViewModel.postValue(it)
                    Log.i("commentList", it.toString())
                }, onError = {
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
     * 关注主播点击事件
     */
    fun clickSubMemberFun(){
        clickSubMember()
    }

    /**
     * 获取分页列表
     */
    fun setPager(totalcount: Int) {

        var anthologyList = ArrayList<DataStr>()

        var size = totalcount / pageSize

        if (sort.get().equals("desc")) {
            for (i in 0 until size) {
                anthologyList.add(
                    DataStr(
                        (totalcount - i * pageSize).toString() + "~" + (totalcount - (i + 1) * pageSize + 1) ,i+1
                    )
                )
            }
            if (size != 0) {
                anthologyList.add(DataStr("${totalcount - size * pageSize}-1",size+1) )
            }
        } else {
            for (i in 0 until size) {
                anthologyList.add(DataStr("${i * pageSize + 1}-" + (i + 1) * pageSize, i+1))
            }
            if (size !== 0) {
                anthologyList.add(DataStr("${size * pageSize + 1}",size+1))
            }
        }
        ChapterAnthologyAdapter.setList(anthologyList)
    }

}