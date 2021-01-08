package com.rm.module_mine.activity

import android.view.WindowManager
import com.rm.baselisten.binding.bindKeyboardVisibilityListener
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.business_lib.loginUser
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineActivitySettingPersonalSignatureBinding
import com.rm.module_mine.viewmodel.MinePersonalSignatureSettingViewModel
import kotlinx.android.synthetic.main.mine_activity_setting_personal_signature.*

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description 个性签名设置页面
 *
 */
class MineSettingPersonalSignatureActivity :
    BaseVMActivity<MineActivitySettingPersonalSignatureBinding, MinePersonalSignatureSettingViewModel>() {

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.mine_activity_setting_personal_signature

    /**
     * 初始化数据
     */
    override fun initData() {
    }

    override fun initView() {
        super.initView()
        val titleModel = BaseTitleModel()
            .setLeftIcon(R.drawable.base_icon_back)
            .setTitle(getString(R.string.mine_signature))
            .setRightText(getString(R.string.business_sure))
            .setLeftIconClick { finish() }
            .setRightTextColor(R.color.business_text_color_b1b1b1)
            .setRightEnabled(true)
            .setRightTextClick { mViewModel.updateUserInfo() }

        mViewModel.baseTitleModel.value = titleModel
        loginUser.get()?.signature?.let {
            mViewModel.inputText.set(it)
        }

        mine_sign_ed.requestFocus()
        //弹起输入法
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) // 设置默认键盘弹出


        mine_sign_ed.bindKeyboardVisibilityListener { b, _ ->
            if (!b) {
                mine_sign_ed.clearFocus()
            }
        }
        mine_sign_root_view.setOnClickListener {
            hideKeyboard(mine_sign_ed)
            mine_sign_ed.clearFocus()
        }
    }

    override fun startObserve() {
    }

    override fun onResume() {
        super.onResume()
        mine_sign_ed.setSelection(mine_sign_ed.text.toString().length)
    }
}