package com.app.woney.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by houan on 2016/12/14.
 */

public class WoneyViewPager extends ViewPager {

    private boolean isPagingEnabled = true;

    public WoneyViewPager(Context context) {
        super(context);
    }

    public WoneyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isPagingEnabled && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isPagingEnabled && super.onInterceptTouchEvent(ev);
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
}
