package com.rm.module_main.activity.splash

import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import androidx.databinding.Observable
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.dialog.CommonMvFragmentDialog
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.*
import com.rm.baselisten.util.constant.PermissionConstants
import com.rm.baselisten.util.spannable.ChangeItem
import com.rm.baselisten.util.spannable.SpannableHelper
import com.rm.baselisten.util.spannable.TextClickListener
import com.rm.baselisten.utilExt.Color
import com.rm.baselisten.utilExt.String
import com.rm.baselisten.utilExt.dip
import com.rm.baselisten.web.BaseWebActivity
import com.rm.business_lib.FIRST_OPEN_APP
import com.rm.business_lib.HomeGlobalData
import com.rm.business_lib.coroutinepermissions.requestPermissionsForResult
import com.rm.business_lib.insertpoint.BusinessInsertConstance
import com.rm.business_lib.insertpoint.BusinessInsertManager
import com.rm.business_lib.isLogin
import com.rm.business_lib.wedgit.bindAdId
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.component_comm.utils.BannerJumpUtils
import com.rm.module_main.BR
import com.rm.module_main.R
import com.rm.module_main.activity.MainMainActivity
import com.rm.module_main.activity.guide.MainGuideActivity
import com.rm.module_main.databinding.HomeActivitySplashBinding
import com.rm.module_main.databinding.HomeDialogPrivateServiceBinding
import com.rm.module_main.viewmodel.HomeSplashViewModel
import kotlinx.android.synthetic.main.home_activity_splash.*

/**
 * desc   :
 * date   : 2020/11/02
 * version: 1.0
 */
class SplashActivity : BaseVMActivity<HomeActivitySplashBinding, HomeSplashViewModel>() {

    var result = false

    override fun initModelBrId() = BR.viewModel
    override fun getLayoutId() = R.layout.home_activity_splash

    override fun startObserve() {
        mViewModel.isSkipAd.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (mViewModel.isSkipAd.get()) {
                    MainMainActivity.startMainActivity(this@SplashActivity)
                    finish()
                }
            }
        })
        mViewModel.mainAdScreen.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val adScreen = mViewModel.mainAdScreen.get()
                if (null != adScreen && !TextUtils.isEmpty(adScreen.image_url)) {
                    val options: RequestOptions = RequestOptions() //图片加载出来前，显示的图片
                        .placeholder(R.drawable.splash) //url为空的时候,显示的图片
                        .fallback(R.drawable.splash) //图片加载失败后，显示的图片
                        .error(R.drawable.splash)
                    Glide.with(this@SplashActivity).load(adScreen.image_url)
                        .apply(options)
                        .into(splash_ad_img)
                }
            }
        })

    }

    override fun initView() {
        super.initView()
        setTransparentStatusBar()

        if (isLogin.get()) {
            BusinessInsertManager.doInsertKey(BusinessInsertConstance.INSERT_TYPE_LOGIN)
        }
        BusinessInsertManager.doInsertKey(BusinessInsertConstance.INSERT_TYPE_ACTIVE)

        splash_ad_img.setOnClickNotDoubleListener {
            mViewModel.mainAdScreen.get()?.let {
                MainMainActivity.startMainActivity(this@SplashActivity, splashUrl = it.jump_url)
                BusinessInsertManager.doInsertKeyAndAd(
                    BusinessInsertConstance.INSERT_TYPE_AD_CLICK,
                    it.ad_id.toString()
                )
                finish()
            }
        }
    }

    override fun initData() {
        if (FIRST_OPEN_APP.getBooleanMMKV(true)) {
            FIRST_OPEN_APP.getBooleanMMKV(false)
            BusinessInsertManager.doInsertKey(BusinessInsertConstance.INSERT_TYPE_ACTIVATION)
        }

        if (!HomeGlobalData.HOME_IS_AGREE_PRIVATE_PROTOCOL.getBooleanMMKV(false)) {
            showPrivateServiceDialog()
            mViewModel.isShowAd.set(false)
        } else {
            mViewModel.isShowAd.set(true)
            mViewModel.getSplashAd()
            mViewModel.startSkipTimerCount()
        }
        RouterHelper.createRouter(PlayService::class.java)
            .initPlayService(BaseApplication.baseApplication)

    }


    suspend fun requestPermissions() {
        val result = requestPermissionsForResult(
            permissions = *arrayOf(PermissionConstants.STORAGE),
            title = "权限请求",
            rationale = "类漫听书需要存储权限"
        )
        DLog.d("suolong", "result = $result")
        if (result && mViewModel.isSkipAd.get()) {
            MainMainActivity.startMainActivity(this@SplashActivity)
            finish()
        }
    }

    /**
     * 显示隐私政策弹窗
     */
    private fun showPrivateServiceDialog() {
        CommonMvFragmentDialog().apply {
            dialogHasBackground = true
            dialogCanceledOnTouchOutside = false
            dialogWidth = this@SplashActivity.dip(320f)
            initDialog = {
                mDataBind?.let {
                    val dialogBand = mDataBind as HomeDialogPrivateServiceBinding
                    setSpannableText(
                        activity as FragmentActivity,
                        dialogBand.homeDialogPrivateServiceContent
                    )
                    dialogBand.homeDialogAgreeProtocol.setOnClickListener {
                        HomeGlobalData.HOME_IS_AGREE_PRIVATE_PROTOCOL.putMMKV(true)
                        MainGuideActivity.startGuideActivity(this@SplashActivity)
                        this@SplashActivity.finish()
                        dismiss()
                    }
                    dialogBand.homeDialogNotAgreeProtocol.setOnClickListener {
                        ToastUtil.show(
                            this@SplashActivity,
                            getString(R.string.main_please_agree_private_service)
                        )
                    }
                }
            }

        }.showCommonDialog(
            activity = this,
            layoutId = R.layout.home_dialog_private_service,
            viewModel = mViewModel,
            viewModelBrId = BR.viewModel
        )
    }


    /**
     * 设置富文本
     * @param textView TextView
     */
    private fun setSpannableText(fragmentActivity: FragmentActivity, textView: TextView) {
        SpannableHelper.with(
            textView,
            fragmentActivity.resources.getString(R.string.home_private_service_content)
        )
            .addChangeItem(
                ChangeItem(
                    fragmentActivity.String(R.string.home_private_protocol),
                    ChangeItem.Type.COLOR,
                    fragmentActivity.Color(R.color.business_color_789dcb),
                    object : TextClickListener {
                        override fun onTextClick(clickContent: String) {
                            BaseWebActivity.startBaseWebActivity(
                                this@SplashActivity,
                                "www.baidu.com"
                            )
                        }
                    })
            )
            .addChangeItem(
                ChangeItem(
                    fragmentActivity.String(R.string.home_service_protocol),
                    ChangeItem.Type.COLOR,
                    fragmentActivity.Color(R.color.business_color_789dcb),
                    object : TextClickListener {
                        override fun onTextClick(clickContent: String) {
                            BaseWebActivity.startBaseWebActivity(
                                this@SplashActivity,
                                "www.baidu.com"
                            )
                        }
                    })
            ).build()
    }
}