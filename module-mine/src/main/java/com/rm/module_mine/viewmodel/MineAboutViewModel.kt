package com.rm.module_mine.viewmodel

import android.content.Context
import android.text.TextUtils
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

    fun clickCooperation(context: Context, bean: MineAboutUsBean) {
        if (TextUtils.equals("版本更新", bean.title)) {
            if (bean.showRed) {
                showUploadDialog(context)
            }
        } else {
            bean.jump_url?.let {
                BaseWebActivity.startBaseWebActivity(context, it)
            }
        }
    }

    fun getAboutUs() {
        launchOnIO {
            repository.mineAboutUs().checkResult(onSuccess = {
                mAdapter.addData(it)
            }, onError = {
                showTip("$it", R.color.business_color_ff5e5e)
            })
        }
    }

    fun getLaseVersion() {
        launchOnIO {
            repository.mineGetLaseUrl().checkResult(onSuccess = {
                versionInfo = it
                mAdapter.data[0].sub_title = "${it.version}"
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
            }, onError = {
                showTip("$it", R.color.business_color_ff5e5e)
            })
        }
    }

    private fun showUploadDialog(context: Context) {
        getActivity(context)?.let { activity ->
            versionInfo?.let {
                RouterHelper.createRouter(HomeService::class.java)
                    .showUploadDownDialog(activity, it, INSTALL_RESULT_CODE, false)
            }
        }
    }
}