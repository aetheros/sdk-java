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

package com.aetheros.techfieldtool.activities.WanLink;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.aetheros.aos.onem2m.client.AOSClient;
import com.aetheros.aos.onem2m.client.cse.CSE;
import com.aetheros.aos.onem2m.client.exceptions.OneM2MException;
import com.aetheros.aos.onem2m.common.resources.AE;
import com.aetheros.aos.onem2m.common.resources.Child;
import com.aetheros.aos.onem2m.common.resources.ExternalConnectivityMonitor;
import com.aetheros.aos.onem2m.common.resources.ExternalModuleInformation;
import com.aetheros.aos.onem2m.common.resources.Node;
import com.aetheros.techfieldtool.R;
import com.aetheros.techfieldtool.components.Toolbar.ToolbarComponent;

import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.EndpointManager;
import org.eclipse.californium.elements.UDPConnector;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WanLinkActivity extends AppCompatActivity {
    private final String TAG = "WanLinkActivity";

    private final String SHARED_PREFS = "aie_shared_prefs";
    private final String AEI_KEY = "aie_key";

    private TextView mSSIDTextView;
    private TextView mWANConnectionStatusTextView;

    private static final int RSRQ_MIN = -20;
    private static final int RSRQ_MAX= -2;
    // (less -13 poor, between -12 and -9 good, greater than -9 excellent.
    private static final int[] RSRQ_RANGE = new int[]{-13, -12, -9};

    private static final int RSRP_MIN = -140;
    private static final int RSRP_MAX= -80;
    // (less -120 poor, between -120 and -106 fair, between 106 -90 good, greater than -90 excellent.
    private static final int[] RSRP_RANGE= new int[]{-120, -106, -90};

    private static final String RSRQ_UOM = "dB";
    private static final String RSRP_UOM = "dBm";

    private TextView rsrqTextView;
    private TextView rsrpTextView;
    private TextView rsrqUomTextView;
    private TextView rsrpUomTextView;
    private String deviceIp;
    private WifiManager wifiManager;
    private ScheduledExecutorService executor;
    private String wifiIp;

    private Runnable buildPollingRunnable() {
        // Poll the device every three seconds.
        return new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Making coap request.");
                new CoapGetTask().execute(wifiIp);
            }
        };

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wan_link);

        ToolbarComponent tb = findViewById(R.id.toolbar);
        tb.setSubtitle(getResources().getString(R.string.title_activity_wan_link));

        wifiManager = (WifiManager) getApplicationContext().getSystemService(this.WIFI_SERVICE);

        wifiIp = getWifiIp(wifiManager).substring(0, 12) + "1";

        WifiInfo info = wifiManager.getConnectionInfo ();
        String ssid  = info.getSSID().replace("\"", "");

        Log.i(TAG, "SSID: " + ssid);
        mSSIDTextView = findViewById(R.id.ssid);
        mSSIDTextView.setText(ssid);

        rsrqTextView = findViewById(R.id.rsrq_reading);
        rsrpTextView = findViewById(R.id.rsrp_reading);
        rsrqUomTextView = findViewById(R.id.rsrq_uom);
        rsrpUomTextView = findViewById(R.id.rsrp_uom);
        mWANConnectionStatusTextView = findViewById(R.id.wan_connection_status);

        Typeface digitalTypefaceBold = Typeface.createFromAsset(getAssets(), "fonts/alarm clock.ttf");
        Typeface digitalTypeface = Typeface.createFromAsset(getAssets(), "fonts/alarm clock.ttf");

        rsrqTextView.setTypeface(digitalTypefaceBold);
        rsrpTextView.setTypeface(digitalTypefaceBold);
        rsrqUomTextView.setTypeface(digitalTypeface);
        rsrpUomTextView.setTypeface(digitalTypeface);

        rsrqTextView.setText("n/a");
        rsrpTextView.setText("n/a");
        rsrqUomTextView.setText(RSRQ_UOM);
        rsrpUomTextView.setText(RSRP_UOM);

        rsrqTextView.setTextColor(getResources().getColor(R.color.signal_great));
        rsrqUomTextView.setTextColor(getResources().getColor(R.color.signal_great));
        rsrpTextView.setTextColor(getResources().getColor(R.color.signal_good));
        rsrpUomTextView.setTextColor(getResources().getColor(R.color.signal_good));

        // Get the ap ip.
        Log.i(TAG, "ip: " + getWifiIp((wifiManager)));
        deviceIp = getWifiIp(wifiManager);

        initCoapEndpoint();

        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(buildPollingRunnable(), 0, 3, TimeUnit.SECONDS);
    }

    @Override
    protected void onPause() {
        super.onPause();

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        executor.shutdownNow();
//        wifiManager.disconnect();
    }

    class CoapGetTask extends AsyncTask<String, String, Node> {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, 0);

        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute()");
        }

        protected Node doInBackground(String... args) {
            Log.i(TAG, "doInBackground()");
            String aei = sharedPreferences.getString(AEI_KEY, null);

            Log.i(TAG, String.format("AEI: %s", aei));

            try {
                Log.i(TAG, args[0]);
                AOSClient aos = new AOSClient();
                CSE cse = aos.getCSE(args[0], 8100);

                final String aeAppId = "Nra1.com.grid-net.policynet.m2m";
                final String aeAppName = "adams-test-app";
                final String[] poa = new String[] { String.format("coap://%s:8100", args[0]) };

                AE ae = new AE(aeAppId, aeAppName, poa);

                if(aei == null) {
                    Log.i(TAG, "AE not registered.  Send registration request to CSE.");
                    ae = cse.register(ae); // blocking call

                    Log.i(TAG, "Registered AE received.");
                    // Store the aei for next time.
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(AEI_KEY, ae.getAei());

                    if(editor.commit()) {
                        Log.i(TAG, "aeid stored for future requests.");
                    } else {
                        Log.i(TAG, "unable to store aeid stored for future requests.");
                    }
                } else {
                    Log.i(TAG, "AE already registered.  Using stored AE ID.");

                    // Set the aei retrieved from storage .
                    ae.setAei(aei);
                }

                return cse.retrieveNode(ae); // blocking call.
            } catch(OneM2MException ex) {
                Log.e(TAG, ex.getMessage(), ex);
                return null;
            } catch(Exception ex) {
                Log.e(TAG, ex.getMessage(), ex);
                return null;
            }
        }

        protected void onPostExecute(Node node) {
            Log.i(TAG, "onPostExecute()");

            if (node !=null) {

                List<Child> children = node.getChildren();

                for(Child child: children) {
                    if(child instanceof ExternalConnectivityMonitor) {
                        ExternalConnectivityMonitor cmext = (ExternalConnectivityMonitor) child;

                        Log.i(TAG, cmext.getRsrq() + "");
                        Log.i(TAG, cmext.getRsrp() + "");

                        rsrqTextView.setText("" + cmext.getRsrq());
                        rsrpTextView.setText("" + cmext.getRsrp());

                        int rsrpColor;
                        int rsrqColor;

                        if (cmext.getRsrp() < RSRP_MIN) {
                            rsrpColor = R.color.signal_poor;
                        } else if (cmext.getRsrp() < RSRP_MAX) {
                            rsrpColor = R.color.signal_good;
                        } else {
                            rsrpColor = R.color.signal_great;
                        }

                        if (cmext.getRsrq() < RSRQ_MIN) {
                            rsrqColor = R.color.signal_poor;
                        } else if (cmext.getRsrq() < RSRQ_MAX) {
                            rsrqColor = R.color.signal_good;
                        } else {
                            rsrqColor = R.color.signal_great;
                        }

                        Log.i(TAG, "" + rsrpColor);
                        Log.i(TAG, "" + rsrqColor);

                        rsrqTextView.setTextColor(getResources().getColor(rsrqColor));
                        rsrqUomTextView.setTextColor(getResources().getColor(rsrqColor));
                        rsrpTextView.setTextColor(getResources().getColor(rsrpColor));
                        rsrpUomTextView.setTextColor(getResources().getColor(rsrpColor));
                    }

                    if(child instanceof ExternalModuleInformation) {
                        int cnstat = ((ExternalModuleInformation) child).getCnstat();
                        String wanConnectionStatus = "An error occurred...";
                        int connectionStatusColor = getResources().getColor(R.color.signal_poor);

                        switch(cnstat) {
                            case 3:
                                wanConnectionStatus = "Connected / Provisioned";
                                connectionStatusColor = getResources().getColor(R.color.signal_great);
                                break;
                            case 2:
                                wanConnectionStatus = "Connected / Unprovisioned";
                                connectionStatusColor = getResources().getColor(R.color.signal_good);
                                break;
                            default:
                                wanConnectionStatus = "Not Connected";
                        }

                        mWANConnectionStatusTextView.setText(wanConnectionStatus);
                        mWANConnectionStatusTextView.setTextColor(connectionStatusColor);
                    }
                }
            } else {
                Toast.makeText(getBaseContext(), "No response", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initCoapEndpoint() {
        CoapEndpoint.Builder udpEndpointBuilder = new CoapEndpoint.Builder();
        UDPConnector udpConnector = new UDPConnector();
        udpEndpointBuilder.setConnector(udpConnector);
        EndpointManager.getEndpointManager().setDefaultEndpoint(udpEndpointBuilder.build());
    }

    private void setRSRP(int rsrp) {
        rsrpTextView.setText(rsrp);
        rsrpUomTextView.setText(RSRP_UOM);
    }

    private void setRSRQ(int rsrq) {
        rsrpTextView.setText(rsrq);
        rsrpUomTextView.setText(RSRQ_UOM);
    }



    protected String getWifiIp(WifiManager wifiManager) {
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();

        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }

        return ipAddressString;
    }
}
