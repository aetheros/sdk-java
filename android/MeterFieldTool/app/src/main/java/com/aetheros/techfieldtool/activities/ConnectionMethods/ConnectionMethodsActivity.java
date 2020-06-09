package com.aetheros.techfieldtool.activities.ConnectionMethods;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.aetheros.techfieldtool.R;
import com.aetheros.techfieldtool.activities.AccessPointDiscovery.wifi.SelectSSIDDiscoveryMethodActivity;
import com.aetheros.techfieldtool.activities.AccessPointDiscovery.wifi.WifiScanActivity;
import com.aetheros.techfieldtool.components.Toolbar.ToolbarComponent;
import com.aetheros.techfieldtool.utils.alerts.ErrorAlert;

public class ConnectionMethodsActivity extends AppCompatActivity {
    private static final String TAG = "ConnectionMethods";
    private final int REQUEST_ENABLE_WIFI = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connection_methods);

        // Setup the toolbar.
        ToolbarComponent tb = findViewById(R.id.toolbar);
        tb.setSubtitle(getResources().getString(R.string.title_activity_connection_method));

        // Setup buttons.
        final Button wifiBtn = (Button) findViewById((R.id.select_wifi_btn));

        // Disable bt button.
        Button btBtn = (Button) findViewById((R.id.select_bt_btn));
        btBtn.setEnabled(false);

        wifiBtn.setOnTouchListener(new View.OnTouchListener() {
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
//                        view.performClick();
                }

                return false;
            }
        });

        /*
         * Wifi button press.
         * Ensure wifi is enabled.  If it is not, enable it silently.  Start the connection activity.
         */
        wifiBtn.setOnClickListener(new View.OnClickListener() {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            @Override
            public void onClick(View view) {
                Log.i(TAG, "Checking if WiFi is enabled.");

                // Enable the users wifi.  Do not need to ask permission.
                if (!wifiManager.isWifiEnabled()) {
                    Log.i(TAG, "WiFi is NOT enabled.");
                    Log.i(TAG, "Attempting to enable WiFi.");

                    // Enable wifi.
                    if(wifiManager.setWifiEnabled(true)) {
                        Log.i(TAG, "WiFi successfully enabled.");
                    } else {
                        Log.i(TAG, "Unable to enable WiFi.");

                        ErrorAlert errorAlert = new ErrorAlert(getBaseContext(), "Unable to configure WiFi device.");
                        errorAlert.show();
                        return;
                    }
                }

                startSelectWifiDiscoveryMethodActivity();

            }
        });

    }

    private void startWifiAccessPointScanActivity() {
        Intent intent = new Intent(getApplicationContext(), WifiScanActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Open activity to ask user configure hidden SSID or perform WiFi scan for broadcast
     * SSIDs.
     */
    private void startSelectWifiDiscoveryMethodActivity() {
        Intent intent = new Intent(getApplicationContext(), SelectSSIDDiscoveryMethodActivity.class);
        startActivity(intent);
    }


}
