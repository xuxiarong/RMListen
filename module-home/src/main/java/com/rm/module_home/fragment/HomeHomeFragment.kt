package com.rm.module_home.fragment

import android.text.TextUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.Observable
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.dialog.CommonMvFragmentDialog
import com.rm.baselisten.model.BaseNetStatus
import com.rm.baselisten.mvvm.BaseVMFragment
import com.rm.baselisten.receiver.NetworkChangeReceiver
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.utilExt.DisplayUtils
import com.rm.baselisten.utilExt.dip
import com.rm.business_lib.HomeGlobalData.isHomeDouClick
import com.rm.business_lib.insertpoint.BusinessInsertConstance
import com.rm.business_lib.insertpoint.BusinessInsertManager
import com.rm.component_comm.utils.BannerJumpUtils
import com.rm.module_home.BR
import com.rm.module_home.R
import com.rm.module_home.activity.HomeTopListActivity
import com.rm.module_home.activity.boutique.BoutiqueActivity
import com.rm.module_home.activity.detail.HomeDetailActivity
import com.rm.module_home.activity.menu.HomeMenuActivity
import com.rm.module_home.activity.topic.HomeTopicListActivity
import com.rm.module_home.databinding.HomeDialogHomeAdBinding
import com.rm.module_home.databinding.HomeHomeFragmentBinding
import com.rm.module_home.model.home.HomeAudioModel
import com.rm.module_home.model.home.HomeBlockModel
import com.rm.module_home.model.home.HomeMenuModel
import com.rm.module_home.viewmodel.HomeFragmentViewModel
import kotlinx.android.synthetic.main.home_home_fragment.*

/**
 * desc   :
 * date   : 2020/08/20
 * version: 1.0
 */
class HomeHomeFragment : BaseVMFragment<HomeHomeFragmentBinding, HomeFragmentViewModel>() {
    private var homeDialogAdModelChangedCallback: Observable.OnPropertyChangedCallback? = null
    private var homeItemDataAdModelChangedCallback: Observable.OnPropertyChangedCallback? = null
    private var errorMsgChangedCallback: Observable.OnPropertyChangedCallback? = null
    private var showNetErrorChangedCallback: Observable.OnPropertyChangedCallback? = null
    private var isAvailableChangedCallback: Observable.OnPropertyChangedCallback? = null

    override fun initLayoutId() = R.layout.home_home_fragment

    override fun initModelBrId(): Int = BR.viewModel

    override fun initData() {
        mViewModel.getHomeDataFromService()
    }

    override fun initView() {
        mDataShowView = homeRv
        setStatusBar(R.color.base_activity_bg_color)

        setDefault()
        context?.let {
            val height = DisplayUtils.getStateHeight(it)
            val params = mDataBind.homeHomeSmartLayout.layoutParams as ConstraintLayout.LayoutParams
            params.topMargin = height
        }

        mViewModel.collectItemClickList = { initCollectClick(it) }
        mViewModel.doubleRvLeftScrollOpenDetail = { startBoutique() }
        mViewModel.audioClick = { onAudioClick(it) }
        mViewModel.blockClick = { onBlockClick(it) }
        mDataBind.homeRv.bindVerticalLayout(mViewModel.homeAdapter)
        isHomeDouClick.observe(this, Observer {
            if (it) {
                mDataBind.homeRv.smoothScrollToPosition(0)
                isHomeDouClick.value = false
            }
        })
    }

    private fun showHomeAdDialog() {
        activity?.let { fragmentActivity ->
            CommonMvFragmentDialog().apply {
                dialogHasBackground = true
                dialogCanceledOnTouchOutside = false
                dialogWidth = fragmentActivity.dip(320f)
                initDialog = {
                    mDataBind?.let {
                        val dialogBand = mDataBind as HomeDialogHomeAdBinding
                        dialogBand.homeDialogAdClose.setOnClickListener {
                            BusinessInsertManager.doInsertKeyAndAd(
                                BusinessInsertConstance.INSERT_TYPE_AD_CLOSE,
                                mViewModel.homeDialogAdModel.get()?.ad_id.toString()
                            )
                            dismiss()
                        }
                        dialogBand.homeAdDialogImg.setOnClickListener {
                            mViewModel.homeDialogAdModel.get()?.let {
                                if (context != null && !TextUtils.isEmpty(it.jump_url)) {
                                    BannerJumpUtils.onBannerClick(
                                        context!!,
                                        it.jump_url,
                                        it.ad_id.toString()
                                    )
                                    dismiss()
                                }
                            }
                        }
                    }
                }
            }.showCommonDialog(
                activity = activity as FragmentActivity,
                layoutId = R.layout.home_dialog_home_ad,
                viewModel = mViewModel,
                viewModelBrId = BR.viewModel
            )
        }

    }


    private fun initCollectClick(model: HomeMenuModel) {
        when (model.menu_name) {
            getString(R.string.home_boutique) -> {
                startBoutique()
            }
            getString(R.string.home_rank) -> {
                startRank()
            }
            getString(R.string.home_listen_list) -> {
                startMenu()
            }
        }
    }

    /**
     * ????????????
     */
    override fun startObserve() {
        mViewModel.homeAllData.observe(this, Observer {
            mViewModel.homeAdapter.setList(it)
        })

        homeDialogAdModelChangedCallback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val homeDialogAdModel = mViewModel.homeDialogAdModel.get()
                if (homeDialogAdModel != null && !TextUtils.isEmpty(homeDialogAdModel.image_url)) {
                    showHomeAdDialog()
                }
            }
        }
        homeDialogAdModelChangedCallback?.let {
            mViewModel.homeDialogAdModel.addOnPropertyChangedCallback(it)
        }

        homeItemDataAdModelChangedCallback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val homeItemAdModel = mViewModel.homeItemDataAdModel.get()
                if (homeItemAdModel != null) {
                    //??????????????????????????????????????????????????????????????????
                    if (homeItemAdModel.ad_index_banner == null
                        && homeItemAdModel.ad_index_floor_streamer == null
                        && homeItemAdModel.ad_index_voice == null
                    ) {
                        return
                    }
                    mViewModel.setHomeItemDataAd(homeItemAdModel)
                }
            }
        }
        homeItemDataAdModelChangedCallback?.let {
            mViewModel.homeItemDataAdModel.addOnPropertyChangedCallback(it)
        }

        errorMsgChangedCallback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (mViewModel.errorMsg.get() != null) {
                    ToastUtil.showTopToast(
                        context = activity as FragmentActivity,
                        tipText = mViewModel.errorMsg.get()!!
                    )
                }
            }
        }
        errorMsgChangedCallback?.let {
            mViewModel.errorMsg.addOnPropertyChangedCallback(it)
        }

        showNetErrorChangedCallback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (mViewModel.showNetError.get()) {
                    mViewModel.showNetWorkError()
                }
            }
        }
        showNetErrorChangedCallback?.let {
            mViewModel.showNetError.addOnPropertyChangedCallback(it)
        }

        isAvailableChangedCallback = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (!NetworkChangeReceiver.isAvailable.get() || mViewModel.baseStatusModel.value == null) {
                    return
                }
                if (mViewModel.baseStatusModel.value!!.netStatus == BaseNetStatus.BASE_SHOW_NET_ERROR
                    || mViewModel.baseStatusModel.value!!.netStatus == BaseNetStatus.BASE_SHOW_SERVICE_ERROR
                ) {
                    mViewModel.getHomeDataFromService()
                }
            }
        }
        isAvailableChangedCallback?.let {
            NetworkChangeReceiver.isAvailable.addOnPropertyChangedCallback(it)
        }

    }


    /**
     * ????????????
     */
    private fun startBoutique() {
        context?.let {
            BoutiqueActivity.startActivity(it)
        }
    }

    /**
     * ????????????
     */
    private fun startRank() {
        context?.let {
            HomeTopListActivity.startActivity(it)
        }
    }

    /**
     * ????????????
     */
    private fun startMenu() {
        context?.let {
            HomeMenuActivity.startActivity(it)
        }
    }

    /**
     * ?????????????????????Item???????????????
     * @param audioModel HomeAudioModel
     */
    private fun onAudioClick(audioModel: HomeAudioModel) {
        HomeDetailActivity.startActivity(activity!!, audioModel.audio_id)
    }


    /**
     * ???????????????????????????
     * @param blockModel HomeBlockModel
     */
    private fun onBlockClick(blockModel: HomeBlockModel) {
        HomeTopicListActivity.startTopicActivity(
            activity!!,
            blockModel.topic_id,
            blockModel.block_name
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        homeDialogAdModelChangedCallback?.let {
            mViewModel.homeDialogAdModel.removeOnPropertyChangedCallback(it)
            homeDialogAdModelChangedCallback = null
        }

        homeItemDataAdModelChangedCallback?.let {
            mViewModel.homeItemDataAdModel.removeOnPropertyChangedCallback(it)
            homeItemDataAdModelChangedCallback = null
        }

        errorMsgChangedCallback?.let {
            mViewModel.errorMsg.removeOnPropertyChangedCallback(it)
            errorMsgChangedCallback = null
        }

        showNetErrorChangedCallback?.let {
            mViewModel.showNetError.removeOnPropertyChangedCallback(it)
            showNetErrorChangedCallback = null
        }
        isAvailableChangedCallback?.let {
            NetworkChangeReceiver.isAvailable.removeOnPropertyChangedCallback(it)
            isAvailableChangedCallback = null
        }
    }

}