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
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class comment extends AppCompatActivity {
    RecyclerView adapetr;


    EditText ete;
    Button bete;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    DatabaseReference notification=database.getReference("notification").child("to");
    ArrayList<String> name=new ArrayList<>();
    ArrayList<String> comment=new ArrayList<>();
    ArrayList<String> http=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        final RunTimeSave oh;
        oh=new RunTimeSave();
        myRef = database.getReference("All_comment");
        ete=(EditText)findViewById(R.id.etk) ;
        bete=(Button)findViewById(R.id.btk);
        adapetr=(RecyclerView) findViewById(R.id.commentlistrecycleview);
        adapetr.setLayoutManager(new LinearLayoutManager(this));


        Intent i=getIntent();
        final String value=i.getStringExtra("phonenumber");
        final String postid=i.getStringExtra("postid");


        myRef.child("postid").child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comment.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        String value=snapshot.getValue().toString();
                        comment.add(value);


                    }
                    String comment2[] = new String[comment.size()];
                    String numberph[] = new String[comment.size()];
                    for (int j = 0; j < comment.size(); j++) {
                        comment2[j] = comment.get(j);


                    }
                    List<String> lista = Arrays.asList(comment2);
                    Collections.reverse(lista);
                    comment2= (String[])lista.toArray();
                    adapetr.setAdapter(new commentAdapter(comment.this,comment2));
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







        final String comment=ete.getText().toString().trim();
        bete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String comment=ete.getText().toString().trim();
                if(comment.isEmpty()){
                    ete.setError("It can't be Empty");
                    ete.requestFocus();
                }else {
                    String postkrnevala=oh.getNumber();
                    myRef.child("postid").child(postid).push().setValue(postkrnevala+comment);
                    notification.child(value).child("likenotify").push().setValue(postkrnevala+"Comment on Your Photo");
                    ete.setText("");

                }
            }
        });




    }
}
