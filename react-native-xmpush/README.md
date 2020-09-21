# react-native-mipush

该项目基于小米推送，使用前，请先在小米开发者后台注册app，并获取对应的appid与appkey。

开发环境：xcode8、react-native

##作者
  本库iOS部分参考[react-native-xmpush](https://github.com/a289459798/react-native-mipush)实现，Android部分参考[react-native-mixpush](https://github.com/shuidaocar/react-native-mixpush)实现。
##安装

```
// 本库仅在RN0.62版本项目测试，未在其他版本项目测试，如在其他版本使用请自行测试
npm install react-native-mipush --save 

```

##android

项目的`AndroidManifest.xml`文件中增加下面代码：

```xml
// manifest节点下添加
<permission android:name="${PACKAGE_NAME}.permission.MIPUSH_RECEIVE" android:protectionLevel="signature" />
<uses-permission android:name="${PACKAGE_NAME}.permission.MIPUSH_RECEIVE" />
 ```
 ```app/build.gradle
  android {
    ···
    buildTypes {
      ···
      release {
        ···
        buildConfigField("String", "__MIPUSH_APP_ID", "\"小米推送APP_ID\"")
        buildConfigField("String", "__MIPUSH_APP_KEY", "\"小米推送APP_KEY\"")
      }
      debug {
        ···
        buildConfigField("String", "__MIPUSH_APP_ID", "\"小米推送APP_ID\"")
        buildConfigField("String", "__MIPUSH_APP_KEY", "\"小米推送APP_KEY\"")
      }
    }
  }
 ```
 ```java
// MainActivity中注册并启动推送：

import com.duanglink.mipush.MIPushManager;
import com.duanglink.mipush.MIPushMoudle;

MIPushManager mipush = new MIPushManager(BuildConfig.__MIPUSH_APP_ID, BuildConfig.__MIPUSH_APP_KEY);
      MIPushMoudle.pushManager = mipush;
      mipush.registerPush(this.getApplicationContext());

```


##ios

==必须通过xcode8开发的项目==

添加所需依赖库：
- UserNotifications.framework
- libresolv.dylib(tbd)
- libxml2.dylib(tbd)
- libz.dylib(tbd)
- SystemConfiguration.framework
- MobileCoreServices.framework
- CFNetwork.framework
- CoreTelephony.framework

在`target`的`Capabilities`选项卡打开`Push Notifications`

打开工程下`Info.plist`文件为源代码形式打开，添加以下信息:

```
<key>MiSDKAppID</key>
<string>1000888</string>
<key>MiSDKAppKey</key>
<string>500088888888</string>
<key>MiSDKRun</key>
<string>Online</string>
```

修改 AppDelegate.m 文件：

```objective-c
...
#import "RCTMIPushModule.h"
#import "RNCPushNotificationIOS.h"
...

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
	...
  	[RCTMIPushModule application:application didFinishLaunchingWithOptions:launchOptions];
    ...
}

- (void)application:(UIApplication *)application didRegisterUserNotificationSettings:(UIUserNotificationSettings *)notificationSettings
{

  [RCTMIPushModule application:application didRegisterUserNotificationSettings:notificationSettings];
  ...
}

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
  [RCTMIPushModule application:application didRegisterForRemoteNotificationsWithDeviceToken:deviceToken];
  ...
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)notification
{
  [RCTMIPushModule application:application didReceiveRemoteNotification:notification];
  ...
}

- (void)application:(UIApplication *)application didReceiveLocalNotification:(UILocalNotification *)notification {
  [RCTMIPushModule application:application didReceiveLocalNotification:notification];
  ...
}

// ios 10
// 应用在前台收到通知
- (void)userNotificationCenter:(UNUserNotificationCenter *)center willPresentNotification:(UNNotification *)notification withCompletionHandler:(void (^)(UNNotificationPresentationOptions))completionHandler {
  [RCTMIPushModule userNotificationCenter:center willPresentNotification:notification withCompletionHandler:completionHandler];
  ...
}

// 点击通知进入应用
- (void)userNotificationCenter:(UNUserNotificationCenter *)center didReceiveNotificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void (^)())completionHandler {
  [RCTMIPushModule userNotificationCenter:center didReceiveNotificationResponse:response withCompletionHandler:completionHandler];
  ....
  completionHandler();
}

```


##RN中使用

Demo：

**android**

```js
...

import MIPush from 'react-native-mipush';

import { NativeAppEventEmitter } from 'react-native';

// 监听事件
componentDidMount() {
  NativeAppEventEmitter.addListener(
    'receiveRemoteNotification',
    (notification) => {
      Alert.alert('透传消息到达事件监听',JSON.stringify(notification));
    }
  );
  NativeAppEventEmitter.addListener(
    'receiveClickNotification',
    (notification) => {
      Alert.alert('通知栏消息点击事件监听',JSON.stringify(notification));
    }
  );
}

// 移除监听事件
componentWillUnmount() {
  NativeAppEventEmitter.removeListener('receiveClickNotification');
  NativeAppEventEmitter.removeListener('receiveRemoteNotification');
}
// React-Native客户端方法说明：

MixPush.setAlias(alias); //设置别名<br>
MixPush.unsetAlias(alias); //取消设置别名<br>
MixPush.setTags(tags); //设置用户标签<br>
MixPush.unsetTags(tags); //取消设置用户标签<br>
MixPush.getClientId(); //获取客户端ID<br>  

```

**ios**

```js
...
componentWillUnmount() {

    MIPush.unsetAlias("bbbbbb");
    MIPush.removeEventListener("notification");
  }

  componentDidMount() {

    MIPush.setAlias("bbbbbb");

	MIPush.setBadgeNumber(0);   // 每次进入app将角标设置为0
    MIPush.addEventListener("notification", (notification) => {

      console.log("app接收到通知:", notification);
      
      // 弹出确认框
    });

    MIPush.getInitialNotification((notification) => {

      console.log("app关闭时获取点击通知消息:", notification);
	  // 弹出确认框
    });

  }
  
  /**
 * 设置别名
 * @param text
 */
setAlias(text);

/**
 * 注销别名
 * @param text
 */
unsetAlias(text);

/**
 * 设置主题,类似tag
 * @param text
 */
subscribe(text);

/**
 * 注销主题
 * @param text
 */
unsubscribe(text);

/**
 * 设置账号,一个账号需要多台设备接收通知
 * @param text
 */
setAccount(text);

/**
 * 注销账号
 * @param text
 */
unsetAccount(text);

/**
 *
 * @param type
 * ios :
 * notification => 监听收到apns通知
 * localNotification => 监听收到本地通知
 * register => 注册deviceToken 通知
 *
 * @param handler
 */
addEventListener(type, handler);

removeEventListener(type);

/**
 * 发送一个本地通知
 * @param notification
 */
presentLocalNotification(notification);

/**
 * 清除指定通知
 * @param notifyId
 * ios : userInfo
 * android : id
 */
clearNotification(notifyId);

/**
 * 清除所有通知
 */
clearNotifications();

/**
 * 设置角标,仅支持ios
 * @param num
 */
setBadgeNumber(num);

/**
 * 通过点击通知启动app
 * @param handler
 */
getInitialNotification(handler);

```