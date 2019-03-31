package com.kongzue.dialog.v3;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogHelper;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.view.BlurView;
import com.kongzue.dialog.util.view.ProgressView;

import java.util.List;

import static com.kongzue.dialog.util.DialogSettings.blurAlpha;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/3/22 16:16
 */
public class WaitDialog extends BaseDialog {
    
    private OnDismissListener dismissListener;
    
    public static WaitDialog waitDialogTemp;
    private String message;
    
    private BlurView blurView;
    
    private RelativeLayout boxBody;
    private RelativeLayout boxBlur;
    private ProgressView progress;
    private TextView txtInfo;
    
    public static WaitDialog build(AppCompatActivity context) {
        synchronized (WaitDialog.class) {
            WaitDialog waitDialog = new WaitDialog();
    
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
    
    public static WaitDialog show(AppCompatActivity context, String message) {
        synchronized (WaitDialog.class) {
            WaitDialog waitDialog = build(context);
            
            waitDialogTemp.onDismissListener = new OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (waitDialogTemp.dismissListener != null)
                        waitDialogTemp.dismissListener.onDismiss();
                    waitDialogTemp = null;
                }
            };
            
            if (waitDialog == null) {
                waitDialogTemp.setMessage(message);
                return waitDialogTemp;
            } else {
                waitDialog.message = message;
                waitDialog.showDialog();
                return waitDialog;
            }
        }
    }
    
    @Override
    public void bindView(View rootView) {
        log("WaitDialog: bindView");
        boxBody = rootView.findViewById(R.id.box_body);
        boxBlur = rootView.findViewById(R.id.box_blur);
        progress = rootView.findViewById(R.id.progress);
        txtInfo = rootView.findViewById(R.id.txt_info);
        
        final int bkgResId, blurFrontColor;
        //WaitDialog的颜色与主题色相反
        switch (theme) {
            case DARK:
                bkgResId = R.drawable.rect_light;
                blurFrontColor = Color.argb(blurAlpha, 255, 255, 255);
                int darkColor = Color.rgb(0, 0, 0);
                progress.setStrokeColors(new int[]{darkColor});
                txtInfo.setTextColor(darkColor);
                break;
            case LIGHT:
                bkgResId = R.drawable.rect_dark;
                int lightColor = Color.rgb(255, 255, 255);
                blurFrontColor = Color.argb((blurAlpha + 20) > 255 ? 255 : (blurAlpha + 20), 0, 0, 0);
                progress.setStrokeColors(new int[]{lightColor});
                txtInfo.setTextColor(lightColor);
                break;
            default:
                bkgResId = R.drawable.rect_dark;
                blurFrontColor = Color.argb((blurAlpha + 20) > 255 ? 255 : (blurAlpha + 20), 0, 0, 0);
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
        
        txtInfo.setText(message);
    }
    
    public OnDismissListener getOnDismissListener() {
        return dismissListener == null ? new OnDismissListener() {
            @Override
            public void onDismiss() {
            
            }
        } : dismissListener;
    }
    
    public WaitDialog setOnDismissListener(OnDismissListener onDismissListener) {
        this.dismissListener = onDismissListener;
        return this;
    }
    
    public static void dismiss() {
        for (BaseDialog dialog : dialogList) {
            if (dialog instanceof WaitDialog) {
                dialog.doDismiss();
            }
        }
    }
    
    public String getMessage() {
        return message;
    }
    
    public WaitDialog setMessage(String message) {
        this.message = message;
        if (txtInfo != null) txtInfo.setText(message);
        return this;
    }
}
