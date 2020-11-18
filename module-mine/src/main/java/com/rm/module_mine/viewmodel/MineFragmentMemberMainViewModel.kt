package com.rm.module_mine.viewmodel

import android.content.Context
import androidx.databinding.ObservableField
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.util.DLog
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.business_lib.LISTEN_SHEET_LIST_COLLECTED_LIST
import com.rm.business_lib.LISTEN_SHEET_LIST_MY_LIST
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.listen.ListenService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.activity.MineMemberReleaseBooksActivity
import com.rm.module_mine.bean.*
import com.rm.module_mine.repository.MineRepository

/**
 *
 * @author yuanfang
 * @date 10/21/20
 * @description
 *
 */
class MineFragmentMemberMainViewModel(private val repository: MineRepository) : BaseVMViewModel() {

    //数据源
    val data = ObservableField<MineInfoProfileBean>()

    //创建听单是否显示
    val createSheetVisibility = ObservableField<Boolean>(false)

    //收藏听单是否显示
    val favorSheetVisibility = ObservableField<Boolean>(false)

    //发布听单是否显示
    val releaseSheetVisibility = ObservableField<Boolean>(false)

    //创建听单adapter
    val createSheetAdapter by lazy {
        CommonBindVMAdapter<SheetBean>(
            this,
            mutableListOf(),
            R.layout.mine_adapter_member_create_sheet,
            BR.createSheetViewModel,
            BR.createSheetItem
        )
    }

    //收藏听单adapter
    val favorSheetAdapter by lazy {
        CommonBindVMAdapter<FavorSheetBean>(
            this,
            mutableListOf(),
            R.layout.mine_adapter_member_favor_sheet,
            BR.favorSheetViewModel,
            BR.favorSheetItem
        )
    }

    //发布书籍adapter
    val releaseSheetAdapter by lazy {
        CommonBindVMAdapter<MinePublishDetailBean>(
            this,
            mutableListOf(),
            R.layout.mine_adapter_member_release_book,
            BR.releaseViewModel,
            BR.item
        )
    }

    private var memberId = ""

    val isShowNoData = ObservableField<Boolean>(false)


    /**
     * 获取: 发布书籍/听单/收藏听单列表
     */
    fun getMemberProfile(memberId: String) {
        this.memberId = memberId
        showLoading()
        launchOnUI {
            repository.memberProfile(memberId).checkResult(
                onSuccess = {
                    showContentView()
                    processData(it)
                }, onError = {
                    showContentView()
                    DLog.i("----->", "$it")
                    showTip("$it", R.color.business_color_ff5e5e)
                }
            )
        }
    }

    /**
     * 处理请求回来的数据
     */
    private fun processData(bean: MineInfoProfileBean) {
        data.set(bean)
        val favorSheetInfo = bean.favor_sheet_info
        val publishInfo = bean.publish_info
        val sheetInfo = bean.sheet_info

        val favorSize = favorSheetInfo.list?.size ?: 0
        val releaseSize = publishInfo.list.size
        val createSize = sheetInfo.list?.size ?: 0

        if (favorSize <= 0 && releaseSize <= 0 && createSize <= 0) {
            isShowNoData.set(true)
        }

        if (createSize > 0) {
            createSheetVisibility.set(true)
            if (createSize > 2) {
                createSheetAdapter.setList(sheetInfo.list?.subList(0, 2))
            } else {
                createSheetAdapter.setList(sheetInfo.list)
            }
        }

        if (favorSize > 0) {
            favorSheetVisibility.set(true)
            if (favorSize > 2) {
                favorSheetAdapter.setList(favorSheetInfo.list?.subList(0, 2))
            } else {
                favorSheetAdapter.setList(favorSheetInfo.list)
            }
        }

        if (releaseSize > 0) {
            releaseSheetVisibility.set(true)
            if (releaseSize > 2) {
                releaseSheetAdapter.setList(publishInfo.list.subList(0, 2))
            } else {
                releaseSheetAdapter.setList(publishInfo.list)
            }
        }

    }

    /**
     * 发布书籍查看更多
     */
    fun clickReleaseFun(context: Context) {
        MineMemberReleaseBooksActivity.newInstance(context, memberId)
    }

    /**
     * 收藏听单查看更多
     */
    fun clickFavorFun(context: Context) {
        getActivity(context)?.let {
            RouterHelper.createRouter(ListenService::class.java)
                .startListenSheetList(it, LISTEN_SHEET_LIST_COLLECTED_LIST, memberId)
        }
    }

    /**
     * 创建听单查看更多
     */
    fun clickCreateFun(context: Context) {
        getActivity(context)?.let {
            RouterHelper.createRouter(ListenService::class.java)
                .startListenSheetList(it, LISTEN_SHEET_LIST_MY_LIST, memberId)
        }
    }


    /**
     * 发布的书籍item点击事件
     * @param bean 实体对象
     */
    fun releaseItemClickFun(context: Context, bean: MinePublishDetailBean) {
        RouterHelper.createRouter(HomeService::class.java)
            .toDetailActivity(context, bean.audio_id)
    }

    /**
     * 创建听单item点击事件
     * @param context 上下文
     * @param bean 实体对象
     */
    fun createSheetItemClick(context: Context, bean: SheetBean) {
        getActivity(context)?.let {
            RouterHelper.createRouter(HomeService::class.java)
                .startHomeSheetDetailActivity(it, bean.sheet_id)
        }
    }

    /**
     * 收藏听单item点击事件
     * @param context 上下文
     * @param bean 实体对象
     */
    fun favorSheetItemClick(context: Context, bean: FavorSheetBean) {
        getActivity(context)?.let {
            RouterHelper.createRouter(HomeService::class.java)
                .startHomeSheetDetailActivity(it, bean.sheet_id)
        }
    }

}