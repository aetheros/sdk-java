package com.aetheros.techfieldtool.utils;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.util.Log;

public class Bluetooth {
    private static final String TAG = "Bluetooth()";
    private static final int REQUEST_ENABLE_BT = 1;
    public static final int ACCESS_COARSE_LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private BluetoothAdapter bta;
    private Activity activity;
    private BluetoothProfile.ServiceListener profileListener;

    public Bluetooth(Activity activity) {

        this.bta = BluetoothAdapter.getDefaultAdapter();
        this.activity = activity;

        this.profileListener = new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
                Log.i(TAG, String.format("Connected to service: %s", bluetoothProfile.toString()));
            }

            @Override
            public void onServiceDisconnected(int i) {
                Log.i(TAG, String.format("Disconnected from service: %d", i));
            }
        };
    }

    public boolean isSupported() {
        if(this.bta == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isEnabled() {
        return this.bta.isEnabled();
    }

    public void requestBluetoothByEnabled() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        this.activity.startActivityForResult(intent, REQUEST_ENABLE_BT);
    }

    /**
     * Start bluetooth device discovery scan.
     * @return
     */
    public boolean startDiscovery() {
        /*
         *This function needs explicitly ask the user
         * for ACCESS_COARSE_LOCATION permissions otherwise the ACTION_FOUND events wont fire.
         * Simply having the permission set in the manifest wont due.
         */
        int permissionCheck = this.activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);

        if(permissionCheck == -1) {
            activity.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, ACCESS_COARSE_LOCATION_PERMISSION_REQUEST_CODE);
            return false;
        } else {
            // Cancel any other discoveries that might be going on.
            this.bta.cancelDiscovery();
            return this.bta.startDiscovery();
        }
    }

    public boolean isDiscoveringDevices() {
        return this.bta.isDiscovering();
    }

}
