package com.rm.module_home.viewmodel

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import com.rm.baselisten.adapter.single.CommonBindAdapter
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.*
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.model.home.detail.CommentList
import com.rm.module_home.repository.HomeRepository


class HomeDetailViewModel1(private val repository: HomeRepository) : BaseVMViewModel() {

    //听单详情
    val detailInfoData = ObservableField<HomeDetailBean>()


    // 章节的适配器
    val chapterAdapter by lazy {
        CommonBindVMAdapter(
            this,
            mutableListOf<ChapterList>(),
            R.layout.home_item_detail_chapter1,
            BR.chapterclick,
            BR.DetailChapterViewModel
        )
    }

    //标签adapter
    val homeDetailTagsAdapter by lazy {
        CommonBindAdapter(
            mutableListOf<DetailTags>(),
            R.layout.home_item_book_label,
            BR.detailTags
        )
    }

    //评论dapper
    val homeDetailCommentAdapter by lazy {
        CommonBindAdapter(
            mutableListOf<CommentList>(), R.layout.home_detail_item_comment, BR.comment_list
        )
    }

    /**
     * 获取书籍详情信息
     */
    fun getDetailInfo(audioID: String) {
        launchOnUI {
            repository.getDetailInfo(audioID).checkResult(
                onSuccess = {
                    showContentView()
                    detailInfoData.set(it)
                    homeDetailTagsAdapter.setList(it.list.tags)
                }, onError = {
                    showContentView()
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
     * 评论列表
     */
    fun commentList(audio_id: String, page: Int, page_size: Int) {
        launchOnUI {
            repository.getCommentInfo(audio_id, page, page_size).checkResult(
                onSuccess = {
                    showContentView()
                    homeDetailCommentAdapter.setList(it.list_comment)
                    Log.i("commentList", it.toString())
                }, onError = {
                    showContentView()
                    Log.i("commentList", it.toString())
                }
            )
        }
    }

    /**
     * 跳转到播放器页面
     */
    fun toPlayPage() {

    }

    /**
     * 书籍状态
     */
    fun showStatus() {
    }


    /**
     * 排序
     */
    fun getTrackList(mSort: String) {
    }

    /**
     * 2 上拉，下拉更多
     */
    fun getTrackList(isUp: Boolean) {
    }

    /**
     * 分页查询
     */
    fun getTrackList(mPage: Int) {

    }

    /**
     * 章节item点击事件
     */
    fun chapterItemClick(context: Context, bean: ChapterList) {
        DLog.i("-------->", "章节item点击事件")
    }

    /**
     * 全部播放
     */
    fun clickPlayAll() {
        DLog.i("-------->", "全部播放点击事件")
    }

    /**
     * 排序
     */
    fun clickSort() {
        DLog.i("-------->", "排序点击事件")
    }

    /**
     * 下载
     */
    fun clickDownload() {
        DLog.i("-------->", "下载点击事件")
    }

    fun onRefresh() {
        launchOnIO {
            repository.chapterList("173494340084965376", 1, 20, "asc").checkResult(
                onSuccess = {
                    showContentView()
                    it.chapter_list.let { list ->
                        chapterAdapter.setList(list)
                        chapterAdapter.addData(list)
                        chapterAdapter.addData(list)
                    }
                }, onError = {
                    showContentView()
                })
        }
    }

    /**
     * 收藏点击事件
     */
    fun clickCollectionFun(context: Context) {
    }

    /**
     * 订阅点击事件
     */
    fun clickSubscribeFun(context: Context) {
    }

    /**
     * 获取分页列表
     */
    fun setPager(totalcount: Int) {
    }

}