package com.kongzue.dialog.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.kongzue.dialog.R;
import com.kongzue.dialog.interfaces.DialogLifeCycleListener;
import com.kongzue.dialog.interfaces.OnDialogShowListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.v3.WaitDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/3/22 16:17
 */
public abstract class BaseDialog {
    
    public BaseDialog() {
        initDefaultSettings();
    }
    
    protected enum BOOLEAN {
        NULL, FALSE, TRUE
    }
    
    protected static List<BaseDialog> dialogList = new ArrayList<>();           //对话框队列
    
    protected AppCompatActivity context;
    public DialogHelper dialog;                                              //我才是本体！
    
    private BaseDialog baseDialog;
    private int layoutId;
    private int styleId;
    protected boolean isShow;
    protected boolean isAlreadyShown;
    
    protected DialogSettings.STYLE style;
    protected DialogSettings.THEME theme;
    protected BOOLEAN cancelable;
    
    protected TextInfo titleTextInfo;
    protected TextInfo contentTextInfo;
    protected TextInfo buttonTextInfo;
    protected TextInfo buttonPositiveTextInfo;
    protected Color backgroundColor;
    
    protected DialogLifeCycleListener dialogLifeCycleListener;
    protected OnDismissListener onDismissListener;
    protected OnDismissListener dismissEvent;
    
    public void log(Object o) {
        if (DialogSettings.DEBUGMODE) Log.i(">>>", "Dialog:" + o.toString());
    }
    
    public void error(Object o) {
        if (DialogSettings.DEBUGMODE) Log.e(">>>", "Dialog 错误警告:" + o.toString());
    }
    
    public BaseDialog build(BaseDialog baseDialog, int layoutId) {
        this.baseDialog = baseDialog;
        this.layoutId = layoutId;
        return baseDialog;
    }
    
    public void showDialog() {
        showDialog(R.style.BaseDialog);
    }
    
    protected void showDialog(int style) {
        isAlreadyShown = true;
        styleId = style;
        if (baseDialog instanceof WaitDialog) {
            showNow();
        } else {
            showNext();
        }
        dismissEvent = new OnDismissListener() {
            @Override
            public void onDismiss() {
                isShow = false;
                dialogList.remove(baseDialog);
                showNext();
                if (onDismissListener!=null)onDismissListener.onDismiss();
            }
        };
        dialogList.add(this);
    }
    
    private void showNext() {
        for (BaseDialog dialog : dialogList) {
            if (!(dialog instanceof WaitDialog)) {
                if (dialog.isShow) {
                    return;
                }
            }
        }
        for (BaseDialog dialog : dialogList) {
            if (!(dialog instanceof WaitDialog)) {
                dialog.showNow();
                return;
            }
        }
    }
    
    private void showNow() {
        isShow = true;
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        dialog = new DialogHelper().setLayoutId(baseDialog, layoutId);
        dialog.setStyle(DialogFragment.STYLE_NORMAL, styleId);
        dialog.show(fragmentManager, "kongzueDialog");
        if (style == DialogSettings.STYLE.STYLE_IOS) {
            dialog.setOnDialogShowListener(new OnDialogShowListener() {
                @Override
                public void onShow(Dialog dialog) {
                    dialog.getWindow().setWindowAnimations(R.style.iOSDialogAnimStyle);
                }
            });
        }
        dialog.setOnDismissListener(dismissEvent);
        
        if (baseDialog instanceof WaitDialog) {
            if (cancelable == null)
                cancelable = DialogSettings.cancelableWaitDialog ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        } else {
            if (cancelable == null)
                cancelable = DialogSettings.cancelable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        }
        if (dialog != null) {
            dialog.setCancelable(cancelable == BOOLEAN.TRUE);
        }
    }
    
    public abstract void bindView(View rootView);
    
    public void doDismiss() {
        dialog.dismiss();
    }
    
    protected void initDefaultSettings() {
        if (theme == null) theme = DialogSettings.theme;
        if (style == null) style = DialogSettings.style;
    }
    
    //网络传输文本判空规则
    public boolean isNull(String s) {
        if (s == null || s.trim().isEmpty() || s.equals("null") || s.equals("(null)")) {
            return true;
        }
        return false;
    }
}
