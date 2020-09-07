package com.rm.business_lib.wedgit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
import com.rm.business_lib.R;
import java.util.ArrayList;
import java.util.List;

public class RadioTabLayout extends RadioGroup implements View.OnClickListener {

    private int selectDrawable;//选中背景
    private int defaultDrawable;//默认背景

    private float selectTextSize;//选中字体大小
    private float defaultTextSize;//默认字体大小

    private int selectTextColor;//选中字体颜色
    private int defaultTextColor;//默认字体颜色

    private int radioHeight;//按钮高度

    private int mSelectTypeface;//选中字体样式 0 ：默认 ； 1 ： 加粗
    private int mDefaultTypeface;//默认字体样式


    private ViewPager2 mViewPager2;
    private ViewPager mViewPager;

    private List<RadioButton> buttonList = new ArrayList<>();
    private RadioButton mCurButton;//当前选中的radioButton

    public RadioTabLayout(Context context) {
        this(context, null);
    }

    public RadioTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 初始化信息
     */
    private void init(Context context, AttributeSet attrs) {
        setGravity(Gravity.CENTER_VERTICAL);
        setBackgroundResource(R.drawable.business_shape_radio_group_bg);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RadioTabLayout);
        selectDrawable = ta.getResourceId(R.styleable.RadioTabLayout_radioSelectBackground, R.drawable.business_shape_radio_btn_select_bg);
        defaultDrawable = ta.getResourceId(R.styleable.RadioTabLayout_radioDefaultBackground, R.drawable.business_shape_radio_btn_default_bg);

        selectTextSize = ta.getDimension(R.styleable.RadioTabLayout_radioSelectTexSize, 14f);
        defaultTextSize = ta.getDimension(R.styleable.RadioTabLayout_radioDefaultTexSize, 14f);

        selectTextColor = ta.getColor(R.styleable.RadioTabLayout_radioSelectTextColor, ContextCompat.getColor(getContext(), R.color.business_text_color_333333));
        defaultTextColor = ta.getColor(R.styleable.RadioTabLayout_radioDefaultTextColor, ContextCompat.getColor(getContext(), R.color.business_color_999999));

        mSelectTypeface = ta.getInt(R.styleable.RadioTabLayout_radioDefaultTypeface, 1);
        mDefaultTypeface = ta.getInt(R.styleable.RadioTabLayout_radioSelectTypeface, 0);
        radioHeight = ta.getDimensionPixelOffset(R.styleable.RadioTabLayout_radioHeight, getResources().getDimensionPixelOffset(R.dimen.dp_34));
        ta.recycle();

    }

    /**
     * 注册ViewPager2 滑动监听
     */
    private void registerPageChanger() {
        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }


    private void addPagerChangeListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 添加tab
     *
     * @param text tab文本
     * @return CustomTabLayout
     */
    public RadioTabLayout addTab(CharSequence text) {
        RadioButton radioButton = createButton(text);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, radioHeight);
        addView(radioButton, layoutParams);
        return this;
    }

    /**
     * 将ViewPager2与之关联
     *
     * @param viewPager2 ViewPager2对象
     */
    public void bindViewPager2(ViewPager2 viewPager2) {
        this.mViewPager2 = viewPager2;
        registerPageChanger();
    }

    public void bindViewPager(ViewPager viewPager) {
        this.mViewPager = viewPager;
        addPagerChangeListener();
    }

    /**
     * 设置当前选中tab
     *
     * @param position tab下标
     */
    public void setSelect(int position) {
        RadioButton button = buttonList.get(position);
        if (mCurButton == button) {
            return;
        }
        if (mCurButton != null) {
            setState(false, mCurButton);
        }
        setState(true, button);
    }

    /**
     * 修改选中后状态
     *
     * @param isSelect    是否选择
     * @param radioButton 选中的对象
     */
    private void setState(boolean isSelect, RadioButton radioButton) {
        if (isSelect) {
            radioButton.setBackgroundResource(selectDrawable);
            radioButton.setTextColor(selectTextColor);
            radioButton.setTextSize(selectTextSize);
            radioButton.setTypeface(mSelectTypeface == 0 ? Typeface.defaultFromStyle(Typeface.NORMAL) : Typeface.defaultFromStyle(Typeface.BOLD));
            mCurButton = radioButton;
        } else {
            radioButton.setTextColor(defaultTextColor);
            radioButton.setBackgroundResource(defaultDrawable);
            radioButton.setTextSize(defaultTextSize);
            radioButton.setTypeface(mDefaultTypeface == 0 ? Typeface.defaultFromStyle(Typeface.NORMAL) : Typeface.defaultFromStyle(Typeface.BOLD));
        }
    }

    /**
     * 动态创建添加tab button对象
     */
    private RadioButton createButton(CharSequence text) {
        RadioButton radioButton = new RadioButton(getContext());
        radioButton.setText(text);
        setState(false, radioButton);
        radioButton.setButtonDrawable(null);
        radioButton.setOnClickListener(this);
        buttonList.add(radioButton);
        return radioButton;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof RadioButton) {
            int index = buttonList.indexOf(v);
            if (index != -1) {
                setSelect(index);
                if (mViewPager2 != null) {
                    mViewPager2.setCurrentItem(index);
                }

                if (mViewPager != null) {
                    mViewPager.setCurrentItem(index);
                }
            }
        }
    }
}
