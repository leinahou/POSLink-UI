package com.example.poslinkui.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.poslinkui.ActionHandlerImp;
import com.example.poslinkui.EntryExtraData;
import com.example.poslinkui.EntryRequest;
import com.example.poslinkui.EntryResponse;
import com.example.poslinkui.R;
import com.example.poslinkui.api.IRespStatus;

public class EnterExpiryDateActivity extends AppCompatActivity implements IRespStatus {

    private String packageName;
    private ActionHandlerImp helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_expiry_date);
        parseIntent(getIntent());
        helper = new ActionHandlerImp(this, packageName, this);
        long expDate = 1219;
        helper.sendNext(packBundle(expDate));
    }

    public Bundle packBundle(long amount){
        Bundle bundle = new Bundle();
        bundle.putLong(EntryRequest.PARAM_TOTAL_AMOUNT, amount);
        return bundle;
    }

    private void parseIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        String action = intent.getAction();
        packageName = intent.getStringExtra(EntryExtraData.PARAM_PACKAGE);
        String transType = bundle.getString(EntryExtraData.PARAM_TRANS_TYPE);
        String message = bundle.getString(EntryExtraData.PARAM_MESSAGE);
        //promptTv.setText(TextUtils.isEmpty(message) ? "Please Input Amount" : message);
        String transMode = bundle.getString(EntryExtraData.PARAM_TRANS_MODE);
        String currency = bundle.getString(EntryExtraData.PARAM_CURRENCY);
    }

    @Override
    public void onAccepted() {

    }

    @Override
    public void onDeclined(@Nullable Bundle bundle) {
        final long code = bundle.getLong(EntryResponse.PARAM_CODE, -1);
        final String message = bundle.getString(EntryResponse.PARAM_MSG);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String buff;
                if (TextUtils.isEmpty(message))
                    buff = "Trans Failed! Error Code : " + code;
                else
                    buff = message + "\n Error Code : " + code;
                Toast.makeText(EnterExpiryDateActivity.this, buff, Toast.LENGTH_LONG).show();
            }
        });
    }
}
