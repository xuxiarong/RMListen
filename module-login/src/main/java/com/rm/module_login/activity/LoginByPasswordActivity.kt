package com.rm.module_login.activity

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.dialog.CommonDragMvDialog
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.util.spannable.ChangeItem
import com.rm.baselisten.util.spannable.SpannableHelper
import com.rm.baselisten.util.spannable.TextClickListener
import com.rm.baselisten.utilExt.dip
import com.rm.module_login.BR
import com.rm.module_login.R
import com.rm.module_login.adapter.CountryListAdapter
import com.rm.module_login.databinding.LoginActivityLoginByPassowrdBinding
import com.rm.module_login.databinding.LoginDialogCountryChoiceListBinding
import com.rm.module_login.utils.CountryDataManager
import com.rm.module_login.viewmodel.LoginByPasswordViewModel
import kotlinx.android.synthetic.main.login_activity_login_by_passowrd.*
import kotlinx.android.synthetic.main.login_include_layout_phone_input.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * desc   : 密码登陆界面
 * date   : 2020/08/26
 * version: 1.0
 */
class LoginByPasswordActivity :
    BaseVMActivity<LoginActivityLoginByPassowrdBinding, LoginByPasswordViewModel>() {
    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, LoginByPasswordActivity::class.java))
        }
    }


    /**
     * 国家地区的列表Adapter
     */
    private val countryListAdapter by lazy {
        val adapter = CountryListAdapter()

        if (CountryDataManager.pinyinCountryList.isEmpty()) {
            GlobalScope.launch(Dispatchers.Main) {
                val countryList = GlobalScope.async {
                    CountryDataManager.getCountryList()
                }
                adapter.setList(countryList.await())
                adapter.setLetter()
                adapter.notifyDataSetChanged()
            }
        }
        adapter
    }

    // 选择国家的dialog
    private val countryChoiceDialog by lazy {
        CommonDragMvDialog().apply {
            gravity = Gravity.BOTTOM
            dialogWidthIsMatchParent = true
            dialogHasBackground = true
            dialogHeight = this@LoginByPasswordActivity.dip(752)
            initDialog = {
                val dialogBinding = this.mDataBind as LoginDialogCountryChoiceListBinding
                dialogBinding.loginDialogCountryRecyclerView.bindVerticalLayout(countryListAdapter)
                dialogBinding.loginDialogCountryLetterBar.setIndexChangeListener { position: Int, tag: String, event: MotionEvent ->
                    val pos: Int = countryListAdapter.getLetterPosition(tag)
                    if (pos != -1) {
                        (dialogBinding.loginDialogCountryRecyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                            pos,
                            0
                        )
                        dialogBinding.loginDialogCountryIndexBar.setDrawData(event.y, tag, position)
                    }
                }
                countryListAdapter.setOnItemClickListener { _, _, position: Int ->
                    mViewModel.phoneInputViewModel.countryCode.set("+${countryListAdapter.data[position].data.phone_code}")
                    dismiss()
                }
            }
        }
    }


    override fun getLayoutId(): Int = R.layout.login_activity_login_by_passowrd

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {
    }

    override fun initView() {
        super.initView()
        login_include_phone_input_country_code.setOnClickListener {
            countryChoiceDialog.showCommonDialog(
                this,
                R.layout.login_dialog_country_choice_list,
                mViewModel,
                BR.viewModel
            )
        }

        login_include_phone_input_arrow_view.setOnClickListener {
            countryChoiceDialog.showCommonDialog(
                this,
                R.layout.login_dialog_country_choice_list,
                mViewModel,
                BR.viewModel
            )
        }
    }

    override fun initData() {
        // 设置checkbox选择协议相关文本
        SpannableHelper.with(
            login_by_password_tips,
            resources.getString(R.string.login_login_tips_all)
        )
            .addChangeItem(
                ChangeItem(
                    resources.getString(R.string.login_login_tips_user),
                    ChangeItem.Type.COLOR,
                    resources.getColor(R.color.login_high_color),
                    object : TextClickListener {
                        override fun onTextClick(clickContent: String) {
                            ToastUtil.show(this@LoginByPasswordActivity, "用户协议")
                        }
                    })
            )
            .addChangeItem(
                ChangeItem(
                    resources.getString(R.string.login_login_tips_privacy),
                    ChangeItem.Type.COLOR,
                    resources.getColor(R.color.login_high_color),
                    object : TextClickListener {
                        override fun onTextClick(clickContent: String) {
                            ToastUtil.show(this@LoginByPasswordActivity, "隐私保护协议")
                        }
                    })
            ).build()
    }
}