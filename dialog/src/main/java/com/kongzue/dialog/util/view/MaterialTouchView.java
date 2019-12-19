package com.kongzue.dialog.util.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/5/11 18:10
 */
public class MaterialTouchView extends LinearLayout {
    public MaterialTouchView(Context context) {
        super(context);
    }
    
    public MaterialTouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    public MaterialTouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
    private boolean isMove = false;
    private int touchY, touchX;
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (onTouchListener != null) onTouchListener.onTouch(this, event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMove = false;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (!isMove) {
                    return false;
                }
                isMove = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isMove) {
                    touchY = (int) event.getRawY();
                    touchX = (int) event.getRawX();
                }
                isMove = true;
                
                float moveY = event.getRawY();
                float moveX = event.getRawX();
                
                if (Math.abs(moveY - touchY) > dip2px(20) || Math.abs(moveX - touchX) > dip2px(20)) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    return true;
                }
                break;
        }
        return isMove;
    }
    
    private OnTouchListener onTouchListener;
    
    @Override
    public void setOnTouchListener(OnTouchListener l) {
        onTouchListener = l;
        super.setOnTouchListener(l);
    }
}
