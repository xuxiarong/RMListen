package com.rm.module_mine.activity

import android.view.WindowManager
import com.rm.baselisten.binding.bindKeyboardVisibilityListener
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.business_lib.loginUser
import com.rm.module_mine.BR
import com.rm.module_mine.R
import com.rm.module_mine.databinding.MineActivitySettingNicknameBinding
import com.rm.module_mine.viewmodel.MineNicknameSettingViewModel
import kotlinx.android.synthetic.main.mine_activity_setting_nickname.*

/**
 *
 * @author yuanfang
 * @date 9/21/20
 * @description 昵称设置页面
 *
 */
class MineSettingNicknameActivity :
    BaseVMActivity<MineActivitySettingNicknameBinding, MineNicknameSettingViewModel>() {

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.mine_activity_setting_nickname

    /**
     * 初始化数据
     */
    override fun initData() {
    }

    override fun initView() {
        super.initView()
        val titleModel = BaseTitleModel()
            .setLeftIcon(R.drawable.base_icon_back)
            .setTitle(getString(R.string.mine_change_nickname))
            .setRightText(getString(R.string.business_sure))
            .setRightTextColor(R.color.business_text_color_b1b1b1)
            .setRightTextClick { mViewModel.updateUserInfo() }
            .setLeftIconClick { finish() }
        mViewModel.baseTitleModel.value = titleModel

        loginUser.get()?.nickname?.let {
            mViewModel.inputText.set(it)
        }
        mine_setting_nike_ed.requestFocus()
        //弹起输入法
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) // 设置默认键盘弹出


        mine_setting_nike_ed.bindKeyboardVisibilityListener { b, _ ->
            if (!b) {
                mine_setting_nike_ed.clearFocus()
            }
        }
        mine_setting_nike_root_view.setOnClickListener {
            hideKeyboard(mine_setting_nike_ed)
            mine_setting_nike_ed.clearFocus()
        }
    }

    override fun startObserve() {
    }

    override fun onResume() {
        super.onResume()
        mine_setting_nike_ed.setSelection(mine_setting_nike_ed.text.toString().length)
    }

}