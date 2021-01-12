package com.kongzue.dialog.v3;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
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
import com.kongzue.dialog.util.kToast;
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
    
    public enum Mode {
        FLOATING_WINDOW,
        TOAST
    }
    
    public static Mode mode = Mode.TOAST;
    
    protected kToast toast;
    private OnNotificationClickListener onNotificationClickListener;
    private OnDismissListener onDismissListener;
    
    private DialogSettings.STYLE style;
    private int backgroundColor;
    private Notification.DURATION_TIME durationTime = Notification.DURATION_TIME.LONG;
    private WeakReference<Context> context;
    private CharSequence title;
    private CharSequence message;
    private int iconResId;
    
    private View customView;
    private NotifyToastShadowView rootView;
    
    private RelativeLayout boxBody;
    private LinearLayout boxTitle;
    private LinearLayout boxNotic;
    private LinearLayout btnNotic;
    private TextView txtTitle;
    private TextView txtMessage;
    private ImageView imgIcon;
    private RelativeLayout boxCustom;
    
    private TextInfo titleTextInfo;
    private TextInfo messageTextInfo;
    
    private Notification() {
    }
    
    public static Notification build(Context context, CharSequence message) {
        synchronized (Notification.class) {
            Notification notification = new Notification();
            notification.log("装载消息通知: " + notification.toString());
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
    
    public static Notification show(Context context, CharSequence message) {
        Notification notification = build(context, message);
        notification.showNotification();
        return notification;
    }
    
    public static Notification show(Context context, int messageResId) {
        return show(context, context.getString(messageResId));
    }
    
    public static Notification show(Context context, CharSequence message, DURATION_TIME durationTime) {
        Notification notification = build(context, message);
        notification.durationTime = durationTime;
        notification.showNotification();
        return notification;
    }
    
    public static Notification show(Context context, int messageResId, DURATION_TIME durationTime) {
        return show(context, context.getString(messageResId), durationTime);
    }
    
    public static Notification show(Context context, CharSequence message, DialogSettings.STYLE style) {
        Notification notification = build(context, message);
        notification.style = style;
        notification.showNotification();
        return notification;
    }
    
    public static Notification show(Context context, int messageResId, DialogSettings.STYLE style) {
        return show(context, context.getString(messageResId), style);
    }
    
    public static Notification show(Context context, CharSequence message, DialogSettings.STYLE style, DURATION_TIME durationTime) {
        Notification notification = build(context, message);
        notification.durationTime = durationTime;
        notification.style = style;
        notification.showNotification();
        return notification;
    }
    
    public static Notification show(Context context, int messageResId, DialogSettings.STYLE style, DURATION_TIME durationTime) {
        return show(context, context.getString(messageResId), style, durationTime);
    }
    
    public static Notification show(Context context, CharSequence title, CharSequence message) {
        Notification notification = build(context, message);
        notification.title = title;
        notification.showNotification();
        return notification;
    }
    
    public static Notification show(Context context, int titleResId, int messageResId) {
        return show(context, context.getString(titleResId), context.getString(messageResId));
    }
    
    public static Notification show(Context context, CharSequence title, CharSequence message, DURATION_TIME durationTime) {
        Notification notification = build(context, message);
        notification.title = title;
        notification.durationTime = durationTime;
        notification.showNotification();
        return notification;
    }
    
    public static Notification show(Context context, int titleResId, int messageResId, DURATION_TIME durationTime) {
        return show(context, context.getString(titleResId), context.getString(messageResId), durationTime);
    }
    
    public static Notification show(Context context, CharSequence title, CharSequence message, DialogSettings.STYLE style) {
        Notification notification = build(context, message);
        notification.title = title;
        notification.style = style;
        notification.showNotification();
        return notification;
    }
    
    public static Notification show(Context context, int titleResId, int messageResId, DialogSettings.STYLE style) {
        return show(context, context.getString(titleResId), context.getString(messageResId), style);
    }
    
    public static Notification show(Context context, CharSequence title, CharSequence message, DialogSettings.STYLE style, DURATION_TIME durationTime) {
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
    
    public static Notification show(Context context, CharSequence title, CharSequence message, int iconResId) {
        Notification notification = build(context, message);
        notification.title = title;
        notification.iconResId = iconResId;
        notification.showNotification();
        return notification;
    }
    
    public static Notification show(Context context, int titleResId, int messageResId, int iconResId) {
        return show(context, context.getString(titleResId), context.getString(messageResId), iconResId);
    }
    
    public static Notification show(Context context, CharSequence title, CharSequence message, int iconResId, DURATION_TIME durationTime) {
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
    
    public static Notification show(Context context, CharSequence title, CharSequence message, int iconResId, DialogSettings.STYLE style) {
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
    
    public static Notification show(Context context, CharSequence title, CharSequence message, int iconResId, DialogSettings.STYLE style, DURATION_TIME durationTime) {
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
        log("启动消息通知 -> " + toString());
        isShow = true;
        if (style == null) style = DialogSettings.style;
        switch (style) {
            case STYLE_MIUI:
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
        boxNotic = rootView.findViewById(R.id.box_notic);
        btnNotic = rootView.findViewById(R.id.btn_notic);
        txtTitle = rootView.findViewById(R.id.txt_title);
        txtMessage = rootView.findViewById(R.id.txt_message);
        imgIcon = rootView.findViewById(R.id.img_icon);
        boxCustom = rootView.findViewById(R.id.box_custom);
        
        rootView.setOnNotificationClickListener(new OnNotificationClickListener() {
            @Override
            public void onClick() {
                if (customView == null) {
                    toast.cancel();
                    if (onNotificationClickListener != null) onNotificationClickListener.onClick();
                }
            }
        });
    
        boxNotic.post(new Runnable() {
            @Override
            public void run() {
                boxNotic.setY(-boxBody.getHeight());
                boxNotic.animate().setInterpolator(new DecelerateInterpolator(2.5f)).translationY(0).setDuration(800);
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
        
        toast = new kToast(durationTime, onDismissListener).show(context.get(), rootView);
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
        
        rootView.setOnNotificationClickListener(new OnNotificationClickListener() {
            @Override
            public void onClick() {
                if (customView == null) {
                    toast.cancel();
                    if (onNotificationClickListener != null) onNotificationClickListener.onClick();
                }
            }
        });
        
        btnNotic.post(new Runnable() {
            @Override
            public void run() {
                btnNotic.setY(-boxBody.getHeight());
                btnNotic.animate().setInterpolator(new DecelerateInterpolator(2.5f)).translationY(getStatusBarHeight()-dip2px(10)).setDuration(800);
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
        
        toast = new kToast(durationTime, onDismissListener).show(context.get(), rootView);
    }
    
    private void showKongzueNotification() {
        LayoutInflater inflater = (LayoutInflater) context.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rootView = (NotifyToastShadowView) inflater.inflate(R.layout.notification_kongzue, null);
        
        boxBody = rootView.findViewById(R.id.box_body);
        boxNotic = rootView.findViewById(R.id.box_notic);
        btnNotic = rootView.findViewById(R.id.btn_notic);
        txtTitle = rootView.findViewById(R.id.txt_title);
        txtMessage = rootView.findViewById(R.id.txt_message);
        imgIcon = rootView.findViewById(R.id.img_icon);
        boxCustom = rootView.findViewById(R.id.box_custom);
        
        rootView.setOnNotificationClickListener(new OnNotificationClickListener() {
            @Override
            public void onClick() {
                if (customView == null) {
                    toast.cancel();
                    if (onNotificationClickListener != null) onNotificationClickListener.onClick();
                }
            }
        });
    
        boxNotic.post(new Runnable() {
            @Override
            public void run() {
                boxNotic.setY(-boxBody.getHeight());
                boxNotic.animate().setInterpolator(new DecelerateInterpolator(2.5f)).translationY(0).setDuration(800);
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
        
        toast = new kToast(durationTime, onDismissListener).show(context.get(), rootView);
    }
    
    private void refreshView() {
        if (style != DialogSettings.STYLE.STYLE_IOS && style != DialogSettings.STYLE.STYLE_MIUI) {
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
                if (customView.getParent() != null && customView.getParent() instanceof ViewGroup) {
                    ((ViewGroup) customView.getParent()).removeView(customView);
                }
                boxCustom.addView(customView);
                rootView.setDispatchTouchEvent(false);
                if (onBindView != null) onBindView.onBind(this, customView);
            } else {
                boxCustom.setVisibility(View.GONE);
                rootView.setDispatchTouchEvent(true);
            }
        }
        
        useTextInfo(txtTitle, titleTextInfo);
        useTextInfo(txtMessage, messageTextInfo);
    }
    
    public static void log(Object o) {
        if (DialogSettings.DEBUGMODE) Log.i(">>>", o.toString());
    }
    
    public static void error(Object o) {
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
    
    
    protected boolean isNull(String s) {
        if (s == null || s.length() == 0 || s.trim().isEmpty() || s.equals("null") || s.equals("(null)")) {
            return true;
        }
        return false;
    }
    
    protected boolean isNull(CharSequence s) {
        if (s == null || s.length() == 0 || s.toString().trim().isEmpty() || s.toString().equals("null") || s.toString().equals("(null)")) {
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
    
    public CharSequence getTitle() {
        return title;
    }
    
    public Notification setTitle(CharSequence title) {
        this.title = title;
        refreshView();
        return this;
    }
    
    public Notification setTitle(int titleResId) {
        this.title = context.get().getString(titleResId);
        refreshView();
        return this;
    }
    
    public CharSequence getMessage() {
        return message;
    }
    
    public Notification setMessage(CharSequence message) {
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
    
    private OnBindView onBindView;
    
    public Notification setCustomView(int customViewLayoutId, OnBindView onBindView) {
        customView = LayoutInflater.from(context.get()).inflate(customViewLayoutId, null);
        this.onBindView = onBindView;
        refreshView();
        return this;
    }
    
    public void dismiss() {
        if (toast != null) toast.cancel();
    }
    
    public interface OnBindView {
        void onBind(Notification notification, View v);
    }
    
    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    }
    
    public static boolean hasFloatingWindowPermission(Context context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && Settings.canDrawOverlays(context))
            return true;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {//USING APP OPS MANAGER
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            if (manager != null) {
                try {
                    int result = manager.checkOp(AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW, Binder.getCallingUid(), context.getPackageName());
                    return result == AppOpsManager.MODE_ALLOWED;
                } catch (Exception ignore) {
                }
            }
        }
        
        try {
            WindowManager mgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (mgr == null) return false;
            View viewToAdd = new View(context);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(0, 0, android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O ?
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);
            viewToAdd.setLayoutParams(params);
            mgr.addView(viewToAdd, params);
            mgr.removeView(viewToAdd);
            return true;
        } catch (Exception ignore) {
        }
        return false;
    }
    
    public static void requestFloatingWindowPermission(Activity context) {
        try {
            context.startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName())), 1);
        } catch (Exception e) {
            error("无法开启悬浮窗权限，请检查是否已在 AndroidManifest.xml 声明：<uses-permission android:name=\"android.permission.SYSTEM_ALERT_WINDOW\" />");
        }
    }
}
