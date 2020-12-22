package com.rm.module_mine.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.math.MathUtils;

import com.rm.module_mine.R;


/**
 * @function: TopBar部分的Behavior
 */
public class TopBarBehavior extends CoordinatorLayout.Behavior<View> {
    //滑动内容初始化TransY
    private float contentTransY;
    //topBar内容高度
    private int topBarHeight;

    @SuppressWarnings("unused")
    public TopBarBehavior(Context context) {
        this(context,null);
    }

    @SuppressWarnings("WeakerAccess")
    public TopBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        //引入尺寸值
        contentTransY= (int) context.getResources().getDimension(R.dimen.dp_160);
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        topBarHeight= (int) context.getResources().getDimension(R.dimen.dp_44)+statusBarHeight;
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        //依赖Content
        return dependency.getId() == R.id.mine_member_detail_content_layout;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        //计算Content上滑的百分比，设置子view的透明度
        float upPro = (contentTransY- MathUtils.clamp(dependency.getTranslationY(), topBarHeight, contentTransY)) / (contentTransY - topBarHeight);
        View tvName=child.findViewById(R.id.mine_member_detail_title);
        View tvColl=child.findViewById(R.id.mine_member_detail_title_follow);
        tvName.setAlpha(upPro);
        tvColl.setAlpha(upPro);
        return true;
    }
}
