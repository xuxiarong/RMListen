package com.rm.module_login.adapter

import android.text.TextUtils
import com.rm.baselisten.adapter.single.CommonBindAdapter
import com.rm.module_login.BR
import com.rm.module_login.R
import com.rm.module_login.bean.Country
import com.rm.module_login.pinyin.CNPinyin
import com.rm.module_login.utils.CountryDataManager
import java.util.*

/**
 * desc   :
 * date   : 2020/09/07
 * version: 1.0
 */
class CountryListAdapter : CommonBindAdapter<CNPinyin<Country>>(
    CountryDataManager.pinyinCountryList,
    R.layout.login_adapter_country_item,
    BR.country
) {
    private var letterIndexes: HashMap<String, Int> = hashMapOf()

    fun setLetter() {
        val size: Int = data.size
        for (index in 0 until size) {
            val currentLetter: String = java.lang.String.valueOf(data[index].firstChar)
            val previousLetter = if (index >= 1) java.lang.String.valueOf(
                data[index - 1].firstChar
            ) else ""
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                letterIndexes[currentLetter] = index
            }
        }
    }

    fun getLetterPosition(letter: String?): Int {
        val integer = letterIndexes[letter]
        return integer ?: -1
    }
}