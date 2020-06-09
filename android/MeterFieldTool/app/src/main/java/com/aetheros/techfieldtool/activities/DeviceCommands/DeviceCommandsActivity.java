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

package com.aetheros.techfieldtool.activities.DeviceCommands;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aetheros.techfieldtool.R;
import com.aetheros.techfieldtool.activities.AccessPointDiscovery.wifi.ConfigureHiddenSSIDConnectionActivity;
import com.aetheros.techfieldtool.activities.WanLink.WanLinkActivity;
import com.aetheros.techfieldtool.components.Toolbar.ToolbarComponent;
import com.aetheros.techfieldtool.utils.DeviceCommand;

import java.util.ArrayList;

public class DeviceCommandsActivity extends AppCompatActivity {
    private final String TAG = "DeviceCommands";
    private ListView mListView;
    private DeviceCommandListAdapter mListAdapter;
    private TextView mSSIDTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_commands);

        ToolbarComponent tb = findViewById(R.id.toolbar);
        tb.setSubtitle(getResources().getString(R.string.title_activity_device_commands));

        final ArrayList<DeviceCommand> commands = new ArrayList<>();
        commands.add(new DeviceCommand(DeviceCommand.WAN_LINK,"WAN Link", getDrawable(R.drawable.ic_wan_grey600_24dp)));
        commands.add(new DeviceCommand(DeviceCommand.METER_INFORMATION,"Meter Information", getDrawable(R.drawable.ic_cellphone_information_grey600_24dp)));
        commands.add(new DeviceCommand(DeviceCommand.SMART_HUB,"Smart Hub", getDrawable(R.drawable.ic_hubspot_grey600_24dp)));

        mListView = (ListView) findViewById(R.id.command_list);
        mListAdapter = new DeviceCommandListAdapter(this, commands);
        mListView.setAdapter(mListAdapter);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(this.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo ();
        String ssid  = info.getSSID().replace("\"", "");

        // Sometimes the activity loads before the wifimanager updates the ssid.
        // It is connected, it just does not have the ssid available yet.
//        if(ssid == "<unknown ssid>") {
//            ssid = getIntent().getStringExtra(ConfigureHiddenSSIDConnectionActivity.SSID);
//        }
//
//        Log.i(TAG, String.format("SSID: %s", ssid));

        ConnectivityManager cm = (ConnectivityManager) this.getApplication().getSystemService(CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE) {
            AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setMessage("Please disabled mobile data");
            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.show();
        }

        mSSIDTextView =  findViewById(R.id.ssid);
        mSSIDTextView.setText(ssid);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch(commands.get(i).getCommandId()) {
                    case DeviceCommand.WAN_LINK:
                        startWanLinkActivity();
                        break;
                    default:
                        Toast.makeText(getBaseContext(), String.format("%s not supported yet.", commands.get(i).getLabel()), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class DeviceCommandListAdapter extends ArrayAdapter<DeviceCommand> {
        public DeviceCommandListAdapter(Context context, ArrayList<DeviceCommand> commands) {
            super(context, 0, commands);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            DeviceCommand command = getItem(position);

            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.component_command_list_item, parent, false);
            }

            ImageView commandImageView= convertView.findViewById(R.id.command_list_item_icon);
            TextView commandLabelTextView = convertView.findViewById(R.id.command_list_item_label);

            commandLabelTextView.setText(command.getLabel());
            commandImageView.setImageDrawable(command.getIconDrawable());

            return convertView;
        }
    }

    public void startWanLinkActivity() {
        Intent intent = new Intent(this, WanLinkActivity.class);
        startActivity(intent);
    }


}
