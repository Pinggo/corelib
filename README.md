#### 使用
1. 添加JitPack仓库到项目根目录

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
2. 添加依赖

```
implementation 'com.github.Pinggo:corelib:v0.0.1'
```
#### 概览
包含3个模块：commfunc、commlib、commui
- commfunc:多媒体、nfc、二维码
- commlib：总体上是rxjava+retrofit+mvp结构。包含基类封装、mvp封装、recyclerview封装、retrofit简单封装、全局异常处理、全局网络状态监听
- commui:一些常用的自定义控件，包含加载的dialog、右边弹出选择框、分段选择器、状态切换管理
