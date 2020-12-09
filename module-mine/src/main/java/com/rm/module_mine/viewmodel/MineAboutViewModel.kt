package com.rm.module_mine.viewmodel

import android.content.Context
import android.text.TextUtils
import com.rm.baselisten.adapter.single.CommonBindVMAdapter
import com.rm.baselisten.net.checkResult
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.baselisten.web.BaseWebActivity
import com.rm.business_lib.aria.AriaUploadVersionDownloadManager
import com.rm.business_lib.base.dialog.TipsFragmentDialog
import com.rm.business_lib.base.dialog.VersionUploadDialog
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.bean.MineAboutUsBean
import com.rm.business_lib.bean.BusinessVersionUrlBean
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
//            val lastVersion = versionInfo?.version?.replace(".", "") ?: "0"
//            val localVersion = BuildConfig.VERSION_NAME.replace(".", "")
//            try {
//                if (lastVersion.toInt() - localVersion.toInt() > 0) {
            showUploadDialog(context)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }

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
                mAdapter.notifyItemChanged(0)
            }, onError = {
                showTip("$it", R.color.business_color_ff5e5e)
            })
        }
    }

    private fun showUploadDialog(context: Context) {
        getActivity(context)?.let {
            VersionUploadDialog().apply {
                contentText = "${versionInfo?.description}"
                uploadUrl = "${versionInfo?.package_url}"
                version = "${versionInfo?.version}"
                downloadComplete = {
                    //TODO 下载完成，跳转安装
                }
            }.show(it)
        }
    }
}