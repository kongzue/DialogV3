# Kongzue Dialog V3
献给要求安卓照着苹果设计稿做开发的产品们（手动滑稽

<a href="https://github.com/kongzue/dialogV3/">
<img src="https://img.shields.io/badge/Kongzue%20Dialog-3.2.4-green.svg" alt="Kongzue Dialog">
</a> 
<a href="https://bintray.com/myzchh/maven/dialogV3/3.2.4/link">
<img src="https://img.shields.io/badge/Maven-3.2.4-blue.svg" alt="Maven">
</a> 
<a href="http://www.apache.org/licenses/LICENSE-2.0">
<img src="https://img.shields.io/badge/License-Apache%202.0-red.svg" alt="License">
</a> 
<a href="http://www.kongzue.com">
<img src="https://img.shields.io/badge/Homepage-Kongzue.com-brightgreen.svg" alt="Homepage">
</a>

![Kongzue Dialog V3](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/img_dialog_v3.png)

![Kongzue Dialog V3 MIUI](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/img_dialog_v3_miui.jpg)

### Kongzue Dialog V3 的优势

#### 1，不会发生 WindowLeaked 错误
一般情况下直接创建的 Dialog 或 AlertDialog 启动后，一旦其依赖的 Activity 优先于 Dialog 关闭，则会抛出 android.view.WindowLeaked 错误。

这对于普通对话框还好处理，只需要在 Activity 的 onPause() 或 onDestroy() 中对 dialog.dismiss(); 但对于可能出现的，诸如等待提示框、提醒框等 Dialog，很容易发生此问题。

Google 现已推荐使用 DialogFragment 来代替普通 Dialog 使用。

Kongzue Dialog 组件 V3 全部采用 DialogFragment 来实现对话框组件，且创造对话框更为简单方便，只需简单配置即可上手，也不会出现 WindowLeaked 错误。

#### 2，横竖屏切换、分屏大小切换、甚至未来的折叠屏，都没有问题！
已知 Android 系统在发生横竖屏切换、分屏大小切换、未来的折叠屏、窗口化等界面大小发生变化的情况时若不进行处理一般都会销毁当前 Activity 重新创建，但这会导致我们已经弹出的 Dialog 也发生重启。

重启最大的麻烦在于需要对重启后的 Dialog 进行原状态恢复，即事件重绑定，包括 Dialog 的样式、相关事件监听器等都需要重新设置，使用 Kongzue Dialog V3 您就不需要再担心这些麻烦的问题，Kongzue Dialog V3 会自动重建被销毁的 Dialog，而无需您做任何处理。

#### 3，目前市面上最像 iOS 风格的对话框组件
Kongzue Dialog V3 依然会像第二代一样提供多种主题风格选择，且更为强大，iOS 风格自带高斯模糊让您再也无需担心产品经理和设计师放飞自我。

功能强大且真正的实时高斯模糊效果让您的程序更具逼格，同时我们也比苹果更早提供了 iOS 风格对话框夜间模式，若您正在使用暗色的程序设计，这款与您更搭！

我们的组件也会提供许许多多的接口供您自定义对话框的每一点细节，方便而快捷，迅速构建您的程序。

#### 4，模态化&快速创建
Kongzue Dialog V3 支持模态化窗口模式，启用模态化设置项后，即便从代码一次执行显示多个对话框，实际也会在上一个对话框消失后再显示下一个，以避免对话框叠加造成的混乱情况发生。

另外 Kongzue Dialog 不强制您必须使用 Builder 等方式创建，且为了避免额外的代码量，所有组件均提供了可灵活使用的 show(...) 构造方法，因此只需要输入组件名称，按一下 “.” 按键，即可快速根据提示创建出一个对话框。

下边是 AlertDialog 和 Kongzue Dialog V3 在创建一个典型对话框的代码对比：

使用 AlertDialog：
```
//需要先创建Builder
android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);         
builder.setTitle(R.string.error_title);
builder.setCancelable(false);   //每次都需要指定的设置                                                                                                
builder.setMessage(R.string.error_not_install_tip);
builder.setPositiveButton(context.getString(R.string.dialog_iknow_button), new DialogInterface.OnClickListener() {                  
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        //处理确定按钮事务
    }
});
builder.setNegativeButton(context.getString(R.string.cancel), null);
//不要忘记.show()
builder.show();                                                                                                                 
```

使用 Kongzue Dialog V3：
```
//一次性完成所有参数预设操作
MessageDialog.show(MainActivity.this, R.string.error_title, R.string.error_not_install_tip, R.string.dialog_iknow_button, R.string.cancel)     
        .setOkButton(new OnDialogButtonClickListener() {  //仅需要对需要处理的按钮进行操作                                                                     
            @Override
            public boolean onClick(BaseDialog baseDialog, View v) {
                //处理确定按钮事务
                return false;    //可以通过 return 决定点击按钮是否默认自动关闭对话框                                                                                               
            }
        }); 
//很多设置可通过全局进行设置，不需要每次都指定                                                                                                                    
```

## Demo

现已提供 Kongzue Dialog V3 Demo 演示程序供下载体验： <http://beta.kongzue.com/DialogV3>

## 引入

### Support 版本
Maven仓库：
```
<dependency>
  <groupId>com.kongzue.dialog_v3</groupId>
  <artifactId>dialog</artifactId>
  <version>3.2.4</version>
  <type>pom</type>
</dependency>
```
Gradle：
在dependencies{}中添加引用：
```
implementation 'com.kongzue.dialog_v3:dialog:3.2.4'
```

从 Kongzue Dialog V2 升级至 Kongzue Dialog V3，请参考 [Kongzue Dialog V2升级注意事项](kongzue_dialog_v2_upto_v3.md)

### AndroidX 版本

⚡ 另外提供 AndroidX 版本，源码详见 [分支](https://github.com/kongzue/DialogV3/tree/androidx)

在dependencies{}中添加引用：
```
implementation 'com.kongzue.dialog_v3x:dialog:3.2.4'       
```

## 全局配置
在完成引入 Kongzue Dialog V3 库后，首先需要进行一些预先配置，诸如对话框组件整体的风格、主题和字体等，它们都可以在一个工具类中进行配置，说明如下：
```
import com.kongzue.dialog.util.DialogSettings;

DialogSettings.isUseBlur = (boolean);                   //是否开启模糊效果，默认关闭
DialogSettings.modalDialog = (boolean);                 //是否开启模态窗口模式，一次显示多个对话框将以队列形式一个一个显示，默认关闭
DialogSettings.style = (DialogSettings.STYLE);          //全局主题风格，提供三种可选风格，STYLE_MATERIAL, STYLE_KONGZUE, STYLE_IOS
DialogSettings.theme = (DialogSettings.THEME);          //全局对话框明暗风格，提供两种可选主题，LIGHT, DARK
DialogSettings.tipTheme = (DialogSettings.THEME);       //全局提示框明暗风格，提供两种可选主题，LIGHT, DARK
DialogSettings.titleTextInfo = (TextInfo);              //全局对话框标题文字样式
DialogSettings.menuTitleInfo = (TextInfo);              //全局菜单标题文字样式
DialogSettings.menuTextInfo = (TextInfo);               //全局菜单列表文字样式
DialogSettings.contentTextInfo = (TextInfo);            //全局正文文字样式
DialogSettings.buttonTextInfo = (TextInfo);             //全局默认按钮文字样式
DialogSettings.buttonPositiveTextInfo = (TextInfo);     //全局焦点按钮文字样式（一般指确定按钮）
DialogSettings.inputInfo = (InputInfo);                 //全局输入框文本样式
DialogSettings.backgroundColor = (ColorInt);            //全局对话框背景颜色，值0时不生效
DialogSettings.cancelable = (boolean);                  //全局对话框默认是否可以点击外围遮罩区域或返回键关闭，此开关不影响提示框（TipDialog）以及等待框（TipDialog）
DialogSettings.cancelableTipDialog = (boolean);         //全局提示框及等待框（WaitDialog、TipDialog）默认是否可以关闭
DialogSettings.DEBUGMODE = (boolean);                   //是否允许打印日志
DialogSettings.blurAlpha = (int);                       //开启模糊后的透明度（0~255）
DialogSettings.systemDialogStyle = (styleResId);        //自定义系统对话框style，注意设置此功能会导致原对话框风格和动画失效
DialogSettings.dialogLifeCycleListener = (DialogLifeCycleListener);  //全局Dialog生命周期监听器
DialogSettings.defaultCancelButtonText = (String);      //设置 BottomMenu 和 ShareDialog 默认“取消”按钮的文字
DialogSettings.tipBackgroundResId = (drawableResId);    //设置 TipDialog 和 WaitDialog 的背景资源
DialogSettings.tipTextInfo = (InputInfo);               //设置 TipDialog 和 WaitDialog 文字样式
DialogSettings.autoShowInputKeyboard = (boolean);       //设置 InputDialog 是否自动弹出输入法
DialogSettings.okButtonDrawable = (drawable);           //设置确定按钮背景资源
DialogSettings.cancelButtonDrawable = (drawable);       //设置取消按钮背景资源
DialogSettings.otherButtonDrawable = (drawable);        //设置其他按钮背景资源
Notification.mode = Notification.Mode.FLOATING_WINDOW;  //通知实现方式。可选 TOAST 使用自定义吐司实现以及 FLOATING_WINDOW 悬浮窗实现方式

//检查 Renderscript 兼容性，若设备不支持，DialogSettings.isUseBlur 会自动关闭；
boolean renderscriptSupport = DialogSettings.checkRenderscriptSupport(context)

DialogSettings.init(context);                           //初始化清空 BaseDialog 队列
```

如果需要开启模糊效果，即 DialogSettings.isUseBlur = true; 需要进行额外 renderscript 配置，需要注意的是在部分低配置手机上此功能效率可能存在问题。

在 app 的 build.gradle 中添加以下代码：
```
android {
    ...
    defaultConfig {
        ...

        renderscriptTargetApi 17
        renderscriptSupportModeEnabled true
    }
}
```

上述配置为全局配置，即在不进行特意定制的情况下，所有对话框组件默认按照此配置显示，如有特殊需求，可以通过各对话框组件的 build(...) 方法创建对话框后进行配置，最后使用 show() 方法执行显示即可。

## 使用
### 基本消息对话框
提供日常消息展示，区分为单按钮、双按钮和三按钮的效果。

![Kongzue Dialog V3 消息对话框](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/img_dialog_v3_messagedialog.png)

以下范例通过参数快速创建一个基本的消息对话框：
```
MessageDialog.show(MainActivity.this, "提示", "这是一条消息", "确定");
```

额外的，MessageDialog 还提供多种参数的构建方法，方便快速创建合适的对话框：
```
MessageDialog.show(MainActivity.this, "提示", "这是一条双按钮消息", "确定", "取消");

MessageDialog.show(MainActivity.this, "提示", "这是一条三按钮消息", "确定", "取消", "其他");
```

消息对话框的按钮回调方法提供了一个 return 值用于判断点击按钮后是否需要关闭对话框，默认 return false 会关闭当前的输入对话框，若 return true 则点击该按钮后不会关闭：
```
MessageDialog.show(MainActivity.this, "更多功能", "点击左边的按钮是无法关掉此对话框的，Kongzue Dialog提供的回调函数可以轻松帮你实现你想要的判断功能", "点我关闭", "我是关不掉的")
        .setOnCancelButtonClickListener(new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(BaseDialog baseDialog, View v) {
                return true;                    //位于“取消”位置的按钮点击后无法关闭对话框
            }
        });
```

此功能便于做选择判断，在合理的事件触发后可允许关闭对话框。

也可以通过 build(...) 方法创建，并定制更多效果：
```
MessageDialog.build(MainActivity.this)
        .setStyle(DialogSettings.STYLE.STYLE_MATERIAL)
        .setTheme(DialogSettings.THEME.DARK)
        .setTitle("定制化对话框")
        .setMessage("我是内容")
        .setOkButton("OK", new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了OK！", Toast.LENGTH_SHORT).show();
                return false;
            }
        })
        .show();
```

需注意的是，只有修改主题风格的 setStyle(...) 和 setTheme(...) 方法必须在使用 build(...) 创建时才可以修改。

一些特殊需求中可能用到需要纵向排列按钮的三按钮消息框，则可以通过以下方法实现：
```
MessageDialog
        .show(MainActivity.this, "纵向排列", "如果你正在使用iOS风格或Kongzue风格，这里的按钮可以纵向排列，以方便提供更多选择", "还不错", "有点意思", "还有呢？")
        .setButtonOrientation(LinearLayout.VERTICAL);
```

💡 额外说明，V3 库支持更灵活的配置按钮方式，除了默认的 setOnOkButtonClickListener(...) 方法以外，你还可以这样写：
```
//仅设置文字
.setOkButton("知道了")
        
//设置文字同时设置回调
.setOkButton("知道了", new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了知道了！", Toast.LENGTH_SHORT).show();
                return false;
            }
        })
        
//仅设置回调
.setOkButton(new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了知道了！", Toast.LENGTH_SHORT).show();
                return false;
            }
        })
        
//使用资源 id 设置文字
.setOkButton(R.string.iknow)

//其他你能想到的同样支持...
```

### 输入对话框
提供额外输入需求的对话框组件，可控制输入内容类型，并在点击按钮时判断是否关闭对话框。

![Kongzue Dialog V3 输入对话框](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/img_dialog_v3_input.png)

以下范例通过参数快速创建一个基本的输入对话框：
```
InputDialog.show(MainActivity.this, "输入对话框", "输入一些内容", "确定");
```

InputDialog 与 MessageDialog 类似也提供多种构建方法，在此不再赘述。

输入对话框的按钮回调中会直接返回当前输入的文本内容：
```
InputDialog.show(MainActivity.this, "提示", "请输入密码", "确定", "取消")
        .setOnOkButtonClickListener(new OnInputDialogButtonClickListener() {
            @Override
            public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                //inputStr 即当前输入的文本
                return false;
            }
        })
```

如果需要修改输入框的提示语（HintText）或内容（InputText），可以使用以下方法：
```
InputDialog.show(MainActivity.this, "输入对话框", "输入一些内容", "确定")
        .setInputText("123456")
        .setHintText("请输入密码")
;
```

如需控制输入内容的字号、颜色、输入长度、文本类型，可以通过以下方法实现：
```
InputDialog.show(MainActivity.this, "输入对话框", "请输入6位密码", "确定")
        .setInputInfo(new InputInfo()
                              .setMAX_LENGTH(6)     //限制最大输入长度
                              .setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)     //仅输入密码类型
                              .setTextInfo(new TextInfo()       //设置文字样式
                                                   .setFontColor(Color.RED)     //修改文字样式颜色为红色
                              )
                              .setMultipleLines(true)       //支持多行输入
        )
;
```
备注：TextInfo（com.kongzue.dialog.util.TextInfo）类提供了基本的文字样式控制，InputInfo（com.kongzue.dialog.util.InputInfo）类提供了基础的输入文字类型控制。

### 等待和提示对话框
等待提示对话框提供居中于屏幕阻断操作的等待和状态提示功能。

![Kongzue Dialog V3 等待和提示对话框](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/img_dialog_v3_tip.png)

使用以下代码构建等待对话框：
```
WaitDialog.show(MainActivity.this, "请稍候...");
```

使用以下代码构建提示对话框：
```
TipDialog.show(MainActivity.this, "警告提示", TipDialog.TYPE.WARNING);
```

TipDialog 自带三种类型的提示图标（TipDialog.TYPE），可通过参数设置指定：
```
TipDialog.TYPE.SUCCESS                                  //对勾提示图
TipDialog.TYPE.WARNING                                  //感叹号提示图
TipDialog.TYPE.ERROR                                    //错误叉提示图
```

也可以通过如下代码设置自定义的图片：
```
TipDialog.show(MainActivity.this, "警告提示", R.mipmap.img_tip);        //入参自定义图片资源文件
```

额外的，可以通过以下语句设置 TipDialog 自动关闭的时长（单位：毫秒）：
```
TipDialog.show(MainActivity.this, "成功！", TipDialog.TYPE.SUCCESS)
        .setTipTime(5000);
```

如果连续使用，两者会有衔接的效果。

如需手动关闭，执行对应的 dismiss() 方法即可。

需要注意的是，WaitDialog 本质上继承自 TipDialog，它们属于长时间提示功能的组件，且内存中只会创建一次，不会重复创建直到被 dismiss() 关闭，但因为该组件使用 DialogFragment 构建，请勿需担心造成 WindowLeaked 错误问题。

WaitDialog 和 TipDialog 的主题可以通过以下代码自定义：
```
WaitDialog.show(MainActivity.this, null)
        .setTheme(DialogSettings.THEME.LIGHT);      //强制指定为亮色模式
```

### 底部菜单
即从屏幕底部弹出的可选择的菜单。

![Kongzue Dialog V3 底部菜单](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/img_dialog_v3_bottomdialog.png)

使用以下代码构建底部菜单：
```
BottomMenu.show(MainActivity.this, new String[]{"菜单1", "菜单2", "菜单3"}, new OnMenuItemClickListener() {
    @Override
    public void onClick(String text, int index) {
        //返回参数 text 即菜单名称，index 即菜单索引
    }
});
```

BottomMenu 可以通过 String[] 集合创建，也可以通过 List<String> 创建。

要为底部菜单加上标题，可以使用以下语句：
```
BottomMenu.show(MainActivity.this, new String[]{"菜单1", "菜单2", "菜单3"}, new OnMenuItemClickListener() {
    @Override
    public void onClick(String text, int index) {
        log("点击了：" + text);
    }
})
.setTitle("这里是标题文字");
```

底部菜单也允许自定义菜单 Adapter 以支持您自己的菜单样式，您可以设置自定义的继承自 BaseAdapter 的菜单 Adapter 给菜单 ListView，此时您无需设置 StringArray 或 List<String> 的集合。

另外请注意，使用自定义菜单 Adapter 的情况下可能导致使用 iOS 风格时无法有效的支持顶部菜单、底部菜单的上下圆角裁切，您可以尝试参考 BottomMenu.IOSMenuArrayAdapter 来编写您的自定义 Adapter。

使用自定义菜单 Adapter 可以使用以下语句：
```
List<String> datas = new ArrayList<>();
datas.add("菜单1");
datas.add("菜单2");
datas.add("菜单3");

//您自己的Adapter
BaseAdapter baseAdapter = new ArrayAdapter(MainActivity.this, com.kongzue.dialog.R.layout.item_bottom_menu_kongzue, datas);    

BottomMenu.show(MainActivity.this, baseAdapter, new OnMenuItemClickListener() {
    @Override
    public void onClick(String text, int index) {
        //注意此处的 text 返回为自定义 Adapter.getItem(position).toString()，如需获取自定义Object，请尝试 datas.get(index)
        toast(text);
    }
});
```

⚠ 特别说明：

Material 风格的 BottomMenu 默认不支持“取消”按钮，按照设计规范，使用下滑手势关闭。

### 通知
这里的通知并非系统通知，且不具备在您的设备通知栏中持久显示的特性，它本质上是通过对 Toast 进行修改实现的跨界面屏幕顶部提示条。

不依赖于界面显示，也不会打断用户操作，可作为即时通迅 IM 类软件跨界面消息提醒，或者用于网络错误状态提示。

![Kongzue Dialog V3 通知](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/img_dialog_v3_notify.png)

使用以下代码快速构建通知：
```
Notification.show(MainActivity.this, "提示", "提示信息");
```

需要加入图标与点击、关闭事件：
```
Notification.show(MainActivity.this, "提示", "提示信息", R.mipmap.ico_wechat).setOnNotificationClickListener(new OnNotificationClickListener() {
    @Override
    public void onClick() {
        MessageDialog.show(MainActivity.this, "提示", "点击了消息");
    }
}).setOnDismissListener(new OnDismissListener() {
    @Override
    public void onDismiss() {
        log("消息溜走了");
    }
});
```

在 3.2.2 版本以上，由于最 Android 10+ 对自定义 Toast 进行了屏蔽，可能导致部分功能无法正常工作，您可以通过以下方式修改通知的实现方式：
```
Notification.mode = Notification.Mode.TOAST;                //使用自定义 Toast 实现方式
Notification.mode = Notification.Mode.FLOATING_WINDOW;      //使用悬浮窗实现方式
```
使用悬浮窗实现方式默认只能够在一个界面显示通知，您可以在 AndroidManifest.xml 增加以下权限声明来开启跨窗口悬浮窗权限：
```
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
```
另附申请开启悬浮窗权限的代码：
```
Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
intent.setData(Uri.parse("package:" + getPackageName()));
startActivityForResult(intent, 0);
```

### 分享对话框
分享对话框会从屏幕底部弹出，并提供图标加文字的选择分享列表。

![Kongzue Dialog V3 通知](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/img_dialog_v3_share.png)

要使用分享对话框，需要先创建分享 Item：
```
List<ShareDialog.Item> itemList = new ArrayList<>();
itemList.add(new ShareDialog.Item(MainActivity.this ,R.mipmap.img_email_ios,"邮件"));
itemList.add(new ShareDialog.Item(MainActivity.this ,R.mipmap.img_qq_ios,"QQ"));
itemList.add(new ShareDialog.Item(MainActivity.this ,R.mipmap.img_wechat_ios,"微信"));
itemList.add(new ShareDialog.Item(MainActivity.this ,R.mipmap.img_weibo_ios,"微博"));
itemList.add(new ShareDialog.Item(MainActivity.this ,R.mipmap.img_memorandum_ios,"添加到“备忘录”"));
itemList.add(new ShareDialog.Item(MainActivity.this ,R.mipmap.img_remind_ios,"提醒事项"));
```

然后创建分享对话框及监听点击事件：
```
ShareDialog.show(MainActivity.this, itemList, new ShareDialog.OnItemClickListener() {
    @Override
    public boolean onClick(ShareDialog shareDialog, int index, ShareDialog.Item item) {
        toast("点击了：" + item.getText());
        return false;
    }
});
```

额外需要注意，iOS 风格模式下，默认会自动对图片进行圆角裁切，使用时只需要直接提供方形图标即可。

⚠ 特别说明：

Material 风格的 ShareDialog 默认不支持“取消”按钮，按照设计规范，使用下滑手势关闭。

### 全屏对话框
Kongzue Dialog V3 提供了全屏对话框样式，这种对话框主体采用自定义布局，启动后会占据全屏，可设置标题和标题左右两个按钮。

![Kongzue Dialog V3 自定义对话框](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/img_dialog_v3_fullscreen.png)

使用以下代码创建全屏对话框：
```
FullScreenDialog
        .show(MainActivity.this, R.layout.layout_full_login, new FullScreenDialog.OnBindView() {
            @Override
            public void onBind(FullScreenDialog dialog, View rootView) {
                boxUserName = rootView.findViewById(R.id.box_userName);
                editUserName = rootView.findViewById(R.id.edit_userName);
                boxPassword = rootView.findViewById(R.id.box_password);
                editPassword = rootView.findViewById(R.id.edit_password);
            }
        })
        .setOkButton("下一步", nextStepListener)
        .setCancelButton("取消")
        .setTitle("登录")
;
```
其中第二个参数为自定义的布局资源 id，第三个参数为布局绑定回调，其余方法与其他 Dialog 一致。

额外的，可自定义 FullScreenDialog 的背景颜色：
```
fullScreenDialog.setBackgroundColor(Color.WHITE);
```

## 定制化

### 自定义布局
对于任意一个对话框组件，Kongzue Dialog V3 提供了自定义布局功能，您可以使用一下代码来插入自定义布局：
```
//对于未实例化的布局：
MessageDialog.show(MainActivity.this, "提示", "这个窗口附带自定义布局", "知道了")
        .setCustomView(R.layout.layout_custom, new MessageDialog.OnBindView() {
            @Override
            public void onBind(MessageDialog dialog, View v) {
                //绑定布局事件，可使用v.findViewById(...)来获取子组件
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toast("点击了自定义布局");
                    }
                });
            }
        });
        
//对于已实例化的布局：
View customView;
MessageDialog.show(MainActivity.this, "提示", "这个窗口附带自定义布局", "知道了")
        .setCustomView(customView);
```

目前支持自定义子布局的有：消息对话框组件（MessageDialog）、底部菜单组件（BottomMenu）、输入框组件（InputDialog）、分享对话框（ShareDialog）和通知组件（Notification）

### 自定义对话框
Kongzue Dialog V3 提供了完全自定义对话框方便快速实现特殊效果的对话框样式。

![Kongzue Dialog V3 自定义对话框](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/img_dialog_v3_custom.png)

使用以下代码创建自定义对话框：
```
//对于未实例化的布局：
CustomDialog.show(MainActivity.this, R.layout.layout_custom_dialog, new CustomDialog.OnBindView() {
    @Override
    public void onBind(final CustomDialog dialog, View v) {
        ImageView btnOk = v.findViewById(R.id.btn_ok);
        
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.doDismiss();
            }
        });
    }
});

//对于已实例化的布局：
View customView;
CustomDialog.show(MainActivity.this, customView, new CustomDialog.OnBindView() {
    @Override
    public void onBind(final CustomDialog dialog, View v) {
        ImageView btnOk = v.findViewById(R.id.btn_ok);
        
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.doDismiss();
            }
        });
    }
});
```

其他方法
```
//全屏幕宽高
customDialog.setFullScreen(true);

//设置 CustomDialog 处于屏幕的位置
customDialog.setAlign(CustomDialog.ALIGN.BOTTOM)        //从屏幕底端出现
customDialog.setAlign(CustomDialog.ALIGN.TOP)           //从屏幕顶端出现
customDialog.setAlign(CustomDialog.ALIGN.DEFAULT)       //从屏幕中部出现
```
### 自定义对话框按钮背景（颜色）资源：
除 Material 风格外，iOS 和 Kongzue 风格支持自定义 drawable 的方式修改按钮背景（颜色）资源：

您可以通过以下方式全局指定自定义按钮背景资源：
```
//修改确定按钮背景资源：
DialogSettings.okButtonDrawable = getDrawable(R.drawable.btn_ok);
//其他按钮：
DialogSettings.cancelButtonDrawable = getDrawable(R.drawable.btn_cancel);
DialogSettings.otherButtonDrawable = getDrawable(R.drawable.btn_other);
```
也可以单独指定对话框的按钮背景资源：
```
//使用资源 id：
messageDialog.setOkButtonDrawable(resId);
//或直接使用 drawable：
messageDialog.setOkButtonDrawable(drawable);
```

drawable资源可按照如下方式设计：
btn_ok.xml
```
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@color/dialogButtonBlueLightPress" android:state_pressed="true" />
    <item android:drawable="@color/dialogButtonBlueLight" android:state_focused="false" android:state_pressed="false" />
    <item android:drawable="@color/dialogButtonBlueLight" android:state_focused="true" />
    <item android:drawable="@color/dialogButtonBlueLight" android:state_focused="false" />
</selector>
```
其中，dialogButtonBlueLight为默认颜色，dialogButtonBlueLightPress为按下时颜色，请在您的colors.xml资源文件中添加其颜色。

## 其他设置

### 通用功能
屏幕顶端、底部弹出
```
//从顶部弹出
Dialog.build(me)
    .setAlign(BaseDialog.ALIGN.TOP);   
    
//从底部弹出 
Dialog.build(me)
    .setAlign(BaseDialog.ALIGN.BOTTOM);  
```

显示时执行
```
dialog.setOnShowListener(onShowListener);
```

关闭时执行
```
dialog.setOnDismissListener(onDismissListener);
```

设置是否可以点击外部区域或“返回”按键关闭对话框：
```
dialog.setCancelable(boolean);       
```

设置“返回”按键监听
```
dialog.setOnBackClickListener(new OnBackClickListener() {
    @Override
    public boolean onBackClick() {
        toast("按下返回！");
        return true;        //return 结果代表是否拦截此事件
    }
});
```

使用自定义的 Dialog style
```
dialog.setCustomDialogStyleId(R.style.XXX);
```

### 文字样式
因文字样式牵扯的属性较多，因此提供了封装类 `TextInfo（com.kongzue.dialog.util.TextInfo）`来进行。

该类提供了以下属性进行设置：

| 属性 | 用途 | 默认值 |
| ------ | ------ | ------ |
| fontSize | 文字大小(单位：dp) | 值为-1时不生效 |
| gravity | 对齐方式 | Gravity.Left，值为-1时不生效 |
| fontColor | 文字颜色 | 值为1时不生效 |
| bold | 是否粗体 | - |

以上属性可通过对应的 get、set方法设置或获取

您可以直接进行 <a href="#全局配置">全局设置</a> 也可以单独对某个组件的标题、内容、按钮等进行设置：
```
MessageDialog.show(MainActivity.this, "提示", "这个窗口附带自定义布局", "知道了")
    .setTitleTextInfo(new TextInfo().setBold(true).setFontColor(Color.RED))     //设置标题文字样式
;
```

### 输入内容设置
对于输入对话框 InputDialog，提供额外的 `InputInfo（com.kongzue.dialog.util.InputInfo）` 工具类控制输入内容的属性，其中各属性介绍如下：

| 属性 | 用途 | 默认值 |
| ------ | ------ | ------ |
| MAX_LENGTH | 可输入最大长度 | 值为-1时不生效 |
| inputType | 输入类型 | 类型详见 android.text.InputType |
| textInfo | 文字样式 | null时不生效 |

您可以直接进行 <a href="#全局配置">全局设置</a> 也可以单独对某个输入对话框进行设置：
```
InputDialog.show(MainActivity.this, "提示", "请输入密码（123456）", "确定", "取消")
    .setInputInfo(new InputInfo()       //设置输入样式
        .setSelectAllText(true)                                     //默认选中全部文字
        .setMAX_LENGTH(6)                                           //最大允许6个字
        .setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)       //密码类型
        .setTextInfo(new TextInfo()     //设置文字样式
                             .setFontColor(Color.RED)               //颜色指定为红色
        )
;
```

### 监听事件
如果需要全局的控制所有对话框显示、隐藏触发事件，可以设置 <a href="#全局配置">全局设置</a> 中的 dialogLifeCycleListener 监听器，其中会返回所有对话框的生命周期管理，以便做相应处理：
```
DialogSettings.dialogLifeCycleListener = new DialogLifeCycleListener() {
    @Override
    public void onCreate(BaseDialog dialog) {
    
    }
    @Override
    public void onShow(BaseDialog dialog) {
    
    }
    @Override
    public void onDismiss(BaseDialog dialog) {
    
    }
}
```

要单独对某个对话框进行监听，可使用对应的 setOnShowListener(...) 及 setOnDismissListener(...) 进行处理，例如，在提示过后关闭本界面可以这样写：
```
TipDialog.show(MainActivity.this, "成功！", TipDialog.TYPE.SUCCESS).setOnDismissListener(new OnDismissListener() {
    @Override
    public void onDismiss() {
        finish();
    }
});
```

### 自定义背景
目前 MessageDialog、InpurDialog、TipDialog、WaitDialog 支持使用以下方法自定义背景资源：
```
dialog.setBackgroundResId(int resId);
```

## 一些建议

### 关于内存泄漏问题

由于采用了模态化的对话框展示模式、等待提示框延时关闭以及事件重绑定等技术，可能会被某些 BUG 检测软件定性为“内存泄漏”的问题，您可以通过以下语句彻底清空所有 Kongzue Dialog V3 使用的内存句柄：
```
BaseDialog.unload();
```
另外，从 3.0.7 版本起，我们对可能引用 Activity 的 Context 加入了弱引用，以尽可能保证它在 gc() 回收时能被正确释放。

### 其他建议

Kongzue Dialog V3 是基于 DialogFragment 的Dialog封装库，由于默认情况下 Android 会在屏幕横竖向切换时重启前台显示的 DialogFragment，这会不正常的触发 onDismiss() 事件，由于我们采用了模态化的对话框展示模式，在具有多个对话框等待序列的情况下可能造成下一个对话框被触发显示。

基于此问题的影响，我们建议您在 AndroidManifest.xml 中的 Activity 加入以下属性：
```
android:configChanges="orientation|keyboardHidden|screenSize"
```
这可以确保您的 Activity 不受重启影响自动适应横竖屏切换并保证 Kongzue Dialog V3 能够正常运行。

### 常见问题

**Q：如何检查是否支持即时模糊（Renderscript）？**

A：请执行 DialogSettings.checkRenderscriptSupport(context) 方法检查，如果支持，isUseBlur 会自动被设置为 true，反之会被关闭。

**Q：为什么不支持非Activity作为Context？为什么只支持AppCompatActivity而不支持普通Activity？**

A：不支持非 Activity 是因为 Android 在的限制，Dialog 必须基于 Activity 显示，并且受到 Activity 的生命周期管理。

如果想“凭空”显示出 Dialog，可以考虑使用透明主题的 Activity，再在其上显示 Dialog，即在 manifest 中设置 Activity 的 theme 为自定义的透明主题，具体代码如下：
```
<style name="activityTransparent" parent="Theme.AppCompat.Light.NoActionBar">
    <item name="android:windowBackground">@color/transparent</item>
    <item name="android:windowNoTitle">true</item>
    <item name="android:windowIsTranslucent">true</item>
</style>
```
对于为什么只支持 AppCompatActivity 而不支持普通 Activity，是因为需要使用到 SupportFragmentManager()，而它是 AppCompatActivity 中才能提供的。

**Q：Dialog不启动，不显示**

A：首先请检查您传入的 Context 是否为已回收的上下文索引，然后尝试使用 `BaseDialog.unload();` 清空内存队列，如果还是不显示，请提交 issues 返回问题，记得备注您的创建 Dialog 代码。

**Q：显示位置或大小异常**

A：您可能是用了第三方的屏幕适配方案，例如头条适配方案，此问题是适配方案导致的，请查询适配方案是否有解决办法，DialogV3 库主要支持标准适配方案，在第三方适配方案下可能存在显示问题。

**Q：API-29（AndroidQ）下，通知（Notification）组件无法显示**

A：经查证是 Google 在 API-29 下限制了反射功能导致的，Notification 组件是依赖反射修改 Toast 来完成跨域悬浮显示功能的，您可以修改项目 targetSdkVersion 为 28 来解决此问题。

## 混淆设置
为避免不必要的问题，可以将以下代码加入 proguard-rules.pro 文件中。
```
-keep class com.kongzue.dialog.** { *; }
-dontwarn com.kongzue.dialog.**

# 额外的，建议将 android.view 也列入 keep 范围：
-keep class android.view.** { *; }

# 如果有开启模糊效果，建议将 Renderscript 也列入 keep 范围：
-dontwarn android.support.v8.renderscript.**
-keep public class android.support.v8.renderscript.** { *; }

# AndroidX版本请使用如下配置：
-dontwarn androidx.renderscript.**
-keep public class androidx.renderscript.** { *; }
```

## 开源协议
```
Copyright Kongzue DialogV3

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## 更新日志：
v3.2.4.2(beta):
- 修复BottomMenu设置setMenuTextList(...)时出错的问题；
- 修复BaseDialog中存在的偶发性启动空指针问题（219行）；
- 修复BottomMenu在Material主题时，设置cancelable = false时依然可以下划关闭的问题；

v3.2.4:
- BottomMenu 新增 List<String> 构造方法；
- BottomMenu 新增 setMenuTextStringList(...) 设置菜单的方法；
- 修复 DialogHelper 引发的 BaseDialog 中的空指针异常；

v3.2.3:
- 优化对话框在全屏 Activity 上启动时保持隐藏状态栏、导航栏的显示方式；
- 修复 CustomDialog 全屏模式在挖孔屏设备上无法完全全屏的问题；
- 修复 FullscreenDialog 自定义布局无法滑动操作的问题；
- 修复 BottomMenu 在处于 MIUI 主题时的点击穿透问题；
- 新增 BottomMenu setCancelButtonDrawable(...) 方法可设置取消按钮样式；

v3.2.2:
- 新增 Notification.mode 通知实现方式修改，具体请参照章节 <a href="#通知">通知</a>；
- 所有组件的文本入参方式由 String 修改为 CharSequence，以支持更多的文本形式；
- 修复其他 bug；

v3.2.1:
- 修复 BottomDialog 以及 ShareDialog 底部导航栏折叠问题；

v3.2.0:
- 调整对齐方式，为所有非提示和等待的 Dialog 组件提供 setAlign(...) 自定义屏幕顶端/底部的额外设置；
- 新增 MIUI12 主题；
- 修复其他 bug；

v3.1.9:
- 增加全局设置，可修改对话框按钮背景资源；
- InputInfo 新增 selectAllText 设置，可实现启动对话框后默认选中已输入的文字；
- BottomMenu 新增 onCancelButtonClickListener 设置，可监听或拦截“取消”按钮点击事件；
- 修复部分情况下 InputDialog 对话框关闭输入法不消失的 bug；

v3.1.8:
- 完善 DialogSettings.checkRenderscriptSupport(context) 判断 Renderscript 支持性逻辑；
- CustomDialog 创建布局默认使用 WRAP_CONTENT 的 LayoutParams；
- 修复 TipDialog 或 WaitDialog 在执行 dismiss() 方法时可能出现的异常；
- 修复 BaseDialog 存在的 dialog 空指针异常；

v3.1.7:
- BottomMenu 的 Material 和 Kongzue 风格新增暗色模式；
- FullScreenDialog 新增 setBackgroundColor(colorInt) 设置背景颜色；
- FullScreenDialog 新增暗色模式；
- ShareDialog 新增暗色模式；
- 修复 TipDialog 启动时可能触发的空指针异常以及自定义布局时可能存在的子布局未卸载异常；
- 修复 onBackClickListener 可能触发两次的bug；
- 修复执行 doDismiss() 方法时可能触发的空指针异常；

v3.1.6:
- 新增 FullScreenDialog 全屏对话框；
- Dialog 组件新增 setOnBackClickListener(onBackClickListener) 可监听“返回”按键，并允许阻止“返回”按键操作；
- 改进刘海屏适配，Notification 通知组件和 FullScreenDialog 全屏对话框组件的背景部分可实现刘海部分的完美沉浸；
- 修复 InputDialog 在 Material 主题时开启 DialogSettings.autoShowInputKeyboard 设置后键盘不自动弹出的问题；
- 修复 InputDialog 在 Material 主题时输入框的主题颜色适配问题，已知问题：因 Google 在新版本系统中增加反射限制，已知输入框光标颜色暂时无法适配颜色；
- 对 BottomMenu、ShareDialog 代码重新进行整理；

v3.1.5:
- 新增 DialogSettings.autoShowInputKeyboard 设置开启时，InputDialog 将自动弹出输入法；
- 修复 WaitDialog 等待对话框动画在切换至后台再切换至前台恢复显示后消失的问题；
- 修复 BottomMenu 在屏内虚拟导航按键的设备上隐藏虚拟导航按键情况下使用时底部依然存在虚拟导航栏高度的问题；
- 修复 Notification 在部分刘海屏设备上存在的顶部额外安全区高度空留问题；

v3.1.4:
- BottomMenu 新增标题字体控制 menuTitleInfo 和菜单字体控制 menuTextInfo，也可通过全局（DialogSettings）设置；
- 新增模态模式开关 `DialogSettings.modalDialog`，关闭后对话框会以正常方式启动；
- 再次优化内存泄漏问题，对 DialogHelper 的释放问题进行了改进；
- 修复了 customDialog.getAlign() 空指针异常的问题；
- 修复 ShareDialog 在华为设备上使用 Material 风格时的点击按钮无效问题；

v3.1.3:
- CustomDialog 新增 `setAlign(Align)` 方法，可设置显示在顶部、底部或默认位置；
- InputDialog 支持多行内容输入，请通过 `.setInputInfo(new InputInfo().setMultipleLines(true))` 设置开启；
- 所有 Dialog 支持通过 `setCustomDialogStyleId(styleId)` 自定义 Dialog 样式；
- DialogSettings 新增 `init(Context)` 方法，用于初始化 BaseDialog 队列并检查 Renderscript 兼容性，此方法建议在 Activity 的 onCreate 执行；
- 增强日志打印信息以协助排查可能存在的问题；

v3.1.2:
- 底部菜单 BottomMenu 中，Material 风格升级为 Android Q 风格；
- 底部菜单 BottomMenu 和分享对话框 ShareDialog 中，Material 风格与 Kongzue 风格支持底部导航栏沉浸式；
- 底部菜单 BottomMenu 在内容过多的情况下只占用2/3屏幕高度，且支持列表滚动；
- 修复底部菜单 BottomMenu 中，Material 风格在部分华为手机上的触控兼容性问题；
- 修复了TipDialog存在的关闭问题；
- 修复了输入对话框偶发设置inputInfo空指针问题；

v3.1.1:
- 新增 DialogSettings.defaultCancelButtonText 可设置 BottomMenu 和 ShareDialog 默认“取消”按钮的文字；
- 新增 DialogSettings.tipBackgroundResId 可设置 TipDialog 和 WaitDialog 的背景资源；
- 新增 DialogSettings.tipTextInfo 可设置 TipDialog 和 WaitDialog 文字样式；
- 修复 ShareDialog 可能存在的 Android 5- 版本崩溃问题；
- 修复 InputDialog 可能存在的崩溃问题；

v3.1.0:
- 用 dismissAllowingStateLoss() 替换之前使用的 dismiss();

v3.0.9:
- 修复了 TipDialog/WaitDialog 进行文本长度变化时背景大小未发生变化的 bug；
- 修复了 OnShowListener 触发时机不正确的问题；
- 调整了 OnShowListener 的参数为对应的 BaseDialog 对象；
- MessageDialog、InpurDialog、TipDialog、WaitDialog 新增 setBackgroundResId(int resId) 方法用于自定义对话框背景资源；

v3.0.8:
- CustomDialog 新增 build(...) 方法；
- CustomDialog 新增 setFullScreen(boolean) 控制是否全屏；
- InputDialog 新增 getEditTextView() 可以自行对文本框进行一些事件或属性处理；
- 最低 API 支持版本从 19 下降至 17；

v3.0.7:
- 对所有可能造成内存泄漏的 Activity 引用使用了弱引用处理；
- 新增其他建议说明；

v3.0.6:
- 自定义布局在 MessageDialog、InputDialog 的 Material 风格使用时宽度未顶头的问题修复；
- 自定义布局在 MessageDialog、InputDialog 的 Material 风格使用时存在的未 removeView 问题修复；
- 重复旋转屏幕导致空指针问题修复；

v3.0.5:
- BottomMenu 和 ShareDialog 的 IOS 风格新增暗色模式，其他风格后续跟进；
- 根据 IOS13 实际效果对所有暗色模式颜色进行了调整；
- 修复了切换 Activity 导致已经 dismiss 的 Dialog 重复出现的 bug；
- 修复了 dismissEvent 可能引发的空指针异常； 

v3.0.4:
- 修复多次显示 TipDialog 后设置 dismissListener 不触发的问题。

v3.0.3:
- 修复 TipDialog 不会自动消失的问题。

v3.0.2:
- 修复切换后台可能导致的序列化对话框恢复显示队列问题；
- 修复切换后台可能导致等待、提示对话框出现的无法关闭或显示的问题；
- 修复等待提示对话框可能存在的异步线程空指针异常；

v3.0.1:
- 修复 MessageDialog 自定义布局事件绑定不执行的问题；

v3.0.0:
- 全新发布。
