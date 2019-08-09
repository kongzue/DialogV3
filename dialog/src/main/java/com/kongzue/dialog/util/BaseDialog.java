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
import com.kongzue.dialog.interfaces.OnShowListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.v3.BottomMenu;
import com.kongzue.dialog.v3.ShareDialog;
import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;

import java.lang.ref.WeakReference;
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
    protected static WeakReference<AppCompatActivity> newContext;
    
    public BaseDialog() {
        initDefaultSettings();
    }
    
    protected enum BOOLEAN {
        NULL, FALSE, TRUE
    }
    
    protected static List<BaseDialog> dialogList = new ArrayList<>();           //对话框队列
    
    public WeakReference<AppCompatActivity> context;
    public DialogHelper dialog;                                              //我才是本体！
    
    private BaseDialog baseDialog;
    private int layoutId;
    private int styleId;
    public boolean isShow;
    protected boolean isAlreadyShown;
    
    protected DialogSettings.STYLE style;
    protected DialogSettings.THEME theme;
    protected BOOLEAN cancelable;
    
    protected TextInfo titleTextInfo;
    protected TextInfo messageTextInfo;
    protected TextInfo tipTextInfo;
    protected TextInfo buttonTextInfo;
    protected TextInfo buttonPositiveTextInfo;
    protected InputInfo inputInfo;
    protected int backgroundColor = 0;
    protected View customView;
    protected int backgroundResId = -1;
    
    protected OnDismissListener onDismissListener;
    protected OnDismissListener dismissEvent;
    protected OnShowListener onShowListener;
    
    public void log(Object o) {
        if (DialogSettings.DEBUGMODE) Log.i(">>>", o.toString());
    }
    
    public void error(Object o) {
        if (DialogSettings.DEBUGMODE) Log.e(">>>", o.toString());
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
    
    protected void showDialog() {
        showDialog(R.style.BaseDialog);
    }
    
    protected void showDialog(int style) {
        dismissedFlag = false;
        if (DialogSettings.dialogLifeCycleListener != null)
            DialogSettings.dialogLifeCycleListener.onCreate(this);
        isAlreadyShown = true;
        styleId = style;
        dismissEvent = new OnDismissListener() {
            @Override
            public void onDismiss() {
                log("# dismissEvent");
                dismissedFlag = true;
                isShow = false;
                dialogList.remove(baseDialog);
                if (!(baseDialog instanceof TipDialog)) showNext();
                if (onDismissListener != null) onDismissListener.onDismiss();
                if (DialogSettings.dialogLifeCycleListener != null)
                    DialogSettings.dialogLifeCycleListener.onDismiss(BaseDialog.this);
            }
        };
        dialogList.add(this);
        if (baseDialog instanceof TipDialog) {
            showNow();
        } else {
            showNext();
        }
    }
    
    protected void showNext() {
        List<BaseDialog> cache = new ArrayList<>();
        cache.addAll(BaseDialog.dialogList);
        for (BaseDialog dialog : cache) {
            if (dialog.context.get().isDestroyed()) {
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
        log("# showNow");
        isShow = true;
        if (context.get().isDestroyed()) {
            if (newContext.get() == null) {
                error("Context错误的指向了一个已被关闭的Activity或者Null，有可能是Activity因横竖屏切换被重启或者您手动执行了unload()方法，请确认其能够正确指向一个正在使用的Activity");
                return;
            }
            context = new WeakReference<>(newContext.get());
        }
        FragmentManager fragmentManager = context.get().getSupportFragmentManager();
        dialog = new DialogHelper().setLayoutId(baseDialog, layoutId);
        if (baseDialog instanceof BottomMenu || baseDialog instanceof ShareDialog) {
            styleId = R.style.BottomDialog;
        }
        if (DialogSettings.systemDialogStyle != 0) {
            styleId = DialogSettings.systemDialogStyle;
        }
        dialog.setStyle(DialogFragment.STYLE_NORMAL, styleId);
        dialog.show(fragmentManager, "kongzueDialog");
        dialog.setOnShowListener(new DialogHelper.PreviewOnShowListener() {
            @Override
            public void onShow(Dialog dialog) {
                if (DialogSettings.dialogLifeCycleListener != null)
                    DialogSettings.dialogLifeCycleListener.onShow(BaseDialog.this);
            }
        });
        if (DialogSettings.systemDialogStyle == 0 && style == DialogSettings.STYLE.STYLE_IOS && !(baseDialog instanceof TipDialog) && !(baseDialog instanceof BottomMenu) && !(baseDialog instanceof ShareDialog))
            dialog.setAnim(R.style.iOSDialogAnimStyle);
        
        if (baseDialog instanceof TipDialog) {
            if (cancelable == null)
                cancelable = DialogSettings.cancelableTipDialog ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        } else {
            if (cancelable == null)
                cancelable = DialogSettings.cancelable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        }
        if (dialog != null) {
            dialog.setCancelable(cancelable == BOOLEAN.TRUE);
        }
    }
    
    public abstract void bindView(View rootView);
    
    public abstract void refreshView();
    
    public abstract void show();
    
    protected boolean dismissedFlag = false;
    
    public void doDismiss() {
        dismissedFlag = true;
        dialog.dismiss();
    }
    
    protected void initDefaultSettings() {
        if (theme == null) theme = DialogSettings.theme;
        if (style == null) style = DialogSettings.style;
        if (backgroundColor == 0) backgroundColor = DialogSettings.backgroundColor;
        if (titleTextInfo == null) titleTextInfo = DialogSettings.titleTextInfo;
        if (messageTextInfo == null) messageTextInfo = DialogSettings.contentTextInfo;
        if (tipTextInfo == null) tipTextInfo = DialogSettings.tipTextInfo;
        if (buttonTextInfo == null) buttonTextInfo = DialogSettings.buttonTextInfo;
        if (inputInfo == null) inputInfo = DialogSettings.inputInfo;
        if (buttonPositiveTextInfo == null) {
            if (DialogSettings.buttonPositiveTextInfo == null) {
                buttonPositiveTextInfo = buttonTextInfo;
            } else {
                buttonPositiveTextInfo = DialogSettings.buttonPositiveTextInfo;
            }
        }
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
    
    //网络传输文本判空规则
    protected boolean isNull(String s) {
        if (s == null || s.trim().isEmpty() || s.equals("null") || s.equals("(null)")) {
            return true;
        }
        return false;
    }
    
    protected int dip2px(float dpValue) {
        final float scale = context.get().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
    //不放心或强迫症可用的卸载方法
    public static void unload() {
        for (BaseDialog dialog : dialogList) {
            if (dialog.isShow) {
                dialog.doDismiss();
            }
        }
        dialogList = new ArrayList<>();
        newContext = null;
        WaitDialog.waitDialogTemp = null;
    }
    
    public static int getSize() {
        return dialogList.size();
    }
}
