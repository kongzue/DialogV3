package com.kongzue.dialog.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kongzue.dialog.R;
import com.kongzue.dialog.v3.FullScreenDialog;
import com.kongzue.dialog.v3.BottomMenu;
import com.kongzue.dialog.v3.CustomDialog;
import com.kongzue.dialog.v3.ShareDialog;
import com.kongzue.dialog.v3.TipDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/3/22 16:24
 */
public class DialogHelper extends DialogFragment {
    
    private Dialog rootDialog;
    
    private PreviewOnShowListener onShowListener;
    private AlertDialog materialDialog;
    
    private WeakReference<BaseDialog> parent;
    
    private int layoutId;
    private View rootView;
    private String parentId;
    private int styleId;
    private int animResId;
    
    public DialogHelper() {
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (layoutId == -1) {
            if (onShowListener != null) onShowListener.onShow(getDialog());
            findMyParentAndBindView(null);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        if (animResId != 0) getDialog().getWindow().setWindowAnimations(animResId);
        rootView = inflater.inflate(layoutId, null);
        if (onShowListener != null) onShowListener.onShow(getDialog());
        findMyParentAndBindView(rootView);
        return rootView;
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (layoutId == -1) {
            materialDialog = new AlertDialog.Builder(getActivity(), styleId)
                    .setTitle("")
                    .setMessage("")
                    .setPositiveButton("", new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    })
                    .create();
            return materialDialog;
        }
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        refreshDialogPosition(dialog);
        return dialog;
    }
    
    private void refreshDialogPosition(Dialog dialog) {
        if (dialog != null && parent != null) {
            if (parent.get() instanceof BottomMenu || parent.get() instanceof ShareDialog) {
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                Window window = dialog.getWindow();
                window.getDecorView().setPadding(0, 0, 0, 0);
                WindowManager windowManager = getActivity().getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = display.getWidth();
                lp.windowAnimations = R.style.bottomMenuAnim;
                window.setGravity(Gravity.BOTTOM);
                window.setWindowAnimations(R.style.bottomMenuAnim);
                window.setAttributes(lp);
            }
            if (parent.get() instanceof FullScreenDialog) {
                dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                Window window = dialog.getWindow();
                window.getDecorView().setPadding(0, 0, 0, 0);
                WindowManager windowManager = getActivity().getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = display.getWidth();
                lp.windowAnimations = R.style.dialogNoAnim;
                window.setGravity(Gravity.BOTTOM);
                window.setWindowAnimations(R.style.dialogNoAnim);
                window.setAttributes(lp);
            }
            if (parent.get() instanceof CustomDialog) {
                CustomDialog customDialog = (CustomDialog) parent.get();
                
                if (customDialog.isFullScreen()) {
                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    Window window = dialog.getWindow();
                    window.getDecorView().setPadding(0, 0, 0, 0);
                    WindowManager windowManager = getActivity().getWindowManager();
                    Display display = windowManager.getDefaultDisplay();
                    WindowManager.LayoutParams lp = window.getAttributes();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        lp.layoutInDisplayCutoutMode = LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                    }
                    lp.width = display.getWidth();
                    lp.height = display.getHeight();
                    window.setAttributes(lp);
                }
                Window window;
                WindowManager windowManager;
                Display display;
                WindowManager.LayoutParams lp;
                if (customDialog.getAlign() != null) {
                    switch (customDialog.getAlign()) {
                        case BOTTOM:
                            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            window = dialog.getWindow();
                            window.getDecorView().setPadding(0, 0, 0, 0);
                            windowManager = getActivity().getWindowManager();
                            display = windowManager.getDefaultDisplay();
                            lp = window.getAttributes();
                            lp.width = display.getWidth();
                            lp.windowAnimations = R.style.bottomMenuAnim;
                            window.setGravity(Gravity.BOTTOM);
                            window.setWindowAnimations(R.style.bottomMenuAnim);
                            window.setAttributes(lp);
                            break;
                        case TOP:
                            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            window = dialog.getWindow();
                            window.getDecorView().setPadding(0, 0, 0, 0);
                            windowManager = getActivity().getWindowManager();
                            display = windowManager.getDefaultDisplay();
                            lp = window.getAttributes();
                            lp.width = display.getWidth();
                            lp.windowAnimations = R.style.topMenuAnim;
                            window.setGravity(Gravity.TOP);
                            window.setWindowAnimations(R.style.topMenuAnim);
                            window.setAttributes(lp);
                            break;
                    }
                }
            }
        }
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
            
        }
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("layoutId", layoutId);
        outState.putString("parentId", parentId);
        super.onSaveInstanceState(outState);
    }
    
    //找爸爸行动
    private void findMyParentAndBindView(View rootView) {
        List<BaseDialog> cache = new ArrayList<>();
        cache.addAll(BaseDialog.dialogList);
        BaseDialog.newContext = new WeakReference<>((AppCompatActivity) getContext());
        for (BaseDialog baseDialog : cache) {
            baseDialog.context = new WeakReference<>((AppCompatActivity) getContext());
            if (baseDialog.toString().equals(parentId)) {
                parent = new WeakReference<>(baseDialog);
                parent.get().dialog = new WeakReference<>(this);
                refreshDialogPosition(getDialog());
                parent.get().bindView(rootView);
                parent.get().initDefaultSettings();
            }
        }
    }
    
    private boolean findMyParent() {
        boolean flag = false;
        List<BaseDialog> cache = new ArrayList<>();
        cache.addAll(BaseDialog.dialogList);
        BaseDialog.newContext = new WeakReference<>((AppCompatActivity) getContext());
        for (BaseDialog baseDialog : cache) {
            baseDialog.context = new WeakReference<>((AppCompatActivity) getContext());
            if (baseDialog.toString().equals(parentId)) {
                flag = true;
                parent = new WeakReference<>(baseDialog);
                parent.get().dialog = new WeakReference<>(this);
                refreshDialogPosition(getDialog());
            }
        }
        return flag;
    }
    
    @Override
    public void onDismiss(DialogInterface dialog) {
        if (parent == null || parent.get() == null) {
            if (!findMyParent()) {
                return;
            }
        }
        if (parent != null && parent.get().dismissEvent != null) {
            parent.get().dismissEvent.onDismiss();
        }
        super.onDismiss(dialog);
        parent.clear();
        parent = null;
    }
    
    public int getLayoutId() {
        return layoutId;
    }
    
    public DialogHelper setLayoutId(BaseDialog baseDialog, int layoutId) {
        this.layoutId = layoutId;
        this.parent = new WeakReference<>(baseDialog);
        this.parentId = baseDialog.toString();
        return this;
    }
    
    public PreviewOnShowListener getOnShowListener() {
        return onShowListener;
    }
    
    public void setOnShowListener(PreviewOnShowListener onShowListener) {
        this.onShowListener = onShowListener;
    }
    
    @Override
    public void dismiss() {
        try {
            super.dismissAllowingStateLoss();
        } catch (Exception e) {
        }
    }
    
    @Override
    public void setStyle(int style, int theme) {
        styleId = theme;
        super.setStyle(style, theme);
    }
    
    public void setAnim(int animResId) {
        this.animResId = animResId;
    }
    
    @Override
    public void onPause() {
        super.onPause();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (parent == null || parent.get() == null) {
            if (!findMyParent()) {
                return;
            }
        }
        if (parent != null) {
            if (parent.get() instanceof TipDialog) {
                if (parent.get().dismissedFlag) {
                    if (getDialog() != null) if (getDialog().isShowing()) getDialog().dismiss();
                    if (parent.get().dismissEvent != null) parent.get().dismissEvent.onDismiss();
                }
            } else {
                if (parent.get().dismissedFlag) {
                    dismiss();
                }
            }
        }
    }
    
    @Override
    public void onStart() {
        super.onStart();
    }
    
    public interface PreviewOnShowListener {
        void onShow(Dialog dialog);
    }
}