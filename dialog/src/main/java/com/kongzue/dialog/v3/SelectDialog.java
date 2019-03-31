package com.kongzue.dialog.v3;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.interfaces.DialogLifeCycleListener;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
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
 * CreateTime: 2019/3/29 16:43
 */
public class SelectDialog extends BaseDialog {
    
    private int buttonOrientation;
    
    private OnDialogButtonClickListener onOkButtonClickListener;
    private OnDialogButtonClickListener onCancelButtonClickListener;
    private OnDialogButtonClickListener onOtherButtonClickListener;
    
    private Drawable okButtonDrawable;
    private Drawable cancelButtonDrawable;
    private Drawable otherButtonDrawable;
    
    private String title = "提示";
    private String message = "提示信息";
    private String okButton = "确定";
    private String cancelButton = "取消";
    private String otherButton;
    
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
    
    public static SelectDialog build(@NonNull AppCompatActivity context) {
        synchronized (WaitDialog.class) {
            SelectDialog selectDialog = new SelectDialog();
            selectDialog.log("装载选择对话框");
            selectDialog.context = context;
            
            switch (selectDialog.style) {
                case STYLE_IOS:
                    selectDialog.build(selectDialog, R.layout.dialog_select_ios);
                    break;
                case STYLE_KONGZUE:
                    selectDialog.build(selectDialog, R.layout.dialog_select);
                    break;
                case STYLE_MATERIAL:
                    selectDialog.build(selectDialog);
                    break;
            }
            return selectDialog;
        }
    }
    
    public static SelectDialog show(@NonNull AppCompatActivity context, String title, String message) {
        synchronized (WaitDialog.class) {
            SelectDialog selectDialog = show(context, title, message, null, null, null);
            return selectDialog;
        }
    }
    
    public static SelectDialog show(@NonNull AppCompatActivity context, String title, String message, String okButton, String cancelButton) {
        synchronized (WaitDialog.class) {
            SelectDialog selectDialog = show(context, title, message, okButton, cancelButton, null);
            return selectDialog;
        }
    }
    
    public static SelectDialog show(@NonNull AppCompatActivity context, String title, String message, String okButton, String cancelButton, String otherButton) {
        synchronized (WaitDialog.class) {
            SelectDialog selectDialog = build(context);
            
            if (message != null) selectDialog.message = message;
            if (title != null) selectDialog.title = title;
            if (okButton != null) selectDialog.okButton = okButton;
            if (cancelButton != null) selectDialog.cancelButton = cancelButton;
            if (otherButton != null) selectDialog.otherButton = otherButton;
            
            selectDialog.showDialog();
            return selectDialog;
        }
    }
    
    private AlertDialog materialAlertDialog;
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
    
    private void refreshView() {
        if (txtDialogTitle != null) txtDialogTitle.setText(title);
        if (txtDialogTip != null) txtDialogTip.setText(message);
        
        if (rootView != null || materialAlertDialog != null) {
            final int bkgResId, blurFrontColor;
            switch (style) {
                case STYLE_IOS:
                    if (theme == DialogSettings.THEME.LIGHT) {
                        bkgResId = R.drawable.rect_selectdialog_ios_bkg_light;
                        blurFrontColor = Color.argb(blurAlpha, 255, 255, 255);
                    } else {
                        bkgResId = R.drawable.rect_selectdialog_ios_bkg_dark;
                        blurFrontColor = Color.argb((blurAlpha + 20) > 255 ? 255 : (blurAlpha + 20), 0, 0, 0);
                        txtDialogTitle.setTextColor(Color.WHITE);
                        txtDialogTip.setTextColor(Color.WHITE);
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
                    refreshTextViews();
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
                    refreshTextViews();
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
                    materialAlertDialog.setButton(BUTTON_NEGATIVE, cancelButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        
                        }
                    });
                    if (otherButton != null) {
                        materialAlertDialog.setButton(BUTTON_NEUTRAL, otherButton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            
                            }
                        });
                    }
                    materialAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            Button positiveButton = materialAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                            positiveButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (onOkButtonClickListener != null) {
                                        if (!onOkButtonClickListener.onClick(v))
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
                                        if (!onCancelButtonClickListener.onClick(v))
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
                                            if (!onOtherButtonClickListener.onClick(v))
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
                        if (!onOkButtonClickListener.onClick(v)) {
                            doDismiss();
                        }
                    } else {
                        doDismiss();
                    }
                }
            });
        }
        if (btnSelectNegative != null) {
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
                        if (!onCancelButtonClickListener.onClick(v)) {
                            doDismiss();
                        }
                    } else {
                        doDismiss();
                    }
                }
            });
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
                        if (!onOtherButtonClickListener.onClick(v)) {
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
    }
    
    @Override
    public void showDialog() {
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
    
    public SelectDialog setTitle(String title) {
        this.title = title;
        return this;
    }
    
    public String getMessage() {
        return message;
    }
    
    public SelectDialog setMessage(String content) {
        this.message = content;
        return this;
    }
    
    public String getOkButton() {
        return okButton;
    }
    
    public SelectDialog setOkButton(String okButton) {
        this.okButton = okButton;
        refreshView();
        return this;
    }
    
    public SelectDialog setOkButton(String okButton, OnDialogButtonClickListener onOkButtonClickListener) {
        this.okButton = okButton;
        this.onOkButtonClickListener = onOkButtonClickListener;
        refreshView();
        return this;
    }
    
    public String getCancelButton() {
        return cancelButton;
    }
    
    public SelectDialog setCancelButton(String cancelButton) {
        this.cancelButton = cancelButton;
        refreshView();
        return this;
    }
    
    public SelectDialog setCancelButton(String cancelButton, OnDialogButtonClickListener onCancelButtonClickListener) {
        this.cancelButton = cancelButton;
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        refreshView();
        return this;
    }
    
    public String getOtherButton() {
        return otherButton;
    }
    
    public SelectDialog setOtherButton(String otherButton) {
        this.otherButton = otherButton;
        refreshView();
        return this;
    }
    
    public SelectDialog setOtherButton(String otherButton, OnDialogButtonClickListener onOtherButtonClickListener) {
        this.otherButton = otherButton;
        this.onOtherButtonClickListener = onOtherButtonClickListener;
        refreshView();
        return this;
    }
    
    public OnDialogButtonClickListener getOnOkButtonClickListener() {
        return onOkButtonClickListener;
    }
    
    public SelectDialog setOnOkButtonClickListener(OnDialogButtonClickListener onOkButtonClickListener) {
        this.onOkButtonClickListener = onOkButtonClickListener;
        refreshView();
        return this;
    }
    
    public OnDialogButtonClickListener getOnCancelButtonClickListener() {
        return onCancelButtonClickListener;
    }
    
    public SelectDialog setOnCancelButtonClickListener(OnDialogButtonClickListener onCancelButtonClickListener) {
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        refreshView();
        return this;
    }
    
    public OnDialogButtonClickListener getOnOtherButtonClickListener() {
        return onOtherButtonClickListener;
    }
    
    public SelectDialog setOnOtherButtonClickListener(OnDialogButtonClickListener onOtherButtonClickListener) {
        this.onOtherButtonClickListener = onOtherButtonClickListener;
        refreshView();
        return this;
    }
    
    public SelectDialog setOkButtonDrawable(@DrawableRes int okButtonDrawableResId) {
        this.okButtonDrawable = ContextCompat.getDrawable(context, okButtonDrawableResId);
        refreshView();
        return this;
    }
    
    public SelectDialog setOkButtonDrawable(Drawable okButtonDrawable) {
        this.okButtonDrawable = okButtonDrawable;
        refreshView();
        return this;
    }
    
    public SelectDialog setCancelButtonDrawable(@DrawableRes int okButtonDrawableResId) {
        this.cancelButtonDrawable = ContextCompat.getDrawable(context, okButtonDrawableResId);
        refreshView();
        return this;
    }
    
    public SelectDialog setCancelButtonDrawable(Drawable cancelButtonDrawable) {
        this.cancelButtonDrawable = cancelButtonDrawable;
        refreshView();
        return this;
    }
    
    public SelectDialog setOtherButtonDrawable(@DrawableRes int okButtonDrawableResId) {
        this.otherButtonDrawable = ContextCompat.getDrawable(context, okButtonDrawableResId);
        refreshView();
        return this;
    }
    
    public SelectDialog setOtherButtonDrawable(Drawable otherButtonDrawable) {
        this.otherButtonDrawable = otherButtonDrawable;
        refreshView();
        return this;
    }
    
    public int getButtonOrientation() {
        return buttonOrientation;
    }
    
    public SelectDialog setButtonOrientation(@LinearLayoutCompat.OrientationMode int buttonOrientation) {
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
    
    public SelectDialog setDialogLifeCycleListener(DialogLifeCycleListener listener) {
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
    
    public SelectDialog setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }
    
    public DialogSettings.STYLE getStyle() {
        return style;
    }
    
    public SelectDialog setStyle(DialogSettings.STYLE style) {
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
    
    public SelectDialog setTheme(DialogSettings.THEME theme) {
        
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
    
    public SelectDialog setCancelable(boolean enable) {
        this.cancelable = enable ? BOOLEAN.TRUE : BOOLEAN.FALSE;
        if (dialog != null) dialog.setCancelable(cancelable == BOOLEAN.TRUE);
        return this;
    }
    
    
    public TextInfo getTitleTextInfo() {
        return titleTextInfo;
    }
    
    public SelectDialog setTitleTextInfo(TextInfo titleTextInfo) {
        this.titleTextInfo = titleTextInfo;
        return this;
    }
    
    public TextInfo getMessageTextInfo() {
        return messageTextInfo;
    }
    
    public SelectDialog setMessageTextInfo(TextInfo messageTextInfo) {
        this.messageTextInfo = messageTextInfo;
        return this;
    }
    
    public TextInfo getButtonTextInfo() {
        return buttonTextInfo;
    }
    
    public SelectDialog setButtonTextInfo(TextInfo buttonTextInfo) {
        this.buttonTextInfo = buttonTextInfo;
        return this;
    }
    
    public TextInfo getButtonPositiveTextInfo() {
        return buttonPositiveTextInfo;
    }
    
    public SelectDialog setButtonPositiveTextInfo(TextInfo buttonPositiveTextInfo) {
        this.buttonPositiveTextInfo = buttonPositiveTextInfo;
        return this;
    }
    
    public int getBackgroundColor() {
        return backgroundColor;
    }
    
    public SelectDialog setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }
}
