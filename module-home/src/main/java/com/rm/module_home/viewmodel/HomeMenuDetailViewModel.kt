package com.rm.module_home.viewmodel

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.databinding.ObservableField
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.getBooleanMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.IS_FIRST_FAVORITES
import com.rm.business_lib.LISTEN_SHEET_LIST_COLLECTED_LIST
import com.rm.business_lib.base.dialog.CustomTipsFragmentDialog
import com.rm.business_lib.bean.AudioListBean
import com.rm.business_lib.bean.SheetDetailInfoBean
import com.rm.business_lib.db.download.DownloadAudio
import com.rm.business_lib.insertpoint.BusinessInsertConstance
import com.rm.business_lib.insertpoint.BusinessInsertManager
import com.rm.business_lib.isLogin
import com.rm.business_lib.loginUser
import com.rm.business_lib.share.ShareManage
import com.rm.business_lib.wedgit.smartrefresh.model.SmartRefreshLayoutStatusModel
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.login.LoginService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.activity.detail.HomeDetailActivity
import com.rm.module_home.activity.menu.HomeMenuDetailActivity.Companion.SHEET_ID
import com.rm.module_home.repository.HomeRepository

class HomeMenuDetailViewModel(private var repository: HomeRepository) : BaseVMViewModel() {

    // 下拉刷新和加载更多控件状态控制Model
    val refreshStatusModel = SmartRefreshLayoutStatusModel()

    //数据源
    val data = ObservableField<SheetDetailInfoBean>()

    /**
     * 收藏是否显示
     */
    val collectedVisibility = ObservableField<Boolean>(false)

    //是否收藏
    val isFavor = ObservableField<Boolean>()

    //听单Id
    var sheetId = ObservableField<String>()


    //刷新控件内的recyclerview
    val contentRvId = R.id.home_menu_detail_recycler_view

    //当前加载的页码
    private var mPage = 1

    //每次加载数据的条数
    private val pageSize = 12

    //创建adapter
    val mAdapter by lazy {
        CommonBindVMAdapter<DownloadAudio>(
            this,
            mutableListOf(),
            R.layout.home_adapter_menu_detail,
            BR.click,
            BR.item
        )
    }

    /**
     * item 点击事件
     */
    fun itemClickFun(context: Context, bookBean: DownloadAudio) {
        if (TextUtils.equals("0", bookBean.status)) {
            showTip("该书籍已下架", R.color.base_ff5e5e)
        } else {
            HomeDetailActivity.startActivity(context, bookBean.audio_id.toString())
        }
    }

    /**
     * 收藏点击事件
     */
    fun clickCollectionRelated(view: View) {
        if (isLogin.get()) {
            data.get()?.sheet_id?.let {
                if (isFavor.get() == false) {
                    favoritesSheet(view, it)
                } else {
                    unFavoritesSheet(it)
                }
            }
        } else {
            getActivity(view.context)?.let {
                RouterHelper.createRouter(LoginService::class.java)
                    .quicklyLogin(it) {
                        showLoading()
                        getData()
                    }
            }
        }
    }


    /**
     * 返回点击事件
     */
    fun clickBack() {
        finish()
    }

    /**
     * 分享点击事件
     */
    fun clickShare(context: Context) {
        getActivity(context)?.let { activity ->
            data.get()?.let {
                ShareManage.shareSheet(activity, it.sheet_id ?: "", it.sheet_name ?: "")
            }
        }
    }

    /**
     * 刷新
     */
    fun refreshData() {
        mPage = 1
        refreshStatusModel.setResetNoMoreData(true)
        getAudioList()
    }

    /**
     * 加载更多
     */
    fun loadData() {
        getAudioList()
    }

    fun setFavorState(b: Boolean) {
        isFavor.set(b)
        val map = getHasMap()
        map["isFavorite"] = b
        map[SHEET_ID] = sheetId.get() ?: ""
        setResult(200, map)
    }

    /**
     * 获取听单详情
     */
    fun getData() {
        launchOnIO {
            repository.getData(sheetId.get() ?: "")
                .checkResult(
                    onSuccess = {
                        if (it.sheet_id == null) {
                            showDataEmpty("此页面空荡荡的…什么都没有")
                        } else {
                            showContentView()
                            setFavorState(it.favor == 1)
                            data.set(it)
                            if (it.created_from == 1 || it.created_from == 3) {
                                collectedVisibility.set(false)
                            } else {
                                collectedVisibility.set(
                                    !TextUtils.equals(
                                        it.member_id,
                                        loginUser.get()?.id
                                    )
                                )
                            }
                        }
                    },
                    onError = { it, _ ->
                        showServiceError()
                        showTip("$it", R.color.business_color_ff5e5e)
                    }
                )
        }
    }

    /**
     * 获取听单音频列表
     */
    fun getAudioList() {
        launchOnIO {
            repository.getAudioList(sheetId.get() ?: "", mPage, pageSize)
                .checkResult(
                    onSuccess = {
                        processAudioList(it)
                    },
                    onError = { it, _ ->
                        if (mPage == 1) {
                            refreshStatusModel.finishRefresh(false)
                        } else {
                            refreshStatusModel.finishLoadMore(false)
                        }
                    }
                )
        }
    }

    private fun processAudioList(bean: AudioListBean) {
        if (mPage == 1) {
            //刷新完成
            refreshStatusModel.finishRefresh(true)
            if (bean.list?.size ?: 0 > 0) {
                mAdapter.setList(bean.list)
            } else {
                showDataEmpty("此页面空荡荡的…什么都没有")
            }
        } else {
            refreshStatusModel.finishLoadMore(true)
            //加载更多完成
            bean.list?.let { list -> mAdapter.addData(list) }
        }

        //是否有更多数据
        if (bean.list?.size ?: 0 < pageSize || mAdapter.data.size >= bean.total) {
            refreshStatusModel.setNoHasMore(true)
        } else {
            ++mPage
        }
    }

    /**
     * 收藏听单
     */
    private fun favoritesSheet(view: View, sheetId: String) {
        showLoading()
        launchOnIO {
            repository.favoritesSheet(sheetId).checkResult(
                onSuccess = {
                    showContentView()
                    favoritesSuccess(view.context)
                    setFavorState(true)
                    BusinessInsertManager.doInsertKeyAndSheet(
                        BusinessInsertConstance.INSERT_TYPE_LISTEN_COLLECTION,
                        sheetId
                    )
                },
                onError = { it, _ ->
                    showContentView()
                    showTip("$it", R.color.business_color_ff5e5e)
                }
            )
        }
    }

    /**
     * 取消收藏
     */
    private fun unFavoritesSheet(sheetId: String) {
        showLoading()
        launchOnIO {
            repository.unFavoritesSheet(sheetId).checkResult(
                onSuccess = {
                    showContentView()
                    showTip("取消收藏成功")
                    setFavorState(false)
                    BusinessInsertManager.doInsertKeyAndSheet(
                        BusinessInsertConstance.INSERT_TYPE_LISTEN_UN_FAVORITE,
                        sheetId
                    )
                },
                onError = { it, _ ->
                    showContentView()
                    showTip("$it", R.color.business_color_ff5e5e)
                }
            )
        }
    }


    /**
     * 收藏成功
     */
    private fun favoritesSuccess(context: Context) {
        val activity = getActivity(context)
        if (IS_FIRST_FAVORITES.getBooleanMMKV(true) && activity != null) {
            CustomTipsFragmentDialog().apply {
                titleText = context.getString(R.string.business_collect_success)
                contentText = context.getString(R.string.business_favorites_success_content)
                leftBtnText = context.getString(R.string.business_know)
                rightBtnText = context.getString(R.string.business_goto_look)
                leftBtnTextColor = R.color.business_text_color_333333
                rightBtnTextColor = R.color.business_color_ff5e5e
                leftBtnClick = {
                    dismiss()
                }
                rightBtnClick = {
                    RouterHelper.createRouter(ListenService::class.java).startListenSheetList(
                        activity,
                        LISTEN_SHEET_LIST_COLLECTED_LIST,
                        ""
                    )
                    dismiss()
                }
                customView =
                    ImageView(activity).apply { setImageResource(R.mipmap.business_img_dycg) }
            }.show(activity)
        } else {
            showTip(context.getString(R.string.business_favorites_success_tip))
        }
        IS_FIRST_FAVORITES.putMMKV(false)
    }

}
