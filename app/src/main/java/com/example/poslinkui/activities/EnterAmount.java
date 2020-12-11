package com.example.poslinkui.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.poslinkui.ActionHandlerImp;
import com.example.poslinkui.EntryExtraData;
import com.example.poslinkui.EntryRequest;
import com.example.poslinkui.EntryResponse;
import com.example.poslinkui.R;
import com.example.poslinkui.api.IRespStatus;

public class EnterAmount extends AppCompatActivity implements View.OnClickListener, IRespStatus {

    private Button btn_next;
    private EditText et_amount;
    private String packageName;
    private ActionHandlerImp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_amount);
        btn_next = findViewById(R.id.next);
        et_amount = findViewById(R.id.editText);
        et_amount.setHint(R.string.activityName_EnterAmount);
        packageName = getIntent().getStringExtra(EntryExtraData.PARAM_PACKAGE);
        //parseIntent(getIntent());
        helper = new ActionHandlerImp(this, packageName, this);
        btn_next.setOnClickListener(this);
    }

    public Bundle packBundle(long amount){
        Bundle bundle = new Bundle();
        bundle.putLong(EntryRequest.PARAM_AMOUNT, amount);
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
    public void onClick(View view){
        if(et_amount.getText().toString().equals(""))
            Toast.makeText(this, R.string.enter_amount, Toast.LENGTH_LONG).show();
        else{
            long amount = Long.valueOf(et_amount.getText().toString());
            helper.sendNext(packBundle(amount));
            finish();
        }
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
                Toast.makeText(EnterAmount.this, buff, Toast.LENGTH_LONG).show();
            }
        });
    }
}
