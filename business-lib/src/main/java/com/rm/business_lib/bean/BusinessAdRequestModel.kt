package com.rm.business_lib.bean

/**
 * desc   :
 * date   : 2020/12/07
 * version: 1.0
 */
data class BusinessAdRequestModel (var ad_key: Array<String>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BusinessAdRequestModel

        if (!ad_key.contentEquals(other.ad_key)) return false

        return true
    }

    override fun hashCode(): Int {
        return ad_key.contentHashCode()
    }
}
