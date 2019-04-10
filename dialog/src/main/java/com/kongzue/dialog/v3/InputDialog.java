package com.kongzue.dialog.v3;

import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.InputInfo;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.interfaces.DialogLifeCycleListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.util.view.BlurView;

import java.lang.reflect.Field;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_NEUTRAL;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import static com.kongzue.dialog.util.DialogSettings.blurAlpha;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/4/8 19:39
 */
public class InputDialog extends BaseDialog {
    
    private int buttonOrientation;
    
    private OnInputDialogButtonClickListener onOkButtonClickListener;
    private OnInputDialogButtonClickListener onCancelButtonClickListener;
    private OnInputDialogButtonClickListener onOtherButtonClickListener;
    
    private Drawable okButtonDrawable;
    private Drawable cancelButtonDrawable;
    private Drawable otherButtonDrawable;
    
    private String title = "提示";
    private String message = "提示信息";
    private String okButton = "确定";
    private String cancelButton = "取消";
    private String otherButton;
    private String inputText = "";
    private String hintText;
    
    private BlurView blurView;
    
    private RelativeLayout bkg;
    private TextView txtDialogTitle;
    private TextView txtDialogTip;
    private RelativeLayout boxCustom;
    private EditText txtInput;
    private ImageView splitHorizontal;
    private LinearLayout boxButton;
    private TextView btnSelectNegative;
    private ImageView splitVertical1;
    private TextView btnSelectOther;
    private ImageView splitVertical2;
    private TextView btnSelectPositive;
    
    public static InputDialog build(@NonNull AppCompatActivity context) {
        synchronized (InputDialog.class) {
            InputDialog inputDialog = new InputDialog();
            inputDialog.log("装载输入对话框");
            inputDialog.context = context;
            
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
    
    public static InputDialog show(@NonNull AppCompatActivity context, String title, String message, String okButton) {
        synchronized (TipDialog.class) {
            InputDialog inputDialog = show(context, title, message, okButton, null, null);
            return inputDialog;
        }
    }
    
    public static InputDialog show(@NonNull AppCompatActivity context, String title, String message, String okButton, String cancelButton) {
        synchronized (TipDialog.class) {
            InputDialog inputDialog = show(context, title, message, okButton, cancelButton, null);
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
    
    protected AlertDialog materialAlertDialog;
    private View rootView;
    
    @Override
    public void bindView(View rootView) {
        if (style == DialogSettings.STYLE.STYLE_MATERIAL) {
            materialAlertDialog = (AlertDialog) dialog.getDialog();
        } else {
            if (rootView != null) {
                this.rootView = rootView;
                bkg = rootView.findViewById(R.id.bkg);
                txtDialogTitle = rootView.findViewById(R.id.txt_dialog_title);
                txtDialogTip = rootView.findViewById(R.id.txt_dialog_tip);
                boxCustom = rootView.findViewById(R.id.box_custom);
                txtInput = rootView.findViewById(R.id.txt_input);
                splitHorizontal = rootView.findViewById(R.id.split_horizontal);
                boxButton = rootView.findViewById(R.id.box_button);
                btnSelectNegative = rootView.findViewById(R.id.btn_selectNegative);
                splitVertical1 = rootView.findViewById(R.id.split_vertical1);
                btnSelectOther = rootView.findViewById(R.id.btn_selectOther);
                splitVertical2 = rootView.findViewById(R.id.split_vertical2);
                btnSelectPositive = rootView.findViewById(R.id.btn_selectPositive);
            }
        }
        
        refreshView();
    }
    
    protected void refreshView() {
        if (txtDialogTitle != null) {
            if (title == null) {
                txtDialogTitle.setVisibility(View.GONE);
            } else {
                txtDialogTitle.setVisibility(View.VISIBLE);
                txtDialogTitle.setText(title);
            }
        }
        if (txtDialogTip != null) {
            if (message == null) {
                txtDialogTip.setVisibility(View.GONE);
            } else {
                txtDialogTip.setVisibility(View.VISIBLE);
                txtDialogTip.setText(message);
            }
        }
        
        if (rootView != null || materialAlertDialog != null) {
            final int bkgResId, blurFrontColor;
            switch (style) {
                case STYLE_IOS:
                    if (theme == DialogSettings.THEME.LIGHT) {
                        bkgResId = R.drawable.rect_selectdialog_ios_bkg_light;
                        blurFrontColor = Color.argb(blurAlpha, 255, 255, 255);
                    } else {
                        bkgResId = R.drawable.rect_selectdialog_ios_bkg_dark;
                        blurFrontColor = Color.argb(blurAlpha, 0, 0, 0);
                        txtDialogTitle.setTextColor(Color.WHITE);
                        txtDialogTip.setTextColor(Color.WHITE);
                        splitHorizontal.setBackgroundColor(context.getResources().getColor(R.color.dialogSplitIOSDark));
                        splitVertical1.setBackgroundColor(context.getResources().getColor(R.color.dialogSplitIOSDark));
                        splitVertical2.setBackgroundColor(context.getResources().getColor(R.color.dialogSplitIOSDark));
                        txtInput.setBackgroundResource(R.drawable.editbox_dialog_bkg_ios_dark);
                    }
                    if (DialogSettings.isUseBlur) {
                        bkg.post(new Runnable() {
                            @Override
                            public void run() {
                                blurView = new BlurView(context, null);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, bkg.getHeight());
                                blurView.setOverlayColor(blurFrontColor);
                                bkg.addView(blurView, 0, params);
                            }
                        });
                    } else {
                        bkg.setBackgroundResource(bkgResId);
                    }
                    break;
                case STYLE_KONGZUE:
                    if (theme == DialogSettings.THEME.DARK) {
                        bkg.setBackgroundResource(R.color.dialogBkgDark);
                        boxButton.setBackgroundColor(Color.TRANSPARENT);
                        btnSelectNegative.setBackgroundResource(R.drawable.button_selectdialog_kongzue_gray_dark);
                        btnSelectOther.setBackgroundResource(R.drawable.button_selectdialog_kongzue_gray_dark);
                        btnSelectPositive.setBackgroundResource(R.drawable.button_selectdialog_kongzue_blue_dark);
                        btnSelectNegative.setTextColor(Color.rgb(255, 255, 255));
                        btnSelectPositive.setTextColor(Color.rgb(255, 255, 255));
                        btnSelectOther.setTextColor(Color.rgb(255, 255, 255));
                        txtDialogTitle.setTextColor(Color.WHITE);
                        txtDialogTip.setTextColor(Color.WHITE);
                    } else {
                        bkg.setBackgroundResource(R.color.white);
                        txtDialogTitle.setTextColor(Color.BLACK);
                        txtDialogTip.setTextColor(Color.BLACK);
                    }
                    
                    if (backgroundColor != 0) {
                        bkg.setBackgroundColor(backgroundColor);
                    }
                    break;
                case STYLE_MATERIAL:
                    materialAlertDialog.setTitle(title);
                    if (backgroundColor != 0)
                        materialAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(backgroundColor));
                    materialAlertDialog.setMessage(message);
                    materialAlertDialog.setButton(BUTTON_POSITIVE, okButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            
                        }
                    });
                    if (cancelButton != null) {
                        materialAlertDialog.setButton(BUTTON_NEGATIVE, cancelButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            
                            }
                        });
                    }
                    if (otherButton != null) {
                        materialAlertDialog.setButton(BUTTON_NEUTRAL, otherButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                
                            }
                        });
                    }
                    
                    if (inputText != null) {
                        txtInput = new EditText(context);
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
                    }
                    
                    materialAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            Button positiveButton = materialAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            positiveButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (onOkButtonClickListener != null) {
                                        if (!onOkButtonClickListener.onClick(v, getInputText()))
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
                                        if (!onCancelButtonClickListener.onClick(v, getInputText()))
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
                                            if (!onOtherButtonClickListener.onClick(v, getInputText()))
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
                    break;
            }
            refreshTextViews();
        }
        
        if (btnSelectPositive != null) {
            btnSelectPositive.setText(okButton);
            if (okButtonDrawable != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    btnSelectPositive.setBackground(okButtonDrawable);
                } else {
                    btnSelectPositive.setBackgroundDrawable(okButtonDrawable);
                }
            }
            
            btnSelectPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onOkButtonClickListener != null) {
                        if (!onOkButtonClickListener.onClick(v, getInputText())) {
                            doDismiss();
                        }
                    } else {
                        doDismiss();
                    }
                }
            });
        }
        if (btnSelectNegative != null) {
            if (isNull(cancelButton)) {
                btnSelectNegative.setVisibility(View.GONE);
                if (style == DialogSettings.STYLE.STYLE_IOS) {
                    btnSelectPositive.setBackgroundResource(R.drawable.button_menu_ios_bottom);
                }
            } else {
                btnSelectNegative.setText(cancelButton);
                if (cancelButtonDrawable != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        btnSelectNegative.setBackground(cancelButtonDrawable);
                    } else {
                        btnSelectNegative.setBackgroundDrawable(cancelButtonDrawable);
                    }
                }
                
                btnSelectNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onCancelButtonClickListener != null) {
                            if (!onCancelButtonClickListener.onClick(v, getInputText())) {
                                doDismiss();
                            }
                        } else {
                            doDismiss();
                        }
                    }
                });
            }
        }
        if (btnSelectOther != null) {
            if (!isNull(otherButton)) {
                if (splitVertical1 != null) splitVertical1.setVisibility(View.VISIBLE);
                btnSelectOther.setVisibility(View.VISIBLE);
                btnSelectOther.setText(otherButton);
            }
            if (otherButtonDrawable != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    btnSelectOther.setBackground(otherButtonDrawable);
                } else {
                    btnSelectOther.setBackgroundDrawable(otherButtonDrawable);
                }
            }
            
            btnSelectOther.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onOtherButtonClickListener != null) {
                        if (!onOtherButtonClickListener.onClick(v, getInputText())) {
                            doDismiss();
                        }
                    } else {
                        doDismiss();
                    }
                }
            });
        }
        if (boxButton != null) {
            boxButton.setOrientation(buttonOrientation);
            if (buttonOrientation == LinearLayout.VERTICAL) {
                boxButton.removeAllViews();
                
                if (style == DialogSettings.STYLE.STYLE_IOS) {
                    boxButton.addView(btnSelectPositive);
                    boxButton.addView(splitVertical2);
                    boxButton.addView(btnSelectNegative);
                    boxButton.addView(splitVertical1);
                    boxButton.addView(btnSelectOther);
                    
                    if (okButtonDrawable == null && cancelButtonDrawable == null && otherButtonDrawable == null) {
                        btnSelectPositive.setBackgroundResource(R.drawable.button_menu_ios_center);
                        if (btnSelectOther.getVisibility() == View.GONE) {
                            btnSelectNegative.setBackgroundResource(R.drawable.button_menu_ios_bottom);
                        } else {
                            btnSelectNegative.setBackgroundResource(R.drawable.button_menu_ios_center);
                            btnSelectOther.setBackgroundResource(R.drawable.button_menu_ios_bottom);
                        }
                    }
                    
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
                    splitVertical1.setLayoutParams(lp);
                    splitVertical2.setLayoutParams(lp);
                } else {
                    boxButton.addView(btnSelectPositive);
                    boxButton.addView(btnSelectNegative);
                    boxButton.addView(btnSelectOther);
                    
                    if (okButtonDrawable == null && cancelButtonDrawable == null && otherButtonDrawable == null && theme == DialogSettings.THEME.LIGHT) {
                        btnSelectPositive.setBackgroundResource(R.drawable.button_selectdialog_kongzue_white);
                        btnSelectNegative.setBackgroundResource(R.drawable.button_selectdialog_kongzue_white);
                        btnSelectOther.setBackgroundResource(R.drawable.button_selectdialog_kongzue_white);
                    }
                    
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) btnSelectOther.getLayoutParams();
                    lp.setMargins(0, 1, 0, 0);
                    btnSelectOther.setLayoutParams(lp);
                    btnSelectNegative.setLayoutParams(lp);
                    btnSelectPositive.setLayoutParams(lp);
                }
                
            }
        }
    }
    
    private void refreshTextViews() {
        useTextInfo(txtDialogTitle, titleTextInfo);
        useTextInfo(txtDialogTip, messageTextInfo);
        useTextInfo(btnSelectNegative, buttonTextInfo);
        useTextInfo(btnSelectOther, buttonTextInfo);
        useTextInfo(btnSelectPositive, buttonPositiveTextInfo);
        if (inputText != null) {
            if (txtInput != null) {
                if (theme == DialogSettings.THEME.DARK) {
                    txtInput.setTextColor(Color.WHITE);
                    txtInput.setHintTextColor(context.getResources().getColor(R.color.whiteAlpha30));
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
    public void showDialog() {
        log("启动输入对话框");
        if (style == DialogSettings.STYLE.STYLE_IOS) {
            super.showDialog();
        } else if (style == DialogSettings.STYLE.STYLE_MATERIAL) {
            if (theme == DialogSettings.THEME.LIGHT) {
                super.showDialog(R.style.LightDialogWithShadow);
            } else {
                super.showDialog(R.style.DarkDialogWithShadow);
            }
        } else {
            super.showDialog(R.style.LightDialogWithShadow);
        }
    }
    
    public String getTitle() {
        return title;
    }
    
    public InputDialog setTitle(String title) {
        this.title = title;
        return this;
    }
    
    public String getMessage() {
        return message;
    }
    
    public InputDialog setMessage(String content) {
        this.message = content;
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
    
    public InputDialog setOkButton(String okButton, OnInputDialogButtonClickListener onOkButtonClickListener) {
        this.okButton = okButton;
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
    
    public InputDialog setCancelButton(String cancelButton, OnInputDialogButtonClickListener onCancelButtonClickListener) {
        this.cancelButton = cancelButton;
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
    
    public InputDialog setOtherButton(String otherButton, OnInputDialogButtonClickListener onOtherButtonClickListener) {
        this.otherButton = otherButton;
        this.onOtherButtonClickListener = onOtherButtonClickListener;
        refreshView();
        return this;
    }
    
    public OnInputDialogButtonClickListener getOnOkButtonClickListener() {
        return onOkButtonClickListener;
    }
    
    public InputDialog setOnOkButtonClickListener(OnInputDialogButtonClickListener onOkButtonClickListener) {
        this.onOkButtonClickListener = onOkButtonClickListener;
        refreshView();
        return this;
    }
    
    public OnInputDialogButtonClickListener getOnCancelButtonClickListener() {
        return onCancelButtonClickListener;
    }
    
    public InputDialog setOnCancelButtonClickListener(OnInputDialogButtonClickListener onCancelButtonClickListener) {
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        refreshView();
        return this;
    }
    
    public OnInputDialogButtonClickListener getOnOtherButtonClickListener() {
        return onOtherButtonClickListener;
    }
    
    public InputDialog setOnOtherButtonClickListener(OnInputDialogButtonClickListener onOtherButtonClickListener) {
        this.onOtherButtonClickListener = onOtherButtonClickListener;
        refreshView();
        return this;
    }
    
    public InputDialog setOkButtonDrawable(@DrawableRes int okButtonDrawableResId) {
        this.okButtonDrawable = ContextCompat.getDrawable(context, okButtonDrawableResId);
        refreshView();
        return this;
    }
    
    public InputDialog setOkButtonDrawable(Drawable okButtonDrawable) {
        this.okButtonDrawable = okButtonDrawable;
        refreshView();
        return this;
    }
    
    public InputDialog setCancelButtonDrawable(@DrawableRes int okButtonDrawableResId) {
        this.cancelButtonDrawable = ContextCompat.getDrawable(context, okButtonDrawableResId);
        refreshView();
        return this;
    }
    
    public InputDialog setCancelButtonDrawable(Drawable cancelButtonDrawable) {
        this.cancelButtonDrawable = cancelButtonDrawable;
        refreshView();
        return this;
    }
    
    public InputDialog setOtherButtonDrawable(@DrawableRes int okButtonDrawableResId) {
        this.otherButtonDrawable = ContextCompat.getDrawable(context, okButtonDrawableResId);
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
    
    public InputDialog setButtonOrientation(@LinearLayoutCompat.OrientationMode int buttonOrientation) {
        this.buttonOrientation = buttonOrientation;
        refreshView();
        return this;
    }
    
    //其他
    public DialogLifeCycleListener getDialogLifeCycleListener() {
        return dialogLifeCycleListener == null ? new DialogLifeCycleListener() {
            @Override
            public void onCreate(BaseDialog alertDialog) {
                
            }
            
            @Override
            public void onShow(BaseDialog alertDialog) {
                
            }
            
            @Override
            public void onDismiss() {
                
            }
        } : dialogLifeCycleListener;
    }
    
    public InputDialog setDialogLifeCycleListener(DialogLifeCycleListener listener) {
        dialogLifeCycleListener = listener;
        return this;
    }
    
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
    
    public String getInputText() {
        if (txtInput == null) {
            return inputText;
        } else {
            return txtInput.getText().toString();
        }
    }
    
    public InputDialog setInputText(String inputText) {
        this.inputText = inputText;
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
    
    public InputInfo getInputInfo() {
        return inputInfo;
    }
    
    public InputDialog setInputInfo(InputInfo inputInfo) {
        this.inputInfo = inputInfo;
        refreshView();
        return this;
    }
}
