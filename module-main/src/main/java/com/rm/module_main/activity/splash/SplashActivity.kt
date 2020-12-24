package com.rm.module_main.activity.splash

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
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
import com.rm.baselisten.util.spannable.ChangeItem
import com.rm.baselisten.util.spannable.SpannableHelper
import com.rm.baselisten.util.spannable.TextClickListener
import com.rm.baselisten.utilExt.Color
import com.rm.baselisten.utilExt.String
import com.rm.baselisten.utilExt.dip
import com.rm.baselisten.web.BaseWebActivity
import com.rm.business_lib.FIRST_OPEN_APP
import com.rm.business_lib.HomeGlobalData
import com.rm.business_lib.UPLOAD_APP_TIME
import com.rm.business_lib.insertpoint.BusinessInsertConstance
import com.rm.business_lib.insertpoint.BusinessInsertManager
import com.rm.business_lib.isLogin
import com.rm.business_lib.utils.ApkInstallUtils
import com.rm.business_lib.utils.ApkInstallUtils.REQUEST_CODE_INSTALL_APP
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.play.PlayService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_main.BR
import com.rm.module_main.BuildConfig
import com.rm.module_main.R
import com.rm.module_main.activity.MainMainActivity
import com.rm.module_main.activity.guide.MainGuideActivity
import com.rm.module_main.databinding.HomeActivitySplashBinding
import com.rm.module_main.databinding.HomeDialogPrivateServiceBinding
import com.rm.module_main.viewmodel.HomeSplashViewModel
import com.rm.module_main.viewmodel.HomeSplashViewModel.Companion.INSTALL_RESULT_CODE
import kotlinx.android.synthetic.main.home_activity_splash.*

/**
 * desc   :
 * date   : 2020/11/02
 * version: 1.0
 */
class SplashActivity : BaseVMActivity<HomeActivitySplashBinding, HomeSplashViewModel>() {

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

        mViewModel.versionInfo.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val versionInfo = mViewModel.versionInfo.get()
                if (versionInfo != null) {
                    val lastVersion = versionInfo.version?.replace(".", "") ?: "0"
//                    val localVersion = BuildConfig.VERSION_NAME.replace(".", "")
                    val localVersion = BuildConfig.VERSION_CODE
                    try {
                        if (lastVersion.toInt() - localVersion > 0) {
//                            //强制更新
                            if (TextUtils.equals(versionInfo.type, "2")) {
                                mViewModel.showUploadDialog(this@SplashActivity,
                                    sureIsDismiss = true,
                                    cancelIsFinish = true,
                                    sureBlock = {

                                    },
                                    cancelBlock = {
                                    })
                            } else {
                                //一天内只显示一次
                                val curTime = 24 * 60 * 60 * 1000
//                                if (System.currentTimeMillis() - UPLOAD_APP_TIME.getLongMMKV() > curTime) {
                                UPLOAD_APP_TIME.putMMKV(System.currentTimeMillis())
                                //普通更新
                                mViewModel.showUploadDialog(this@SplashActivity,
                                    sureIsDismiss = true,
                                    cancelIsFinish = false,
                                    sureBlock = {

                                    },
                                    cancelBlock = {
                                        loadAd()
                                    })
//                                } else {
//                                    loadAd()
//                                }
                            }
                        } else {
                            loadAd()
                        }
                    } catch (e: Exception) {
                        DLog.i("=====>versionInfo", "Exception:${e.message}")
                        e.printStackTrace()
                        loadAd()
                    }
                } else {
                    loadAd()
                }
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        DLog.i("requestCode", "requestCode:$requestCode   resultCode$resultCode")
        if (requestCode == INSTALL_RESULT_CODE) {
            val path = data?.getStringExtra("apkPath")
            if (requestCode == RESULT_OK) {
                ApkInstallUtils.install(this, path)
            } else {
                //200毫秒后再次查询
                Handler().postDelayed({
                    DLog.i("requestCode", "=====${mViewModel.downPath}")
                    if (mViewModel.downPath.isNotEmpty()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val hasInstallPermission =
                                packageManager.canRequestPackageInstalls()
                            DLog.i("requestCode", "=====$hasInstallPermission")
                            if (!hasInstallPermission) {
                                RouterHelper.createRouter(HomeService::class.java)
                                    .gotoInstallPermissionSetting(
                                        this,
                                        mViewModel.downPath,
                                        INSTALL_RESULT_CODE
                                    )

                            } else {
                                ApkInstallUtils.install(this, mViewModel.downPath)
                            }
                        }
                    } else {
                        loadAd()
                    }
                }, 200)
            }
        } else if (requestCode == REQUEST_CODE_INSTALL_APP) {
            if (requestCode != RESULT_OK) {
                mViewModel.versionInfo.get()?.let {
                    if (!TextUtils.equals(it.type, "2")) {
                        loadAd()
                    }
                }
            }
        }
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
        requestPermissions()
    }

    private fun initSplashData() {
        if (FIRST_OPEN_APP.getBooleanMMKV(true)) {
            FIRST_OPEN_APP.getBooleanMMKV(false)
            BusinessInsertManager.doInsertKey(BusinessInsertConstance.INSERT_TYPE_ACTIVATION)
        }

        mViewModel.getLaseVersion()
//        loadAd()

    }

    private fun loadAd() {
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

    private fun requestPermissions() {
        requestPermissionForResult(permission = Manifest.permission.WRITE_EXTERNAL_STORAGE,
            actionDenied = {
                initSplashData()
            },
            actionGranted = {
                // todo
                initSplashData()
            },
            actionPermanentlyDenied = {
                finish()
            })
    }


    /**
     * 显示隐私政策弹窗
     */
    private fun showPrivateServiceDialog() {
        CommonMvFragmentDialog().apply {
            dialogHasBackground = true
            dialogCanceledOnTouchOutside = false
            dialogCancel = false
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