package com.example.test11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class chatidsaver extends AppCompatActivity {
    Button cahtidsearch;
    EditText cahtidenter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    RunTimeSave objkj;
    RecyclerView adapetrc;
    ArrayList<String> namec=new ArrayList<>();
    ArrayList<String> commentc=new ArrayList<>();
    ArrayList<String> httpc=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatidsaver);
        final String mynumber=objkj.getNumber();
        objkj=new RunTimeSave();
        adapetrc=(RecyclerView) findViewById(R.id.chatlistrecycleview);
        adapetrc.setLayoutManager(new LinearLayoutManager(this));
        cahtidsearch=(Button)findViewById(R.id.chatbtk);
        cahtidenter=(EditText)findViewById(R.id.chatetk);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef.child(mynumber).child("All_chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentc.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        String value=snapshot.getValue().toString();
                        commentc.add(value);


                    }
                    String comment2[] = new String[commentc.size()];
                    String numberph[] = new String[commentc.size()];
                    for (int j = 0; j < commentc.size(); j++) {
                        comment2[j] = commentc.get(j);


                    }
                    List<String> lista = Arrays.asList(comment2);
                    Collections.reverse(lista);
                    comment2= (String[])lista.toArray();
                    adapetrc.setAdapter(new chatingadapter(chatidsaver.this,comment2));
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        cahtidsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String number=cahtidenter.getText().toString().trim();
                if(number.isEmpty()){
                    Toast.makeText(chatidsaver.this, "error", Toast.LENGTH_SHORT).show();
                    cahtidenter.setError("Empty");

                }else {
                    myRef.child(number).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){

                                myRef.child(mynumber).child("All_chat").child(number).setValue(number);
                                chating(number);
                            }else {
                                Toast.makeText(chatidsaver.this, "Here is no id,ya number m koai space h", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(chatidsaver.this, "Sayd tero net na chal ro ", Toast.LENGTH_SHORT).show();


                        }
                    });









                }
            }
        });


    }

    void chating(String numbernumber){
        RunTimeSave objj;
        objj=new RunTimeSave();
        String hh=objj.getNumber();
        String chatid=hh+numbernumber;
        String chatid2=numbernumber+hh;
        Chatid(myRef,chatid,chatid2);


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
                        Intent intent2=new Intent(chatidsaver.this,chating.class);
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
