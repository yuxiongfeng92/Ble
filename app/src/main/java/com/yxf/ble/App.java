package com.yxf.ble;

import android.app.Application;
import android.content.Context;

import com.yxf.ble.ble.BleManager;

/**
 * Created by yuxiongfeng.
 * Date: 2019/5/14
 */
public class App  extends Application {
    private static App context;
    @Override
    public void onCreate() {
        super.onCreate();
        BleManager.getInstance().init(this);
        context=this;
    }

    public static Context getContext(){
        return context;
    }
}
