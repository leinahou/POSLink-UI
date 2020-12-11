package com.example.poslinkui;

import android.app.AlarmManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pax.poslink.CommSetting;
import com.pax.poslink.ManageRequest;
import com.pax.poslink.POSLinkAndroid;
import com.pax.poslink.PaymentRequest;
import com.pax.poslink.PaymentResponse;
import com.pax.poslink.PosLink;
import com.pax.poslink.ProcessTransResult;
import com.pax.poslink.broadpos.BroadPOSCommunicator;
import com.pax.poslink.poslink.POSLinkCreator;

import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button button;
    PosLink posLink;
    PaymentRequest paymentRequest;
    CommSetting commSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        doPayment();
                        //doInputAccount();
                    }
                });
                thread.start();
            }
        });

        /**
         *
         *  Set time using Calendar and AlarmManager
         *
         *      try{
         *             Calendar c = Calendar.getInstance();
         *             c.set(2013, 8, 15, 12, 34, 56);
         *             AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
         *             am.setTime(c.getTimeInMillis());
         *         }catch (Exception e){
         *             System.out.println("Issue captured: " + e.getMessage());
         *         }
         */


    }

    private void doPayment(){
        paymentRequest = new PaymentRequest();

        Random random = new Random();
        int amount = random.nextInt(1000) + 500;

        paymentRequest.TenderType = paymentRequest.ParseTenderType("CREDIT");
        paymentRequest.TransType = paymentRequest.ParseTransType("SALE");
        //paymentRequest.Amount = String.valueOf(amount);
        paymentRequest.ECRRefNum = "1";

        commSetting = buildConnection();

        POSLinkAndroid.init(getApplicationContext(), commSetting);//fragment_commSetting.getCommSetting());

        posLink = new POSLinkCreator().createPoslink(getApplicationContext());
        posLink.PaymentRequest = paymentRequest;
        posLink.SetCommSetting(commSetting);//(fragment_commSetting.getCommSetting());

        try{
            ProcessTransResult processTransResult = posLink.ProcessTrans();
            PaymentResponse paymentResponse = posLink.PaymentResponse;
            String str = paymentResponse.ResultTxt + " " + paymentResponse.ResultCode + " " + paymentResponse.ApprovedAmount;
            System.out.println(str);
        }catch (Exception e){
            System.out.println("Issue Captured: " + e.getMessage());
        }
    }

    private void doInputAccount(){
        ManageRequest manageRequest = new ManageRequest();

        manageRequest.TransType = manageRequest.ParseTransType("INPUTACCOUNT");
        manageRequest.EDCType = manageRequest.ParseEDCType("GIFT");
        manageRequest.MagneticSwipeEntryFlag = "1";
        manageRequest.KeySlot = "1";
        manageRequest.TimeOut = "300";
        manageRequest.ContinuousScreen = "1";

        commSetting = buildConnectionUsingTCP();

        POSLinkAndroid.init(getApplicationContext(), commSetting);//fragment_commSetting.getCommSetting());

        posLink = new POSLinkCreator().createPoslink(getApplicationContext());
        posLink.ManageRequest = manageRequest;
        posLink.SetCommSetting(commSetting);//(fragment_commSetting.getCommSetting());

        try{
            ProcessTransResult processTransResult = posLink.ProcessTrans();
            //PaymentResponse paymentResponse = posLink.PaymentResponse;
            //String str = paymentResponse.ResultTxt + " " + paymentResponse.ResultCode + " " + paymentResponse.ApprovedAmount;
            //System.out.println(str);
        }catch (Exception e){
        }
    }

    private CommSetting buildConnectionUsingTCP(){
        CommSetting commSetting = new CommSetting();
        commSetting.setDestIP("172.16.2.36");
        commSetting.setDestPort("10009");
        commSetting.setTimeOut("-1");
        commSetting.setType(CommSetting.TCP);
        return commSetting;
    }

    private void buildBroadCommunictor(){
        BroadPOSCommunicator broadPOSCommunicator = BroadPOSCommunicator.getInstance(getApplicationContext());
        broadPOSCommunicator.startListeningService(new BroadPOSCommunicator.StartListenerCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Success",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFail(String s) {
                Toast.makeText(getApplicationContext(), "Fail: " + s,Toast.LENGTH_LONG).show();
            }
        });
    }
    private CommSetting buildConnection(){
        CommSetting commSetting = new CommSetting();
        commSetting.setType(CommSetting.AIDL);
        commSetting.setTimeOut("-1");
        return commSetting;
    }
}
