package com.yxf.ble.ble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by yuxiongfeng.
 * Date: 2019/5/14
 */
public class BleManager {

    public static final String TAG = "bleManager";

    private Context mCtx;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private Toast mToast;
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;

    /**
     * 静态内部类实现单例（优势：懒加载，线程安全，高效）
     */
    public static class TempInner {
        private static final BleManager instance = new BleManager();
    }

    public static BleManager getInstance() {
        return TempInner.instance;
    }

    public void init(Context context) {
        this.mCtx = context;
        mToast = Toast.makeText(mCtx, "", Toast.LENGTH_SHORT);
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) mCtx.getSystemService(Context.BLUETOOTH_SERVICE);
        }

        if (mBluetoothManager != null && mBluetoothAdapter == null) {
            mBluetoothAdapter = mBluetoothManager.getAdapter();
        }
    }

    /**
     * 用于activity调用处初始化
     *
     * @param activity    当前activity
     * @param requestCode 打开蓝牙用于回调的requestCode
     */
    public void initActity(Activity activity, int requestCode) {

        if (!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(activity, "不支持ble", Toast.LENGTH_LONG).show();
            activity.finish();
            return;
        }

        if (mBluetoothAdapter == null) {
            Toast.makeText(activity, "不支持ble", Toast.LENGTH_LONG).show();
            activity.finish();
            return;
        }

        mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();

        if (mBluetoothLeAdvertiser == null) {
            showToast("the device not support peripheral");
            Log.e(TAG, "the device not support peripheral");
            activity.finish();
            return;
        }

        //打开蓝牙
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, requestCode);
        }

    }

    /**
     * 发送广播数据包
     */
    public void startAdvertising() {

        if (mBluetoothManager != null && mBluetoothAdapter == null) {
            mBluetoothAdapter = mBluetoothManager.getAdapter();
        }

        //获取BluetoothLeAdvertiser，BLE发送BLE广播用的一个API
        if (mBluetoothLeAdvertiser == null) {
            mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();
        }

        try{
            mBluetoothLeAdvertiser.startAdvertising(BleUtil.createAdvSettings(),BleUtil.createAdvertiseData(),mAdvCallback);
        }catch (Exception e){
            Log.v(TAG, "Fail start advertise");
        }

    }

    /**
     * 停止广播发送
     */
    public void stopAdvertising() {
        //关闭BluetoothLeAdvertiser，BluetoothAdapter，BluetoothGattServer
        if (mBluetoothLeAdvertiser != null) {
            mBluetoothLeAdvertiser.stopAdvertising(mAdvCallback);
            mBluetoothLeAdvertiser = null;
        }
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter = null;
        }
    }

    /**
     * 发送广播的回调
     */
    private AdvertiseCallback mAdvCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
            showToast("Advertise Start Success");
            if (settingsInEffect != null) {
                Log.d(TAG, "onStartSuccess TxPowerLv=" + settingsInEffect.getTxPowerLevel() + " mode=" + settingsInEffect.getMode() + " timeout=" + settingsInEffect.getTimeout());
            } else {
                Log.d(TAG, "onStartSuccess, settingInEffect is null");
            }
        }

        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            showToast("Advertise Start Fail");
            String description;
            switch (errorCode) {
                case ADVERTISE_FAILED_DATA_TOO_LARGE:
                    description = "data is to large";
                    break;
                case ADVERTISE_FAILED_TOO_MANY_ADVERTISERS:
                    description = "no advertising instance is available";
                    break;
                case ADVERTISE_FAILED_ALREADY_STARTED:
                    description = "advertising is already started";
                    break;
                case ADVERTISE_FAILED_INTERNAL_ERROR:
                    description = "Operation failed due to an internal error";
                    break;
                case ADVERTISE_FAILED_FEATURE_UNSUPPORTED:
                    description = "This feature is not supported on this platform";
                    break;
                default:
                    description = "fail";
            }
            showToast(description);
        }
    };


    private void showToast(String msg) {
        if (mToast != null) {
            mToast.setText(msg);
            mToast.show();
        }
    }

}
