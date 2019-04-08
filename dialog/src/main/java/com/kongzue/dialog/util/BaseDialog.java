package com.kongzue.dialog.util;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.interfaces.DialogLifeCycleListener;
import com.kongzue.dialog.interfaces.OnDialogShowListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.v3.TipDialog;

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
    
    protected Handler mainHandler = new Handler(Looper.getMainLooper());
    public static AppCompatActivity newContext;
    
    public BaseDialog() {
        initDefaultSettings();
    }
    
    protected enum BOOLEAN {
        NULL, FALSE, TRUE
    }
    
    protected static List<BaseDialog> dialogList = new ArrayList<>();           //对话框队列
    
    public AppCompatActivity context;
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
    protected TextInfo messageTextInfo;
    protected TextInfo buttonTextInfo;
    protected TextInfo buttonPositiveTextInfo;
    protected InputInfo inputInfo;
    protected int backgroundColor = 0;
    
    protected DialogLifeCycleListener dialogLifeCycleListener;
    protected OnDismissListener onDismissListener;
    protected OnDismissListener dismissEvent;
    
    public void log(Object o) {
        if (DialogSettings.DEBUGMODE) Log.i(">>>", "NormalDialog:" + o.toString());
    }
    
    public void error(Object o) {
        if (DialogSettings.DEBUGMODE) Log.e(">>>", "NormalDialog 错误警告:" + o.toString());
    }
    
    public BaseDialog build(BaseDialog baseDialog, int layoutId) {
        this.baseDialog = baseDialog;
        this.layoutId = layoutId;
        return baseDialog;
    }
    
    public BaseDialog build(BaseDialog baseDialog) {
        this.baseDialog = baseDialog;
        this.layoutId = -1;
        return baseDialog;
    }
    
    
    public void showDialog() {
        showDialog(R.style.BaseDialog);
    }
    
    protected void showDialog(int style) {
        isAlreadyShown = true;
        styleId = style;
        dismissEvent = new OnDismissListener() {
            @Override
            public void onDismiss() {
                isShow = false;
                dialogList.remove(baseDialog);
                showNext();
                if (onDismissListener != null) onDismissListener.onDismiss();
            }
        };
        dialogList.add(this);
        if (baseDialog instanceof TipDialog) {
            showNow();
        } else {
            showNext();
        }
    }
    
    private void showNext() {
        List<BaseDialog> cache = new ArrayList<>();
        cache.addAll(BaseDialog.dialogList);
        for (BaseDialog dialog : cache) {
            if (dialog.context.isDestroyed()){
                dialogList.remove(dialog);
            }
        }
        for (BaseDialog dialog : dialogList) {
            if (!(dialog instanceof TipDialog)) {
                if (dialog.isShow) {
                    return;
                }
            }
        }
        for (BaseDialog dialog : dialogList) {
            if (!(dialog instanceof TipDialog)) {
                dialog.showNow();
                return;
            }
        }
    }
    
    private void showNow() {
        log("showNow");
        isShow = true;
        if (context.isDestroyed()) {
            context = newContext;
        }
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        dialog = new DialogHelper().setLayoutId(baseDialog, layoutId);
        dialog.setStyle(DialogFragment.STYLE_NORMAL, styleId);
        dialog.show(fragmentManager, "kongzueDialog");
        if (style == DialogSettings.STYLE.STYLE_IOS && !(baseDialog instanceof TipDialog)) {
            dialog.setOnDialogShowListener(new OnDialogShowListener() {
                @Override
                public void onShow(Dialog dialog) {
                    dialog.getWindow().setWindowAnimations(R.style.iOSDialogAnimStyle);
                }
            });
        }
        dialog.setOnDismissListener(dismissEvent);
        
        if (baseDialog instanceof TipDialog) {
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
        if (backgroundColor == 0) backgroundColor = DialogSettings.backgroundColor;
        if (titleTextInfo == null) titleTextInfo = DialogSettings.titleTextInfo;
        if (messageTextInfo == null) messageTextInfo = DialogSettings.contentTextInfo;
        if (buttonTextInfo == null) buttonTextInfo = DialogSettings.buttonTextInfo;
        if (inputInfo==null)inputInfo = DialogSettings.inputInfo;
        if (buttonPositiveTextInfo == null)
            buttonPositiveTextInfo = DialogSettings.buttonPositiveTextInfo;
    }
    
    protected void useTextInfo(TextView textView, TextInfo textInfo) {
        if (textInfo == null) return;
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
    
    //网络传输文本判空规则
    public boolean isNull(String s) {
        if (s == null || s.trim().isEmpty() || s.equals("null") || s.equals("(null)")) {
            return true;
        }
        return false;
    }
    
    public int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
