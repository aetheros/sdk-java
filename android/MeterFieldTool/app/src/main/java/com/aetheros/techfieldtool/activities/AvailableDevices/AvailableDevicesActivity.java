/*
 *                   AETHEROS, INC. CONFIDENTIAL
 *
 * The source code contained or described herein and all documents related
 * to the source code ("Material") are owned by Aetheros, Inc. or its
 * suppliers or licensors. Title to the Material remains with Aetheros or its
 * suppliers and licensors. The Material contains trade secrets and proprietary
 * and confidential information of Aetheros or its suppliers and licensors. The
 * Material is protected by worldwide copyright and trade secret laws and treaty
 * provisions. No part of the Material may be used, copied, reproduced, modified,
 * published, uploaded, posted, transmitted, distributed, or disclosed in any way
 * without the prior express written permission of Aetheros, Inc.
 *
 * No license under any patent, copyright, trade secret or other intellectual
 * property right is granted to or conferred upon you by disclosure or delivery
 * of the Material, either expressly, by implication, inducement, estoppel or
 * otherwise. Any license under such intellectual property rights must be
 * express and approved by Aetheros, Inc. in writing.
 *
 *       Copyright (c) 2020 Aetheros, Inc.  All Rights Reserved.
 *
 *
 */

package com.aetheros.techfieldtool.activities.AvailableDevices;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aetheros.techfieldtool.R;
import com.aetheros.techfieldtool.utils.Bluetooth;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class AvailableDevicesActivity extends Activity {
    private static final String TAG = "AvailableDevicesAct";
    // The service UUID specified by the server developer.
    private static final String SERVICE_ID = "94f39d29-7d6d-437d-973b-fba39e49d4ee";
    // Device source for the list view.
    private ArrayList<BluetoothDevice> devices = new ArrayList<BluetoothDevice>();

    // Device list view.
    private ListView deviceList;
    private DeviceListAdapter deviceListAdapter;

    public AvailableDevicesActivity() {
        super();
    }

    // Fired when the discovery process is started.
    private final BroadcastReceiver discoveryStartedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            ProgressBar pb = findViewById(R.id.loading_spinner);
            pb.setVisibility(View.VISIBLE);

            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Log.i(TAG, "discovery started");
            }
        }
    };

    // Fired EVERY TIME a device is discovered.  May fire multiple times.
    // When a new device is found, it is added to the devices arraylist to
    // be displayed by the list view.
    private final BroadcastReceiver discoveryActionFoundReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            Log.i(TAG, "discoveryActionFoundReceiver()");

            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Bluetooth device found.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                Log.i(TAG, String.format("Device found: %s.", device.getAddress()));
                deviceListAdapter.add(device);
            }
        }
    };

    // Fired when discovery process ends.
    private final BroadcastReceiver discoveryFinishedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            ProgressBar pb = findViewById(R.id.loading_spinner);
            pb.setVisibility(View.INVISIBLE);

            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.i(TAG, "discovery finished");
                Log.i(TAG, String.format("%d devices found", devices.size()));
            }
        }

    };

    /**
     * Callback to the requestPermissions call in the bluetooth class's startDiscovery method.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Bluetooth.ACCESS_COARSE_LOCATION_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    searchDevices();
                } else {
                    Log.i(TAG, "Permission denied");
                    Toast.makeText(getBaseContext(), "You must grant location permission to user bluetooth.",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup the activity view.
        setContentView(R.layout.activity_available_device);

        // Device list controls.
        this.deviceListAdapter = new DeviceListAdapter(this, devices);
        this.deviceList = findViewById(R.id.device_list);
        this.deviceList.setAdapter(deviceListAdapter);
        this.deviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final BluetoothDevice device = (BluetoothDevice) deviceList.getItemAtPosition(i);

                // Stop device discovery.
                // Pair to the selected device.

//                IntentFilter openDevicePairingActivityIntentFilter = new IntentFilter();
//                Intent openDevicePairingActivityIntent = new Intent();
                Toast.makeText(getBaseContext(), String.format("%s: %s", device.getName(), device.getAddress()), Toast.LENGTH_LONG).show();
                new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, String.format("Creating RFcomm Socket to device %s", device.getAddress()));
                            try {
                                // This UUID must match the one on the server.
                                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(UUID.fromString(SERVICE_ID));
                                socket.connect();
                                OutputStream stream = socket.getOutputStream();
                                stream.write("Hello, World".getBytes());
                                stream.flush();
                                stream.close();
                            } catch(IOException exception) {
                                Log.i(TAG, exception.toString());
                            }
                        }
                    }
                ).start();
            }
        });

        // Search button.
        Button searchDevicesBtn = findViewById(R.id.search_devices_btn);

        // Bluetooth intent and registration.
        IntentFilter discoveryStartedFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(discoveryStartedReceiver, discoveryStartedFilter);

        // Register broadcast receiver.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(discoveryActionFoundReceiver, filter);

        IntentFilter discoveryFinishedFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(discoveryFinishedReceiver, discoveryFinishedFilter);

        // Search blue tooth devices button click hanlder.
        searchDevicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDevices();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Remove all the broadcast receivers.
        unregisterReceiver(discoveryStartedReceiver);
        unregisterReceiver(discoveryActionFoundReceiver);
        unregisterReceiver(discoveryFinishedReceiver);
    }

    private void searchDevices() {
        // Clear the device list view.
        this.deviceListAdapter.clear();
        // Create an instance of bt class and query for discoverable devices.
        Bluetooth bt = new Bluetooth(this);

        // Start device discovery.  All response to the discovery process are
        // handled by the three broadcast receivers.
        Boolean discoveryStarted = bt.startDiscovery();
    }

    /**
     * Describes the item view in the device list.
     */
    private class DeviceListAdapter extends ArrayAdapter<BluetoothDevice> {
        public DeviceListAdapter(Context context, ArrayList<BluetoothDevice> devices) {
            super(context, 0, devices);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            BluetoothDevice device = getItem(position);

            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.component_device_list_item, parent, false);
            }

            TextView deviceNameTextView = convertView.findViewById(R.id.device_list_item_name);
            TextView deviceAddrTextView = convertView.findViewById(R.id.device_list_item_address);

            if(device.getName() != null) {
                deviceNameTextView.setText(device.getName());
            } else {
                deviceNameTextView.setText("No device name found.");
            }

            deviceAddrTextView.setText((device.getAddress()));

            return convertView;
        }
    }
}

