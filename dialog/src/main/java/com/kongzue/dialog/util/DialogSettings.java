package com.kongzue.dialog.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import com.kongzue.dialog.interfaces.DialogLifeCycleListener;
import com.kongzue.dialog.util.view.BlurView;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/3/22 16:03
 */
public class DialogSettings {
    
    public enum STYLE {
        STYLE_MATERIAL, STYLE_KONGZUE, STYLE_IOS, STYLE_MIUI
    }
    
    public enum THEME {
        LIGHT, DARK
    }
    
    //是否开启模糊效果
    public static boolean isUseBlur = false;
    
    //开启模态化队列启动方式
    public static boolean modalDialog = false;
    
    //全局主题风格
    public static STYLE style = STYLE.STYLE_MATERIAL;
    
    //全局对话框明暗风格
    public static THEME theme = THEME.LIGHT;
    
    //全局提示框明暗风格
    public static THEME tipTheme = THEME.DARK;
    
    //全局标题文字样式
    public static TextInfo titleTextInfo;
    
    //全局正文文字样式
    public static TextInfo contentTextInfo;
    
    //全局提示文字样式
    public static TextInfo tipTextInfo;
    
    //全局默认按钮文字样式
    public static TextInfo buttonTextInfo;
    
    //全局焦点按钮文字样式
    public static TextInfo buttonPositiveTextInfo;
    
    //全局输入框文本样式
    public static InputInfo inputInfo;
    
    //全局菜单标题样式
    public static TextInfo menuTitleInfo;
    
    //全局菜单文字样式
    public static TextInfo menuTextInfo;
    
    //全局对话框背景颜色，值0时不生效
    public static int backgroundColor = 0;
    
    //全局对话框默认是否可以点击外围遮罩区域或返回键关闭，此开关不影响提示框（TipDialog）以及等待框（TipDialog）
    public static boolean cancelable = true;
    
    //全局提示框及等待框（WaitDialog、TipDialog）默认是否可以关闭
    public static boolean cancelableTipDialog = false;
    
    //是否允许显示日志
    public static boolean DEBUGMODE = false;
    
    //模糊透明度(0~255)
    public static int blurAlpha = 210;
    
    //允许自定义系统对话框style，注意设置此功能会导致原对话框风格和动画失效
    public static int systemDialogStyle;
    
    //默认取消按钮文本文字，影响BottomDialog、ShareDialog
    public static String defaultCancelButtonText;
    
    //全局Dialog生命周期监听器
    public static DialogLifeCycleListener dialogLifeCycleListener;
    
    //全局提示框背景资源，值0时不生效
    public static int tipBackgroundResId = 0;
    
    //对话框，全局按钮资源
    public static Drawable okButtonDrawable;
    public static Drawable cancelButtonDrawable;
    public static Drawable otherButtonDrawable;
    
    //输入对话框，是否自动弹出输入键盘
    public static boolean autoShowInputKeyboard = false;
    
    //检查Renderscript支持性
    public static boolean checkRenderscriptSupport(Context context) {
        boolean isSupport = true;
        try {
            DialogSettings.class.getClassLoader().loadClass("android.graphics.drawable.RippleDrawable");
            DialogSettings.class.getClassLoader().loadClass("android.support.v8.renderscript.RenderScript");
        } catch (ClassNotFoundException e) {
            isSupport = false;
            if (DEBUGMODE) {
                Log.e(">>>", "\n错误！\nRenderScript支持库未启用，要启用模糊效果，请在您的app的Gradle配置文件中添加以下语句：" +
                        "\nandroid { \n...\n  defaultConfig { \n    ...\n    renderscriptTargetApi 17 \n    renderscriptSupportModeEnabled true \n  }\n}");
            }
        }
        
        RenderScript renderScript = null;
        ScriptIntrinsicBlur blurScript = null;
        try {
            renderScript = RenderScript.create(context);
            blurScript = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        } catch (Exception e) {
            isSupport = false;
        } finally {
            if (renderScript != null) {
                renderScript.destroy();
                renderScript = null;
            }
            if (blurScript != null) {
                blurScript.destroy();
                blurScript = null;
            }
        }
        isUseBlur = isSupport;
        
        if (DEBUGMODE) {
            Log.i(">>>", "检查Renderscript支持性: " + isSupport);
        }
        return isSupport;
    }
    
    public static void init() {
        BaseDialog.reset();
    }
}
