package com.duanglink.mipush;
import android.os.Build;
import androidx.annotation.Nullable;

import android.widget.Toast;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by wangheng on 2017/12/4.
 */
public class MIPushMoudle extends ReactContextBaseJavaModule {
    public static final String EVENT_RECEIVE_REMOTE_NOTIFICATION = "receiveRemoteNotification";
    public static final String EVENT_RECEIVE_CLICK_NOTIFICATION = "receiveClickNotification";

    private static ReactApplicationContext mRAC;
    public static MIPushManager pushManager;
    public static ArrayList<String> eventList = new ArrayList<String>();
    public MIPushMoudle(ReactApplicationContext reactContext) {
        super(reactContext);
        mRAC=reactContext;
    }

    public static boolean isInit() {
        return mRAC != null && mRAC.hasActiveCatalystInstance();
    }
  @Override
    public String getName() {
        return "MIPushModule";
    }

    @Override
    public boolean canOverrideExistingModule() {
        return true;
    }

    public static void sendEvent(String eventName, @Nullable WritableMap params){
        mRAC.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    public static void sendEvent(String eventName, @Nullable String params){
        if(isInit()) {
            mRAC.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
        }
    }

    @ReactMethod
    public void  setAlias(String alias){
        if(pushManager!=null){
            pushManager.setAlias(mRAC,alias);
        }
    }

    @ReactMethod
    public void  unsetAlias(String alias){
        if(pushManager!=null){
            pushManager.unsetAlias(mRAC,alias);
        }
    }

  @ReactMethod
  public void registerEvent(String eventName) {
        eventList.add(eventName);
  }

  public static boolean isEventRegistered(String eventName) {
    return eventList.contains(eventName);
  }

    @ReactMethod
    public void  setTags(String tags){
        if(pushManager!=null){
            pushManager.setTags(mRAC,tags);
        }
    }

    @ReactMethod
    public void  unsetTags(String tags){
        if(pushManager!=null){
            pushManager.unsetTags(mRAC,tags);
        }
    }

    @ReactMethod
    public void  getClientId(final Callback callback){
        String clientId="";
        if(pushManager!=null){
            clientId= pushManager.getClientId(mRAC);
        }
        callback.invoke(clientId);
    }

    @ReactMethod
    public void getDeviceInfo(final Callback callback) throws JSONException{
        JSONObject info = new JSONObject();
        info.put("BRAND", Build.BRAND);
        info.put("DEVICE", Build.DEVICE);
        info.put("MODEL", Build.MODEL);
        info.put("TAGS", Build.TAGS);
        callback.invoke(info.toString());
    }

    //小米推送测试
    @ReactMethod
    public void sendXiaoMiPushMessage(String cid) throws IOException,JSONException{

    }

    private void showToast(String msg){
        Toast.makeText(mRAC,  msg , Toast.LENGTH_SHORT).show();
    }
}
