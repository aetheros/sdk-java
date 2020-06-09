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

package com.aetheros.techfieldtool.activities.AccessPointDiscovery.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aetheros.techfieldtool.R;
import com.aetheros.techfieldtool.activities.ConnectionMethods.ConnectionMethodsActivity;
import com.aetheros.techfieldtool.activities.DeviceCommands.DeviceCommandsActivity;
import com.aetheros.techfieldtool.components.Toolbar.ToolbarComponent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WifiScanActivity extends AppCompatActivity {
    private static final String TAG = "MeterConnectionAct";

    private Boolean mWifiScanning = false;
    private ProgressBar mProgressBar;
    private Button mButton;
    private TextView mListTitle;
    private ImageView mBackButton;

    private ArrayList<ScanResult> mAccessPoints = new ArrayList<ScanResult>();
    private ListView mAccessPointListView;
    private AccessPointListAdapter mAccessPointListViewAdapter;
    private WifiManager mWifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_scan);

        ToolbarComponent tb = findViewById(R.id.toolbar);
        tb.setSubtitle(getResources().getString(R.string.title_activity_ssid_scan));

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mBackButton = (ImageView) findViewById(R.id.back_button);
        mButton = (Button) findViewById(R.id.btn);
        mListTitle = (TextView) findViewById(R.id.list_title);

        mButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, "Button is down");
                        view.setBackgroundColor(getResources().getColor(R.color.clicked_button));
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "Button is up");
                        view.setBackgroundColor(getResources().getColor(R.color.white));
                        view.performClick();
                }

                return false;
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mWifiScanning) {
                    Log.i(TAG, "Cancel WiFi scan.");
                } else {
                    Log.i(TAG, "Starting WiFi scan.");
                    startAccessPointScan();
                }
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backButtonClickHandler();
            }
        });

        mAccessPointListViewAdapter = new AccessPointListAdapter(this, mAccessPoints);
        mAccessPointListView = findViewById(R.id.access_point_list);
        mAccessPointListView.setAdapter(mAccessPointListViewAdapter);
        mAccessPointListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final ScanResult accessPoint = (ScanResult) mAccessPointListView.getItemAtPosition(i);

                WifiConfiguration wifiConfiguration = new WifiConfiguration();
                wifiConfiguration.SSID = "\"".concat(accessPoint.SSID).concat("\"");
//                wifiConfiguration.status = WifiConfiguration.Status.DISABLED;
//                wifiConfiguration.priority = 40;
//                wifiConfiguration.BSSID = accessPoint.BSSID;
//                wifiConfiguration.preSharedKey = "\""+"password"+"\"";

//                wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
//                wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
//                wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
//                wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
//                wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
//                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
//                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
//                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
//                wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                wifiConfiguration.preSharedKey = "\"".concat("password").concat("\"");

                int netId = mWifiManager.addNetwork(wifiConfiguration);

                Log.i(TAG, wifiConfiguration.preSharedKey);
                Log.i(TAG, netId+"");


                // Attempt to connect to the selected network
                Boolean connected = mWifiManager.enableNetwork(netId, true);

                Log.i(TAG, connected+"");

                if(connected) {
                    startDeviceCommandsActivity();
                } else {
                    Toast.makeText(getBaseContext(), String.format("Error connecting to access point %s", accessPoint.SSID), Toast.LENGTH_LONG).show();
                }
            }
        });

        /*
         * Initialize a broadcast receiver that will be executed when the WiFi scan is complete.
         */
        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "Broadcast received");

                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false
                );

                if (success) {
                    Log.i(TAG, "Scan success");

                    // Clear out the list of existing access points.
                    if(!mAccessPointListViewAdapter.isEmpty()) {
                        mAccessPointListViewAdapter.clear();
                        mAccessPointListViewAdapter.notifyDataSetChanged();
                    }

                    List<ScanResult> results = mWifiManager.getScanResults();
                    Log.i(TAG, results.size()+"");

                    Iterator itt = results.iterator();

                    while (itt.hasNext()) {
                        ScanResult scanResult = (ScanResult) itt.next();
                        mAccessPointListViewAdapter.add(scanResult);
                    }
                    // Display wifi access points.
                } else {
                    // Scan failure.  User old results?
                    Log.i(TAG, "Scan failure");
                }

                stopProgressBar();
            }
        };

        // Launch broadcast intent.
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        getApplicationContext().registerReceiver(br, intentFilter);

        startAccessPointScan();
    }

    private void startAccessPointScan() {
        // Start the progress bar and toggle the button text.
        startProgressBar();

        mWifiManager.startScan();
    }

    /**
     * Describes the item view in the device list.
     */
    private class AccessPointListAdapter extends ArrayAdapter<ScanResult> {
        public AccessPointListAdapter(Context context, ArrayList<ScanResult> accessPoints) {
            super(context, 0, accessPoints);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ScanResult accessPoint = getItem(position);

            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.component_device_list_item, parent, false);
            }

            TextView deviceNameTextView = convertView.findViewById(R.id.device_list_item_name);
            TextView deviceAddrTextView = convertView.findViewById(R.id.device_list_item_address);

            if(accessPoint.SSID != null) {
                deviceNameTextView.setText(accessPoint.SSID);
            } else {
                deviceNameTextView.setText("No SSID set.");
            }

            deviceAddrTextView.setText(accessPoint.BSSID);

            return convertView;
        }
    }

    private void startProgressBar() {
        mWifiScanning = true;
        mProgressBar.setVisibility(View.VISIBLE);
        mListTitle.setText("Scanning for access points");
        mButton.setText("Scanning");
        mButton.setEnabled(false);
    }

    private void stopProgressBar() {
        mWifiScanning = false;
        mProgressBar.setVisibility(View.INVISIBLE);
        mListTitle.setText("Available Access Points");
        mButton.setText("Scan");
        mButton.setEnabled(true);
    }

    private void backButtonClickHandler() {
        Intent intent = new Intent(this, ConnectionMethodsActivity.class);
        startActivity(intent);
        this.finish();
    }


    private void startDeviceCommandsActivity() {
        Intent intent = new Intent(this, DeviceCommandsActivity.class);
        startActivity(intent);
    }

}
