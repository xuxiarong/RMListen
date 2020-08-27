package com.rm.baselisten.util.spannable

/**
 * desc   :
 * date   : 2020/08/26
 * version: 1.0
 */
class ChangeItem(
    val partStr: String,
    val type: Type,
    val value: Int,
    val clickListener: TextClickListener?
) {

    enum class Type {
        SIZE,//字体大小
        COLOR,//字体颜色
        ICON,//文字变图片
        NONE//不做任何处理，只想单独加点击时可用
    }


}