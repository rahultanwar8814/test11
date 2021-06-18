package com.example.test11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class page1 extends AppCompatActivity {

    EditText et1,et2,et3,et4;
    Button bt11,b99;
    FirebaseAuth mAuth;
    String mVerificationId;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);
        et1=(EditText)findViewById(R.id.phonenumber);
        et2=(EditText)findViewById(R.id.otp);
        et3=(EditText)findViewById(R.id.p11);
        et4=(EditText)findViewById(R.id.p2);
        mAuth=FirebaseAuth.getInstance();

        bt11=(Button)findViewById(R.id.reset);
        b99=(Button)findViewById(R.id.bty);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        bt11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number,p1,p2;
                phone_number=et1.getText().toString().trim();

                p1=et3.getText().toString().trim();
                p2=et4.getText().toString().trim();
                int gg=0;
                gg=fild(phone_number,p1,p2);
                if(gg==1){
                    Toast.makeText(page1.this, "Wait For OTP", Toast.LENGTH_SHORT).show();
                    send_otp(phone_number);


                }









            }
        });
        b99.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String get;
                get=et2.getText().toString().trim();
                verify(get);
            }
        });

    }




    public void send_otp(String s){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+s,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);


    }


    public PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            String code=credential.getSmsCode();
            Toast.makeText(getApplicationContext(),code,Toast.LENGTH_LONG).show();
            verify(code);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(getApplicationContext(),"Invalid Number",Toast.LENGTH_LONG).show();
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Toast.makeText(getApplicationContext(),"OTP Limit Over",Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            mVerificationId = verificationId;

        }
    };



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            String passs,nmber;
                            passs=et4.getText().toString();
                            nmber=et1.getText().toString();
                            myRef.child(nmber).child("Verification").child("password").setValue(passs);
                            Toast.makeText(page1.this, "Successful password Reset Process", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = task.getResult().getUser();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                            }
                        }
                    }
                });
    }
    public void verify(String code){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }



    public int fild(String phone_number,String p1,String p2){
        int fix=1;
        int n,pt1,pt2;
        n=phone_number.length();
        pt1=p1.length();
        pt2=p2.length();
        if(phone_number.isEmpty()){
            et1.setError("Empty field");
            et1.requestFocus();
            fix=0;
        }else if (n<10||n>10){
            et1.setError("Invalid number");
            et1.requestFocus();
            fix=0;

        }else if (p1.isEmpty()){
            et3.setError("Empty field");
            et3.requestFocus();
            fix=0;

        }else if (pt1<6||pt1>12){
            et3.setError("The password should be between 6-12 Numeric");
            et3.requestFocus();
            fix=0;

        }else if (p2.isEmpty()){
            et4.setError("Field empty");
            et4.requestFocus();
            fix=0;

        }else if (pt2<6||pt1>12){
            et4.setError("The password should be between 6-12 Numeric");
            et4.requestFocus();
            fix=0;

        }else if (sam(p1,p2)==0){

            fix=0;
        }
        return fix;





    }
    public int sam(String p1,String p2){

        int fix2=1;
        boolean jk;
        jk=p1.equals(p2);
        if(jk==true){
            fix2=1;
        }else {
            et4.requestFocus();
            et4.setError("Password not Match");
            fix2=0;
        }
        return fix2;


    }

}
