package com.example.test11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class succeslogin extends AppCompatActivity {
    Button bt1;
    EditText et1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succeslogin);
        bt1=(Button)findViewById(R.id.bt);
        et1=(EditText)findViewById(R.id.et);

        FirebaseDatabase database;
        final DatabaseReference myRef;
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number=et1.getText().toString().trim();
                int n;
                n=number.length();



               if(n==10){
                   idChaker(myRef,number);

               }else {
                   et1.setError("Invalid Field");
                   et1.requestFocus();
               }






            }
        });

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

                        RunTimeSave objj;
                        objj=new RunTimeSave();
                        String hh=objj.getNumber();
                        String chatid=hh+ph;
                        String chatid2=ph+hh;
                        Chatid(myRef,chatid,chatid2);
                        kk=1;
                        break;
                    }

                }
                if(kk==0){

                    Toast.makeText(succeslogin.this, "This number have no Account.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void Chatid(final DatabaseReference myRef, final String ph11,final String ph2){
        myRef.child("Chatid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int kk=0;
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String value=snapshot.getValue().toString();


                    Boolean hi,hi2;
                    hi2=ph2.equals(value);
                    hi=ph11.equals(value);
                    if(hi==true||hi2==true){
                        Intent intent2=new Intent(succeslogin.this,chating.class);
                        intent2.putExtra("pnb3",value);
                        startActivity(intent2);
                        kk=1;
                        break;
                    }

                }

                if(kk==0){
                    intent(myRef,ph11);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void intent(DatabaseReference myRef,String chadid){

        myRef.child("Chatid").push().setValue(chadid);


    }







}



