package com.rm.module_login.activity

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.rm.baselisten.BaseApplication.Companion.CONTEXT
import com.rm.baselisten.model.BaseTitleModel
import com.rm.baselisten.mvvm.BaseVMActivity
import com.rm.baselisten.util.DLog
import com.rm.module_login.BR
import com.rm.module_login.R
import com.rm.module_login.databinding.LoginActivityCountryListBinding
import com.rm.module_login.utils.CountryDataManager
import com.rm.module_login.viewmodel.LoginCountryListViewModel

/**
 *
 * @author yuanfang
 * @date 10/20/20
 * @description
 *
 */
class CountryListActivity :
    BaseVMActivity<LoginActivityCountryListBinding, LoginCountryListViewModel>() {
    companion object {
        fun newInstance(context: Activity, code: Int) {
            context.startActivityForResult(
                Intent(context, CountryListActivity::class.java), code
            )
        }
    }

    override fun initModelBrId() = BR.viewModel

    override fun getLayoutId() = R.layout.login_activity_country_list

    override fun initView() {
        super.initView()
        mDataBind?.let {
            it.loginActivityCountryLetterBar.setIndexChangeListener { position, tag, event ->
                val index = mViewModel.countryListAdapter.getLetterPosition(tag)
                if (index != -1) {
                    (it.loginActivityCountryRecyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                        index,
                        0
                    )
                    it.loginActivityCountryIndexBar.setDrawData(event.y, tag, position)
                }
            }
        }

        mViewModel.countryListAdapter.setOnItemClickListener { _, _, position: Int ->
            if (TextUtils.isEmpty(mViewModel.countryListAdapter.data[position].data.phone_code)) return@setOnItemClickListener
            CountryDataManager.setCurrentCountry(mViewModel.countryListAdapter.data[position].data)
            val data = mViewModel.countryListAdapter.data[position].data
            DLog.i("---->", "$data")
            val intent = intent.putExtra("country", data)
            setResult(200, intent)
            finish()
        }
        val baseTitleModel = BaseTitleModel()
            .setTitle(CONTEXT.getString(R.string.login_choice_country))
            .setLeftIcon(R.drawable.base_icon_back)
            .setLeftIconClick { finish() }
        mViewModel.baseTitleModel.value = baseTitleModel
    }

    override fun startObserve() {
    }

    override fun initData() {
    }
}