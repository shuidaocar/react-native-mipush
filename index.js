/**
 * Created by zhangzy on 16/7/27.
 */

'use strict';

import {
    NativeModules,
    Platform,
    NativeEventEmitter,
} from 'react-native';

const MIPushModule = NativeModules.MIPushModule;
import PushNotificationIOS from '@react-native-community/push-notification-ios'
/**
 * 获取app的版本名称\版本号和渠道名
 */
class MIPushIOS extends NativeEventEmitter {
    // 构造
    constructor(props) {
        super(MIPushModule);
        // 初始状态
        this.state = {};
    }

    /**
     * 设置别名
     * @param text
     */
    setAlias(text) {

        MIPushModule.setAlias(text);
    }

    /**
     * 注销别名
     * @param text
     */
    unsetAlias(text) {

        MIPushModule.unsetAlias(text);
    }

    /**
     * 设置主题,类似tag
     * @param text
     */
    subscribe(text) {

        MIPushModule.subscribe(text);
    }

    /**
     * 注销主题
     * @param text
     */
    unsubscribe(text) {

        MIPushModule.unsubscribe(text);
    }

    /**
     * 设置账号,一个账号需要多台设备接收通知
     * @param text
     */
    setAccount(text) {

        MIPushModule.setAccount(text);
    }

    /**
     * 注销账号
     * @param text
     */
    unsetAccount(text) {

        MIPushModule.unsetAccount(text);
    }

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
    addEventListener(type, handler) {
        switch (type) {
            case 'notification':
            case 'localNotification':
            case 'register':
                PushNotificationIOS.addEventListener(type, handler);
                break;
            default:
                this.addListener(type, handler);
                break;
        }
    }

    removeEventListener(type) {
        switch (type) {
            case 'notification':
            case 'localNotification':
            case 'register':
                PushNotificationIOS.removeEventListener(type);
                break;
            default:
                this.removeListener(type);
                break;
        }
    }

    /**
     * 发送一个本地通知
     * @param notification
     */
    presentLocalNotification(notification) {
        PushNotificationIOS.presentLocalNotification({
            alertBody: notification.alertBody,
            alertAction: '查看',
            category: 'push',
            userInfo: notification.userInfo,
        });
    }

    /**
     * 清除指定通知
     * @param notifyId
     * ios : userInfo
     * android : id
     */
    clearNotification(notifyId) {
        PushNotificationIOS.cancelLocalNotifications(notifyId);
    }

    /**
     * 清除所有通知
     */
    clearNotifications() {
        PushNotificationIOS.cancelAllLocalNotifications();
    }

    /**
     * 设置角标,仅支持ios
     * @param num
     */
    setBadgeNumber(num) {
        PushNotificationIOS.setApplicationIconBadgeNumber(num);
    }

    /**
     * 通过点击通知启动app
     * @param handler
     */
    getInitialNotification(handler) {
        PushNotificationIOS.getInitialNotification().then(handler);
    }
}

class MIPushAndroid extends NativeEventEmitter {
    constructor(props) {
        super(MIPushModule);
        // 初始状态
        this.state = {};
    }
    static EVENT_RECEIVE_REMOTE_NOTIFICATION = "receiveRemoteNotification";
    static EVENT_RECEIVE_CLICK_NOTIFICATION = "receiveClickNotification";

    /**
   * 设置别名
   * @param text
   */
    setAlias(text) {

        MIPushModule.setAlias(text);
    }

    /**
     * 注销别名
     * @param text
     */
    unsetAlias(text) {

        MIPushModule.unsetAlias(text);
    }

    onClickNotification = (callback) => {
        new NativeEventEmitter().addListener(
            MIPushAndroid.EVENT_RECEIVE_CLICK_NOTIFICATION,
            callback
        );
        MIPushModule.registerEvent(MIPushAndroid.EVENT_RECEIVE_CLICK_NOTIFICATION);
    }

    onRemoteNotification = (callback) => {
        new NativeEventEmitter().addListener(
            MIPushAndroid.EVENT_RECEIVE_REMOTE_NOTIFICATION,
            callback
        );
        MIPushModule.registerEvent(MIPushAndroid.EVENT_RECEIVE_REMOTE_NOTIFICATION);
    }
}

module.exports = Platform.OS === 'ios' ? new MIPushIOS() : new MIPushAndroid();
