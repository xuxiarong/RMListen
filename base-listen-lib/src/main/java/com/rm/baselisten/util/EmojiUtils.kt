package com.rm.baselisten.util

/**
 *
 * @author yuanfang
 * @date 11/9/20
 * @description
 *
 */
class EmojiUtils {

    /**
     * 判断是否是特殊字符（表情）
     */
    companion object {
        fun containsEmoji(str: String): Boolean {
            str.forEach {
                return !isEmojiCharacter(it)
            }
            return false
        }

        private fun isEmojiCharacter(codePoint: Char): Boolean {
            return codePoint.toInt() == 0x0 || codePoint.toInt() == 0x9 || codePoint.toInt() == 0xA ||
                    codePoint.toInt() == 0xD || codePoint.toInt() in 0x20..0xD7FF ||
                    codePoint.toInt() in 0xE000..0xFFFD || (codePoint.toInt() in 0x10000..0x10FFFF)
        }
    }
}