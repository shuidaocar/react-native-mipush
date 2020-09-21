/**
 * Created by wangheng on 2017/11/22.
 */
package com.duanglink.mipush;

import android.content.Context;

import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

public class MIPushManager {
    public static final String NAME = "mipush";
    private String appId;
    private String appKey;

    public MIPushManager(String appId, String appKey) {
        this.appId = appId;
        this.appKey = appKey;
    }

    public void registerPush(Context context) {
        MiPushClient.registerPush(context.getApplicationContext(), appId, appKey);
    }

    public void unRegisterPush(Context context) {
        unsetAlias(context, null);
        MiPushClient.unregisterPush(context.getApplicationContext());
    }

    public void setAlias(Context context, String alias) {
        if (!MiPushClient.getAllAlias(context).contains(alias)) {
            MiPushClient.setAlias(context, alias, null);
        }
    }

    public void unsetAlias(Context context, String alias) {
        List<String> allAlias = MiPushClient.getAllAlias(context);
        for (int i = 0; i < allAlias.size(); i++) {
            MiPushClient.unsetAlias(context, allAlias.get(i), null);
        }
    }

    public void setTags(Context context, String... tags) {
        for (String tag : tags){
            MiPushClient.subscribe(context, tag, null);
        }

    }

    public void unsetTags(Context context, String... tags) {
        for (String tag : tags) {
            MiPushClient.unsubscribe(context, tag, null);
        }
    }

    public String getClientId(Context context) {
       return  MiPushClient.getRegId(context);
    }

    public String getName() {
        return NAME;
    }
}
