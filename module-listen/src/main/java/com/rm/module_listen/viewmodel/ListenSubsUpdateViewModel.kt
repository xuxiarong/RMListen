package com.rm.module_listen.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.adapter.multi.CommonMultiVMAdapter
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.util.TimeUtils
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.net.BusinessRetrofitClient
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
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

    val subsAdapter = CommonMultiVMAdapter(this, mutableListOf(), BR.viewModel, BR.item)

    val subsDateAdapter = CommonBindVMAdapter<ListenSubsDateModel>(this, mutableListOf(),R.layout.listen_item_subs_top_date,BR.viewModel,BR.item)

    // 下拉刷新和加载更多控件状态控制Model
    val refreshStatusModel = SmartRefreshLayoutStatusModel()

    var subsDateVisible = ObservableBoolean(true)
    private var allSubsRvList = MutableLiveData<MutableList<MultiItemEntity>>()
    private var allAudioList = MutableLiveData<MutableList<ListenAudio>>()
    private var allDateList = MutableLiveData<MutableList<ListenSubsDateModel>>()

    private var currentPage = 1
    val pageSize = 12

    init {
        allAudioList.value = ArrayList()
        allSubsRvList.value = ArrayList()
        allDateList.value = ArrayList()
    }

    fun getSubsDataFromService() {
        showLoading()
        launchOnIO {
            repository.getListenSubsUpgradeList(currentPage, pageSize).checkResult(
                onSuccess = {
                    showContentView()
                    refreshStatusModel.finishLoadMore(true)
                    currentPage++
                    if(it.list.size<=pageSize){
                        refreshStatusModel.setHasMore(false)
                    }
                    dealData(it.list)

                }, onError = {
                    showNetError()
                    refreshStatusModel.finishLoadMore(true)
                    DLog.d("suolong", "on error")
                }
            )
        }
    }


    private fun dealData(chapterList: ArrayList<ListenAudioChapter>) {
        val checkedChapterList = checkLastAudioContainerChapter(checkChapterList = chapterList)
        getAudioList(checkedChapterList)
    }



    /**
     * 把剩余的章节列表重新归类为书籍
     * @param chapterList List<ListenAudioChapter>
     * @return ArrayList<ListenAudio>
     */
    private fun getAudioList(chapterList: List<ListenAudioChapter>){
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

        audioList.forEach {
            if(subsDateAdapter.data.size>0){
                val lastDate = subsDateAdapter.data[subsDateAdapter.data.lastIndex]
                if(lastDate.date != TimeUtils.getDateStr(it.info.upgrade_time)){
                    subsAdapter.addData(ListenSubsDateModel(date = TimeUtils.getDateStr(it.info.upgrade_time)))
                    subsDateAdapter.addData(ListenSubsDateModel(date = TimeUtils.getDateStr(it.info.upgrade_time),isTopRvItem = true))
                }
            }else{
                subsDateAdapter.addData(ListenSubsDateModel(date = TimeUtils.getDateStr(it.info.upgrade_time),isSelected = true,isTopRvItem = true))
                subsAdapter.addData(ListenSubsDateModel(date = TimeUtils.getDateStr(it.info.upgrade_time),isSelected = true))
            }
            subsAdapter.addData(it)
        }
    }

    /**
     * 将服务器获取的章节列表进行检测，如果章节包含在上次数据的最后一本书籍里面，则放入上次的书籍的章节列表中
     * @param checkChapterList ArrayList<ListenAudioChapter>
     * @return ArrayList<ListenAudioChapter>
     */
    private fun checkLastAudioContainerChapter(checkChapterList: ArrayList<ListenAudioChapter>): ArrayList<ListenAudioChapter> {
        val tempAllAudioList = allAudioList.value
        val iterator = checkChapterList.iterator()
        while (iterator.hasNext()) {
            val chapter = iterator.next()
            chapter.itemType = R.layout.listen_item_subs_list_chapter
            if (tempAllAudioList != null && tempAllAudioList.size > 0) {
                val lastAudio = tempAllAudioList[tempAllAudioList.size - 1]
                if (lastAudio.info.audio_id == chapter.audio_id) {
                    lastAudio.chapter.add(chapter)
                    iterator.remove()
                }
            }
        }
        allAudioList.value = tempAllAudioList
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

    fun onTopDateClick(model : ListenSubsDateModel){
        model.isSelected = true
    }

}