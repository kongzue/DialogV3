# Kongzue Dialog V2升级注意事项

Kongzue Dialog V3 重写了全部代码，且尽可能与 V2 版本的使用习惯保持一致，但依然存在很大的差异，此文档将帮助你更好的了解本次升级。

## 为什么要升级到 V3

Kongzue Dialog V2 代码最早提交于 2017 年 6 月 1 日，当时采用的是标准的 AlertDialog 作为 Dialog 实现主体，因原生 AlertDialog 构建复杂同时修改样式不够灵活，因此我编写了 Kongzue Dialog V2 目的是快速构建，同时提供更为丰富的功能和能够轻松完成定置化需求，而 AlertDialog 一直以来存在不少问题，诸如当其依赖的 Activity 被先于 AlertDialog 关闭时会发生 WindowLeaked 异常，该异常表示 AlertDialog 必须先于 Activity 关闭，为解决这些问题，V2 版本在升级过程中修改更换使用了 DialogFragment，而为保证快速完成更换，修改的方式仅仅是将创建好的 AlertDialog 交给 DialogFragment 来处理事务，因此整个框架越发臃肿，这是升级 V3 版本的主要原因。

其次 V2 版本提供的很多方法在累计 160+ 次更新迭代的过程中并没有做到完美，产生了很多冗余的问题，因此我抽时间重新整理了所有代码，并构建了 Kongzue Dialog V3 版本。

## 与 V2 相比有哪些不同

V3 版本的库更为灵活，构建方法（show方法）相较于 V2 减少了很多参数，将更多的参数移至 set 方法中处理，build 方法的职能更为明确，且完善了事件处理，操作性更加灵活。

V3 版本重新整理了对话框类型，之前的 SelectDialog、MessageDialog 合并为 MessageDialog 一个组件来处理，其中提供了一、二和三个可选显示的按钮，减少库的代码体积。

InputDialog 也改为基于继承 MessageDialog 实现的新组件，增强了代码的重复利用率。

WaitDialog 和 TipDialog 已被合并为 TipDialog，这样做的主要目的是方便等待提示框结束后立即显示结果提示的过程不再打断，而是在本界面中直接切换显示，视觉效果更好。

组件全员直接使用 DialogFragment 实现，且支持了事件重绑定机制，即便 Activity 意外被系统重启也不用担心对话框组件出现问题。

Pop 组件已被暂时移除，不过不确定之后会不会回归。

## 若要升级到 V3，我需要做哪些事

### 不再提供的组件

首先请确认您是否正在使用 V2 版本中的 Pop 组件，该组件暂时在 V3 库中并不提供。

其次，SelectDialog 在 V3 版本中被合并为 MessageDialog，现在您直接在 MessageDialog 中设置一个或多个按钮即可实现多按钮对话框。其次，在 V3 库中部分主题还支持纵向按钮排列，具体请参考[正文文档](https://github.com/kongzue/DialogV3)

### 需要修改的部分

您需要阅读 V3 库的 [说明文档](https://github.com/kongzue/DialogV3) 以了解 V3 库的构建方式，并对您当前项目中的对话框组件构建方式进行修改。

V3 库的构建方式中，不再支持在参数中添加回调监听器（例如确定、取消按钮的点击事件），需要使用 set 方法设置，这是为了增加代码的灵活性作出的修改。

例如 V2 版本的代码：
```
MessageDialog.show(context, "消息提示框", "用于提示一些消息", "知道了", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
    }
});
```
在 V3 版本需要修改为：
```
MessageDialog.show(context, "消息提示框", "用于提示一些消息", "知道了")
        .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(BaseDialog baseDialog, View v) {
                return false;               
            }
        });
```
灵活性主要体现在，1是你不必再设置空代码的监听器，2是统一使用的 OnDialogButtonClickListener 可以通过 return 值直接判断点击按钮后是否关闭对话框，3是额外的增加了可以直接设置资源 id——例如 R.string.xxx 的调用接口。

V3 库与 V2 版本的回调方法不一样，例如 InputDialog 的确定按钮回调可设置成 OnInputDialogButtonClickListener 来直接获取值（请参考[正文文档](https://github.com/kongzue/DialogV3)）。

### 新的特性

V3 库相比 V2 库在代码灵活性上做了很多调整，例如您可以通过上述方法创建一个消息框，若不习惯，您同样可以使用“流式代码”的风格创建同样的对话框：
```
MessageDialog.build(context)
        .setStyle(DialogSettings.STYLE.STYLE_MATERIAL)
        .setTheme(DialogSettings.THEME.DARK)
        .setTitle("消息提示框")
        .setMessage("用于提示一些消息")
        .setOkButton("知道了", new OnDialogButtonClickListener() {
            @Override
            public boolean onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了知道了！", Toast.LENGTH_SHORT).show();
                return false;
            }
        })
        .show();
```
这样同时也可以做更多临时设置，当然直接使用静态方法 show(...) 创建的方式也支持，综上所述您的代码编写方式将具有更大的灵活性。

额外的，在例如 setOkButton(...)、setCancelButton(...)、setOtherButton(...) 三个按钮的设置方法中，您可以花式组合按需求进行不同设置：
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
同一个方法将具有更为灵活的应用方式。

### 最后

感谢您一直以来对 Kongzue Dialog 的支持，如有任何问题可以通过提交 Issues 或者加 QQ 群 271127803 与我取得联系。
