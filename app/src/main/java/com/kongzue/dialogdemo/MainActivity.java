package com.kongzue.dialogdemo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.NormalDialog;
import com.kongzue.dialog.v3.InputDialog;
import com.kongzue.dialog.v3.TipDialog;

public class MainActivity extends AppCompatActivity {
    
    private MainActivity me;
    
    private Button btnWaitDialog;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        me = this;
        
        btnWaitDialog = findViewById(R.id.btn_waitDialog);
        
        DialogSettings.DEBUGMODE = true;
        DialogSettings.isUseBlur = true;
        //DialogSettings.backgroundColor = Color.BLUE;
        //DialogSettings.titleTextInfo = new TextInfo().setFontSize(50);
        //DialogSettings.buttonPositiveTextInfo = new TextInfo().setFontColor(Color.GREEN);
        DialogSettings.style = DialogSettings.STYLE.STYLE_MATERIAL;
        DialogSettings.theme = DialogSettings.THEME.LIGHT;
        
        btnWaitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                InputDialog.show(me, null, null)
                        .setHintText("Test!")
                        .setButtonOrientation(LinearLayout.VERTICAL)
                        .setOkButton("确定")
                        .setCancelButton("取消")
                        .setOtherButton("其他", new OnInputDialogButtonClickListener() {
                            @Override
                            public boolean onClick(View v, String inputStr) {
                                Log.d(">>>", "已输入: " + inputStr);
                                TipDialog.showWait(me, "请稍候...");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                TipDialog.showTip(me, "完成！", TipDialog.TYPE.SUCCESS)
                                                        .setOnDismissListener(new OnDismissListener() {
                                                            @Override
                                                            public void onDismiss() {
                                                                Log.e(">>>", "onDismiss! ");
                                                            }
                                                        });
                                            }
                                        });
                                    }
                                }, 2000);
                                return true;
                            }
                        }).setCancelable(false);
                NormalDialog.show(me, "提示", "提示信息", "确认", null);
                //TipDialog.show(me,"请稍候");
                NormalDialog.build(me).setTitle("哈哈").showDialog();
                
                
                //NormalDialog.show(me, "标题2", "测试2", "ok", "no", "miss")
                //        .setButtonOrientation(LinearLayout.VERTICAL)
                //        .setOnOtherButtonClickListener(new OnDialogButtonClickListener() {
                //            @Override
                //            public boolean onClick(View v) {
                //                TipDialog.show(me, "请稍候...");
                //                new Handler().postDelayed(new Runnable() {
                //                    @Override
                //                    public void run() {
                //                        runOnUiThread(new Runnable() {
                //                            @Override
                //                            public void run() {
                //                                TipDialog.show(me, "hahaha");
                //                            }
                //                        });
                //                    }
                //                }, 2000);
                //                new Handler().postDelayed(new Runnable() {
                //                    @Override
                //                    public void run() {
                //                        runOnUiThread(new Runnable() {
                //                            @Override
                //                            public void run() {
                //                                TipDialog.show(me, "222");
                //                            }
                //                        });
                //                    }
                //                }, 4000);
                //                new Handler().postDelayed(new Runnable() {
                //                    @Override
                //                    public void run() {
                //                        runOnUiThread(new Runnable() {
                //                            @Override
                //                            public void run() {
                //                                startActivity(new Intent(me, MainActivity.class));
                //                                me.finish();
                //                            }
                //                        });
                //                    }
                //                }, 6000);
                //                return true;
                //            }
                //        })
                //        .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                //            @Override
                //            public boolean onClick(View v) {
                //                NormalDialog.show(me,"AAA","BBB");
                //                return false;
                //            }
                //        })
                //        .setOnDismissListener(new OnDismissListener() {
                //    @Override
                //    public void onDismiss() {
                //
                //    }
                //});
                //NormalDialog.show(me, "标题", "内容放啥好呢？", "我知道了", "我不知道", "随意好了")
                //        .setCancelable(true)
                //        .setOnCancelButtonClickListener(new OnDialogButtonClickListener() {
                //            @Override
                //            public boolean onClick(View v) {
                //                return true;
                //            }
                //        })
                //        .setOnDismissListener(new OnDismissListener() {
                //            @Override
                //            public void onDismiss() {
                //                Log.e(">>>", "onDismiss: ");
                //            }
                //        });
            }
        });
    }
    
}
