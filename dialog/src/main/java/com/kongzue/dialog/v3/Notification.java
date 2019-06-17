package com.kongzue.dialog.v3;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kongzue.dialog.R;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.interfaces.OnNotificationClickListener;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.view.NotifyToastShadowView;
import com.kongzue.dialog.util.SafelyHandlerWrapper;
import com.kongzue.dialog.util.TextInfo;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/4/10 18:32
 */
public class Notification {
    
    public enum DURATION_TIME {
        SHORT, LONG
    }
    
    private OnNotificationClickListener onNotificationClickListener;
    private OnDismissListener onDismissListener;
    
    private DialogSettings.STYLE style;
    private DURATION_TIME durationTime = DURATION_TIME.LONG;
    private int backgroundColor;
    
    private Toast toast;
    private WeakReference<Context> context;
    private String title;
    private String message;
    private int iconResId;
    
    private View customView;
    private NotifyToastShadowView rootView;
    
    private RelativeLayout boxBody;
    private LinearLayout btnNotic;
    private LinearLayout boxTitle;
    private ImageView imgIcon;
    private TextView txtTitle;
    private TextView txtMessage;
    private RelativeLayout boxCustom;
    
    private TextInfo titleTextInfo;
    private TextInfo messageTextInfo;
    
    private Notification() {
    }
    
    public static Notification build(Context context, String message) {
        synchronized (Notification.class) {
            Notification notification = new Notification();
            notification.context = new WeakReference<>(context);
            notification.message = message;
            return notification;
        }
    }
    
    public static Notification build(Context context, int messageResId) {
        synchronized (Notification.class) {
            Notification notification = new Notification();
            notification.context = new WeakReference<>(context);
            notification.message = context.getString(messageResId);
            return notification;
        }
    }
    
    public static Notification show(Context context, String message) {
        Notification notification = build(context, message);
        notification.showNotification();
        return notification;
    }
    
    public static Notification show(Context context, int messageResId) {
        return show(context, context.getString(messageResId));
    }
    
    public static Notification show(Context context, String message, DURATION_TIME durationTime) {
        Notification notification = build(context, message);
        notification.durationTime = durationTime;
        notification.showNotification();
        return notification;
    }
    
    public static Notification show(Context context, int messageResId, DURATION_TIME durationTime) {
        return show(context, context.getString(messageResId), durationTime);
    }
    
    public static Notification show(Context context, String message, DialogSettings.STYLE style) {
        Notification notification = build(context, message);
        notification.style = style;
        notification.showNotification();
        return notification;
    }
    
    public static Notification show(Context context, int messageResId, DialogSettings.STYLE style) {
        return show(context, context.getString(messageResId), style);
    }
    
    public static Notification show(Context context, String message, DialogSettings.STYLE style, DURATION_TIME durationTime) {
        Notification notification = build(context, message);
        notification.durationTime = durationTime;
        notification.style = style;
        notification.showNotification();
        return notification;
    }
    
    public static Notification show(Context context, int messageResId, DialogSettings.STYLE style, DURATION_TIME durationTime) {
        return show(context, context.getString(messageResId), style, durationTime);
    }
    
    public static Notification show(Context context, String title, String message) {
        Notification notification = build(context, message);
        notification.title = title;
        notification.showNotification();
        return notification;
    }
    
    public static Notification show(Context context, int titleResId, int messageResId) {
        return show(context, context.getString(titleResId), context.getString(messageResId));
    }
    
    public static Notification show(Context context, String title, String message, DURATION_TIME durationTime) {
        Notification notification = build(context, message);
        notification.title = title;
        notification.durationTime = durationTime;
        notification.showNotification();
        return notification;
    }
    
    public static Notification show(Context context, int titleResId, int messageResId, DURATION_TIME durationTime) {
        return show(context, context.getString(titleResId), context.getString(messageResId), durationTime);
    }
    
    public static Notification show(Context context, String title, String message, DialogSettings.STYLE style) {
        Notification notification = build(context, message);
        notification.title = title;
        notification.style = style;
        notification.showNotification();
        return notification;
    }
    
    public static Notification show(Context context, int titleResId, int messageResId, DialogSettings.STYLE style) {
        return show(context, context.getString(titleResId), context.getString(messageResId), style);
    }
    
    public static Notification show(Context context, String title, String message, DialogSettings.STYLE style, DURATION_TIME durationTime) {
        Notification notification = build(context, message);
        notification.title = title;
        notification.durationTime = durationTime;
        notification.style = style;
        notification.showNotification();
        return notification;
    }
    
    public static Notification show(Context context, int titleResId, int messageResId, DialogSettings.STYLE style, DURATION_TIME durationTime) {
        return show(context, context.getString(titleResId), context.getString(messageResId), style, durationTime);
    }
    
    public static Notification show(Context context, String title, String message, int iconResId) {
        Notification notification = build(context, message);
        notification.title = title;
        notification.iconResId = iconResId;
        notification.showNotification();
        return notification;
    }
    
    public static Notification show(Context context, int titleResId, int messageResId, int iconResId) {
        return show(context, context.getString(titleResId), context.getString(messageResId), iconResId);
    }
    
    public static Notification show(Context context, String title, String message, int iconResId, DURATION_TIME durationTime) {
        Notification notification = build(context, message);
        notification.title = title;
        notification.iconResId = iconResId;
        notification.durationTime = durationTime;
        notification.showNotification();
        return notification;
    }
    
    public static Notification show(Context context, int titleResId, int messageResId, int iconResId, DURATION_TIME durationTime) {
        return show(context, context.getString(titleResId), context.getString(messageResId), iconResId, durationTime);
    }
    
    public static Notification show(Context context, String title, String message, int iconResId, DialogSettings.STYLE style) {
        Notification notification = build(context, message);
        notification.title = title;
        notification.iconResId = iconResId;
        notification.style = style;
        notification.showNotification();
        return notification;
    }
    
    public static Notification show(Context context, int titleResId, int messageResId, int iconResId, DialogSettings.STYLE style) {
        return show(context, context.getString(titleResId), context.getString(messageResId), iconResId, style);
    }
    
    public static Notification show(Context context, String title, String message, int iconResId, DialogSettings.STYLE style, DURATION_TIME durationTime) {
        Notification notification = build(context, message);
        notification.title = title;
        notification.iconResId = iconResId;
        notification.durationTime = durationTime;
        notification.style = style;
        notification.showNotification();
        return notification;
    }
    
    public static Notification show(Context context, int titleResId, int messageResId, int iconResId, DialogSettings.STYLE style, DURATION_TIME durationTime) {
        return show(context, context.getString(titleResId), context.getString(messageResId), iconResId, style, durationTime);
    }
    
    private boolean isShow;
    
    public void showNotification() {
        isShow = true;
        if (style == null) style = DialogSettings.style;
        switch (style) {
            case STYLE_IOS:
                showIOSNotification();
                break;
            case STYLE_MATERIAL:
                showMaterialNotification();
                break;
            default:
                showKongzueNotification();
                break;
        }
    }
    
    private void showMaterialNotification() {
        LayoutInflater inflater = (LayoutInflater) context.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = (NotifyToastShadowView) inflater.inflate(R.layout.notification_material, null);
        
        boxBody = rootView.findViewById(R.id.box_body);
        btnNotic = rootView.findViewById(R.id.btn_notic);
        boxTitle = rootView.findViewById(R.id.box_title);
        imgIcon = rootView.findViewById(R.id.img_icon);
        txtTitle = rootView.findViewById(R.id.txt_title);
        txtMessage = rootView.findViewById(R.id.txt_message);
        boxCustom = rootView.findViewById(R.id.box_custom);
        
        rootView.setParent(context.get());
        rootView.setOnNotificationClickListener(new OnNotificationClickListener() {
            @Override
            public void onClick() {
                if (customView == null) {
                    toast.cancel();
                    if (onNotificationClickListener != null) onNotificationClickListener.onClick();
                }
            }
        });
        
        boxBody.post(new Runnable() {
            @Override
            public void run() {
                boxBody.setY(-boxBody.getHeight());
                boxBody.animate().setInterpolator(new DecelerateInterpolator()).translationY(0).setDuration(500);
            }
        });
        btnNotic.post(new Runnable() {
            @Override
            public void run() {
                rootView.setNotifyHeight(btnNotic.getHeight() + getStatusBarHeight());  //可触控区域高度
            }
        });
        
        if (messageTextInfo == null) {
            messageTextInfo = DialogSettings.contentTextInfo;
        }
        if (titleTextInfo == null) {
            titleTextInfo = DialogSettings.titleTextInfo;
        }
        
        useTextInfo(txtTitle, titleTextInfo);
        useTextInfo(txtMessage, messageTextInfo);
        
        btnNotic.setPadding(dip2px(15), getStatusBarHeight() + dip2px(15), dip2px(15), dip2px(15));
        
        if (isNull(title)) {
            txtTitle.setVisibility(View.GONE);
        } else {
            txtTitle.setVisibility(View.VISIBLE);
            txtTitle.setText(title);
        }
        
        if (iconResId == 0) {
            imgIcon.setVisibility(View.GONE);
        } else {
            imgIcon.setVisibility(View.VISIBLE);
            if (iconResId != 0) {
                imgIcon.setImageResource(iconResId);
            }
        }
        
        txtMessage.setText(message);
        if (isNull(title)) {
            TextPaint tp = txtMessage.getPaint();
            tp.setFakeBoldText(true);
        } else {
            TextPaint tp = txtMessage.getPaint();
            tp.setFakeBoldText(false);
        }
        
        boxBody.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (customView == null) toast.cancel();
                return false;
            }
        });
        
        new kToast().show(context.get(), rootView);
    }
    
    private void showIOSNotification() {
        LayoutInflater inflater = (LayoutInflater) context.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = (NotifyToastShadowView) inflater.inflate(R.layout.notification_ios, null);
        
        boxBody = rootView.findViewById(R.id.box_body);
        btnNotic = rootView.findViewById(R.id.btn_notic);
        boxTitle = rootView.findViewById(R.id.box_title);
        imgIcon = rootView.findViewById(R.id.img_icon);
        txtTitle = rootView.findViewById(R.id.txt_title);
        txtMessage = rootView.findViewById(R.id.txt_message);
        boxCustom = rootView.findViewById(R.id.box_custom);
        
        rootView.setParent(context.get());
        rootView.setOnNotificationClickListener(new OnNotificationClickListener() {
            @Override
            public void onClick() {
                if (customView == null) {
                    toast.cancel();
                    if (onNotificationClickListener != null) onNotificationClickListener.onClick();
                }
            }
        });
        
        boxBody.post(new Runnable() {
            @Override
            public void run() {
                boxBody.setY(-boxBody.getHeight());
                boxBody.animate().setInterpolator(new DecelerateInterpolator()).translationY(-dip2px(5)).setDuration(500);
            }
        });
        btnNotic.post(new Runnable() {
            @Override
            public void run() {
                rootView.setNotifyHeight(btnNotic.getHeight() + getStatusBarHeight());  //可触控区域高度
            }
        });
        
        if (messageTextInfo == null) {
            messageTextInfo = DialogSettings.contentTextInfo;
        }
        if (titleTextInfo == null) {
            titleTextInfo = DialogSettings.titleTextInfo;
        }
        
        useTextInfo(txtTitle, titleTextInfo);
        useTextInfo(txtMessage, messageTextInfo);
        
        boxBody.setPadding(0, getStatusBarHeight(), 0, 0);
        
        if (isNull(title)) {
            txtTitle.setVisibility(View.GONE);
        } else {
            txtTitle.setVisibility(View.VISIBLE);
            txtTitle.setText(title);
        }
        
        if (iconResId == 0) {
            imgIcon.setVisibility(View.GONE);
        } else {
            imgIcon.setVisibility(View.VISIBLE);
            if (iconResId != 0) {
                imgIcon.setImageResource(iconResId);
            }
        }
        
        txtMessage.setText(message);
        if (isNull(title)) {
            boxTitle.setVisibility(View.GONE);
            TextPaint tp = txtMessage.getPaint();
            tp.setFakeBoldText(true);
        } else {
            boxTitle.setVisibility(View.VISIBLE);
            TextPaint tp = txtMessage.getPaint();
            tp.setFakeBoldText(false);
        }
        
        boxBody.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (customView == null) toast.cancel();
                return false;
            }
        });
        
        new kToast().show(context.get(), rootView);
    }
    
    private void showKongzueNotification() {
        LayoutInflater inflater = (LayoutInflater) context.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = (NotifyToastShadowView) inflater.inflate(R.layout.notification_kongzue, null);
        
        boxBody = rootView.findViewById(R.id.box_body);
        btnNotic = rootView.findViewById(R.id.btn_notic);
        imgIcon = rootView.findViewById(R.id.img_icon);
        txtTitle = rootView.findViewById(R.id.txt_title);
        txtMessage = rootView.findViewById(R.id.txt_message);
        boxCustom = rootView.findViewById(R.id.box_custom);
        
        rootView.setParent(context.get());
        rootView.setNotifyHeight(dip2px(50) + getStatusBarHeight());  //可触控区域高度
        rootView.setOnNotificationClickListener(new OnNotificationClickListener() {
            @Override
            public void onClick() {
                if (customView == null) {
                    toast.cancel();
                    if (onNotificationClickListener != null) onNotificationClickListener.onClick();
                }
            }
        });
        
        boxBody.post(new Runnable() {
            @Override
            public void run() {
                boxBody.setY(-boxBody.getHeight());
                boxBody.animate().setInterpolator(new DecelerateInterpolator()).translationY(0).setDuration(500);
            }
        });
        btnNotic.post(new Runnable() {
            @Override
            public void run() {
                rootView.setNotifyHeight(btnNotic.getHeight() + getStatusBarHeight());  //可触控区域高度
            }
        });
        
        if (messageTextInfo == null) {
            messageTextInfo = DialogSettings.contentTextInfo;
        }
        if (titleTextInfo == null) {
            titleTextInfo = DialogSettings.titleTextInfo;
        }
        
        btnNotic.setPadding(dip2px(10), getStatusBarHeight(), dip2px(10), 0);
        
        refreshView();
        
        new kToast().show(context.get(), rootView);
    }
    
    private void refreshView() {
        if (style != DialogSettings.STYLE.STYLE_IOS) {
            if (btnNotic != null) {
                if (backgroundColor == 0)
                    if (style == DialogSettings.STYLE.STYLE_KONGZUE) {
                        backgroundColor = context.get().getResources().getColor(R.color.notificationNormal);
                    } else {
                        backgroundColor = context.get().getResources().getColor(R.color.white);
                    }
                btnNotic.setBackgroundColor(backgroundColor);
            }
        }
        if (txtTitle != null) {
            if (isNull(title)) {
                txtTitle.setVisibility(View.GONE);
            } else {
                txtTitle.setVisibility(View.VISIBLE);
                txtTitle.setText(title);
            }
        }
        if (txtMessage != null) {
            txtMessage.setText(message);
            if (isNull(title)) {
                txtMessage.setGravity(Gravity.CENTER);
                TextPaint tp = txtMessage.getPaint();
                tp.setFakeBoldText(true);
            } else {
                txtMessage.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                TextPaint tp = txtMessage.getPaint();
                tp.setFakeBoldText(false);
            }
        }
        if (imgIcon != null) {
            if (iconResId == 0) {
                imgIcon.setVisibility(View.GONE);
            } else {
                imgIcon.setVisibility(View.VISIBLE);
                if (iconResId != 0) {
                    imgIcon.setImageResource(iconResId);
                }
            }
        }
        if (boxCustom != null) {
            if (customView != null) {
                boxCustom.removeAllViews();
                boxCustom.setVisibility(View.VISIBLE);
                boxCustom.addView(customView);
                rootView.setDispatchTouchEvent(false);
                if (onBindView!=null)onBindView.onBind(this,customView);
            } else {
                boxCustom.setVisibility(View.GONE);
                rootView.setDispatchTouchEvent(true);
            }
        }
        
        useTextInfo(txtTitle, titleTextInfo);
        useTextInfo(txtMessage, messageTextInfo);
    }
    
    public void log(Object o) {
        if (DialogSettings.DEBUGMODE) Log.i(">>>", o.toString());
    }
    
    public void error(Object o) {
        if (DialogSettings.DEBUGMODE) Log.e(">>>", o.toString());
    }
    
    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.get().getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    private Method show;
    
    public class kToast {
        private LinearLayout btn;
        
        public void show(final Context context, final View view) {
            if (toast != null) toast.cancel();
            toast = null;
            
            toast = new Toast(context.getApplicationContext());
            toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 0, 0);
            toast.setDuration(durationTime.ordinal());
            toast.setView(view);
            toast.getView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            
            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                
                }
                
                @Override
                public void onViewDetachedFromWindow(View v) {
                    isShow = false;
                    if (onDismissListener != null) onDismissListener.onDismiss();
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
                        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                        params.width = WindowManager.LayoutParams.MATCH_PARENT;
                        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        
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
    }
    
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
    
    protected boolean isNull(String s) {
        if (s == null || s.trim().isEmpty() || s.equals("null") || s.equals("(null)")) {
            return true;
        }
        return false;
    }
    
    protected void useTextInfo(TextView textView, TextInfo textInfo) {
        if (textInfo == null) return;
        if (textView == null) return;
        if (textInfo.getFontSize() > 0) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textInfo.getFontSize());
        }
        if (textInfo.getFontColor() != 1) {
            textView.setTextColor(textInfo.getFontColor());
        }
        if (textInfo.getGravity() != -1) {
            textView.setGravity(textInfo.getGravity());
        }
        Typeface font = Typeface.create(Typeface.SANS_SERIF, textInfo.isBold() ? Typeface.BOLD : Typeface.NORMAL);
        textView.setTypeface(font);
    }
    
    protected int dip2px(float dpValue) {
        final float scale = context.get().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
    //其他
    public OnNotificationClickListener getOnNotificationClickListener() {
        return onNotificationClickListener;
    }
    
    public Notification setOnNotificationClickListener(OnNotificationClickListener onNotificationClickListener) {
        this.onNotificationClickListener = onNotificationClickListener;
        return this;
    }
    
    public DialogSettings.STYLE getStyle() {
        return style;
    }
    
    public Notification setStyle(DialogSettings.STYLE style) {
        this.style = style;
        if (isShow) {
            error("必须使用 build(...) 方法创建时，才可以使用 setStyle(...) 来修改通知主题或风格。");
        }
        return this;
    }
    
    public DURATION_TIME getDurationTime() {
        return durationTime;
    }
    
    public Notification setDurationTime(DURATION_TIME durationTime) {
        this.durationTime = durationTime;
        if (isShow) {
            error("必须使用 build(...) 方法创建时，才可以使用 setDurationTime(...) 来修改通知持续时间。");
        }
        return this;
    }
    
    public int getBackgroundColor() {
        return backgroundColor;
    }
    
    public Notification setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        refreshView();
        return this;
    }
    
    public String getTitle() {
        return title;
    }
    
    public Notification setTitle(String title) {
        this.title = title;
        refreshView();
        return this;
    }
    
    public Notification setTitle(int titleResId) {
        this.title = context.get().getString(titleResId);
        refreshView();
        return this;
    }
    
    public String getMessage() {
        return message;
    }
    
    public Notification setMessage(String message) {
        this.message = message;
        refreshView();
        return this;
    }
    
    public Notification setMessage(int messageResId) {
        this.message = context.get().getString(messageResId);
        refreshView();
        return this;
    }
    
    public int getIconResId() {
        return iconResId;
    }
    
    public Notification setIconResId(int iconResId) {
        this.iconResId = iconResId;
        refreshView();
        return this;
    }
    
    public TextInfo getTitleTextInfo() {
        return titleTextInfo;
    }
    
    public Notification setTitleTextInfo(TextInfo titleTextInfo) {
        this.titleTextInfo = titleTextInfo;
        refreshView();
        return this;
    }
    
    public TextInfo getMessageTextInfo() {
        return messageTextInfo;
    }
    
    public Notification setMessageTextInfo(TextInfo messageTextInfo) {
        this.messageTextInfo = messageTextInfo;
        refreshView();
        return this;
    }
    
    public OnDismissListener getOnDismissListener() {
        return onDismissListener;
    }
    
    public Notification setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }
    
    public View getCustomView() {
        return customView;
    }
    
    public Notification setCustomView(View customView) {
        this.customView = customView;
        refreshView();
        return this;
    }
    
    private  OnBindView onBindView;
    
    public Notification setCustomView(int customViewLayoutId, OnBindView onBindView) {
        customView = LayoutInflater.from(context.get()).inflate(customViewLayoutId, null);
        this.onBindView=onBindView;
        refreshView();
        return this;
    }
    
    public void dismiss() {
        if (toast != null) toast.cancel();
    }
    
    public interface OnBindView {
        void onBind(Notification notification, View v);
    }
}
