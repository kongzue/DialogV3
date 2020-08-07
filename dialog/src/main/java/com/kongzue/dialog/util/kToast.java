package com.kongzue.dialog.util;

import android.app.Application;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kongzue.dialog.R;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.util.view.NotifyToastShadowView;
import com.kongzue.dialog.v3.Notification;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/7/25 14:56
 */
public class kToast {
    
    private NotifyToastShadowView rootView;
    
    private Toast toast;
    private Notification.DURATION_TIME durationTime = Notification.DURATION_TIME.LONG;
    private OnDismissListener onDismissListener;
    
    public kToast(Notification.DURATION_TIME durationTime, OnDismissListener onDismissListener) {
        this.durationTime = durationTime;
        this.onDismissListener = onDismissListener;
    }
    
    public kToast show(final Context context, final NotifyToastShadowView view) {
        cancel();
        rootView = view;
        switch (Notification.mode) {
            case TOAST:
                showToast(context);
                break;
            case FLOATING_WINDOW:
                showFloatingWindow(context);
                break;
        }
        view.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                cancel();
            }
        });
        return this;
    }
    
    private WindowManager windowManager;
    private WindowManager.LayoutParams windowParams;
    
    private void showFloatingWindow(Context context) {
        windowManager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
        
        windowParams = new WindowManager.LayoutParams();
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        windowParams.format = PixelFormat.TRANSLUCENT;
        windowParams.gravity = Gravity.TOP;
        windowParams.windowAnimations = android.R.style.Animation_Toast;
        windowParams.packageName = context.getPackageName();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            windowParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
    
        windowParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        windowParams.flags &= ~WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        
        if (context instanceof Application) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                windowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                windowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
        }
        
        try {
            windowManager.addView(rootView, windowParams);
            
            rootView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!rootView.isTouched())cancel();
                }
            }, durationTime == Notification.DURATION_TIME.LONG ? 3000 : 1500);
        } catch (Exception e) {
            error("启动通知错误，在使用 context 为 Application 时必须声明：<uses-permission android:name=\"android.permission.SYSTEM_ALERT_WINDOW\" />，并开启悬浮窗权限！");
        }
    }
    
    private void showToast(Context context) {
        toast = new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
        toast.setDuration(durationTime.ordinal());
        toast.setView(rootView);
        toast.getView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        
        rootView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                rootView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!rootView.isTouched())cancel();
                    }
                }, durationTime == Notification.DURATION_TIME.LONG ? 3000 : 1500);
            }
            
            @Override
            public void onViewDetachedFromWindow(View v) {
            
            }
        });
        
        hookHandler(toast);
        try {
            Object mTN;
            mTN = getField(toast, "mTN");
            if (mTN != null) {
                Field tnParamsField = mTN.getClass().getDeclaredField("mParams");
                if (tnParamsField != null) {
                    tnParamsField.setAccessible(true);
                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) tnParamsField.get(mTN);
                    
                    //params.windowAnimations = R.style.toastAnim;
                    params.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
                    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                    }
                    
                    Field tnNextViewField = mTN.getClass().getDeclaredField("mNextView");
                    tnNextViewField.setAccessible(true);
                    tnNextViewField.set(mTN, toast.getView());
                }
                
                try {
                    //目前是没办法了，新版本Android Toast 的TN要show必须有IBinder，IBinder必须取得TN中mWM实例化对象WindowManagerImpl，这几乎没辙了
                    Object mWM = getField(mTN, "mWM");
                    Field tnField = mWM.getClass().getDeclaredField("mDefaultToken");
                    tnField.setAccessible(true);
                    IBinder token = (IBinder) tnField.get(mWM);
                    
                    if (Build.VERSION.SDK_INT >= 25) {
                        show = mTN.getClass().getDeclaredMethod("show", IBinder.class);
                    } else {
                        show = mTN.getClass().getMethod("show");
                    }
                    
                    show.invoke(mTN, token);
                } catch (Exception e) {
                    //e.printStackTrace();
                    toast.show();
                }
            }
            
            //if (durationTime > DURATION_TIME.ALWAYS) {
            //    handler.postDelayed(hideRunnable, mDuration * 1000);
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private Object getField(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        if (field != null) {
            field.setAccessible(true);
            return field.get(object);
        }
        return null;
    }
    
    private Method show;
    
    //捕获8.0之前Toast的BadTokenException，Google在Android 8.0的代码提交中修复了这个问题(By @Dovar66[https://github.com/Dovar66/DToast])
    private static void hookHandler(Toast toast) {
        if (toast == null || Build.VERSION.SDK_INT >= 26) return;
        try {
            Field sField_TN = Toast.class.getDeclaredField("mTN");
            sField_TN.setAccessible(true);
            Field sField_TN_Handler = sField_TN.getType().getDeclaredField("mHandler");
            sField_TN_Handler.setAccessible(true);
            
            Object tn = sField_TN.get(toast);
            Handler preHandler = (Handler) sField_TN_Handler.get(tn);
            sField_TN_Handler.set(tn, new SafelyHandlerWrapper(preHandler));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void cancel() {
        try {
            if (onDismissListener != null) onDismissListener.onDismiss();
            if (toast != null) {
                toast.cancel();
            }
            if (windowManager != null) {
                windowManager.removeView(rootView);
            }
        } catch (Exception e) {
        }
    }
    
    public void log(Object o) {
        if (DialogSettings.DEBUGMODE) Log.i(">>>", o.toString());
    }
    
    public void error(Object o) {
        if (DialogSettings.DEBUGMODE) Log.e(">>>", o.toString());
    }
}