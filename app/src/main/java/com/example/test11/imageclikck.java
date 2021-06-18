package com.example.test11;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class imageclikck extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef,posturl;
    ArrayList<String> list=new ArrayList<>();
    ArrayList<String> postid=new ArrayList<>();
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageclikck);
        Intent i=getIntent();
        final String number7=i.getStringExtra("notifyrnumbero");

        posturl=database.getReference("PostUrl");
        recyclerView=(RecyclerView)findViewById(R.id.imageclikeprofile);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myRef =database.getReference(number7).child("Images").child("galry");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                postid.clear();
                String kk = null;
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String value=snapshot.getValue().toString();
                    String pot=snapshot.getKey().toString();
                    list.add(value);
                    postid.add(pot);

                }
                String str[] = new String[list.size()];
                String str2[] = new String[postid.size()];
                for (int j = 0; j < list.size(); j++) {
                    str[j] = list.get(j);
                    str2[j]=postid.get(j);

                }
                recyclerView.setAdapter(new tmultiviewrecycleview(imageclikck.this,str,str2,number7));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(imageclikck.this, "url na le pa ro", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
