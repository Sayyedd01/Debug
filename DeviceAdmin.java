package com.example.digitalshield; // Aapke package ka naam yahan aayega

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DeviceAdmin extends DeviceAdminReceiver {

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Toast.makeText(context, "Device Admin: Enabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        // Yahan aap parent ko email bhej sakte hain
        // Abhi ke liye, hum ek Toast message dikha rahe hain
        Toast.makeText(context, "Uninstall Protection Hata Di Gayi Hai!", Toast.LENGTH_LONG).show();
    }
}
