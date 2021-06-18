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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
public class info extends AppCompatActivity {
    EditText e1,e2,e3,e4;
    Button b1,b2;
    String name;
    String phone_number;
    String password;
    String verification_code;
    String mVerificationId;
    int Check;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        e1=(EditText)findViewById(R.id.Name);
        e2=(EditText)findViewById(R.id.Phonenumber);
        e3=(EditText)findViewById(R.id.Password);
        e4=(EditText)findViewById(R.id.Verificationcode);
        b1=(Button)findViewById(R.id.Sumbit);
        b2=(Button)findViewById(R.id.vy);
        mAuth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        b1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               name=e1.getText().toString().trim();
               phone_number=e2.getText().toString().trim();
               password=e3.getText().toString().trim();
               Check=correct(name,phone_number,password);
               if(Check==1){
                   String ev;
                   ev=e2.getText().toString().trim();
                   idChaker(myRef,ev);


               }else {
                   Toast.makeText(info.this, " Invalid inpust", Toast.LENGTH_SHORT).show();
               }
           }
       });



       b2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String get;
               get=verification_code=e4.getText().toString().trim();
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
                            CreateNewid(myRef,phone_number,password,name);
                            RunTimeSave obj;
                            obj=new RunTimeSave();
                            obj.setNumber(phone_number);
                            Toast.makeText(info.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            Intent intent2=new Intent(info.this,homefirst.class);
                            startActivity(intent2);
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
    public void idChaker(final DatabaseReference myRef, final String ph){
        myRef.child("Uid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int kk =0;
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){

                    String value=snapshot.getValue().toString();
                    String get=ph;
                    Boolean hi;
                    hi=ph.equals(value);


                    if(hi==true){
                       Toast.makeText(info.this, "You already have Registered this number", Toast.LENGTH_SHORT).show();
                       kk=1;
                       break;

                    }

                }
                if(kk==0){
                    send_otp(phone_number);
                    Toast.makeText(info.this, "Wait For OTP", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public int correct(String name,String phone_number,String password){
        this.name = name;
        this.phone_number = phone_number;
        this.password = password;


        int n,p;
        n=phone_number.length();
        p=password.length();

        int fix=999;
        fix=1;
        if(name.isEmpty()){
            e1.setError("It can't be Empty");
            e1.requestFocus();
            fix=0;

        }else  if(n==0){
            e2.setError("It can't be Empty");
            e2.requestFocus();
            fix=0;



        }else if(n<10||n>10){
            e2.setError("Invalid number");
            e2.requestFocus();
            fix=0;
        }else if(password.isEmpty()){
            e3.setError("It can't be Empty");
            e3.requestFocus();
            fix=0;

        }
        else if(p<6||p>12){
            e3.setError("The password should be between 6-12 Numeric");
            e3.requestFocus();
            fix=0;
        }

        return fix;
    }
    public void CreateNewid(DatabaseReference myRef,String phone_number,String password,String name){
        myRef.child("Uid").push().setValue(phone_number);
        myRef.child(phone_number).child("Password").setValue(password);
        myRef.child(phone_number).child("All_chat").push().setValue("hi");
        HashMap<String,Object> map=new HashMap<>();
        map.put("PhonneNumber",phone_number);
        map.put("Nmae",name);
        myRef.child(phone_number).child("info").updateChildren(map);
        myRef.child(phone_number).child("Verification").child("password").setValue(password);
        myRef.child(phone_number).child("Verifiction").child("Phone_number").setValue(phone_number);

    }

}
