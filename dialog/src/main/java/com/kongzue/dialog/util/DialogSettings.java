package com.kongzue.dialog.util;

import android.graphics.Color;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/3/22 16:03
 */
public class DialogSettings {
    
    public enum STYLE {
        STYLE_MATERIAL, STYLE_KONGZUE, STYLE_IOS
    }
    
    public enum THEME {
        LIGHT, DARK
    }
    
    //是否开启模糊效果
    public static boolean isUseBlur = false;
    
    //全局主题风格
    public static STYLE style = STYLE.STYLE_MATERIAL;
    
    //全局明暗风格
    public static THEME theme = THEME.LIGHT;
    
    //全局标题文字样式
    public static TextInfo titleTextInfo;
    
    //全局正文文字样式
    public static TextInfo contentTextInfo;
    
    //全局默认按钮文字样式
    public static TextInfo buttonTextInfo;
    
    //全局焦点按钮文字样式
    public static TextInfo buttonPositiveTextInfo;
    
    //全局对话框背景颜色
    public static Color backgroundColor;
    
    //全局对话框默认是否可以点击外围遮罩区域或返回键关闭，此开关不影响提示框（TipDialog）以及等待框（WaitDialog）
    public static boolean cancelable = true;
    
    //全局提示框（TipDialog）默认是否可以关闭
    public static boolean cancelableTipDialog = false;
    
    //全局等待框（WaitDialog）默认是否可以关闭
    public static boolean cancelableWaitDialog = false;
    
    //日志
    public static boolean DEBUGMODE = false;
    
    //模糊透明度(0~255)
    public static int blurAlpha = 180;
}
