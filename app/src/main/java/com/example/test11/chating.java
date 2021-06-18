package com.example.test11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class chating extends AppCompatActivity {
    EditText writtenchat;
    ListView chatView;
    Button b1;
    ArrayList<String> list=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chating);
        writtenchat=(EditText)findViewById(R.id.et);
        chatView=(ListView) findViewById(R.id.lv);
        b1=(Button)findViewById(R.id.bt);
        myRef = database.getReference();
        Intent i=getIntent();
        String hh=i.getStringExtra("pnb3");
        myRef.child("All_chat").child(hh).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String value=snapshot.getValue().toString();
                    list.add(value);
                    arrayAdapter= new ArrayAdapter<String>(chating.this,android.R.layout.simple_list_item_1, list);
                    chatView.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(chating.this, "Chat get faild", Toast.LENGTH_SHORT).show();

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String get;
                get=writtenchat.getText().toString();
                if(get.length()!=0){
                    Send(get,myRef);


                }

                writtenchat.setText("");
            }
        });

    }

    private void Send(String get, DatabaseReference myRef) {
        Intent i=getIntent();
        String hh=i.getStringExtra("pnb3");
        myRef.child("All_chat").child(hh).push().setValue(get);

    }

}
