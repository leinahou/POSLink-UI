package com.example.poslinkui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

public class BroadcastSender {
    private WeakReference<Context> context;


    public BroadcastSender(Context context){
        this.context = new WeakReference<>(context.getApplicationContext());
    }

    public void send(@NonNull Intent intent){
        Context con = context.get();
        if(con != null)
            con.sendBroadcast(intent);
    }
}
