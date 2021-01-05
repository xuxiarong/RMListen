package com.rm.module_search.widget.autohint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
/**
 * @author yuanfang
 * @date 1/4/21
 * @description
 */
public interface IAutoHintDrawer {
    void draw(Rect drawBounds, float lastDrawX, float drawX, float drawY, float fraction, String lastHint, String currHint, Canvas canvas, Paint paint);
}
