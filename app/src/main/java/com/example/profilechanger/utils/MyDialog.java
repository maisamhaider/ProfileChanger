package com.example.profilechanger.utils;

import android.app.AlertDialog;
import android.content.Context;

public class MyDialog extends AlertDialog {


    Context context;
    AlertDialog dialog;
    Builder builder;

    public MyDialog(Context context) {
        super(context);
        this.context = context;
    }

    public void createDialog(String title, String message) {
        builder = new Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false);
        dialog = builder.create();


    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
