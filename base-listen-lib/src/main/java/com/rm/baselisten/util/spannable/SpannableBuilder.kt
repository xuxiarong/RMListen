package com.rm.baselisten.util.spannable

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.widget.TextView

/**
 * desc   :
 * date   : 2020/08/26
 * version: 1.0
 */
class SpannableBuilder {
    private lateinit var context: Context
    private var tv: TextView? = null
    private var content: String? = ""
    private var spannableString: SpannableString? = null

    private val changeItemList = mutableListOf<ChangeItem>()

    fun setTextView(tv: TextView): SpannableBuilder {
        this.tv = tv
        context = tv.context
        return this
    }

    fun setContent(content: String): SpannableBuilder {
        this.content = content
        return this
    }

    /**
     * 单个添加要被操作条目
     * */
    fun addChangeItem(item: ChangeItem): SpannableBuilder {
        changeItemList.add(item)
        return this
    }

    /**
     * 批量添加要被操作条目
     * */
    fun addChangeItems(items: List<ChangeItem>): SpannableBuilder {
        changeItemList.addAll(items)
        return this
    }

    fun build(): SpannableBuilder {
        tv?.highlightColor = Color.parseColor("#00ffffff")
        tv?.movementMethod = LinkMovementMethod.getInstance()
        spannableString = SpannableHelper.changeMultiPart(context, content, changeItemList)
        tv?.text = spannableString
        return this
    }

    fun removeSpan() {
        spannableString?.let {
            val spans = it.getSpans(0, it.length, ClickableSpan::class.java)
            spans.forEach { span ->
                it.removeSpan(span)
            }
        }
    }
}