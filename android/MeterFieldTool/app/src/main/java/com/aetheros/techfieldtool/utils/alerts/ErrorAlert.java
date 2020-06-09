package com.aetheros.techfieldtool.utils.alerts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.aetheros.techfieldtool.R;

public class ErrorAlert extends AlertDialog {
    private AlertDialog dialog;
    private AlertDialog.Builder builder;


    public ErrorAlert(Context context, String errMsg) {
        super(context);

        this.builder = new Builder(context);
        this.builder.setMessage(errMsg)
                    .setTitle((context.getResources().getString(R.string.error_alert_title)));

        this.builder.setPositiveButton(
            context.getResources().getString(R.string.error_alert_btn_text),
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                   System.exit(0);
                }
            }
         );

    }

    public void show() {
        this.dialog = builder.create();
        this.dialog.show();
    }
}
