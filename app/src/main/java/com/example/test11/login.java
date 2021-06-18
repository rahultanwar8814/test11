package com.example.test11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {
    private static final int resms=0;
    EditText e1,e2;
    Button bt1,bt2,bt3;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECEIVE_SMS)){

            }else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS},resms);

            }




        }
        e2=(EditText)findViewById(R.id.getpass);
        e1=(EditText)findViewById(R.id.getnumber);
        bt1=(Button)findViewById(R.id.reset);
        bt2=(Button)findViewById(R.id.Verify_otp);
        bt3=(Button)findViewById(R.id.bt9);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();






        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("loading...");
                progressDialog.show();

           /* Intent intent2=new Intent(login.this,homefirst.class);
                startActivity(intent2);*/







                String phone_number,password;
                phone_number=e1.getText().toString().trim();
                password=e2.getText().toString().trim();
                int dd;
                dd=checkfield(phone_number,password);
                if(dd==1){
                    String ev=e1.getText().toString().trim();
                    idChaker(myRef,ev);

                }else {
                    progressDialog.dismiss();

                }
















            }




        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this,page1.class);
                startActivity(intent);
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(login.this,info.class);
                startActivity(intent);
            }
        });
    }


    public int checkfield(String phone_number,String password){

        int ph,ps;
        int fix=1;
        ph=phone_number.length();
        ps=password.length();
        if(phone_number.isEmpty()){
            e1.setError("Empty input");
            e1.requestFocus();
            fix=0;


        }else if (ph<10||ph>10){
            e1.setError("Invalid_Number");
            e1.requestFocus();
            fix=0;

        }else if(password.isEmpty()){
            e2.setError("Empty Input");
            e2.requestFocus();
            fix=0;
        }else if(ps<6||ps>12){
            e2.setError("Password must be 6 to 12");
            e2.requestFocus();
            fix=0;
        }

        return fix;




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
                        String phon,pasww;
                        phon=e1.getText().toString().trim();
                        pasww=e2.getText().toString().trim();
                        loginid(myRef,phon,pasww);
                        kk=1;
                        break;
                    }

                }
                if(kk==0){
                    progressDialog.dismiss();

                    Toast.makeText(login.this, "You Don't Have Account", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void loginid(final DatabaseReference myRef, final String nmb, final String psw){
        myRef.child(nmb).child("Verification").child("password").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String getpass=dataSnapshot.getValue(String.class);
                boolean hi;
                hi=getpass.equals(psw);
                if(hi==true){
                    RunTimeSave obj;
                    obj=new RunTimeSave();
                    obj.setNumber(nmb);
                    progressDialog.dismiss();
                    Intent intent2=new Intent(login.this,homefirst.class);
                    startActivity(intent2);
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                    e2.requestFocus();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case resms:{
                if(grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Request Accept", Toast.LENGTH_SHORT).show();


                }else {
                    Toast.makeText(this, "Request Rejected", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
