package com.yxf.ble.ble;

import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.os.ParcelUuid;

/**
 * Created by yuxiongfeng.
 * Date: 2019/5/14
 */
public class BleUtil {
    public static final ParcelUuid testBleServerUUID = ParcelUuid.fromString("fda50693-a4e2-4fb1-afcf-c6eb07647825");

    /**
     * advertiseMode说明：广播发送的频率
     * ADVERTISE_MODE_LOW_LATENCY 100ms
     * ADVERTISE_MODE_BALANCED  250ms
     * ADVERTISE_MODE_LOW_POWER 1s
     * 蓝牙广播的设置（模式 时长  是否可连接  信号强度）
     *
     * @return
     */
    public static AdvertiseSettings createAdvSettings() {
        AdvertiseSettings.Builder builder = new AdvertiseSettings.Builder();
        builder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY);//平衡模式
        builder.setConnectable(false);//设置广播是否可连接
        builder.setTimeout(0);//设置为0表示无限发送
        builder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH);//广播的信号强度
        return builder.build();
    }


    public static AdvertiseData createAdvertiseData() {
        AdvertiseData.Builder builder = new AdvertiseData.Builder();
//        byte[] serverData = new byte[]{0x07, (byte) 0xFF, 0x50, 0x72, 0x6F, 0x74, 0x6F, 0x6E};
//        builder.addServiceData(bleServerUUID, serverData);
        byte[] serverData = new byte[]{0x6F, 0x74, 0x6F, 0x6E};
        builder.addManufacturerData(0x7250, serverData);
        builder.setIncludeDeviceName(true);
        AdvertiseData adv = builder.build();
        return adv;
    }
}
