package com.example.test11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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

public class notification extends AppCompatActivity {
    RecyclerView adapetr5;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference notification=database.getReference("notification").child("to");
    ArrayList<String> name=new ArrayList<>();
    ArrayList<String> comment=new ArrayList<>();
    ArrayList<String> http=new ArrayList<>();
    RunTimeSave oh;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        adapetr5=(RecyclerView) findViewById(R.id.notificationList2);
        adapetr5.setLayoutManager(new LinearLayoutManager(this));
        oh=new RunTimeSave();
        String tonumber=oh.getNumber();

        next(tonumber);

    }


    void next(String tonumber){
        notification.child(tonumber).child("likenotify").addValueEventListener(new ValueEventListener() {
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
                    adapetr5.setAdapter(new commentAdapter(notification.this,comment2));
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
