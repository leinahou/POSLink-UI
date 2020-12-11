package com.example.poslinkui.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.poslinkui.activities.InputAccountActivity;

public class StatusReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // an Intent broadcast.
        InputAccountActivity.printStatus(context, intent);

    }
}
