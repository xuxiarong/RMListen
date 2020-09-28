package com.rm.business_lib.db.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rm.business_lib.bean.AudioChapterListModel
import com.rm.business_lib.bean.ChapterList
import org.greenrobot.greendao.converter.PropertyConverter


/**
 *
 * @des:
 * @data: 9/27/20 12:09 PM
 * @Version: 1.0.0
 */
class ChapterListConverter : PropertyConverter<List<ChapterList>, String> {
    override fun convertToDatabaseValue(arrays: List<ChapterList>?): String {
        return if (arrays == null || arrays.isEmpty())
            ""
        else Gson().toJson(arrays)
    }

    override fun convertToEntityProperty(databaseValue: String?): List<ChapterList> {
        val typeToken: TypeToken<List<ChapterList>> =
            object : TypeToken<List<ChapterList>>() {}
        return Gson().fromJson(databaseValue, typeToken.type)
    }

}