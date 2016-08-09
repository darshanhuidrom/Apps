package com.apps.android.apps.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by DARSHAN HUIDROJM on 8/8/2016.
 */
abstract public class DialogBox {
    Activity activity;

    public DialogBox(Activity activity) {
        this.activity = activity;
    }

    public void setValues(String ok, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onPositive(dialog);
            }
        });
        builder.show();
    }

    public void setValues(String ok, String cancel, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message);
        builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onPositive(dialog);

            }
        });
        builder.setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onNegative(dialog);

            }
        });
        builder.show();
    }

    public abstract void onPositive(DialogInterface dialog);

    public abstract void onNegative(DialogInterface dialog);


}
