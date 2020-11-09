package com.rm.module_mine.activity

import android.content.Intent
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.DLog
import com.rm.business_lib.bean.Country
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.activity.MineCropActivity.Companion.FILE_PATH
import com.rm.module_mine.activity.MineCropActivity.Companion.RESULT_CODE_CROP
import com.rm.module_mine.bean.UpdateUserInfoBean
import com.rm.module_mine.databinding.MineActivityPersonalInfoBinding
import com.rm.module_mine.viewmodel.MinePersonalInfoViewModel

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description 个人资料页面
 *
 */
class MinePersonalInfoActivity :
    BaseVMActivity<MineActivityPersonalInfoBinding, MinePersonalInfoViewModel>() {

    companion object {
        const val RESULT_CODE_ADDRESS = 200
        const val RESULT_CODE_SIGNATURE = 300
        const val RESULT_CODE_NICK = 400
    }

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.mine_activity_personal_info

    override fun initView() {
        super.initView()
        val titleModel = BaseTitleModel()
            .setLeftIcon(R.drawable.base_icon_back)
            .setTitle(getString(R.string.mine_personal_info))
            .setLeftIconClick { finish() }
        mViewModel.baseTitleModel.value = titleModel

    }

    /**
     * 初始化数据
     */
    override fun initData() {
    }

    override fun startObserve() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        DLog.i("----->onActivityResult", "requestCode:$requestCode   resultCode:$resultCode ")
        if (requestCode == 100) {
            when (resultCode) {
                RESULT_CODE_ADDRESS -> {
                    countrySuccess(data)
                }
                RESULT_CODE_NICK -> {

                }
                RESULT_CODE_SIGNATURE -> {
                }
                RESULT_CODE_CROP -> {

                    data?.getStringExtra(FILE_PATH)?.let {
                        DLog.i("----->onActivityResult", "CROP $it")
                        mViewModel.uploadPic(it)
                    }
                }
            }


        }
    }

    private fun countrySuccess(data: Intent?) {
        val country = data?.getSerializableExtra("country") as Country
        mViewModel.userInfo.get()?.let {
            mViewModel.updateUserInfo(
                UpdateUserInfoBean(
                    it.nickname!!,
                    it.gender!!,
                    it.birthday!!,
                    country.cn,
                    it.signature!!
                )
            )
        }
    }

}