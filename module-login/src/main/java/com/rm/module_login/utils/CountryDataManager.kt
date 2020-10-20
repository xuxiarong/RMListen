package com.rm.module_login.utils

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rm.baselisten.BaseApplication
import com.rm.baselisten.util.getObjectMMKV
import com.rm.baselisten.util.putMMKV
import com.rm.business_lib.bean.Country
import com.rm.business_lib.pinyin.CNPinyin
import com.rm.business_lib.pinyin.CNPinyinFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * desc   : 国家代码信息数据管理类
 * date   : 2020/09/07
 * version: 1.0
 */
object CountryDataManager {
    val pinyinCountryList = mutableListOf<CNPinyin<Country>>()
    // 当前全局选中并使用的国家信息(目前就用到了phone_code,所以名字啥的直接写了)
    var choiceCountry: Country =
        Country("中国", "China", "86")

    /**
     * 当前选中的国家信息
     */
    private const val CHOICE_COUNTRY = "choice_country"

    init {
        val country = CHOICE_COUNTRY.getObjectMMKV(Country::class.java, null)
        if (country != null && !TextUtils.equals(country.phone_code, choiceCountry.phone_code)) {
            choiceCountry = country
        }
    }

    /**
     * 设置当前选中的国家信息
     * @param country Country
     */
    fun setCurrentCountry(country: Country) {
        this.choiceCountry = country
        CHOICE_COUNTRY.putMMKV(choiceCountry)
    }

    suspend fun getCountryList(): MutableList<CNPinyin<Country>> {
        return withContext(Dispatchers.IO) {
            var tempList = mutableListOf<Country>()
            val json = getCountryJson("country.json")
            if (!TextUtils.isEmpty(json)) {
                tempList = Gson().fromJson<MutableList<Country>>(
                    json,
                    object : TypeToken<List<Country>>() {}.type
                )
            }
            pinyinCountryList.addAll(CNPinyinFactory.createCNPinyinList(tempList))
            pinyinCountryList.sort()
            pinyinCountryList
        }
    }

    private fun getCountryJson(fileName: String): String {
        val stringBuilder = StringBuilder()
        val assetManager = BaseApplication.CONTEXT.assets
        BufferedReader(
            InputStreamReader(
                assetManager.open(fileName)
            )
        ).use {
            var line: String
            while (true) {
                line = it.readLine() ?: break
                stringBuilder.append(line)
            }
            it.close()
        }
        return stringBuilder.toString()
    }
}