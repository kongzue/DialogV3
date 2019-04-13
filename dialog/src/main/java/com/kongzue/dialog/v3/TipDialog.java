package com.kongzue.dialog.v3;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.view.BlurView;
import com.kongzue.dialog.util.view.ProgressView;

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
                if (waitDialogTemp.context != context) {
                    dismiss();
                    waitDialogTemp = waitDialog;
                } else {
                    waitDialog = waitDialogTemp;
                    return null;
                }
            }
            waitDialog.log("装载等待对话框");
            waitDialog.context = context;
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
                    if (waitDialogTemp.dismissListener != null)
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
    
    public static TipDialog show(AppCompatActivity context, String message, TYPE type) {
        synchronized (TipDialog.class) {
            TipDialog waitDialog = build(context);
            
            waitDialogTemp.onDismissListener = new OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (waitDialogTemp.dismissListener != null)
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
    
    public static TipDialog show(AppCompatActivity context, String message, int resId) {
        synchronized (TipDialog.class) {
            TipDialog waitDialog = build(context);
            
            waitDialogTemp.onDismissListener = new OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (waitDialogTemp.dismissListener != null)
                        waitDialogTemp.dismissListener.onDismiss();
                    waitDialogTemp = null;
                }
            };
            
            if (waitDialog == null) {
                waitDialogTemp.setTip(resId);
                waitDialogTemp.setMessage(message);
                waitDialogTemp.autoDismiss();
                return waitDialogTemp;
            } else {
                waitDialog.message = message;
                waitDialog.setTip(resId);
                waitDialog.showDialog();
                waitDialog.autoDismiss();
                return waitDialog;
            }
        }
    }
    
    protected void showDialog() {
        log("启动等待对话框 -> " + message);
        super.showDialog();
    }
    
    private View rootView;
    
    @Override
    public void bindView(View rootView) {
        
        this.rootView = rootView;
        
        boxBody = rootView.findViewById(R.id.box_body);
        boxBlur = rootView.findViewById(R.id.box_blur);
        progress = rootView.findViewById(R.id.progress);
        boxTip = rootView.findViewById(R.id.box_tip);
        txtInfo = rootView.findViewById(R.id.txt_info);
        
        refreshView();
    }
    
    private Timer cancelTimer;
    
    private void autoDismiss() {
        if (cancelTimer != null) cancelTimer.cancel();
        cancelTimer = new Timer();
        cancelTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                        cancelTimer.cancel();
                    }
                });
            }
        }, tipTime);
    }
    
    public void refreshView() {
        if (rootView != null) {
            final int bkgResId, blurFrontColor;
            //WaitDialog的颜色与主题色相反
            switch (theme) {
                case DARK:
                    bkgResId = R.drawable.rect_light;
                    blurFrontColor = Color.argb(blurAlpha, 255, 255, 255);
                    int darkColor = Color.rgb(0, 0, 0);
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
                case LIGHT:
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
            if (DialogSettings.isUseBlur) {
                boxBlur.post(new Runnable() {
                    @Override
                    public void run() {
                        blurView = new BlurView(context, null);
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(boxBlur.getWidth(), boxBlur.getHeight());
                        blurView.setOverlayColor(blurFrontColor);
                        boxBlur.addView(blurView, 0, params);
                    }
                });
            } else {
                boxBody.setBackgroundResource(bkgResId);
            }
            
            if (isNull(message)){
                txtInfo.setVisibility(View.GONE);
            }else{
                txtInfo.setVisibility(View.VISIBLE);
                txtInfo.setText(message);
            }
        }
    }
    
    @Override
    public void show() {
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
        return this;
    }
    
    public static void dismiss() {
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
        log("启动等待对话框 -> " + message);
        this.message = message;
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
        tipImage = ContextCompat.getDrawable(context, resId);
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
        if (theme== DialogSettings.THEME.LIGHT){
            this.theme = DialogSettings.THEME.DARK;
        }else{
            this.theme = DialogSettings.THEME.LIGHT;
        }
        refreshView();
        return this;
    }
}
