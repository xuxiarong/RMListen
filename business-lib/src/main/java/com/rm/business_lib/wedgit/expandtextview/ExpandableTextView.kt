package com.rm.business_lib.wedgit.expandtextview

import android.animation.ObjectAnimator
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.rm.baselisten.util.DLog
import com.rm.baselisten.utilExt.dip
import com.rm.baselisten.utilExt.sp
import com.rm.business_lib.R


class ExpandableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), View.OnClickListener {

    private var mMaxLine = 3//最大行数
    private var mText: String? = null//当前的文本信息
    private var isExpand = true//是否折叠
    private lateinit var mTextView: TextView
    private lateinit var mImageView: ImageView
    private var mTextColor = ContextCompat.getColor(context, R.color.business_text_color_333333)
    private var mTextSize = 0
    private var mExpandIcon = R.drawable.business_icon_unfold
    private var textHeight: Int = 0

    init {
        val ta =
            context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView)
        mTextColor = ta.getColor(R.styleable.ExpandableTextView_expand_text_color, mTextColor)
        mTextSize =
            ta.getDimensionPixelSize(R.styleable.ExpandableTextView_expand_text_size, sp(16f))
        mExpandIcon = ta.getResourceId(R.styleable.ExpandableTextView_expand_iv_icon, mExpandIcon)
        mMaxLine = ta.getInteger(R.styleable.ExpandableTextView_expand_text_max_line, mMaxLine)
        ta.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun initView() {
        mTextView = TextView(context)
        mTextView.setLineSpacing(dip(4).toFloat(), 1f)
        mTextView.setTextColor(mTextColor)
        mTextView.textSize = 16f
        addView(mTextView)
        textHeight = textHeight()


        mImageView = ImageView(context)
        mImageView.visibility = View.GONE
        mImageView.setImageResource(mExpandIcon)
        val layoutParams = LayoutParams(textHeight * 2, textHeight)
        layoutParams.bottomToBottom = ConstraintSet.PARENT_ID
        layoutParams.endToEnd = ConstraintSet.PARENT_ID
        mImageView.setOnClickListener(this)
        addView(mImageView, layoutParams)
    }

    //设置文本信息
    fun setText(text: String?) {
        mText = text
        changeState()
    }


    //点击展开/收起  对文本进行处理
    private fun changeState() {
        if (TextUtils.isEmpty(mText)) {
            visibility = View.GONE
            return
        }
        mTextView.text = mText
        visibility = View.VISIBLE

        mTextView.post {
            if (mMaxLine < mTextView.lineCount && isExpand) {
                mImageView.visibility = View.VISIBLE
                val buffer = StringBuffer()
                mTextView.layout?.let {
                    var start = 0
                    var end: Int
                    var lastWidth = 0f//最后一行的宽度
                    var maxWidth = 0f//最大宽度
                    var sub: String

                    for (i in 0 until mMaxLine) {
                        //获取当前这一行的字数
                        end = it.getLineEnd(i)
                        lastWidth = it.getLineWidth(i)
                        if (i == 0) {
                            maxWidth = lastWidth
                        }
                        //获取当前这一行的文本
                        sub = mText!!.substring(start, end)
                        start = end
                        buffer.append(sub)
                    }


                    //判断最后一行是否能够显示完全，如果显示不全则添加空格让其换行
                    if ((textHeight * 2 + lastWidth) > maxWidth) {
                        buffer.delete(buffer.length - 4, buffer.length)
                        buffer.append("...")
                    }
                    mTextView.text = buffer
                }
            }
        }
    }

    //测量当前单行文本的高度
    private fun textHeight(): Int {
        val fontMetrics = mTextView.paint.fontMetrics
        return (fontMetrics.bottom - fontMetrics.top).toInt()
    }

    override fun onClick(v: View?) {
        isExpand = !isExpand
        changeState()
        startAnim()
    }

    //开始动画，对ImageView进行旋转操作
    private fun startAnim() {
        val value = if (!isExpand) {
            floatArrayOf(0f, 180f)
        } else {
            floatArrayOf(180f, 0f)
        }
        val anim = ObjectAnimator.ofFloat(mImageView, "rotation", value[0], value[1])
        anim.duration = 200
        anim.start()
    }

}

@BindingAdapter("expandText")
fun ExpandableTextView.setExpandText(mText:String?){
    setText(mText)
}