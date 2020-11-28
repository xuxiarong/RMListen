package com.rm.module_listen.viewmodel

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.LottieAnimationView
import com.rm.baselisten.dialog.CommonMvFragmentDialog
import com.rm.business_lib.swipe.CommonMultiSwipeVmAdapter
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.listen.ListenAudioEntity
import com.rm.business_lib.db.listen.ListenChapterEntity
import com.rm.business_lib.db.listen.ListenDaoUtils
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_listen.BR
import com.rm.module_listen.R
import com.rm.module_listen.databinding.ListenDialogDeleteTipBindingImpl
import com.rm.module_listen.model.ListenHistoryModel
import kotlinx.android.synthetic.main.listen_dialog_delete_tip.*

/**
 * desc   :
 * date   : 2020/09/30
 * version: 1.0
 */
class ListenHistoryViewModel : BaseVMViewModel() {

    var allHistory = MutableLiveData<MutableList<ListenHistoryModel>>()

    var startSearchHistory: (String) -> Unit = { searchHistory(it) }

    var searchHasData = ObservableBoolean(true)

    val keyword = ObservableField<String>("")

    val deleteListenFinish = ObservableBoolean(false)

    //输入法搜索按钮监听
    val bindActionListener: (View) -> Unit = { clickSearchFun(it) }



    private val playService by lazy {
        RouterHelper.createRouter(PlayService::class.java)
    }

    val mSwipeAdapter: CommonMultiSwipeVmAdapter by lazy {
        CommonMultiSwipeVmAdapter(
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
                    val recentChapter = ListenDaoUtils.queryChapterRecentUpdate(audio.audio_id, audio.listenChapterId.toLong())
                    recentChapter?.let { chapter ->
                        val listenHistoryModel = ListenHistoryModel(audio, chapter)
                        listenHistoryModel.itemType = R.layout.listen_item_history_listen
                        audioList.add(listenHistoryModel)
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
            keyword.set(search)
            searchHasData.set(true)
            mSwipeAdapter.data.clear()
            mSwipeAdapter.addData(sourceList)
            if (sourceList.size > 8) {
                mSwipeAdapter.footerLayout?.visibility = View.VISIBLE
            } else {
                mSwipeAdapter.footerLayout?.visibility = View.GONE
            }
        } else {
            keyword.set(search)
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
        DaoUtil(ListenChapterEntity::class.java, "").deleteAll()
        DaoUtil(ListenAudioEntity::class.java, "").deleteAll()
        allHistory.value = mutableListOf()
    }

    fun showDeleteAllDialog(context: Context) {

    }


    fun startListenRecentDetail(context: Context, model: ListenHistoryModel) {

        playService.startPlayActivity(
                context = context,
                audioId = model.audio.audio_id.toString(),
                chapterId = model.chapter.chapter_id.toString(),
                currentDuration = model.chapter.listen_duration
        )
    }

    fun deleteItem(item: ListenHistoryModel) {
        mSwipeAdapter.mItemManger.closeItem(mSwipeAdapter.data.indexOf(item))
        mSwipeAdapter.data.remove(item)
        DaoUtil(ListenChapterEntity::class.java, "").delete(item.audio.listenChapterList)
        DaoUtil(ListenAudioEntity::class.java, "").delete(item.audio)
        if (mSwipeAdapter.data.size < 8) {
            mSwipeAdapter.footerLayout?.visibility = View.GONE
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

    fun changeCheckListenFinish() {
        deleteListenFinish.set(deleteListenFinish.get().not())
    }

}