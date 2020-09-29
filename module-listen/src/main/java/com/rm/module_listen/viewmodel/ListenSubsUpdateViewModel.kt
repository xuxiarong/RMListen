package com.rm.module_listen.viewmodel

import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.adapter.multi.CommonMultiVMAdapter
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.TimeUtils
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.api.ListenApiService
import com.rm.module_listen.model.ListenAudio
import com.rm.module_listen.model.ListenAudioChapter
import com.rm.module_listen.model.ListenAudioInfo
import com.rm.module_listen.model.ListenSubsDateModel
import com.rm.module_listen.repository.ListenMyListenRepository

/**
 * desc   :
 * date   : 2020/09/02
 * version: 1.0
 */
class ListenSubsUpdateViewModel : BaseVMViewModel() {

    private val repository by lazy {
        ListenMyListenRepository(BusinessRetrofitClient().getService(ListenApiService::class.java))
    }

    val subsAudioAdapter = CommonMultiVMAdapter(this, mutableListOf(), BR.viewModel, BR.item)

    val subsDateAdapter = CommonBindVMAdapter<ListenSubsDateModel>(
        this,
        mutableListOf(),
        R.layout.listen_item_subs_top_date,
        BR.viewModel,
        BR.item
    )

    private var currentDatePosition = 0
    private var currentSubsDateFirstPosition = 0
    private var isClickScroll = false

    // 下拉刷新和加载更多控件状态控制Model
    val refreshStatusModel = SmartRefreshLayoutStatusModel()

    var subsDateVisible = ObservableBoolean(true)
//    private var allSubsRvList = MutableLiveData<MutableList<MultiItemEntity>>()
//    private var allAudioList = MutableLiveData<MutableList<ListenAudio>>()
//    private var allDateList = MutableLiveData<MutableList<ListenSubsDateModel>>()

    private var currentPage = 1
    val pageSize = 12

    fun init() {
        initSubsRvScrollListen()
    }

    fun getSubsDataFromService() {
        showLoading()
        launchOnIO {
            repository.getListenSubsUpgradeList(currentPage, pageSize).checkResult(
                onSuccess = {
                    showContentView()
                    refreshStatusModel.setHasMore(it.list.size > 0)
                    refreshStatusModel.finishLoadMore(true)
                    currentPage++
                    dealData(it.list)
                }, onError = {
                    showNetError()
                    refreshStatusModel.finishLoadMore(false)
                    DLog.d("suolong", "on error")
                }
            )
        }
    }

    /**
     * 处理服务器返回的数据
     * @param chapterList ArrayList<ListenAudioChapter>
     */
    private fun dealData(chapterList: ArrayList<ListenAudioChapter>) {
        sortChapter(chapterList)
        val checkedChapterList = checkLastAudioContainerChapter(checkChapterList = chapterList)
        getAudioList(checkedChapterList)
    }

    /**
     * 对章节进行时间排序
     * @param chapterList ArrayList<ListenAudioChapter>
     */
    private fun sortChapter(chapterList: ArrayList<ListenAudioChapter>) {
        chapterList.sortWith(Comparator { o1, o2 ->
            return@Comparator if (o1.upgrade_time < o2.upgrade_time) {
                1
            } else {
                -1
            }
        })
    }


    /**
     * 把剩余的章节列表重新归类为书籍
     * @param chapterList List<ListenAudioChapter>
     * @return ArrayList<ListenAudio>
     */
    private fun getAudioList(chapterList: List<ListenAudioChapter>) {
        val audioList = ArrayList<ListenAudio>()
        chapterList.forEach { chapter ->
            var hasChapter = false
            audioList.forEach { audio ->
                if (audio.info.audio_id == chapter.audio_id) {
                    audio.chapter.add(chapter)
                    hasChapter = true
                }
            }
            if (!hasChapter) {
                val audioTempList = ArrayList<ListenAudioChapter>()
                audioTempList.add(chapter)
                audioList.add(
                    ListenAudio(
                        ListenAudioInfo(
                            chapter.audio_id,
                            chapter.cover_url,
                            chapter.audio_name,
                            chapter.upgrade_time
                        ),
                        audioTempList
                    )
                )
            }
        }
        audioList.sortWith(Comparator { o1, o2 ->
            return@Comparator if (o1.info.upgrade_time < o2.info.upgrade_time) {
                1
            } else {
                -1
            }
        })

        audioList.forEach { audio ->
            if (subsDateAdapter.data.size > 0) {
                var hasAudioDate = false
                subsDateAdapter.data.forEach { subsDate ->
                    if (subsDate.date == TimeUtils.getDateStr(audio.info.upgrade_time)) {
                        hasAudioDate = true
                    }
                }
                if (!hasAudioDate) {
                    subsAudioAdapter.addData(ListenSubsDateModel(date = TimeUtils.getDateStr(audio.info.upgrade_time)))
                    subsDateAdapter.addData(
                        ListenSubsDateModel(
                            date = TimeUtils.getDateStr(audio.info.upgrade_time),
                            isTopRvItem = true
                        )
                    )
                }
            } else {
                subsDateAdapter.addData(
                    ListenSubsDateModel(
                        date = TimeUtils.getDateStr(audio.info.upgrade_time),
                        isSelected = true,
                        isTopRvItem = true
                    )
                )
                subsAudioAdapter.addData(ListenSubsDateModel(date = TimeUtils.getDateStr(audio.info.upgrade_time)))
            }
            subsAudioAdapter.addData(audio)
        }
    }

    /**
     * 将服务器获取的章节列表进行检测，如果章节包含在上次数据的最后一本书籍里面，则放入上次的书籍的章节列表中
     * @param checkChapterList ArrayList<ListenAudioChapter>
     * @return ArrayList<ListenAudioChapter>
     */
    private fun checkLastAudioContainerChapter(checkChapterList: ArrayList<ListenAudioChapter>): ArrayList<ListenAudioChapter> {
        val tempAllAudioList = subsAudioAdapter.data
        val iterator = checkChapterList.iterator()
        while (iterator.hasNext()) {
            val chapter = iterator.next()
            chapter.itemType = R.layout.listen_item_subs_list_chapter
            if (tempAllAudioList.size > 0) {
                val lastAudio = tempAllAudioList.last() as ListenAudio
                if (lastAudio.info.audio_id == chapter.audio_id) {
                    lastAudio.chapter.add(chapter)
                    iterator.remove()
                }
            }
        }
        return checkChapterList
    }

    /**
     * 根据章节列表生成Adapter
     * @param audio ListenAudio
     * @return CommonMultiVMAdapter
     */
    fun getAudioAdapter(audio: ListenAudio): CommonMultiVMAdapter {
        val rvList = ArrayList<MultiItemEntity>()
        rvList.add(audio.info)
        rvList.addAll(audio.chapter)
        return CommonMultiVMAdapter(
            this,
            rvList,
            BR.viewModel,
            BR.item
        )
    }

    /**
     * 头部的日期Rv某个Item被点击了
     * @param model ListenSubsDateModel
     */
    fun onTopDateClick(model: ListenSubsDateModel) {
        val clickPosition = subsDateAdapter.data.indexOf(model)
        if (clickPosition == currentDatePosition) {
            return
        }
        isClickScroll = true
        val lastModel = subsDateAdapter.data[currentDatePosition]
        lastModel.isSelected = false
        subsDateAdapter.notifyItemChanged(currentDatePosition)
        model.isSelected = true
        currentDatePosition = clickPosition
        subsDateAdapter.notifyItemChanged(clickPosition)
        subsDateAdapter.recyclerView.smoothScrollToPosition(clickPosition)
        subsRvScrollToSelectDate(model)

    }

    /**
     * 滑动到日期点击的那个Item
     * @param model ListenSubsDateModel
     */
    private fun subsRvScrollToSelectDate(model: ListenSubsDateModel) {
        try {
            val indexOf = subsAudioAdapter.data.indexOf(
                ListenSubsDateModel(date = model.date)
            )
            subsAudioAdapter.recyclerView.smoothScrollToPosition(indexOf)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 初始化书籍的Rv的滑动事件
     */
    private fun initSubsRvScrollListen() {
        subsAudioAdapter.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (isClickScroll) {
                    return
                }
                super.onScrolled(recyclerView, dx, dy)
                try {
                    val linearLayoutManager: LinearLayoutManager =
                        subsAudioAdapter.recyclerView.layoutManager as LinearLayoutManager
                    val firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition()
                    if (firstVisiblePosition == currentSubsDateFirstPosition) {
                        return
                    }
                    if (subsAudioAdapter.getItemViewType(firstVisiblePosition) == R.layout.listen_item_subs_list_audio_date) {
                        if (firstVisiblePosition != currentSubsDateFirstPosition) {
                            currentSubsDateFirstPosition = firstVisiblePosition
                            val subsDateModel =
                                subsAudioAdapter.data[firstVisiblePosition] as ListenSubsDateModel
                            changeTopDateWithAudioScroll(subsDateModel)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isClickScroll = false
                }
            }
        })
    }

    /**
     * 头部日期的Rv根据书籍列表的滑动效果更新显示效果
     * @param model ListenSubsDateModel
     */
    fun changeTopDateWithAudioScroll(model: ListenSubsDateModel) {
        try {
            for (i in 0 until subsDateAdapter.data.size) {
                if (subsDateAdapter.data[i].date == model.date) {
                    subsDateAdapter.data[i].isSelected = true
                    subsDateAdapter.notifyItemChanged(i)
                    subsDateAdapter.recyclerView.smoothScrollToPosition(i)
                    if (i != currentDatePosition) {
                        subsDateAdapter.data[currentDatePosition].isSelected = false
                        subsDateAdapter.notifyItemChanged(currentDatePosition)
                        currentDatePosition = i
                    }
                    return
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

//    fun onChapterClick(context: Context,model : ListenAudioChapter){
//        val playService = RouterHelper.createRouter(PlayService::class.java)
//        playService.toPlayPage(context, HomeDetailModel(DetailArticle(
//            audio_id = model.audio_id,
//            audio_name =  model.audio_name,
//            audio_cover =  model.cover_url,
//            audio_cover_url = model.cover_url
//
//        )),0)
//    }

    fun onAudioClick(context: Context,model : ListenAudio){
        val homeService = RouterHelper.createRouter(HomeService::class.java)
        homeService.toDetailActivity(context = context,audioID = model.info.audio_id)
    }

}