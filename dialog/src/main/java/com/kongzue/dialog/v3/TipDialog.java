package com.kongzue.dialog.v3;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.interfaces.OnShowListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.util.view.BlurView;
import com.kongzue.dialog.util.view.ProgressView;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.kongzue.dialog.util.DialogSettings.blurAlpha;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/3/22 16:16
 */
public class TipDialog extends BaseDialog {
    
    private DialogSettings.THEME tipTheme;
    
    public enum TYPE {
        WARNING, SUCCESS, ERROR, OTHER
    }
    
    private OnDismissListener dismissListener;
    
    public static TipDialog waitDialogTemp;
    private String message;
    private TYPE type;
    private Drawable tipImage;
    
    private BlurView blurView;
    
    private RelativeLayout boxBody;
    private RelativeLayout boxBlur;
    private ProgressView progress;
    private RelativeLayout boxTip;
    private TextView txtInfo;
    
    private int tipTime = 1500;
    
    protected TipDialog() {
    }
    
    public static TipDialog build(AppCompatActivity context) {
        synchronized (TipDialog.class) {
            TipDialog waitDialog = new TipDialog();
            
            if (waitDialogTemp == null) {
                waitDialogTemp = waitDialog;
            } else {
                if (waitDialogTemp.context.get() != context) {
                    dismiss();
                    waitDialogTemp = waitDialog;
                } else {
                    waitDialog = waitDialogTemp;
                    return null;
                }
            }
            waitDialog.log("装载等待对话框");
            waitDialog.context = new WeakReference<>(context);
            waitDialog.build(waitDialog, R.layout.dialog_wait);
            return waitDialog;
        }
    }
    
    public static TipDialog showWait(AppCompatActivity context, String message) {
        synchronized (TipDialog.class) {
            TipDialog waitDialog = build(context);
            
            waitDialogTemp.onDismissListener = new OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (waitDialogTemp != null && waitDialogTemp.dismissListener != null)
                        waitDialogTemp.dismissListener.onDismiss();
                    waitDialogTemp = null;
                }
            };
            
            if (waitDialog == null) {
                waitDialogTemp.setTip(null);
                waitDialogTemp.setMessage(message);
                if (waitDialogTemp.cancelTimer != null) waitDialogTemp.cancelTimer.cancel();
                return waitDialogTemp;
            } else {
                waitDialog.message = message;
                waitDialog.type = null;
                waitDialog.tipImage = null;
                if (waitDialog.cancelTimer != null) waitDialog.cancelTimer.cancel();
                waitDialog.showDialog();
                return waitDialog;
            }
        }
    }
    
    public static TipDialog showWait(AppCompatActivity context, int messageResId) {
        synchronized (TipDialog.class) {
            TipDialog waitDialog = build(context);
            
            waitDialogTemp.onDismissListener = new OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (waitDialogTemp != null && waitDialogTemp.dismissListener != null)
                        waitDialogTemp.dismissListener.onDismiss();
                    waitDialogTemp = null;
                }
            };
            
            if (waitDialog == null) {
                waitDialogTemp.setTip(null);
                waitDialogTemp.setMessage(context.getString(messageResId));
                if (waitDialogTemp.cancelTimer != null) waitDialogTemp.cancelTimer.cancel();
                return waitDialogTemp;
            } else {
                waitDialog.message = context.getString(messageResId);
                waitDialog.type = null;
                waitDialog.tipImage = null;
                if (waitDialog.cancelTimer != null) waitDialog.cancelTimer.cancel();
                waitDialog.showDialog();
                return waitDialog;
            }
        }
    }
    
    public static TipDialog show(AppCompatActivity context, String message, TYPE type) {
        synchronized (TipDialog.class) {
            TipDialog waitDialog = build(context);
            
            waitDialogTemp.onDismissListener = new OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (waitDialogTemp != null && waitDialogTemp.dismissListener != null)
                        waitDialogTemp.dismissListener.onDismiss();
                    waitDialogTemp = null;
                }
            };
            
            if (waitDialog == null) {
                waitDialogTemp.setTip(type);
                waitDialogTemp.setMessage(message);
                waitDialogTemp.autoDismiss();
                return waitDialogTemp;
            } else {
                waitDialog.message = message;
                waitDialog.setTip(type);
                waitDialog.showDialog();
                waitDialog.autoDismiss();
                return waitDialog;
            }
        }
    }
    
    public static TipDialog show(AppCompatActivity context, int messageResId, TYPE type) {
        return show(context, context.getString(messageResId), type);
    }
    
    public static TipDialog show(AppCompatActivity context, String message, int icoResId) {
        Log.e("@@@", "show: " + (waitDialogTemp == null));
        synchronized (TipDialog.class) {
            TipDialog waitDialog = build(context);
            
            waitDialogTemp.onDismissListener = new OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (waitDialogTemp != null && waitDialogTemp.dismissListener != null)
                        waitDialogTemp.dismissListener.onDismiss();
                    waitDialogTemp = null;
                }
            };
            
            if (waitDialog == null) {
                waitDialogTemp.setTip(icoResId);
                waitDialogTemp.setMessage(message);
                waitDialogTemp.autoDismiss();
                return waitDialogTemp;
            } else {
                waitDialog.message = message;
                waitDialog.setTip(icoResId);
                waitDialog.showDialog();
                waitDialog.autoDismiss();
                return waitDialog;
            }
        }
    }
    
    public static TipDialog show(AppCompatActivity context, int messageResId, int icoResId) {
        return show(context, context.getString(messageResId), icoResId);
    }
    
    protected void showDialog() {
        log("启动等待对话框 -> " + message);
        super.showDialog();
        setDismissEvent();
    }
    
    private View rootView;
    
    @Override
    public void bindView(View rootView) {
        
        this.rootView = rootView;
        
        if (boxBlur != null) boxBlur.removeAllViews();
        boxBody = rootView.findViewById(R.id.box_body);
        boxBlur = rootView.findViewById(R.id.box_blur);
        progress = rootView.findViewById(R.id.progress);
        boxTip = rootView.findViewById(R.id.box_tip);
        txtInfo = rootView.findViewById(R.id.txt_info);
        
        refreshView();
        if (onShowListener != null) onShowListener.onShow(this);
    }
    
    private Timer cancelTimer;
    
    private void autoDismiss() {
        if (cancelTimer != null) cancelTimer.cancel();
        cancelTimer = new Timer();
        cancelTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                dismiss();
                cancelTimer.cancel();
            }
        }, tipTime);
    }
    
    public void refreshView() {
        if (rootView != null) {
            final int bkgResId, blurFrontColor;
            if (tipTheme == null) tipTheme = DialogSettings.tipTheme;
            if (DialogSettings.tipBackgroundResId != 0 && backgroundResId == -1) {
                backgroundResId = DialogSettings.tipBackgroundResId;
            }
            
            switch (tipTheme) {
                case LIGHT:
                    bkgResId = R.drawable.rect_light;
                    int darkColor = Color.rgb(0, 0, 0);
                    blurFrontColor = Color.argb(blurAlpha, 255, 255, 255);
                    progress.setStrokeColors(new int[]{darkColor});
                    txtInfo.setTextColor(darkColor);
                    if (type != null) {
                        progress.setVisibility(View.GONE);
                        boxTip.setVisibility(View.VISIBLE);
                        switch (type) {
                            case OTHER:
                                boxTip.setBackground(tipImage);
                                break;
                            case ERROR:
                                boxTip.setBackgroundResource(R.mipmap.img_error_dark);
                                break;
                            case WARNING:
                                boxTip.setBackgroundResource(R.mipmap.img_warning_dark);
                                break;
                            case SUCCESS:
                                boxTip.setBackgroundResource(R.mipmap.img_finish_dark);
                                break;
                        }
                    } else {
                        progress.setVisibility(View.VISIBLE);
                        boxTip.setVisibility(View.GONE);
                    }
                    break;
                case DARK:
                    bkgResId = R.drawable.rect_dark;
                    int lightColor = Color.rgb(255, 255, 255);
                    blurFrontColor = Color.argb(blurAlpha, 0, 0, 0);
                    progress.setStrokeColors(new int[]{lightColor});
                    txtInfo.setTextColor(lightColor);
                    if (type != null) {
                        progress.setVisibility(View.GONE);
                        boxTip.setVisibility(View.VISIBLE);
                        switch (type) {
                            case OTHER:
                                boxTip.setBackground(tipImage);
                                break;
                            case ERROR:
                                boxTip.setBackgroundResource(R.mipmap.img_error);
                                break;
                            case WARNING:
                                boxTip.setBackgroundResource(R.mipmap.img_warning);
                                break;
                            case SUCCESS:
                                boxTip.setBackgroundResource(R.mipmap.img_finish);
                                break;
                        }
                    } else {
                        progress.setVisibility(View.VISIBLE);
                        boxTip.setVisibility(View.GONE);
                    }
                    break;
                default:
                    bkgResId = R.drawable.rect_dark;
                    blurFrontColor = Color.argb(blurAlpha, 0, 0, 0);
                    break;
            }
            if (backgroundResId != -1) {
                boxBody.setBackgroundResource(backgroundResId);
            } else {
                
                if (DialogSettings.isUseBlur) {
                    boxBlur.post(new Runnable() {
                        @Override
                        public void run() {
                            blurView = new BlurView(context.get(), null);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                            blurView.setOverlayColor(blurFrontColor);
                            boxBlur.addView(blurView, 0, params);
                        }
                    });
                    boxBody.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            if (boxBlur != null && boxBody != null) {
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(boxBody.getWidth(), boxBody.getHeight());
                                boxBlur.setLayoutParams(params);
                            }
                        }
                    });
                } else {
                    boxBody.setBackgroundResource(bkgResId);
                }
            }
            
            if (isNull(message)) {
                txtInfo.setVisibility(View.GONE);
            } else {
                txtInfo.setVisibility(View.VISIBLE);
                txtInfo.setText(message);
                useTextInfo(txtInfo, tipTextInfo);
            }
            
            if (customView != null) {
                progress.setVisibility(View.GONE);
                boxTip.setBackground(null);
                boxTip.setVisibility(View.VISIBLE);
                boxTip.addView(customView);
                if (onBindView != null) onBindView.onBind(this, customView);
            }
        }
    }
    
    protected void setDismissEvent() {
        onDismissListener = new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (dismissListener != null)
                    dismissListener.onDismiss();
                waitDialogTemp = null;
            }
        };
    }
    
    @Override
    public void show() {
        showDialog();
        autoDismiss();
    }
    
    public void showNoAutoDismiss() {
        showDialog();
    }
    
    public OnDismissListener getOnDismissListener() {
        return dismissListener == null ? new OnDismissListener() {
            @Override
            public void onDismiss() {
            
            }
        } : dismissListener;
    }
    
    public TipDialog setOnDismissListener(OnDismissListener onDismissListener) {
        this.dismissListener = onDismissListener;
        setDismissEvent();
        return this;
    }
    
    public OnShowListener getOnShowListener() {
        return onShowListener == null ? new OnShowListener() {
            @Override
            public void onShow(BaseDialog dialog) {
            
            }
        } : onShowListener;
    }
    
    public TipDialog setOnShowListener(OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
        return this;
    }
    
    public static void dismiss() {
        if (waitDialogTemp != null) waitDialogTemp.doDismiss();
        waitDialogTemp = null;
        for (BaseDialog dialog : dialogList) {
            if (dialog instanceof TipDialog) {
                dialog.doDismiss();
            }
        }
    }
    
    public String getMessage() {
        return message;
    }
    
    public TipDialog setMessage(String message) {
        this.message = message;
        log("启动等待对话框 -> " + message);
        if (txtInfo != null) txtInfo.setText(message);
        refreshView();
        return this;
    }
    
    public TipDialog setMessage(int messageResId) {
        this.message = context.get().getString(messageResId);
        log("启动等待对话框 -> " + message);
        if (txtInfo != null) txtInfo.setText(message);
        refreshView();
        return this;
    }
    
    public TipDialog setTip(TYPE type) {
        this.type = type;
        if (type != TYPE.OTHER) tipImage = null;
        refreshView();
        return this;
    }
    
    public TipDialog setTip(@DrawableRes int resId) {
        this.type = TYPE.OTHER;
        tipImage = ContextCompat.getDrawable(context.get(), resId);
        refreshView();
        return this;
    }
    
    public TYPE getType() {
        return type;
    }
    
    public Drawable getTipImage() {
        return tipImage;
    }
    
    public TextView getTxtInfo() {
        return txtInfo;
    }
    
    public TipDialog setTipTime(int tipTime) {
        this.tipTime = tipTime;
        if (type != null) autoDismiss();
        return this;
    }
    
    public TipDialog setTheme(DialogSettings.THEME theme) {
        tipTheme = theme;
        refreshView();
        return this;
    }
    
    public DialogSettings.THEME getTheme() {
        return tipTheme;
    }
    
    public TipDialog setCustomView(View customView) {
        this.customView = customView;
        refreshView();
        return this;
    }
    
    private OnBindView onBindView;
    
    public TipDialog setCustomView(int customViewLayoutId, OnBindView onBindView) {
        customView = LayoutInflater.from(context.get()).inflate(customViewLayoutId, null);
        this.onBindView = onBindView;
        refreshView();
        return this;
    }
    
    public boolean getCancelable() {
        return cancelable == BOOLEAN.TRUE;
    }
    
    public TipDialog setCancelable(boolean enable) {
        this.cancelable = enable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        if (dialog != null) dialog.setCancelable(cancelable == BOOLEAN.TRUE);
        return this;
    }
    
    public interface OnBindView {
        void onBind(TipDialog dialog, View v);
    }
    
    public TextInfo getMessageTextInfo() {
        return messageTextInfo;
    }
    
    public TipDialog setMessageTextInfo(TextInfo messageTextInfo) {
        this.messageTextInfo = messageTextInfo;
        refreshView();
        return this;
    }
    
    public int getBackgroundResId() {
        return backgroundResId;
    }
    
    public TipDialog setBackgroundResId(int backgroundResId) {
        this.backgroundResId = backgroundResId;
        refreshView();
        return this;
    }
}
