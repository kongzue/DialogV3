package com.kongzue.dialogdemo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.interfaces.DarkNavigationBarTheme;
import com.kongzue.baseframework.interfaces.DarkStatusBarTheme;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.interfaces.NavigationBarBackgroundColor;
import com.kongzue.baseframework.util.JumpParameter;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnNotificationClickListener;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.InputInfo;
import com.kongzue.dialog.util.view.BlurView;
import com.kongzue.dialog.v3.NormalDialog;
import com.kongzue.dialog.v3.InputDialog;
import com.kongzue.dialog.v3.Notification;
import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

@DarkStatusBarTheme(true)
@DarkNavigationBarTheme(true)
@NavigationBarBackgroundColor(a = 255, r = 236, g = 239, b = 241)
@Layout(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    
    private SmartRefreshLayout refreshLayout;
    private LinearLayout boxBody;
    private RadioGroup grpStyle;
    private RadioButton rdoIos;
    private RadioButton rdoMaterial;
    private RadioButton rdoKongzue;
    private RadioGroup grpTheme;
    private RadioButton rdoLight;
    private RadioButton rdoDark;
    private TextView btnMessageDialog;
    private TextView btnSelectDialog;
    private TextView btnInputDialog;
    private TextView btnWaitDialog;
    private TextView btnTipDialog;
    private TextView btnModalDialog;
    private TextView btnShowBreak;
    private TextView btnNotify;
    private RelativeLayout boxTable;
    private LinearLayout boxTableChild;
    private LinearLayout btnBack;
    private ImageView btnShare;
    
    @Override
    public void initViews() {
        refreshLayout = findViewById(R.id.refreshLayout);
        boxBody = findViewById(R.id.box_body);
        grpStyle = findViewById(R.id.grp_style);
        rdoIos = findViewById(R.id.rdo_ios);
        rdoMaterial = findViewById(R.id.rdo_material);
        rdoKongzue = findViewById(R.id.rdo_kongzue);
        grpTheme = findViewById(R.id.grp_theme);
        rdoLight = findViewById(R.id.rdo_light);
        rdoDark = findViewById(R.id.rdo_dark);
        btnMessageDialog = findViewById(R.id.btn_messageDialog);
        btnSelectDialog = findViewById(R.id.btn_selectDialog);
        btnInputDialog = findViewById(R.id.btn_inputDialog);
        btnWaitDialog = findViewById(R.id.btn_waitDialog);
        btnTipDialog = findViewById(R.id.btn_tipDialog);
        btnModalDialog = findViewById(R.id.btn_modalDialog);
        btnShowBreak = findViewById(R.id.btn_showBreak);
        btnNotify = findViewById(R.id.btn_notify);
        boxTable = findViewById(R.id.box_table);
        boxTableChild = findViewById(R.id.box_table_child);
        btnBack = findViewById(R.id.btn_back);
        btnShare = findViewById(R.id.btn_share);
    }
    
    @Override
    public void initDatas(JumpParameter paramer) {
        DialogSettings.DEBUGMODE = true;
        DialogSettings.isUseBlur = true;
        //DialogSettings.backgroundColor = Color.BLUE;
        //DialogSettings.titleTextInfo = new TextInfo().setFontSize(50);
        //DialogSettings.buttonPositiveTextInfo = new TextInfo().setFontColor(Color.GREEN);
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        DialogSettings.theme = DialogSettings.THEME.LIGHT;
        
        refreshLayout.setEnableLoadMore(false).setEnableRefresh(false).setEnableOverScrollDrag(true);
        boxBody.setPadding(dip2px(15), dip2px(50), dip2px(15), dip2px(20));
    }
    
    @Override
    public void setEvents() {
    
        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification.show(me,"提示","提示信息",R.mipmap.ico_wechat).setOnNotificationClickListener(new OnNotificationClickListener() {
                    @Override
                    public void onClick() {
                        log("点击了消息");
                    }
                }).setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        log("消息消失了");
                    }
                });
            }
        });
        
        grpStyle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdo_ios:
                        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
                        break;
                    case R.id.rdo_kongzue:
                        DialogSettings.style = DialogSettings.STYLE.STYLE_KONGZUE;
                        break;
                    case R.id.rdo_material:
                        DialogSettings.style = DialogSettings.STYLE.STYLE_MATERIAL;
                        break;
                }
            }
        });
        
        grpTheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rdo_light:
                        DialogSettings.theme = DialogSettings.THEME.LIGHT;
                        break;
                    case R.id.rdo_dark:
                        DialogSettings.theme = DialogSettings.THEME.DARK;
                        break;
                }
            }
        });
        
        btnMessageDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalDialog.show(me, "提示", "这是一条消息", "确定");
            }
        });
        
        btnSelectDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalDialog.show(me, "提示", "这是一条消息", "是", "否", "取消")
                        .setButtonOrientation(LinearLayout.VERTICAL);
            }
        });
        
        btnInputDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputDialog.show(me, "提示", "请输入密码（123456）", "确定", "取消")
                        .setOnOkButtonClickListener(new OnInputDialogButtonClickListener() {
                            @Override
                            public boolean onClick(View v, String inputStr) {
                                if (inputStr.equals("123456")) {
                                    TipDialog.show(me, "成功！", TipDialog.TYPE.SUCCESS);
                                    return false;
                                } else {
                                    TipDialog.show(me, "密码错误", TipDialog.TYPE.ERROR);
                                    return true;
                                }
                            }
                        })
                        .setInputInfo(new InputInfo().setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD))
                        .setCancelable(false)
                ;
            }
        });
        
        btnWaitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaitDialog.show(me, "请稍候...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TipDialog.dismiss();
                            }
                        });
                    }
                }, 2000);
            }
        });
        
        btnTipDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaitDialog.show(me, "请稍候...");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TipDialog.show(me, "警告提示", TipDialog.TYPE.WARNING);
                            }
                        });
                    }
                }, 1000);
            }
        });
        
        btnModalDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalDialog.build(me)
                        .setTitle("提示")
                        .setMessage("序列化对话框，即模态对话框，是通过代码一次性弹出多个对话框而一次只显示一个，当一个对话框关闭后下一个对话框才会显示。")
                        .setOkButton("了解", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(View v) {
                                return false;
                            }
                        })
                        .showDialog();
                NormalDialog.show(me, "更多功能", "点击左边的按钮是无法关掉此对话框的，Kongzue Dialog提供的回调函数可以轻松帮你实现你想要的判断功能", "点我关闭", "我是关不掉的")
                        .setOnCancelButtonClickListener(new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(View v) {
                                return true;
                            }
                        });
                NormalDialog.show(me, "纵向排列", "如果你正在使用iOS风格或Kongzue风格，这里的按钮可以纵向排列，以方便提供更多选择", "还不错", "有点意思", "还有呢？")
                        .setButtonOrientation(LinearLayout.VERTICAL);
                InputDialog.show(me, "输入对话框", "你也可以仅显示一个按钮，就像这样", "确定");
                NormalDialog.show(me, "提示", "提示性对话框不受模态化影响，在本对话框显示的过程中也可以立即显示", "给我个提示", "结束")
                        .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(View v) {
                                btnTipDialog.callOnClick();
                                return true;
                            }
                        });
            }
        });
        
        btnShowBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalDialog.show(me,"提示","一般的AlertDialog在显示时如果其依附的Activity被finish掉，会发生WindowLeaked错误导致程序崩溃，但Kongzue Dialog没有这个问题，您可以点击下边的按钮开始，等待几秒钟，Activity会被finish掉，但您不会遇到任何崩溃问题。","开始崩溃","取消")
                        .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(View v) {
                                WaitDialog.show(me, "准备崩溃中...");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                jump(MainActivity.class);
                                                finish();
                                            }
                                        });
                                    }
                                }, 2000);
                                return true;
                            }
                        });
            }
        });
        
    }
    
}
