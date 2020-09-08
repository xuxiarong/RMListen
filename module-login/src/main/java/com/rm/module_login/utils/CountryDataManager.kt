package com.rm.module_login.utils

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rm.baselisten.BaseApplication
import com.rm.module_login.bean.Country
import com.rm.module_login.pinyin.CNPinyin
import com.rm.module_login.pinyin.CNPinyinFactory
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