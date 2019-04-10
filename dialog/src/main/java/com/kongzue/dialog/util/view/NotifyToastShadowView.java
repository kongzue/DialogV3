package com.kongzue.dialog.util.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kongzue.dialog.interfaces.OnNotificationClickListener;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/4/10 23:02
 */
public class NotifyToastShadowView extends RelativeLayout {
    
    private Activity activity;
    private int notifyHeight;
    private OnNotificationClickListener onNotificationClickListener;
    
    public NotifyToastShadowView(Context context) {
        super(context);
    }
    
    public NotifyToastShadowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    public NotifyToastShadowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction()==MotionEvent.ACTION_CANCEL ||ev.getAction()==MotionEvent.ACTION_DOWN || ev.getAction()==MotionEvent.ACTION_UP) {
            Log.i(">>>", "y: " + ev.getY() + "   "+notifyHeight);
            if (ev.getY() < notifyHeight) {
                if (onNotificationClickListener!=null)onNotificationClickListener.onClick();
                onNotificationClickListener = null;
                return super.dispatchTouchEvent(ev);
            } else {
                if (activity != null) activity.dispatchTouchEvent(ev);
                return false;
            }
        }else{
            return super.dispatchTouchEvent(ev);
        }
    }
    
    public void setActivity(Activity activity) {
        this.activity = activity;
    }
    
    public void setNotifyHeight(int notifyHeight) {
        this.notifyHeight = notifyHeight;
    }
    
    public void setOnNotificationClickListener(OnNotificationClickListener onNotificationClickListener) {
        this.onNotificationClickListener = onNotificationClickListener;
    }
}
