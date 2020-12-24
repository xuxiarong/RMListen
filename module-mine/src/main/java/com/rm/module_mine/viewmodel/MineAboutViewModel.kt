package com.rm.module_mine.viewmodel

import android.content.Context
import android.text.TextUtils
import android.view.View
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.baselisten.web.BaseWebActivity
import com.rm.business_lib.bean.BusinessVersionUrlBean
import com.rm.component_comm.home.HomeService
import com.rm.component_comm.router.RouterHelper
import com.rm.module_mine.BR
import com.rm.module_mine.BuildConfig
import com.rm.module_mine.R
import com.rm.module_mine.bean.MineAboutUsBean
import com.rm.module_mine.repository.MineRepository


/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description
 *
 */
class MineAboutViewModel(private val repository: MineRepository) : BaseVMViewModel() {

    var versionInfo: BusinessVersionUrlBean? = null

    companion object {
        const val INSTALL_RESULT_CODE = 10001
    }


    val mAdapter by lazy {
        CommonBindVMAdapter(
            this,
            mutableListOf(MineAboutUsBean("版本更新", "", "")),
            R.layout.mine_adapter_about_us,
            BR.viewModel,
            BR.item
        )
    }

    fun clickCooperation(view: View, bean: MineAboutUsBean) {
        if (TextUtils.equals("版本更新", bean.title)) {
            view.postDelayed({
                if (bean.showRed) {
                    showUploadDialog(view.context)
                } else {
                    showTip("当前已经是最新版本了")
                }
            }, 100)
        } else {
            bean.jump_url?.let {
                BaseWebActivity.startBaseWebActivity(view.context, it)
            }
        }
    }

    fun getAboutUs() {
        launchOnIO {
            repository.mineAboutUs().checkResult(onSuccess = {
                mAdapter.addData(it)
            }, onError = { it, _ ->
                showTip("$it", R.color.business_color_ff5e5e)
            })
        }
    }

    fun getLaseVersion() {
        launchOnIO {
            repository.mineGetLaseUrl().checkResult(onSuccess = {
                versionInfo = it
                val version = it.version ?: BuildConfig.VERSION_NAME
                mAdapter.data[0].sub_title = version
                try {
                    val lastVersion = versionInfo?.version?.replace(".", "") ?: "0"
                    val localVersion = BuildConfig.VERSION_NAME.replace(".", "")

                    if (lastVersion.toInt() - localVersion.toInt() > 0) {
                        mAdapter.data[0].showRed = true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                mAdapter.notifyItemChanged(0)
            }, onError = { it, _ ->
                showTip("$it", R.color.business_color_ff5e5e)
            })
        }
    }

    private fun showUploadDialog(context: Context) {
        getActivity(context)?.let { activity ->
            versionInfo?.let {
                RouterHelper.createRouter(HomeService::class.java)
                    .showUploadDownDialog(
                        activity = activity,
                        versionInfo = it,
                        installCode = INSTALL_RESULT_CODE,
                        dialogCancel = true,
                        cancelIsFinish = false,
                        downloadComplete = {},
                        sureIsDismiss = true,
                        cancelBlock = {

                        }, sureBlock = {

                        })
            }
        }
    }
}