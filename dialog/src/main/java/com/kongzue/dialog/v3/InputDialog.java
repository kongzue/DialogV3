package com.kongzue.dialog.v3;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.interfaces.DialogLifeCycleListener;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnShowListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.InputInfo;
import com.kongzue.dialog.util.TextInfo;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/4/12 16:42
 */
public class InputDialog extends MessageDialog {
    
    private String inputText = "";
    private String hintText;
    
    private OnInputDialogButtonClickListener onOkButtonClickListener;
    private OnInputDialogButtonClickListener onCancelButtonClickListener;
    private OnInputDialogButtonClickListener onOtherButtonClickListener;
    
    private InputDialog() {
    }
    
    public static InputDialog build(@NonNull AppCompatActivity context) {
        synchronized (InputDialog.class) {
            InputDialog inputDialog = new InputDialog();
            inputDialog.log("装载输入对话框");
            inputDialog.context = new WeakReference<>(context);
            
            switch (inputDialog.style) {
                case STYLE_IOS:
                    inputDialog.build(inputDialog, R.layout.dialog_select_ios);
                    break;
                case STYLE_KONGZUE:
                    inputDialog.build(inputDialog, R.layout.dialog_select);
                    break;
                case STYLE_MATERIAL:
                    inputDialog.build(inputDialog);
                    break;
            }
            return inputDialog;
        }
    }
    
    public static InputDialog show(@NonNull AppCompatActivity context, String title, String message) {
        synchronized (TipDialog.class) {
            InputDialog inputDialog = show(context, title, message, null, null, null);
            return inputDialog;
        }
    }
    
    public static InputDialog show(@NonNull AppCompatActivity context, int titleResId, int messageResId) {
        synchronized (TipDialog.class) {
            InputDialog inputDialog = show(context,
                    context.getString(titleResId),
                    context.getString(messageResId),
                    null, null, null
            );
            return inputDialog;
        }
    }
    
    public static InputDialog show(@NonNull AppCompatActivity context, String title, String message, String okButton) {
        synchronized (TipDialog.class) {
            InputDialog inputDialog = show(context, title, message, okButton, null, null);
            return inputDialog;
        }
    }
    
    public static InputDialog show(@NonNull AppCompatActivity context, int titleResId, int messageResId, int okButtonResId) {
        synchronized (TipDialog.class) {
            InputDialog inputDialog = show(context,
                    context.getString(titleResId),
                    context.getString(messageResId),
                    context.getString(okButtonResId),
                    null, null
            );
            return inputDialog;
        }
    }
    
    public static InputDialog show(@NonNull AppCompatActivity context, String title, String message, String okButton, String cancelButton) {
        synchronized (TipDialog.class) {
            InputDialog inputDialog = show(context, title, message, okButton, cancelButton, null);
            return inputDialog;
        }
    }
    
    public static InputDialog show(@NonNull AppCompatActivity context, int titleResId, int messageResId, int okButtonResId, int cancelButtonResId) {
        synchronized (TipDialog.class) {
            InputDialog inputDialog = show(
                    context,
                    context.getString(titleResId),
                    context.getString(messageResId),
                    context.getString(okButtonResId),
                    context.getString(cancelButtonResId),
                    null
            );
            return inputDialog;
        }
    }
    
    public static InputDialog show(@NonNull AppCompatActivity context, String title, String message, String okButton, String cancelButton, String otherButton) {
        synchronized (TipDialog.class) {
            InputDialog inputDialog = build(context);
            
            inputDialog.title = title;
            if (okButton != null) inputDialog.okButton = okButton;
            inputDialog.message = message;
            inputDialog.cancelButton = cancelButton;
            inputDialog.otherButton = otherButton;
            
            inputDialog.showDialog();
            return inputDialog;
        }
    }
    
    public static InputDialog show(@NonNull AppCompatActivity context, int titleResId, int messageResId, int okButtonResId, int cancelButtonResId, int otherButtonResId) {
        synchronized (TipDialog.class) {
            InputDialog inputDialog = show(
                    context,
                    context.getString(titleResId),
                    context.getString(messageResId),
                    context.getString(okButtonResId),
                    context.getString(cancelButtonResId),
                    context.getString(otherButtonResId)
            );
            return inputDialog;
        }
    }
    
    private LinearLayout materialCustomViewBox;
    
    @Override
    public void refreshView() {
        super.refreshView();
        log("InputDialog:refreshView");
        if (style == DialogSettings.STYLE.STYLE_MATERIAL) {
            if (materialAlertDialog != null) {
                if (inputText != null) {
                    if (customView == null) {
                        txtInput = new EditText(context.get());
                        txtInput.setSingleLine();
                        txtInput.post(new Runnable() {
                            @Override
                            public void run() {
                                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) txtInput.getLayoutParams();
                                p.setMargins(dip2px(20), 0, dip2px(20), 0);
                                txtInput.requestLayout();
                            }
                        });
                        materialAlertDialog.setView(txtInput);
                    } else {
                        txtInput = new EditText(context.get());
                        txtInput.setSingleLine();
                        txtInput.post(new Runnable() {
                            @Override
                            public void run() {
                                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) txtInput.getLayoutParams();
                                p.setMargins(dip2px(20), 0, dip2px(20), 0);
                                txtInput.requestLayout();
                            }
                        });
                        
                        if (boxCustom != null) boxCustom.removeAllViews();
                        if (materialCustomViewBox != null) materialCustomViewBox.removeAllViews();
                        materialCustomViewBox = new LinearLayout(context.get());
                        materialCustomViewBox.setOrientation(LinearLayout.VERTICAL);
                        materialCustomViewBox.addView(customView);
                        materialCustomViewBox.addView(txtInput);
                        
                        if (onBindView != null) onBindView.onBind(this, materialCustomViewBox);
                        
                        materialAlertDialog.setView(materialCustomViewBox);
                    }
                }
                materialAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button positiveButton = materialAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (onOkButtonClickListener != null) {
                                    if (!onOkButtonClickListener.onClick(InputDialog.this, v, getInputText()))
                                        materialAlertDialog.dismiss();
                                } else {
                                    materialAlertDialog.dismiss();
                                }
                            }
                        });
                        useTextInfo(positiveButton, buttonPositiveTextInfo);
                        
                        Button negativeButton = materialAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        negativeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (onCancelButtonClickListener != null) {
                                    if (!onCancelButtonClickListener.onClick(InputDialog.this, v, getInputText()))
                                        materialAlertDialog.dismiss();
                                } else {
                                    materialAlertDialog.dismiss();
                                }
                            }
                        });
                        useTextInfo(negativeButton, buttonTextInfo);
                        
                        if (otherButton != null) {
                            Button otherButton = materialAlertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                            otherButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (onOtherButtonClickListener != null) {
                                        if (!onOtherButtonClickListener.onClick(InputDialog.this, v, getInputText()))
                                            materialAlertDialog.dismiss();
                                    } else {
                                        materialAlertDialog.dismiss();
                                    }
                                }
                            });
                            useTextInfo(otherButton, buttonTextInfo);
                        }
                        try {
                            Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
                            mAlert.setAccessible(true);
                            Object mAlertController = mAlert.get(dialog);
                            
                            if (titleTextInfo != null) {
                                Field mTitle = mAlertController.getClass().getDeclaredField("mTitleView");
                                mTitle.setAccessible(true);
                                TextView titleTextView = (TextView) mTitle.get(mAlertController);
                                useTextInfo(titleTextView, titleTextInfo);
                            }
                            
                            if (messageTextInfo != null) {
                                Field mMessage = mAlertController.getClass().getDeclaredField("mMessageView");
                                mMessage.setAccessible(true);
                                TextView messageTextView = (TextView) mMessage.get(mAlertController);
                                useTextInfo(messageTextView, messageTextInfo);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        
                    }
                });
            }
        } else {
            if (btnSelectPositive != null) {
                btnSelectPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onOkButtonClickListener != null) {
                            if (!onOkButtonClickListener.onClick(InputDialog.this, v, getInputText())) {
                                doDismiss();
                            }
                        } else {
                            doDismiss();
                        }
                    }
                });
            }
            if (btnSelectNegative != null) {
                btnSelectNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onCancelButtonClickListener != null) {
                            if (!onCancelButtonClickListener.onClick(InputDialog.this, v, getInputText())) {
                                doDismiss();
                            }
                        } else {
                            doDismiss();
                        }
                    }
                });
            }
            if (btnSelectOther != null) {
                btnSelectOther.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onOtherButtonClickListener != null) {
                            if (!onOtherButtonClickListener.onClick(InputDialog.this, v, getInputText())) {
                                doDismiss();
                            }
                        } else {
                            doDismiss();
                        }
                    }
                });
            }
            refreshTextViews();
        }
        
        if (inputText != null) {
            if (txtInput != null) {
                if (theme == DialogSettings.THEME.DARK) {
                    txtInput.setTextColor(Color.WHITE);
                    txtInput.setHintTextColor(context.get().getResources().getColor(R.color.whiteAlpha30));
                }
                txtInput.setText(inputText);
                txtInput.setHint(hintText);
                if (inputInfo != null) {
                    if (inputInfo.getMAX_LENGTH() != -1)
                        txtInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(inputInfo.getMAX_LENGTH())});
                    txtInput.setInputType(InputType.TYPE_CLASS_TEXT | inputInfo.getInputType());
                    if (inputInfo.getTextInfo() != null)
                        useTextInfo(txtInput, inputInfo.getTextInfo());
                }
                txtInput.setVisibility(View.VISIBLE);
            }
        } else {
            txtInput.setVisibility(View.GONE);
        }
    }
    
    @Override
    protected void refreshTextViews() {
        super.refreshTextViews();
        if (txtInput != null) {
            if (inputText != null) {
                if (theme == DialogSettings.THEME.DARK) {
                    txtInput.setTextColor(Color.WHITE);
                    txtInput.setHintTextColor(context.get().getResources().getColor(R.color.whiteAlpha30));
                }
                txtInput.setText(inputText);
                txtInput.setHint(hintText);
                if (inputInfo != null) {
                    if (inputInfo.getMAX_LENGTH() != -1)
                        txtInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(inputInfo.getMAX_LENGTH())});
                    txtInput.setInputType(InputType.TYPE_CLASS_TEXT | inputInfo.getInputType());
                    if (inputInfo.getTextInfo() != null)
                        useTextInfo(txtInput, inputInfo.getTextInfo());
                }
                txtInput.setVisibility(View.VISIBLE);
            } else {
                txtInput.setVisibility(View.GONE);
            }
        }
    }
    
    public String getInputText() {
        if (txtInput == null) {
            return inputText;
        } else {
            return txtInput.getText().toString();
        }
    }
    
    //其他设置
    public String getTitle() {
        return title;
    }
    
    public InputDialog setTitle(String title) {
        this.title = title;
        return this;
    }
    
    public InputDialog setTitle(int titleResId) {
        this.title = context.get().getString(titleResId);
        return this;
    }
    
    public String getMessage() {
        return message;
    }
    
    public InputDialog setMessage(String content) {
        this.message = content;
        return this;
    }
    
    public InputDialog setMessage(int contentResId) {
        this.message = context.get().getString(contentResId);
        return this;
    }
    
    public String getOkButton() {
        return okButton;
    }
    
    public InputDialog setOkButton(String okButton) {
        this.okButton = okButton;
        refreshView();
        return this;
    }
    
    public InputDialog setOkButton(int okButtonResId) {
        setOkButton(context.get().getString(okButtonResId));
        return this;
    }
    
    public InputDialog setOkButton(String okButton, OnInputDialogButtonClickListener onOkButtonClickListener) {
        this.okButton = okButton;
        this.onOkButtonClickListener = onOkButtonClickListener;
        refreshView();
        return this;
    }
    
    public InputDialog setOkButton(int okButtonResId, OnInputDialogButtonClickListener onOkButtonClickListener) {
        setOkButton(context.get().getString(okButtonResId), onOkButtonClickListener);
        return this;
    }
    
    public InputDialog setOkButton(OnInputDialogButtonClickListener onOkButtonClickListener) {
        this.onOkButtonClickListener = onOkButtonClickListener;
        refreshView();
        return this;
    }
    
    public String getCancelButton() {
        return cancelButton;
    }
    
    public InputDialog setCancelButton(String cancelButton) {
        this.cancelButton = cancelButton;
        refreshView();
        return this;
    }
    
    public InputDialog setCancelButton(int cancelButtonResId) {
        setCancelButton(context.get().getString(cancelButtonResId));
        return this;
    }
    
    public InputDialog setCancelButton(String cancelButton, OnInputDialogButtonClickListener onCancelButtonClickListener) {
        this.cancelButton = cancelButton;
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        refreshView();
        return this;
    }
    
    public InputDialog setCancelButton(int cancelButtonResId, OnInputDialogButtonClickListener onCancelButtonClickListener) {
        setCancelButton(context.get().getString(cancelButtonResId), onCancelButtonClickListener);
        return this;
    }
    
    public InputDialog setCancelButton(OnInputDialogButtonClickListener onCancelButtonClickListener) {
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        refreshView();
        return this;
    }
    
    public String getOtherButton() {
        return otherButton;
    }
    
    public InputDialog setOtherButton(String otherButton) {
        this.otherButton = otherButton;
        refreshView();
        return this;
    }
    
    public InputDialog setOtherButton(int otherButtonResId) {
        setCancelButton(context.get().getString(otherButtonResId));
        refreshView();
        return this;
    }
    
    public InputDialog setOtherButton(String otherButton, OnInputDialogButtonClickListener onOtherButtonClickListener) {
        this.otherButton = otherButton;
        this.onOtherButtonClickListener = onOtherButtonClickListener;
        refreshView();
        return this;
    }
    
    public InputDialog setOtherButton(int otherButtonResId, OnInputDialogButtonClickListener onOtherButtonClickListener) {
        setOtherButton(context.get().getString(otherButtonResId), onOtherButtonClickListener);
        return this;
    }
    
    public InputDialog setOtherButton(OnInputDialogButtonClickListener onOtherButtonClickListener) {
        this.onOtherButtonClickListener = onOtherButtonClickListener;
        refreshView();
        return this;
    }
    
    public OnInputDialogButtonClickListener getOnInputOkButtonClickListener() {
        return onOkButtonClickListener;
    }
    
    @Deprecated
    public OnDialogButtonClickListener getOnOkButtonClickListener() {
        error("请使用 getOnInputOkButtonClickListener() 获取 onOkButtonClickListener");
        return null;
    }
    
    public InputDialog setOnOkButtonClickListener(OnInputDialogButtonClickListener onOkButtonClickListener) {
        this.onOkButtonClickListener = onOkButtonClickListener;
        refreshView();
        return this;
    }
    
    public OnInputDialogButtonClickListener getOnInputCancelButtonClickListener() {
        return onCancelButtonClickListener;
    }
    
    @Deprecated
    public OnDialogButtonClickListener getOnCancelButtonClickListener() {
        error("请使用 getOnInputCancelButtonClickListener() 获取 onCancelButtonClickListener");
        return null;
    }
    
    public InputDialog setOnCancelButtonClickListener(OnInputDialogButtonClickListener onCancelButtonClickListener) {
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        refreshView();
        return this;
    }
    
    public OnInputDialogButtonClickListener getOnInputOtherButtonClickListener() {
        return onOtherButtonClickListener;
    }
    
    @Deprecated
    public OnDialogButtonClickListener getOnOtherButtonClickListener() {
        error("请使用 getOnInputOtherButtonClickListener() 获取 onOtherButtonClickListener");
        return null;
    }
    
    public InputDialog setOnOtherButtonClickListener(OnInputDialogButtonClickListener onOtherButtonClickListener) {
        this.onOtherButtonClickListener = onOtherButtonClickListener;
        refreshView();
        return this;
    }
    
    public InputDialog setOkButtonDrawable(@DrawableRes int okButtonDrawableResId) {
        this.okButtonDrawable = ContextCompat.getDrawable(context.get(), okButtonDrawableResId);
        refreshView();
        return this;
    }
    
    public InputDialog setOkButtonDrawable(Drawable okButtonDrawable) {
        this.okButtonDrawable = okButtonDrawable;
        refreshView();
        return this;
    }
    
    public InputDialog setCancelButtonDrawable(@DrawableRes int okButtonDrawableResId) {
        this.cancelButtonDrawable = ContextCompat.getDrawable(context.get(), okButtonDrawableResId);
        refreshView();
        return this;
    }
    
    public InputDialog setCancelButtonDrawable(Drawable cancelButtonDrawable) {
        this.cancelButtonDrawable = cancelButtonDrawable;
        refreshView();
        return this;
    }
    
    public InputDialog setOtherButtonDrawable(@DrawableRes int okButtonDrawableResId) {
        this.otherButtonDrawable = ContextCompat.getDrawable(context.get(), okButtonDrawableResId);
        refreshView();
        return this;
    }
    
    public InputDialog setOtherButtonDrawable(Drawable otherButtonDrawable) {
        this.otherButtonDrawable = otherButtonDrawable;
        refreshView();
        return this;
    }
    
    public int getButtonOrientation() {
        return buttonOrientation;
    }
    
    public InputDialog setButtonOrientation(int buttonOrientation) {
        this.buttonOrientation = buttonOrientation;
        refreshView();
        return this;
    }
    
    //其他
    public OnDismissListener getOnDismissListener() {
        return onDismissListener == null ? new OnDismissListener() {
            @Override
            public void onDismiss() {
            
            }
        } : onDismissListener;
    }
    
    public InputDialog setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }
    
    public OnShowListener getOnShowListener() {
        return onShowListener == null ? new OnShowListener() {
            @Override
            public void onShow(BaseDialog dialog) {
            
            }
        } : onShowListener;
    }
    
    public InputDialog setOnShowListener(OnShowListener onShowListener) {
        this.onShowListener = onShowListener;
        return this;
    }
    
    public DialogSettings.STYLE getStyle() {
        return style;
    }
    
    public InputDialog setStyle(DialogSettings.STYLE style) {
        if (isAlreadyShown) {
            error("必须使用 build(...) 方法创建时，才可以使用 setStyle(...) 来修改对话框主题或风格。");
            return this;
        }
        
        this.style = style;
        switch (this.style) {
            case STYLE_IOS:
                build(this, R.layout.dialog_select_ios);
                break;
            case STYLE_KONGZUE:
                build(this, R.layout.dialog_select);
                break;
            case STYLE_MATERIAL:
                
                break;
        }
        
        return this;
    }
    
    public DialogSettings.THEME getTheme() {
        return theme;
    }
    
    public InputDialog setTheme(DialogSettings.THEME theme) {
        
        if (isAlreadyShown) {
            error("必须使用 build(...) 方法创建时，才可以使用 setTheme(...) 来修改对话框主题或风格。");
            return this;
        }
        
        this.theme = theme;
        refreshView();
        return this;
    }
    
    public boolean getCancelable() {
        return cancelable == BOOLEAN.TRUE;
    }
    
    public InputDialog setCancelable(boolean enable) {
        this.cancelable = enable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        if (dialog != null) dialog.setCancelable(cancelable == BOOLEAN.TRUE);
        return this;
    }
    
    
    public TextInfo getTitleTextInfo() {
        return titleTextInfo;
    }
    
    public InputDialog setTitleTextInfo(TextInfo titleTextInfo) {
        this.titleTextInfo = titleTextInfo;
        refreshView();
        return this;
    }
    
    public TextInfo getMessageTextInfo() {
        return messageTextInfo;
    }
    
    public InputDialog setMessageTextInfo(TextInfo messageTextInfo) {
        this.messageTextInfo = messageTextInfo;
        refreshView();
        return this;
    }
    
    public TextInfo getButtonTextInfo() {
        return buttonTextInfo;
    }
    
    public InputDialog setButtonTextInfo(TextInfo buttonTextInfo) {
        this.buttonTextInfo = buttonTextInfo;
        refreshView();
        return this;
    }
    
    public TextInfo getButtonPositiveTextInfo() {
        return buttonPositiveTextInfo;
    }
    
    public InputDialog setButtonPositiveTextInfo(TextInfo buttonPositiveTextInfo) {
        this.buttonPositiveTextInfo = buttonPositiveTextInfo;
        refreshView();
        return this;
    }
    
    public int getBackgroundColor() {
        return backgroundColor;
    }
    
    public InputDialog setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        refreshView();
        return this;
    }
    
    public InputDialog setInputText(String inputText) {
        this.inputText = inputText;
        refreshView();
        return this;
    }
    
    public InputDialog setInputText(int inputTextResId) {
        this.inputText = context.get().getString(inputTextResId);
        refreshView();
        return this;
    }
    
    public String getHintText() {
        return hintText;
    }
    
    public InputDialog setHintText(String hintText) {
        this.hintText = hintText;
        refreshView();
        return this;
    }
    
    public InputDialog setHintText(int hintTextResId) {
        this.hintText = context.get().getString(hintTextResId);
        refreshView();
        return this;
    }
    
    public InputInfo getInputInfo() {
        return inputInfo;
    }
    
    public InputDialog setInputInfo(InputInfo inputInfo) {
        this.inputInfo = inputInfo;
        refreshView();
        return this;
    }
    
    public View getCustomView() {
        return customView;
    }
    
    public InputDialog setCustomView(View customView) {
        this.customView = customView;
        refreshView();
        return this;
    }
    
    private OnBindView onBindView;
    
    public InputDialog setCustomView(int customViewLayoutId, OnBindView onBindView) {
        customView = LayoutInflater.from(context.get()).inflate(customViewLayoutId, null);
        this.onBindView = onBindView;
        refreshView();
        return this;
    }
    
    public interface OnBindView {
        void onBind(InputDialog dialog, View v);
    }
    
    public EditText getEditTextView() {
        return txtInput;
    }
    
    public int getBackgroundResId() {
        return backgroundResId;
    }
    
    public InputDialog setBackgroundResId(int backgroundResId) {
        this.backgroundResId = backgroundResId;
        refreshView();
        return this;
    }
}
