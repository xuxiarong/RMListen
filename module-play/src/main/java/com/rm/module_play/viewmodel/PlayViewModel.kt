package com.rm.module_play.viewmodel

import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat
import androidx.databinding.ObservableInt
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.bean.*
import com.rm.business_lib.db.DaoUtil
import com.rm.business_lib.db.HistoryPlayBook
import com.rm.business_lib.isLogin
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.mine.MineService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_play.BR
import com.rm.module_play.R
import com.rm.module_play.adapter.BookPlayerAdapter
import com.rm.module_play.cache.PlayBookState
import com.rm.module_play.model.*
import com.rm.module_play.playview.GlobalplayHelp
import com.rm.module_play.repository.BookPlayRepository
import com.rm.music_exoplayer_lib.bean.BaseAudioInfo
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager
import com.rm.music_exoplayer_lib.manager.MusicPlayerManager.Companion.musicPlayerManger
import com.rm.music_exoplayer_lib.utils.ExoplayerLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *
 * @des:
 * @data: 8/24/20 10:44 AM
 * @Version: 1.0.0
 */
open class PlayViewModel(private val repository: BookPlayRepository) : BaseVMViewModel() {
    val playPath = MutableLiveData<List<BaseAudioInfo>>()
    val pathList = ArrayList<BaseAudioInfo>()
    val audioChapterModel = ObservableField<AudioChapterListModel>()
    val process = ObservableFloat(0f)//进度条
    val maxProcess = ObservableFloat(0f)//最大进度
    val updateThumbText = ObservableField<String>()//更改文字
    var playControlModel = ObservableField<PlayControlModel>()
    var playControlAction = ObservableField<String>()
    var playControlRecommentListModel =
        MutableLiveData<MutableList<PlayControlRecommentListModel>>()
    val mutableList = MutableLiveData<MutableList<MultiItemEntity>>()
    val audioCommentList = MutableLiveData<MutableList<Comments>>()
    val playManger: MusicPlayerManager = musicPlayerManger
    val audioID = ObservableField<String>()
    var audioInfo = ObservableField<DetailBookBean>()

    //当前播放的章节ID
    val mChapterId = ObservableField<String>()
    var playBookSate = ObservableField<PlayBookState>()

    //播放状态进度条，0是播放2是加载中1是暂停
    val playSate = ObservableBoolean(false)
    var playStatus = ObservableInt(0)
    var hasPreChapter = ObservableBoolean(false)
    var hasNextChapter = ObservableBoolean(false)

    // 下拉刷新和加载更多控件状态控制Model
    val refreshStatusModel = SmartRefreshLayoutStatusModel()
    var page = 1
    val pageSize = 12
    val mHistoryPlayBook: HistoryPlayBook = HistoryPlayBook()
    val curTime = System.currentTimeMillis()

    init {
        updateThumbText.set("0/0")
        playBookSate.set(PlayBookState())
        audioCommentList.value = arrayListOf()
    }

    val mBookPlayerAdapter: BookPlayerAdapter by lazy {
        BookPlayerAdapter(this, BR.viewModel, BR.itemModel)

    }
    val commentAdapter by lazy {
        CommonBindVMAdapter<Comments>(
            this,
            mutableListOf(),
            R.layout.recy_item_book_comment_center,
            BR.viewModel,
            BR.itemModel
        )
    }

    companion object {
        const val ACTION_PLAY_QUEUE = "ACTION_PLAY_QUEUE"//播放列表
        const val ACTION_PLAY_OPERATING = "ACTION_PLAY_OPERATING"//播放操作
        const val ACTION_GET_PLAYINFO_LIST = "ACTION_GET_PLAYINFO_LIST"//播放列表
        const val ACTION_JOIN_LISTEN = "ACTION_JOIN_LISTEN"//加入听单
        const val ACTION_MORE_COMMENT = "ACTION_MORE_COMMENT"//更多评论
        const val ACTION_MORE_FINSH = "ACTION_MORE_FINSH"//关闭
    }

    fun getHomeDetailListModel() : HomeDetailList?{
        val audioBean = audioInfo.get()
        if(audioBean!=null){
            return HomeDetailList(
                audio_id=audioBean.audio_id,
                audio_type=0,
                audio_name=audioBean.audio_name,
                original_name=audioBean.original_name,
                status=1,
                author_intro="",
                anchor_id="",
                short_intro="",
                audio_intro="",
                audio_cover="",
                cover_url="",
                audio_label="",
                quality=0,
                progress=0,
                play_count=0,
                created_at="11111",
                chapter_updated_at="11111",
                author=audioBean.author,
                member_id="",
                nickname="",
                subscription_count=0,
                last_sequence=0,
                audio_cover_url=audioBean.audio_cover_url,
                anchor=Anchor.getDefault(),
                tags= mutableListOf<DetailTags>(),
                is_subscribe=false,
                is_fav=false
            )
        }else{
            return HomeDetailList(
                audio_id=audioID.get()!!,
                        audio_type=0,
                        audio_name="",
                        original_name="",
                        status=1,
                        author_intro="",
                        anchor_id="",
                        short_intro="",
                        audio_intro="",
                        audio_cover="",
                        cover_url="",
                        audio_label="",
                        quality=0,
                        progress=0,
                        play_count=0,
                        created_at="11111",
                        chapter_updated_at="11111",
                        author="",
                        member_id="",
                        nickname="",
                        subscription_count=0,
                        last_sequence=0,
                        audio_cover_url="",
                        anchor= Anchor.getDefault(),
                        tags= mutableListOf<DetailTags>(),
                        is_subscribe=false,
                        is_fav=false
            )
        }
    }


    fun setPlayPath(chapterList: List<ChapterList>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                chapterList.forEach {
                    pathList.add(
                        BaseAudioInfo(
                            audioPath = it.path_url,
                            audioName = it.chapter_name,
                            filename = it.chapter_name,
                            audioId = it.audio_id,
                            chapterId = it.chapter_id,
                            duration = it.duration,
                            playCount = it.play_count.toString()
                        )
                    )
                }
                playPath.postValue(pathList)

            }
        }

    }


    /**
     * 获取评论列表
     */
    fun getCommentList() {
        launchOnIO {

        }
    }

    /**
     * 初始化数据
     */
    fun initPlayerAdapterModel() {
        playControlModel.set(PlayControlModel())
        mutableList.value = mutableListOf(
            playControlModel.get()!!,
            PlayControlSubModel(),
            PlayControlCommentTitleModel()
        )

    }

    //播放器操作行为
    fun playControlAction(action: String) {
        playControlAction.set(action)
    }

    fun finshActivity(action: String) {
        playControlAction.set(action)
    }

    //订阅
    fun playSubAction(model: PlayControlSubModel) {

    }

    //订阅
    fun playFollowAction(model: PlayControlHotModel) {

    }

    //点赞
    fun playLikeBook(context: Context, bean: Comments) {
        if (isLogin.get()) {
            if (bean.is_liked) {
                unLikeComment(bean)
            } else {
                likeComment(bean)
            }
        } else {
            getActivity(context)?.let { quicklyLogin(it) }
        }
    }

    //精品详情
    fun playBoutiqueDetails(model: PlayControlRecommentListModel) {
        Log.i("", "playBoutiqueDetails")
    }

    fun audioNameClick(context: Context){
        val audioId = audioID.get()
        if(audioId!=null){
            RouterHelper.createRouter(HomeService::class.java).toDetailActivity(context,audioId)
        }
    }

    /**
     * 快捷登陆
     */
    private fun quicklyLogin(it: FragmentActivity) {
        RouterHelper.createRouter(LoginService::class.java)
            .quicklyLogin(this, it, loginSuccess = {
                //todo 登陆成功回调
//                getCommentList(audioId.get()!!)
            })
    }

    /**
     * 上报
     */
    fun playReport(audioID: String, chapterId: String) {
        launchOnIO {
            repository.playerReport(audioID, chapterId).checkResult(onSuccess = {
                ExoplayerLogger.exoLog(it)
            }, onError = {
                ExoplayerLogger.exoLog(it ?: "")
            })
        }
    }

    /**
     * 章节列表
     */
    fun chapterList(audioId: String, page: Int, page_size: Int, sort: String) {
        launchOnUI {
            repository.chapterList(audioId, page, page_size, sort).checkResult(onSuccess = {
                playBookSate.get()?.audioChapterListModel = it
                audioChapterModel.set(it)
                setPlayPath(it.list)
                showContentView()
            }, onError = {
                showContentView()
            })
        }
    }

    /**
     * 章节列表
     */
    fun chapterPageList(
        audioId: String,
        chapterId: String,
        sort: String
    ) {
        launchOnUI {
            repository.chapterPageList(audioId, chapterId, sort).checkResult(onSuccess = {
                playBookSate.get()?.audioChapterListModel = it
                audioChapterModel.set(it)
                setPlayPath(it.list)
                showContentView()
            }, onError = {
                showContentView()
            })
        }
    }

    /**
     * 获取书籍详情信息
     */
    fun getDetailInfo(audioID: String) {
        launchOnUI {
            repository.getDetailInfo(audioID).checkResult(
                onSuccess = {
                    if (TextUtils.isEmpty(it.list.audio_cover_url)) {
                        it.list.audio_cover_url = ""
                    }
                    setBookDetailBean(
                        DetailBookBean(
                            audio_id = it.list.audio_id,
                            audio_name = it.list.audio_name,
                            original_name = it.list.original_name,
                            author = it.list.author,
                            audio_cover_url = it.list.audio_cover_url,
                            anchor = it.list.anchor
                        )
                    )
                }, onError = {
                    it?.let { it1 -> ExoplayerLogger.exoLog(it1) }
                }
            )
        }
    }

    /**
     * 取消评论点赞
     */
    private fun unLikeComment(bean: Comments) {
        launchOnIO {
            repository.homeUnLikeComment(bean.id.toString()).checkResult(
                onSuccess = {
                    mutableList.value?.let {
                        it.forEachIndexed { index, multiItemEntity ->
                            if (multiItemEntity.itemType == BookPlayerAdapter.ITEM_TYPE_COMMENR_LIST) {
                                val comment = (it[index] as PlayControlCommentListModel).comments

                                if (TextUtils.equals(comment?.id, bean.id)) {
                                    bean.is_liked = false
                                    bean.likes = bean.likes + -1
                                    mBookPlayerAdapter.data[index] =
                                        PlayControlCommentListModel(comments = bean)
                                    val headerLayoutCount = mBookPlayerAdapter.headerLayoutCount
                                    mBookPlayerAdapter.notifyItemChanged(index + headerLayoutCount)
                                }
                            }
                        }
                    }

                },
                onError = {
                    DLog.i("----->", "评论点赞:$it")
                }
            )
        }
    }

    /**
     * 评论点赞
     */
    private fun likeComment(bean: Comments) {
        launchOnIO {
            repository.homeLikeComment(bean.id.toString()).checkResult(
                onSuccess = {
                    mutableList.value?.let {
                        it.forEachIndexed { index, multiItemEntity ->
                            if (multiItemEntity.itemType == BookPlayerAdapter.ITEM_TYPE_COMMENR_LIST) {
                                val comment = (it[index] as PlayControlCommentListModel).comments

                                if (TextUtils.equals(comment?.id, bean.id)) {
                                    bean.is_liked = true
                                    bean.likes = bean.likes + 1
                                    mBookPlayerAdapter.data[index] =
                                        PlayControlCommentListModel(comments = bean)
                                    val headerLayoutCount = mBookPlayerAdapter.headerLayoutCount
                                    mBookPlayerAdapter.notifyItemChanged(index + headerLayoutCount)
                                }
                            }
                        }
                    }
                },
                onError = {
                    DLog.i("----->", "评论点赞:$it")
                })
        }
    }

    //设置上次播放缓存的数据
    fun initPlayBookSate(playBook: PlayBookState?) {
        playBook?.let {
            this.playBookSate.set(playBook)
            setBookDetailBean(it.homeDetailModel)
            it.audioChapterListModel?.let { its ->
                audioChapterModel.set(its)
                setPlayPath(its.list)
            }
        }

    }

    //设置书籍
    private fun setHistoryPlayBook(homeDetail: DetailBookBean) {
        mHistoryPlayBook.audio_cover_url = homeDetail.audio_cover_url
        mHistoryPlayBook.audio_id = homeDetail.audio_id.toLong()
        mHistoryPlayBook.audio_name = homeDetail.audio_name
        mHistoryPlayBook.author = homeDetail.author
        mHistoryPlayBook.listBean = arrayListOf()
        repository.insertPlayBook(mHistoryPlayBook)

    }

    /**
     * 书本信息
     */
    fun setBookDetailBean(homeDetailBean: DetailBookBean?) {
        if (homeDetailBean != null) {
            audioInfo.set(homeDetailBean)
        }
        homeDetailBean?.let {
            playBookSate.get()?.homeDetailModel = it
            val listValue = mutableList.value
            listValue?.set(0, PlayControlModel(homeDetailModel = it))
            listValue?.set(1,PlayControlSubModel(anchor = it.anchor))
            audioID.set(it.audio_id)

            commentAudioComments(it.audio_id)
            mutableList.postValue(listValue)
            setHistoryPlayBook(it)
            GlobalplayHelp.instance.setBooKImage(it.audio_cover_url)
        }
    }

    /**
     *评论列表
     */
    private fun commentAudioComments(audioID: String) {
        launchOnIO {
            if (page == 1) {
                showLoading()
            }
            repository.commentAudioComments(audioID, page, pageSize)
                .checkResult(onSuccess = {
                    it.list.forEach {
                        mutableList.value?.add(PlayControlCommentListModel(comments = it))
                    }
                    mutableList.postValue(mutableList.value)
                    if (page == 1) {
                        if (it.list.isEmpty()) {
                            showContentView()
                        } else {
                            showContentView()
                        }
                    } else {
                        refreshStatusModel.finishLoadMore(true)
                    }
                    refreshStatusModel.setHasMore(it.list.isNotEmpty())
                    page++
                }, onError = {
                    if (page == 1) {
                        showServiceError()
                    } else {
                        refreshStatusModel.finishLoadMore(false)
                    }

                })
        }
    }

    /**
     *评论中心列表
     */
    fun commentCenterAudioComments(audioID: String) {
        launchOnIO {
            if (page == 1) {
                showLoading()
            }
            repository.commentAudioComments(audioID, page, pageSize)
                .checkResult(onSuccess = {
                    (audioCommentList.value as ArrayList<Comments>).addAll(it.list)
                    audioCommentList.postValue(audioCommentList.value)
                    if (page == 1) {
                        if (it.list.isEmpty()) {
                            showDataEmpty()
                        } else {
                            showContentView()
                        }
                    } else {
                        refreshStatusModel.finishLoadMore(true)
                    }
                    refreshStatusModel.setHasMore(it.list.isNotEmpty())
                    page++
                }, onError = {
                    if (page == 1) {
                        showServiceError()
                    } else {
                        refreshStatusModel.finishLoadMore(false)
                    }

                })
        }
    }

    /**
     * 记录播放的章节
     */
    fun updatePlayBook(chapter: ChapterList?) {
        chapter?.let {
            repository.updatePlayBook(it)

        }
    }

    /**
     * 更新播放进度
     */
    fun updatePlayBookProcess(chapter: ChapterList?, progress: Long = 0L) {
        chapter?.let {
            repository.updatePlayBookProcess(chapter, progress)
        }
    }

    /**
     * 查询
     */
    fun queryPlayBookList(): List<HistoryPlayBook>? =
        DaoUtil(HistoryPlayBook::class.java, "").queryAll()


    fun playPreClick(){
        if(hasPreChapter.get()){
            playManger.playLastMusic()
        }
    }

    fun playNextClick(){
        if(hasNextChapter.get()){
            playManger.playNextMusic()
        }
    }

    fun commentAvatarClick(context: Context,member_id : String){
        RouterHelper.createRouter(MineService::class.java).toMineCommentFragment(context = context,memberId = member_id)
    }

}