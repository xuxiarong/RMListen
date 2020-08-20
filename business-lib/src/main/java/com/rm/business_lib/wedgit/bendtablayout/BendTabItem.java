package com.rm.business_lib.wedgit.bendtablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.rm.business_lib.R;


public class BendTabItem extends View {
    public final CharSequence mText;
    public final Drawable mIcon;
    public final int mCustomLayout;

    public BendTabItem(Context context) {
        this(context, null);
    }

    public BendTabItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.BendTabItem, 0, 0);
        mText = a.getText(R.styleable.BendTabItem_android_text);
        mIcon = a.getDrawable(R.styleable.BendTabItem_android_icon);
        mCustomLayout = a.getResourceId(R.styleable.BendTabItem_android_layout, 0);
        a.recycle();
    }
}
