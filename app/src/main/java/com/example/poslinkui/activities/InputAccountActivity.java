package com.example.poslinkui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poslinkui.ActionHandlerImp;
import com.example.poslinkui.EntryExtraData;
import com.example.poslinkui.EntryRequest;
import com.example.poslinkui.EntryResponse;
import com.example.poslinkui.R;
import com.example.poslinkui.api.IRespStatus;

public class InputAccountActivity extends AppCompatActivity implements IRespStatus {

    private static TextView status;

    private Button btn_next;
    private ActionHandlerImp helper;
    private String packageName;
    private TextView tv_accountNum;
    private long amount;
    private static boolean finished;

    //for test purpose
    private Button btn_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_account);

        finished = false;
        status = findViewById(R.id.statusView);
        parseIntent(getIntent());
        tv_accountNum = findViewById(R.id.textView);


        // test
        btn_test = findViewById(R.id.testButton);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.setSecurityArea(setSecurityArea());
            }
        });
        helper = new ActionHandlerImp(this, packageName, this);
        status.setText(String.format("Total amount: %d\n", amount));
        //helper.setSecurityArea(setSecurityArea());

        btn_next = findViewById(R.id.next_manualInput);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Click here", Toast.LENGTH_LONG).show();
                helper.sendNext(new Bundle());

            }
        });
    }

    private Bundle setSecurityArea(){
        Bundle bundle = new Bundle();
        int[] location = new int[2];
        tv_accountNum.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        int width = tv_accountNum.getWidth();
        int height = tv_accountNum.getHeight();
        bundle.putInt(EntryRequest.PARAM_X, x);
        bundle.putInt(EntryRequest.PARAM_Y, y);
        bundle.putInt(EntryRequest.PARAM_WIDTH, width);
        bundle.putInt(EntryRequest.PARAM_HEIGHT, height);
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
        amount = bundle.getLong(EntryExtraData.PARAM_TOTAL_AMOUNT);
    }


    public static void printStatus(Context context, Intent intent){
        status.append("Current Status: " + intent.getAction() + "\n");
        if(intent.getAction() == "com.pax.us.pay.TRANS_COMPLETED")
            finished = true;
    }

    @Override
    public void onAccepted() {

    }

    @Override
    public void onResume(){
        super.onResume();
        //helper.setSecurityArea(setSecurityArea());
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
                Toast.makeText(InputAccountActivity.this, buff, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !finished) {
            helper.sendAbort();
            finished = true;
        }
        else
            finish();
        return false;
    }


    /*@Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        helper.setSecurityArea(setSecurityArea());
    }*/
}
