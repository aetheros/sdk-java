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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aetheros.techfieldtool.activities.DeviceCommands.DeviceCommandsActivity;
import com.aetheros.techfieldtool.components.LabeledEditText.LabeledEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;

import com.aetheros.techfieldtool.R;
import com.aetheros.techfieldtool.components.Toolbar.ToolbarComponent;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ConfigureHiddenSSIDConnectionActivity extends AppCompatActivity {
    private final String TAG = "ConfigureHiddenSSID";
    private LabeledEditText ssidEditText;
    private LabeledEditText mPSKEditText;
    private Button mConnectButton;
    private WifiManager mWifiManager;
    private ProgressBar mProgressBar;

    public static String SSID = "ssid";

    private WifiBroadcastReceiver wifiReceiver;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_hidden_ssid_connection);

        ToolbarComponent tb = findViewById(R.id.aetheros_toolbar);
        tb.setSubtitle(getResources().getString(R.string.title_activity_configure_hidden_ssid_connection));

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();

        ssidEditText = findViewById(R.id.ssid_input);
        mPSKEditText = findViewById(R.id.psk_input);
        mConnectButton = findViewById(R.id.connect_btn);
        mProgressBar = findViewById(R.id.progress_bar);

//        ssidEditText.setText("aos_test_device");
//        mPSKEditText.setText("aos_secret0");


        // Open barcode scanner button.
        Button scanBarcodeButton = findViewById(R.id.scan_barcode_btn);
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanBarcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Open scanner");

                scanIntegrator.initiateScan();
            }
        });

        // Connect to ssid button.
        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mWifiManager.isWifiEnabled()) {
                    AlertDialog alert = new AlertDialog.Builder(ConfigureHiddenSSIDConnectionActivity.this).create();
                    alert.setTitle("Enable wifi");
                    alert.setMessage("Please enable wifi.");
                    alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alert.show();
                } else {
                    final String SSID = ssidEditText.getText();
                    final String PSK = mPSKEditText.getText();

                    mProgressBar.setVisibility(View.VISIBLE);

                    // If already connected to the ap, just start the next activity.
                    String currentSSID = wifiInfo.getSSID().replace("\"", "");
                    Log.i(TAG, "Current ssid: " + currentSSID);

                    if (currentSSID == SSID) {
                        Log.i(TAG, "Starting device commands activity.");
                        startDeviceCommandsActivity(SSID);
                    } else {
                        Log.i(TAG, String.format("Configuring %s network.", SSID));

                        wifiReceiver = new WifiBroadcastReceiver();
                        wifiReceiver.setExpectedSSID(SSID);

                        intentFilter = new IntentFilter();
                        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
                        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
                        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);

                        registerReceiver(wifiReceiver, intentFilter);

                        WifiConfiguration wifiConfiguration = new WifiConfiguration();
                        wifiConfiguration.SSID = "\"".concat(SSID).concat("\"");
                        wifiConfiguration.preSharedKey = "\"".concat(PSK).concat("\"");
                        wifiConfiguration.hiddenSSID = true;
                        wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                        wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                        wifiConfiguration.status = WifiConfiguration.Status.ENABLED;
                        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                        wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                        wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

                        int netId = mWifiManager.addNetwork(wifiConfiguration);
                        // Attempt to connect to the selected network
                        Boolean connected = mWifiManager.enableNetwork(netId, true);

                    }
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if(scanningResult != null) {
            ssidEditText.setText(scanningResult.getContents());
        }
    }

    private void startDeviceCommandsActivity(String ssid) {
        Intent intent = new Intent(this, DeviceCommandsActivity.class);
        intent.putExtra(SSID, ssid);
        startActivity(intent);
    }

    private class WifiBroadcastReceiver extends BroadcastReceiver {
        private static final String TAG = "WifiBroadcast";
        private String ssid;

        public void setExpectedSSID(String ssid) {
            this.ssid = ssid;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            Log.i(TAG, intent.getAction().toString());
            Log.i(TAG, mWifiManager.getConnectionInfo().getSupplicantState().name());

            if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                unregisterReceiver(wifiReceiver);

                Log.i(TAG, mWifiManager.getConnectionInfo().getSupplicantState().name());

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                mProgressBar.setVisibility(View.INVISIBLE);

                                if (ssid.equals(mWifiManager.getConnectionInfo().getSSID().replace("\"", ""))) {
                                    startDeviceCommandsActivity(mWifiManager.getConnectionInfo().getSSID());
                                } else {
                                    Toast.makeText(getBaseContext(), "Could not connect to " + ssid + ".", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, 3000);

            }
        }
    }
}
