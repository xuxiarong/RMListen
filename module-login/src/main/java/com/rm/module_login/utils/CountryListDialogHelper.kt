package com.rm.module_login.utils

import android.text.TextUtils
import android.view.Gravity
import android.view.MotionEvent
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.binding.bindVerticalLayout
import com.rm.baselisten.dialog.CommonDragMvDialog
import com.rm.baselisten.utilExt.dip
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_login.BR
import com.rm.module_login.R
import com.rm.module_login.adapter.CountryListAdapter
import com.rm.module_login.databinding.LoginDialogCountryChoiceListBinding
import com.rm.module_login.viewmodel.view.PhoneInputViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.jessyan.autosize.utils.ScreenUtils

/**
 * desc   : 国家列表弹出框帮助类
 * date   : 2020/09/09
 * version: 1.0
 */
object CountryListDialogHelper {
    private var phoneInputViewModel: PhoneInputViewModel? = null
    private val countryChoiceDialog by lazy {
        CommonDragMvDialog().apply {
            gravity = Gravity.BOTTOM
            dialogWidthIsMatchParent = true
            dialogHasBackground = true
            dialogHeight = ScreenUtils.getScreenSize(CONTEXT)[1] - CONTEXT.dip(60)
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

                // 取消点击事件
                dialogBinding.loginDialogCountryCancelBtn.setOnClickListener {
                    dismiss()
                }

                countryListAdapter.setOnItemClickListener { _, _, position: Int ->
                    if (TextUtils.isEmpty(countryListAdapter.data[position].data.phone_code)) return@setOnItemClickListener
                    CountryDataManager.setCurrentCountry(countryListAdapter.data[position].data)
                    phoneInputViewModel!!.countryCode.set("+${countryListAdapter.data[position].data.phone_code}")
                    dismiss()
                }
            }
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
        }else{
            adapter.setList(CountryDataManager.pinyinCountryList)
            adapter.setLetter()
            adapter.notifyDataSetChanged()
        }
        adapter
    }

    /**
     * 显示国家列表显示dialog
     * @param activity FragmentActivity
     * @param viewModel BaseVMViewModel
     */
    fun show(
        activity: FragmentActivity,
        viewModel: BaseVMViewModel,
        phoneInputViewModel: PhoneInputViewModel
    ) {
        this.phoneInputViewModel = phoneInputViewModel
        countryChoiceDialog.showCommonDialog(
            activity,
            R.layout.login_dialog_country_choice_list,
            viewModel,
            BR.viewModel
        )
    }
}