package com.kongzue.dialog.util.view;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kongzue.dialog.R;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.interfaces.OnNotificationClickListener;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/4/10 23:02
 */
public class NotifyToastShadowView extends RelativeLayout {
    
    private OnDismissListener onDismissListener;
    private OnNotificationClickListener onNotificationClickListener;
    private boolean dispatchTouchEvent = true;
    
    public NotifyToastShadowView(Context context) {
        super(context);
    }
    
    public NotifyToastShadowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    public NotifyToastShadowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    private boolean isTouched = false;
    private boolean isTouchDown = false;
    private float touchDownY = 0;
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (dispatchTouchEvent) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isTouched = true;
                    isTouchDown = true;
                    touchDownY = ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isTouchDown) {
                        float delta = ev.getY() - touchDownY;
                        setY(delta > 0 ? 0 : delta);
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    isTouchDown = false;
                    if (getY() > -dip2px(5)) {
                        LinearLayout BoxNotic = findViewById(R.id.btn_notic);
                        if (ev.getY() > 0 && ev.getY() < BoxNotic.getHeight()) {
                            if (onNotificationClickListener != null)
                                onNotificationClickListener.onClick();
                        }
                    }
                    if (getY() > -dip2px(30)) {
                        animate().y(0).setDuration(100).setListener(null);
                    } else {
                        animate().y(-getHeight()).alpha(0).setDuration(200).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            
                            }
                            
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                setVisibility(GONE);
                                if (onDismissListener != null) onDismissListener.onDismiss();
                            }
                            
                            @Override
                            public void onAnimationCancel(Animator animation) {
                            
                            }
                            
                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            
                            }
                        });
                    }
                    break;
            }
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }
    
    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
    public boolean isTouched() {
        return isTouched;
    }
    
    public OnDismissListener getOnDismissListener() {
        return onDismissListener;
    }
    
    public NotifyToastShadowView setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }
    
    public void setOnNotificationClickListener(OnNotificationClickListener onNotificationClickListener) {
        setFocusable(true);
        setEnabled(true);
        setClickable(true);
        this.onNotificationClickListener = onNotificationClickListener;
    }
    
    public void setDispatchTouchEvent(boolean dispatchTouchEvent) {
        this.dispatchTouchEvent = dispatchTouchEvent;
    }
}
