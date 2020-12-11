package com.example.poslinkui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.poslinkui.api.IActionHandler;
import com.example.poslinkui.api.IRespStatus;


public class ActionHandlerImp implements IActionHandler {

    private final RespReceiver receiver = new RespReceiver();
    private Context context;
    private IRespStatus resp;
    private String packageName;
    private BroadcastSender sender;
    private boolean isStart = false;

    public ActionHandlerImp(Context context, String packageName, IRespStatus respStatus) {
        this.context = context;
        this.resp = respStatus;
        this.packageName = packageName;
        this.sender = new BroadcastSender(context);
    }

    @Override
    public void start() {
        if (!isStart) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(EntryResponse.ACTION_ACCEPTED);
            intentFilter.addAction(EntryResponse.ACTION_DECLINED);

            context.registerReceiver(receiver, intentFilter);
            isStart = true;
        }

    }

    @Override
    public void stop() {
        if (isStart) {
            context.unregisterReceiver(receiver);
            isStart = false;
        }
    }

    @Override
    public void sendNext(@Nullable Bundle bundle) {
        Intent intent = new Intent();
        intent.setPackage(packageName);
        intent.setAction(EntryRequest.ACTION_NEXT);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        sender.send(intent);
    }

    @Override
    public void sendAbort() {
        Intent intent = new Intent();
        intent.setPackage(packageName);
        intent.setAction(EntryRequest.ACTION_ABORT);
        sender.send(intent);
    }

    @Override
    public void sendPrev() {
        Intent intent = new Intent();
        intent.setPackage(packageName);
        intent.setAction(EntryRequest.ACTION_PREV);
        sender.send(intent);
    }

    @Override
    public void setSecurityArea(@NonNull Bundle bundle) {
        Intent intent = new Intent();
        intent.setPackage(packageName);
        intent.setAction(EntryRequest.ACTION_SECURITY_AREA);
        intent.putExtras(bundle);
        sender.send(intent);
    }

    private class RespReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (TextUtils.isEmpty(action))
                return;
            Log.i("ActionHandlerImp", "onReceive action : " + action);
            switch (action) {
                case EntryResponse.ACTION_ACCEPTED:
                    stop();
                    Log.i("ActionHandlerImp", "ACCEPTED receiver unregisterReceiver :" + context);
                    if (resp != null) {
                        resp.onAccepted();
                    }
                    break;
                case EntryResponse.ACTION_DECLINED:
                    if (resp != null) {
                        resp.onDeclined(intent.getExtras());
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
