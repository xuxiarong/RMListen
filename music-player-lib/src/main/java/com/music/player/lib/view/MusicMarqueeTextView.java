package com.music.player.lib.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * TinyHung@Outlook.com
 * 2017/11/27.
 */

public class MusicMarqueeTextView extends AppCompatTextView {

    public MusicMarqueeTextView(Context context) {
        super(context);
    }

    public MusicMarqueeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MusicMarqueeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
