package com.kongzue.dialog.util;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kongzue.dialog.R;
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
 * CreateTime: 2019/3/22 16:24
 */
public class DialogHelper extends DialogFragment {
    
    private OnDismissListener onDismissListener;
    private OnDialogShowListener onDialogShowListener;
    private boolean isRestartDialog = false;
    
    private int layoutId;
    private View rootView;
    private String parentId;
    private int styleId;
    
    public DialogHelper() {
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (layoutId == -1) {
            if (onDialogShowListener != null) onDialogShowListener.onShow(getDialog());
            findMyParentAndBindView(null);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        rootView = inflater.inflate(layoutId, null);
        if (onDialogShowListener != null) onDialogShowListener.onShow(getDialog());
        findMyParentAndBindView(rootView);
        return rootView;
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (layoutId == -1) {
            return new AlertDialog.Builder(getActivity(), styleId).setTitle("Title").setMessage("are you ok?")
                    .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    }).setNegativeButton("cancel", null)
                    .create();
        }
        return super.onCreateDialog(savedInstanceState);
    }
    
    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (getDialog() == null) {
            setShowsDialog(false);
        }
        super.onActivityCreated(savedInstanceState);
    }
    
    //parentId：因不可抗力，诸如横竖屏切换、分屏、折叠屏切换等可能造成DialogFragment重启，为保证重启后功能正常运行，因此需要保存自己爹（BaseDialog）的id，方便重新绑定到BaseDialog
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            layoutId = savedInstanceState.getInt("layoutId");
            parentId = savedInstanceState.getString("parentId");
            
            isRestartDialog = false;
        }
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("layoutId", layoutId);
        outState.putString("parentId", parentId);
        isRestartDialog = true;
        super.onSaveInstanceState(outState);
    }
    
    //找爸爸行动
    private void findMyParentAndBindView(View rootView) {
        List<BaseDialog> cache = new ArrayList<>();
        cache.addAll(BaseDialog.dialogList);
        BaseDialog.newContext = (AppCompatActivity) getContext();
        for (BaseDialog baseDialog : cache) {
            baseDialog.context = (AppCompatActivity) getContext();
            if (baseDialog.toString().equals(parentId)) {
                baseDialog.dialog = this;
                baseDialog.bindView(rootView);
                baseDialog.initDefaultSettings();
                setOnDismissListener(baseDialog.dismissEvent);
            }
        }
    }
    
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null && !isRestartDialog) onDismissListener.onDismiss();
    }
    
    public int getLayoutId() {
        return layoutId;
    }
    
    public DialogHelper setLayoutId(BaseDialog baseDialog, int layoutId) {
        this.layoutId = layoutId;
        this.parentId = baseDialog.toString();
        return this;
    }
    
    public OnDismissListener getOnDismissListener() {
        return onDismissListener;
    }
    
    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
    
    public OnDialogShowListener getOnDialogShowListener() {
        return onDialogShowListener;
    }
    
    public void setOnDialogShowListener(OnDialogShowListener onDialogShowListener) {
        this.onDialogShowListener = onDialogShowListener;
    }
    
    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
        }
    }
    
    @Override
    public void setStyle(int style, int theme) {
        styleId = theme;
        super.setStyle(style, theme);
    }
}
