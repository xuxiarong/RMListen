package com.rm.business_lib.bean

import android.text.TextUtils
import com.rm.business_lib.pinyin.CN
import java.util.regex.Pattern

/**
 * desc   : 国家信息bean
 * date   : 2020/09/07
 * version: 1.0
 */
data class Country(val cn: String, val en: String, val phone_code: String) : Comparable<Country>,
    CN {
    override fun compareTo(o: Country): Int {
        return if (getSection() == "#" && o.getSection() != "#") {
            1
        } else if (getSection() != "#" && o.getSection() == "#") {
            -1
        } else {
            getSection().compareTo(o.getSection(), ignoreCase = true)
        }
    }

    override fun chinese(): String {
        return cn
    }

    override fun phoneCode(): String {
        return phone_code
    }

    private fun getSection(): String {
        val s = en
        return if (TextUtils.isEmpty(s)) {
            "#"
        } else {
            val c = s.substring(0, 1)
            val p = Pattern.compile("[a-zA-Z]")
            val m = p.matcher(c)
            if (m.matches()) {
                c.toUpperCase()
            } else {
                "#"
            }
        }
    }
}