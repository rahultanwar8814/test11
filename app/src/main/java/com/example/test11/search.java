package com.example.test11;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class search extends AppCompatActivity {



    EditText t1;
    ImageView image;
    RecyclerView recyclerView2;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef,mane;
    static String http,name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        t1=(EditText) findViewById(R.id.searchtext);
        image=(ImageView)findViewById(R.id.search);
        recyclerView2=(RecyclerView)findViewById(R.id.programingList);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));



        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String searchid=null;
                searchid=t1.getText().toString().trim();
                int n=searchid.length();

                if(n<10||n>10){
                    t1.setError("Invalid number");
                    t1.requestFocus();


                }else {
                    myRef = database.getReference(searchid).child("Images").child("Profile").child("profile");
                    mane=database.getReference().child(searchid).child("info").child("Nmae");
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                http=dataSnapshot.getValue().toString().trim();





                            }else {
                                Toast.makeText(search.this, "No Account", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    mane.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                name=dataSnapshot.getValue().toString().trim();
                                String[] str={http};
                                String[] str2={name};
                                String searchid2=null;
                                searchid2=t1.getText().toString().trim();
                                recyclerView2.setAdapter(new recyclview(str,str2,search.this,searchid2));



                            }else {
                                Toast.makeText(search.this, "There no id", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });








                }



            }
        });



    }
}
