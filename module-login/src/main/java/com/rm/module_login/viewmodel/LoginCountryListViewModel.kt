package com.rm.module_login.viewmodel

import androidx.databinding.ObservableField
import com.rm.baselisten.viewmodel.BaseVMViewModel
import com.rm.module_login.adapter.CountryListAdapter
import com.rm.module_login.utils.CountryDataManager
import com.rm.module_login.viewmodel.view.PhoneInputViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 *
 * @author yuanfang
 * @date 10/20/20
 * @description
 *
 */
class LoginCountryListViewModel : BaseVMViewModel() {
    var phoneInputViewModel: PhoneInputViewModel? = null

    /**
     * 国家地区的列表Adapter
     */
    val countryListAdapter by lazy {
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

}