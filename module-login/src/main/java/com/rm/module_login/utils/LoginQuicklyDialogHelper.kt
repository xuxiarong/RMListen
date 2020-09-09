package com.rm.module_login.utils

import android.view.Gravity
import android.view.MotionEvent
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.dialog.CommonDragMvDialog
import com.rm.baselisten.dialog.CommonMvFragmentDialog
import com.rm.baselisten.util.ToastUtil
import com.rm.baselisten.util.spannable.ChangeItem
import com.rm.baselisten.util.spannable.SpannableHelper
import com.rm.baselisten.util.spannable.TextClickListener
import com.rm.baselisten.utilExt.Color
import com.rm.baselisten.utilExt.String
import com.rm.baselisten.utilExt.dip
import com.rm.module_login.BR
import com.rm.module_login.R
import com.rm.module_login.adapter.CountryListAdapter
import com.rm.module_login.databinding.LoginDialogCountryChoiceListBinding
import com.rm.module_login.databinding.LoginDialogQuicklyLoginBinding
import com.rm.module_login.viewmodel.dialog.LoginQuicklyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * desc   : 快捷登陆弹出框帮助类
 * date   : 2020/09/08
 * version: 1.0
 */
class LoginQuicklyDialogHelper(
    val mViewModel: LoginQuicklyViewModel,
    val fragmentActivity: FragmentActivity
) {

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

    /**
     * 快速登陆dialog
     */
    private val quicklyLoginDialog by lazy {
        CommonMvFragmentDialog().apply {
            gravity = Gravity.BOTTOM
            dialogWidthIsMatchParent = true
            dialogHasBackground = true
//            dialogHeight = BaseApplication.CONTEXT.dip(752)
            initDialog = {
                // 设置checkbox选择协议相关文本
                val dialogBinding = this.mDataBind as LoginDialogQuicklyLoginBinding
                setSpannableText(dialogBinding.loginQuicklyTips)
                dialogBinding.loginQuicklyPhoneInputLay.loginIncludePhoneInputCountryCode.setOnClickListener {
                    showCountryListDialog()
                }
                dialogBinding.loginQuicklyPhoneInputLay.loginIncludePhoneInputArrowView.setOnClickListener {
                    showCountryListDialog()
                }
            }
        }
    }

    fun show() {
        mViewModel.clear()

        quicklyLoginDialog.showCommonDialog(
            fragmentActivity,
            R.layout.login_dialog_quickly_login,
            mViewModel,
            BR.viewModel
        )
    }

    /**
     * 选择国家的dialog
     */
    private val countryChoiceDialog by lazy {
        CommonDragMvDialog().apply {
            gravity = Gravity.BOTTOM
            dialogWidthIsMatchParent = true
            dialogHasBackground = true
            dialogHeight = fragmentActivity.dip(752)
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

    /**
     * 显示列表国家列表
     */
    private fun showCountryListDialog() {
        countryChoiceDialog.showCommonDialog(
            fragmentActivity,
            R.layout.login_dialog_country_choice_list,
            mViewModel,
            BR.viewModel
        )
    }


    /**
     * 设置富文本
     * @param textView TextView
     */
    private fun setSpannableText(textView: TextView) {
        SpannableHelper.with(
            textView,
            fragmentActivity.resources.getString(R.string.login_login_tips_all)
        )
            .addChangeItem(
                ChangeItem(
                    fragmentActivity.String(R.string.login_login_tips_user),
                    ChangeItem.Type.COLOR,
                    fragmentActivity.Color(R.color.login_high_color),
                    object : TextClickListener {
                        override fun onTextClick(clickContent: String) {
                            ToastUtil.show(fragmentActivity, "用户协议")
                        }
                    })
            )
            .addChangeItem(
                ChangeItem(
                    fragmentActivity.String(R.string.login_login_tips_privacy),
                    ChangeItem.Type.COLOR,
                    fragmentActivity.Color(R.color.login_high_color),
                    object : TextClickListener {
                        override fun onTextClick(clickContent: String) {
                            ToastUtil.show(fragmentActivity, "隐私保护协议")
                        }
                    })
            ).build()
    }
}