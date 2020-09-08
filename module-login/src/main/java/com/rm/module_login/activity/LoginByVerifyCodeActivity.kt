package com.rm.module_login.activity

import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.MotionEvent
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.dialog.CommonDragMvDialog
import com.rm.baselisten.dialog.CommonFragmentDialog
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.util.spannable.ChangeItem
import com.rm.baselisten.util.spannable.SpannableHelper
import com.rm.baselisten.util.spannable.TextClickListener
import com.rm.baselisten.utilExt.dip
import com.rm.module_login.BR
import com.rm.module_login.LoginConstants
import com.rm.module_login.R
import com.rm.module_login.adapter.CountryListAdapter
import com.rm.module_login.databinding.LoginActivityLoginByVerifyCodeBinding
import com.rm.module_login.databinding.LoginDialogCountryChoiceListBinding
import com.rm.module_login.utils.CountryDataManager
import com.rm.module_login.viewmodel.LoginByVerifyViewModel
import kotlinx.android.synthetic.main.login_activity_login_by_verify_code.*
import kotlinx.android.synthetic.main.login_include_layout_phone_input.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * desc   : 验证码登陆界面
 * date   : 2020/08/26
 * version: 1.0
 */
class LoginByVerifyCodeActivity :
    BaseVMActivity<LoginActivityLoginByVerifyCodeBinding, LoginByVerifyViewModel>() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, LoginByVerifyCodeActivity::class.java))
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
            dialogHeight = this@LoginByVerifyCodeActivity.dip(752)
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

    override fun getLayoutId(): Int = R.layout.login_activity_login_by_verify_code

    override fun initModelBrId() = BR.viewModel

    override fun startObserve() {
        mViewModel.testDialogData.observe(this, Observer {
//            dialogAdapter.setList(mViewModel.testDialogData.value!!)
        })

        // 监听登陆状态
        LoginConstants.isLogin.observe(this, Observer {
            if (it) {
                // 登陆成功，关闭当前界面
                finish()
            }
        })
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


    private lateinit var commonDialog: CommonFragmentDialog

//    override fun onResume() {
//        super.onResume()
//        /**
//         *  mViewModel ：与Dialog相关的ViewModel，建议使用该dialog依赖的Activity的ViewModel
//         *  R.layout.login_dialong_login_status ： dialog的布局文件
//         *  BR.viewModel ： 布局文件中使用的viewModel变量的名字
//         */
//        loginDialog = CommonMvFragmentDialog().apply {
//            this.dialogBackgroundColor = R.color.businessColorPrimary
//            this.gravity = Gravity.BOTTOM
//            this.dialogHasBackground = true
//            this.dialogWidthIsMatchParent = true
//            this.initDialog = {initDialogView()}
//        }
//
//
//        loginDialog.showCommonDialog(this,R.layout.login_dialong_login_status,mViewModel,BR.viewModel)
//
//    }
//
//    fun initDialogView() {
//        val bind = loginDialog.mDataBind as LoginDialongLoginStatusBinding?
//        bind?.loginDialogRv?.bindVerticalLayout(dialogAdapter)
//        mViewModel.getDialogData()
//    }

    override fun initData() {
        // 设置checkbox选择协议相关文本
        SpannableHelper.with(
            login_by_verify_code_tips,
            resources.getString(R.string.login_login_tips_all)
        )
            .addChangeItem(
                ChangeItem(
                    resources.getString(R.string.login_login_tips_user),
                    ChangeItem.Type.COLOR,
                    resources.getColor(R.color.login_high_color),
                    object : TextClickListener {
                        override fun onTextClick(clickContent: String) {
                            ToastUtil.show(this@LoginByVerifyCodeActivity, "用户协议")
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
                            ToastUtil.show(this@LoginByVerifyCodeActivity, "隐私保护协议")
                        }
                    })
            ).build()
    }
}