package com.rm.module_login.widget.sidebar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.rm.module_login.R;


/**
 * Created by MQ on 2017/5/17.
 */

public class LetterBar extends View {

    private int mRealHeight;
    private int mMeasureHeight;
    private int mMeasureWidth;
    private int mLetterBarMarginTop;
    private final float DEFAULT_LETTER_SIZE = 35.0f;
    private float mLetterSize;
    private float mLetterMarginTop;
    private int mLetterColor;
    private int mLetterPressColor;
    private float mLetterHeight = 20.0f;
    private Paint mPaint;
    private IIndexChangeListener listener;

    private String[] DEFAULT_LETTER_ARRAY = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};

    private String[] mLetterArray = DEFAULT_LETTER_ARRAY;

    public LetterBar(Context context) {
        this(context, null);
    }

    public LetterBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取右边菜单id
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LetterBar);
        mLetterSize = typedArray.getDimension(R.styleable.LetterBar_text_size, DEFAULT_LETTER_SIZE);
        mLetterMarginTop = typedArray.getDimension(R.styleable.LetterBar_text_margin_top, 8);
        mLetterColor = typedArray.getColor(R.styleable.LetterBar_text_color, ContextCompat.getColor(context, R.color.business_text_color_999999));
        mLetterPressColor = typedArray.getColor(R.styleable.LetterBar_text_press_color, ContextCompat.getColor(context, R.color.business_color_0f0f0f));
        typedArray.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mLetterColor);
        mPaint.setTextSize(mLetterSize);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Rect rect = new Rect();
        mPaint.getTextBounds("A", 0, "A".length(), rect);
        mLetterHeight = rect.height();
        //计算真正需要绘制的高度
        mRealHeight = (int) ((mLetterHeight + mLetterMarginTop) * mLetterArray.length);
        mMeasureWidth = getMeasuredWidth();
        mMeasureHeight = getMeasuredHeight();
        mLetterBarMarginTop = (mMeasureHeight - mRealHeight) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < mLetterArray.length; i++) {
            String textTag = mLetterArray[i];
            float xPos = (mMeasureWidth - mPaint.measureText(textTag)) / 2;
            canvas.drawText(textTag, xPos, mLetterBarMarginTop + (mLetterHeight + mLetterMarginTop) * (i + 1), mPaint);
        }
    }


    public void setLetterArray(String[] letterArray) {
        this.mLetterArray = letterArray;
        invalidate();
    }

    public void setCharArray(char[] chars) {
        if (chars == null || chars.length == 0) {
            return;
        }
        String[] letterArray = new String[chars.length];
        for (int i = 0; i < letterArray.length; i++) {
            letterArray[i] = String.valueOf(chars[i]);
        }
        this.mLetterArray = letterArray;
        requestLayout();
        invalidate();
    }

    public void setString(String letterStr) {
        if (TextUtils.isEmpty(letterStr)) {
            return;
        }
        setCharArray(letterStr.toCharArray());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下
                mPaint.setColor(mLetterPressColor);
                invalidate();
            case MotionEvent.ACTION_MOVE:
                float moveY = (event.getY() - getTop() - mLetterBarMarginTop);
                float range = moveY / (mLetterHeight + mLetterMarginTop);
                if (range < 0.5f) {
                    return true;
                }
                int position = (int) Math.floor(range);
                if (position >= 0 && position < mLetterArray.length) {
                    if (listener != null) {
                        listener.indexChanged(position, mLetterArray[position], event);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //抬起
                ((IndexBar) getParent()).setTagStatus(false);
                mPaint.setColor(mLetterColor);
                invalidate();
                break;
        }
        return true;
    }


    public interface IIndexChangeListener {
        void indexChanged(int position, String tag, MotionEvent event);
    }

    public void setIndexChangeListener(IIndexChangeListener listener) {
        this.listener = listener;
    }

}
