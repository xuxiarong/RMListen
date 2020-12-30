package com.rm.module_listen.viewmodel

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.rm.baselisten.util.ConvertUtils
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.PlayGlobalData
import com.rm.business_lib.db.converter.BusinessConvert
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.db.listen.ListenDaoUtils
import com.rm.business_lib.swipe.CommonSwipeVmAdapter
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.model.ListenHistoryModel

/**
 * desc   :
 * date   : 2020/09/30
 * version: 1.0
 */
class ListenHistoryViewModel : BaseVMViewModel() {

    var allHistory = MutableLiveData<MutableList<ListenHistoryModel>>()

    var startSearchHistory: (String) -> Unit = { searchHistory(it) }

    var searchHasData = ObservableBoolean(true)

    var dataEmpty = ObservableBoolean(false)

    val keyword = ObservableField<String>("")

    val deleteListenFinish = ObservableBoolean(false)

    //输入法搜索按钮监听
    val bindActionListener: (View) -> Unit = { clickSearchFun(it) }

    private val playService by lazy {
        RouterHelper.createRouter(PlayService::class.java)
    }

    val mSwipeAdapter: CommonSwipeVmAdapter<ListenHistoryModel> by lazy {
        CommonSwipeVmAdapter<ListenHistoryModel>(
            this, mutableListOf(),
            R.layout.listen_item_history_listen,
            R.id.listenRecentSl,
            R.id.swipe_delete,
            BR.viewModel,
            BR.item
        )
    }

    fun getListenHistory() {
        launchOnIO {
            val queryPlayBookList = ListenDaoUtils.getAllAudioByRecent()
            val audioList = ArrayList<ListenHistoryModel>()
            if (queryPlayBookList.isNotEmpty()) {
                queryPlayBookList.forEach { audio ->
                    if (!TextUtils.isEmpty(audio.listenChapterId)) {
                        val recentChapter = ListenDaoUtils.queryChapterRecentUpdate(
                            audio.audio_id,
                            audio.listenChapterId.toLong()
                        )
                        recentChapter?.let { chapter ->
                            val listenHistoryModel = ListenHistoryModel(audio, chapter)
                            listenHistoryModel.itemType = R.layout.listen_item_history_listen
                            audioList.add(listenHistoryModel)
                        }
                    }
                }
                if (audioList.isNotEmpty()) {
                    searchHasData.set(true)
                    allHistory.postValue(audioList)
                } else {
                    searchHasData.set(false)
                }
            } else {
                searchHasData.set(false)
            }
        }
    }


    fun searchHistory(search: String) {
        val sourceList = allHistory.value
        val resultList = ArrayList<ListenHistoryModel>()

        if (TextUtils.isEmpty(search) && sourceList != null) {
            searchHasData.set(true)
            mSwipeAdapter.data.clear()
            mSwipeAdapter.addData(sourceList)
            if (sourceList.size > 8) {
                mSwipeAdapter.footerLayout?.visibility = View.VISIBLE
            } else {
                mSwipeAdapter.footerLayout?.visibility = View.GONE
            }
        } else {
            if (sourceList != null && sourceList.isNotEmpty()) {
                sourceList.forEach {
                    if (it.audio.audio_name.contains(search)) {
                        resultList.add(it)
                    }
                }
                if (resultList.isNotEmpty()) {
                    searchHasData.set(true)
                    mSwipeAdapter.data.clear()
                    mSwipeAdapter.addData(resultList)
                    mSwipeAdapter.footerLayout?.visibility = View.GONE
                } else {
                    searchHasData.set(false)
                }
            } else {
                searchHasData.set(false)
            }
        }
    }

    fun deleteAllHistory() {
        if (deleteListenFinish.get()) {
            val deleteList = mSwipeAdapter.data
            val iterator = deleteList.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (next.chapter.listen_duration >= next.chapter.realDuration) {
                    ListenDaoUtils.deleteAudio(next.audio)
                    iterator.remove()
                }
            }
            mSwipeAdapter.setList(deleteList)
        } else {
            ListenDaoUtils.deleteAllAudio()
            allHistory.value = mutableListOf()
            mSwipeAdapter.data.clear()
        }
        if (mSwipeAdapter.data.isEmpty()) {
            dataEmpty.set(true)
        }
    }

    fun startListenRecentDetail(context: Context, model: ListenHistoryModel) {
        PlayGlobalData.playNeedQueryChapterProgress.set(true)
        playService.startPlayActivity(
            context = context,
            audioId = model.audio.audio_id.toString(),
            audioInfo = BusinessConvert.convertToDownloadAudio(model.audio),
            chapterId = model.chapter.chapter_id.toString()
        )
    }

    fun deleteItem(item: ListenHistoryModel) {
        mSwipeAdapter.mItemManger.closeItem(mSwipeAdapter.data.indexOf(item))
        mSwipeAdapter.data.remove(item)
        ListenDaoUtils.deleteAudio(item.audio)
        if (mSwipeAdapter.data.size < 8) {
            mSwipeAdapter.footerLayout?.visibility = View.GONE
        }
        if (mSwipeAdapter.data.isEmpty()) {
            dataEmpty.set(true)
        }
    }

    /**
     * 搜索点击事件
     */
    private fun clickSearchFun(view: View?) {
        view?.let {
            val imm =
                it.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm.isActive) {
                imm.hideSoftInputFromWindow(it.applicationWindowToken, 0)
            }
        }
    }

    /**
     * 清除输入内容
     */
    fun clickClearInput() {
        keyword.set("")
    }

    fun changeCheckListenFinish() {
        deleteListenFinish.set(deleteListenFinish.get().not())
    }

}