package com.rm.module_login.activity

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.dialog.CommonDragMvDialog
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.utilExt.dip
import com.rm.module_login.BR
import com.rm.module_login.R
import com.rm.module_login.adapter.CountryListAdapter
import com.rm.module_login.databinding.LoginActivityForgetPasswordBinding
import com.rm.module_login.databinding.LoginDialogCountryChoiceListBinding
import com.rm.module_login.utils.CountryDataManager
import com.rm.module_login.viewmodel.ForgetPasswordViewModel
import kotlinx.android.synthetic.main.login_include_layout_phone_input.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * desc   : 忘记密码界面
 * date   : 2020/08/27
 * version: 1.0
 */
class ForgetPasswordActivity :
    BaseVMActivity<LoginActivityForgetPasswordBinding, ForgetPasswordViewModel>() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, ForgetPasswordActivity::class.java))
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
            dialogHeight = this@ForgetPasswordActivity.dip(752)
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
            }
        }
    }

    override fun initModelBrId(): Int = BR.viewModel

    override fun getLayoutId(): Int = R.layout.login_activity_forget_password

    override fun startObserve() {
    }

    override fun initView() {
        super.initView()
        mViewModel.baseTitleModel.value = BaseTitleModel().setLeftIconClick { finish() }
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
    }
}