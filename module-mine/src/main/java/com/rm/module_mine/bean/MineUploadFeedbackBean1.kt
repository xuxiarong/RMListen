package com.rm.module_mine.bean

/**
 *
 * @author yuanfang
 * @date 12/5/20
 * @description
 *
 */
data class MineUploadFeedbackBean1(
    val content: String = "",
    val img_list: Array<String>?,
    val contact: String? = "",
    val device_id: String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MineUploadFeedbackBean1

        if (content != other.content) return false
        if (img_list != null) {
            if (other.img_list == null) return false
            if (!img_list.contentEquals(other.img_list)) return false
        } else if (other.img_list != null) return false
        if (contact != other.contact) return false
        if (device_id != other.device_id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = content.hashCode()
        result = 31 * result + (img_list?.contentHashCode() ?: 0)
        result = 31 * result + (contact?.hashCode() ?: 0)
        result = 31 * result + device_id.hashCode()
        return result
    }
}