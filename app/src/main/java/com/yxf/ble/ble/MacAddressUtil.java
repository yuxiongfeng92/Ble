package com.yxf.ble.ble;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.yxf.ble.App;

/**
 * Created by yuxiongfeng.
 * Date: 2019/5/14
 */
public class MacAddressUtil {
    public String getMacAddress() {
        String macAddress = null ;
        WifiManager wifiManager =
                (WifiManager) App.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = ( null == wifiManager ? null : wifiManager.getConnectionInfo());

        if (!wifiManager.isWifiEnabled())
        {
            //必须先打开，才能获取到MAC地址
            wifiManager.setWifiEnabled( true );
            wifiManager.setWifiEnabled( false );
        }
        if ( null != info) {
            macAddress = info.getMacAddress();
        }
        return macAddress;
    }
}
