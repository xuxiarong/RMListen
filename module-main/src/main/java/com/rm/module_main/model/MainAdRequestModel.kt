package com.rm.module_main.model

/**
 * desc   :
 * date   : 2020/12/07
 * version: 1.0
 */
data class MainAdRequestModel (var ad_key: Array<String>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MainAdRequestModel

        if (!ad_key.contentEquals(other.ad_key)) return false

        return true
    }

    override fun hashCode(): Int {
        return ad_key.contentHashCode()
    }
}
