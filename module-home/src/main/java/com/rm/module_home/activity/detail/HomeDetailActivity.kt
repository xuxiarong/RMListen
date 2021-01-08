package com.rm.module_home.activity.detail

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.rm.baselisten.BaseConstance
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.model.BasePlayControlModel
import com.rm.baselisten.utilExt.getStateHeight
import com.rm.business_lib.aria.AriaDownloadManager
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.DetailAudioSortDao
import com.rm.business_lib.db.audiosort.DetailAudioSort
import com.rm.business_lib.db.download.DownloadChapter
import com.rm.business_lib.download.DownloadMemoryCache
import com.rm.business_lib.download.file.DownLoadFileUtils
import com.rm.business_lib.insertpoint.BusinessInsertConstance
import com.rm.business_lib.insertpoint.BusinessInsertManager
import com.rm.component_comm.activity.ComponentShowPlayActivity
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.databinding.HomeActivityDetailMainBinding
import com.rm.module_home.viewmodel.HomeDetailViewModel
import com.rm.module_home.widget.HomeDetailInterceptLayout

/**
 * 书籍详情
 *  //1、需添加书籍下架的toast提示，然后finish掉详情页
 */
class HomeDetailActivity :
    ComponentShowPlayActivity<HomeActivityDetailMainBinding, HomeDetailViewModel>() {
    override fun getLayoutId(): Int = R.layout.home_activity_detail_main

    override fun initModelBrId() = BR.viewModel


    companion object {
        const val AUDIO_ID = "audioID"
        fun startActivity(context: Context, audioID: String) {
            val intent = Intent(context, HomeDetailActivity::class.java)
            intent.putExtra(AUDIO_ID, audioID)
            context.startActivity(intent)
        }
    }


    override fun initView() {
        super.initView()
        setTransparentStatusBar()

        recycleScrollListener()

        mDataBind.homeDetailCommentRecycleView.apply {
            bindVerticalLayout(mViewModel.homeDetailCommentAdapter)
            mViewModel.createHeader(this)
        }

        (mDataBind.homeDetailTitleCl.layoutParams as ViewGroup.MarginLayoutParams).apply {
            //动态获取状态栏的高度,并设置标题栏的topMargin
            topMargin = getStateHeight(this@HomeDetailActivity)
        }

        intent?.getStringExtra(AUDIO_ID)?.let {
            BusinessInsertManager.doInsertKeyAndAudio(
                BusinessInsertConstance.INSERT_TYPE_AUDIO_BROWSING,
                it
            )

            mViewModel.audioId.set(it)
            val sortBean = DaoUtil(
                DetailAudioSort::class.java,
                ""
            ).queryBuilder()?.where(DetailAudioSortDao.Properties.Audio_id.eq(it))?.list()
            sortBean?.let { beanList ->
                if (beanList.size > 0) {
                    mViewModel.mCurSort = beanList[0].sort
                }
            }

            mViewModel.intDetailInfo(it)

            mViewModel.chapterRefreshStatus.setCanRefresh(false)
            mViewModel.chapterRefreshStatus.setResetNoMoreData(true)
            mViewModel.chapterRefreshStatus.setNoHasMore(false)

            mViewModel.commentRefreshStateMode.setNoHasMore(false)
            mViewModel.queryAudioListenRecord()
//            mViewModel.getChapterList(1) //初始化章节列表
            mViewModel.getCommentList(it)
        }

        mDataBind.homeDetailChapterHeader.post {
            //获取章节头部的高度
            val measuredHeight = mDataBind.homeDetailChapterHeader.measuredHeight
            val params =
                mDataBind.homeDetailCommentRefresh.layoutParams as ConstraintLayout.LayoutParams

            //评论列表设置bottomMargin使其不被挡住
            params.bottomMargin = measuredHeight + 20
            mDataBind.homeDetailCommentRefresh.layoutParams = params
        }

        //监听章节列表停留的位置，如果在顶部则现实title
        mDataBind.homeDetailInterceptLayout.setScrollChangeTypeListener(object :
            HomeDetailInterceptLayout.ScrollChangeTypeListener {
            override fun changeType(@HomeDetailInterceptLayout.HomeDetailInterceptChangeType nowType: Int) {
                if (nowType == HomeDetailInterceptLayout.TYPE_TOP) {
                    mDataBind.homeDetailTitle.visibility = View.VISIBLE
                    mDataBind.homeDetailBlur.alpha = 1f
                } else {
                    if (!titleIsVisible) {
                        mDataBind.homeDetailBlur.alpha = oldAlpha
                        mDataBind.homeDetailTitle.visibility = View.INVISIBLE
                    }
                }

                if (nowType == HomeDetailInterceptLayout.TYPE_BOTTOM) {
                    mViewModel.basePlayControlModel.set(BasePlayControlModel(false) { startPlayActivity() })
                } else {
                    mViewModel.basePlayControlModel.set(BasePlayControlModel(true) { startPlayActivity() })
                }
            }
        })
    }

    private var titleIsVisible = false
    private var oldAlpha = 0f

    /**
     * recyclerView滑动监听
     */
    private fun recycleScrollListener() {
        mDataBind.homeDetailCommentRecycleView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            private var totalDy = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val top = mViewModel.mDataBinding?.homeDetailIcon?.top ?: 0
                val height = mViewModel.mDataBinding?.homeDetailIcon?.height ?: 0
                totalDy += dy
                if (totalDy > 0) {
                    val alpha = if (totalDy.toFloat() / (top + height) > 1f) {
                        1f
                    } else {
                        totalDy.toFloat() / (top + height)
                    }
                    oldAlpha = alpha
                    mDataBind.homeDetailBlur.alpha = alpha
                    mDataBind.homeDetailTitle.visibility = if (alpha > 0.9) {
                        titleIsVisible = true
                        View.VISIBLE
                    } else {
                        titleIsVisible = false
                        View.INVISIBLE
                    }
                }
            }
        })
    }


    override fun startObserve() {
        mViewModel.listenAudio.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val listenAudio = mViewModel.listenAudio.get()
                if (listenAudio != null) {
                    if(mViewModel.chapterAdapter.data.isEmpty()){
                        mViewModel.getChapterListWithId(listenAudio.listenChapterId)
                    }
                } else {
                    mViewModel.getChapterList(1)
                }
            }
        })
        DownloadMemoryCache.downloadingChapterList.observe(this, Observer {
            mViewModel.chapterAdapter.setList(getChapterStatus(mViewModel.chapterAdapter.data))
            mViewModel.chapterAdapter.notifyDataSetChanged()
        })
        DownloadMemoryCache.downloadingAudioList.observe(this, Observer {
            mViewModel.chapterAdapter.setList(getChapterStatus(mViewModel.chapterAdapter.data))
            mViewModel.chapterAdapter.notifyDataSetChanged()
        })
        AriaDownloadManager.needShowNetError.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                AriaDownloadManager.needShowNetError.get().let {
                    if(it){
                        tipView.showNetError(this@HomeDetailActivity)
                    }
                }
            }
        })

    }

    private fun getChapterStatus(chapterList: List<DownloadChapter>): MutableList<DownloadChapter> {
        val audioName = mViewModel.detailInfoData.get()?.list?.audio_name?:""
        val audioId = mViewModel.detailInfoData.get()?.list?.audio_id?:0L
        chapterList.forEach {
            it.audio_name = audioName
            it.audio_id = audioId
            DownLoadFileUtils.checkChapterIsDownload(chapter = it)
        }
        return chapterList.toMutableList()
    }

    override fun initData() {
    }

    override fun onResume() {
        super.onResume()
        if(mViewModel.listenAudio.get() == null){
            BaseConstance.basePlayInfoModel.get()?.let {
                if(it.playAudioId == mViewModel.audioId.get()){
                    mViewModel.queryAudioListenRecord()
                }
            }
        }
    }

}