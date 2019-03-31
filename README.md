# Kongzue Dialog V3
🚧施工中...

## 施工预告：

1，全组件DialogFragment化，且支持横竖屏任意切换不发生崩溃或其他问题（主要针对界面重启问题做了事件重绑定处理）

2，新特性支持和完善，例如三按钮的对话框（支持纵向排列）等

3，模态化及一些优良特性继续保留，WaitDialog新支持衔接显示

还期待什么欢迎提Issues给我，讨论群：271127803

### Kongzue Dialog V3 的优势

#### 1，不会发生 WindowLeaked 错误
一般情况下直接创建的 Dialog 或 AlertDialog 启动后，一旦其依赖的 Activity 优先于 Dialog 关闭，则会抛出 android.view.WindowLeaked 错误。

这对于普通对话框还好处理，只需要在 Activity 的 onPause() 或 onDestroy() 中对 Dialog.dismiss(); 但对于可能出现的，诸如等待提示框、提醒框等 Dialog，很容易发生此问题。

Google 现已推荐使用 DialogFragment 来代替普通 Dialog 使用。

Kongzue Dialog 组件 V3 全部采用 DialogFragment 来实现对话框组件，且创造对话框更为简单方便，只需简单配置即可上手，也不会出现 WindowLeaked 错误。

#### 2，横竖屏切换、分屏大小切换、甚至未来的折叠屏，都没有问题！
已知 Android 系统在发生横竖屏切换、分屏大小切换、未来的折叠屏、窗口化等界面大小发生变化的情况时若不进行处理一般都会销毁当前 Activity 重新创建，但这会导致我们已经弹出的 Dialog 也发生重启。

重启最大的麻烦在于需要对重启后的 Dialog 进行原状态恢复，即事件重绑定，包括 Dialog 的样式、相关事件监听器等都需要重新设置，使用 Kongzue Dialog V3 您就不需要再担心这些麻烦的问题了，Kongzue Dialog V3 会自动重建被销毁的 Dialog，而无需您做任何处理。

#### 3，目前市面上最像 iOS 风格的对话框组件
Kongzue Dialog V3 依然会像第二代一样提供多种主题风格选择，且更为强大，iOS 风格自带高斯模糊让您再也无需担心产品经理和设计师放飞自我。

功能强大且真正的实时高斯模糊效果让您的程序更具逼格，同时我们也比苹果更早提供了 iOS 风格对话框夜间模式，若您正在使用暗色的程序设计，这款与您更搭！

我们的组件也会提供许许多多的接口供您自定义对话框的每一点细节，方便而快捷，迅速构建您的程序。