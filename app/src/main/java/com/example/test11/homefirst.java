package com.example.test11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class homefirst extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    FirebaseDatabase database2 = FirebaseDatabase.getInstance();
    DatabaseReference posturl2;
    ArrayList<String> list2=new ArrayList<>();
    ArrayList<String> postid2=new ArrayList<>();
    RecyclerView recyclerView2;

    BottomNavigationView n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homefirst);
        recyclerView2=(RecyclerView)findViewById(R.id.recycleview);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        posturl2=database2.getReference("PostUrl");
        n=(BottomNavigationView)findViewById(R.id.bottom_nevigation);
        n.setOnNavigationItemSelectedListener(this);
        posturl2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list2.clear();
                postid2.clear();

                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String value2=snapshot.getValue().toString();
                    String pot2=snapshot.getKey().toString();
                    list2.add(value2);
                    postid2.add(pot2);

                }
                String str[] = new String[list2.size()];
                String str2[] = new String[postid2.size()];
                for (int j = 0; j < list2.size(); j++) {
                    str[j] = list2.get(j);
                    str2[j]=postid2.get(j);

                }
                List<String> lista = Arrays.asList(str);
                Collections.reverse(lista);
                str= (String[])lista.toArray();
                List<String> listab = Arrays.asList(str2);
                Collections.reverse(listab);
                str2= (String[])listab.toArray();









                recyclerView2.setAdapter(new RecycleViewAdaptor(homefirst.this,str,str2));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(homefirst.this, "url na le pa ro", Toast.LENGTH_SHORT).show();
            }
        });





    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int a=menuItem.getItemId();
        switch (a){

            case R.id.search:
                Intent intent=new Intent(this,search.class);
                startActivity(intent);
                return true;


            case R.id.account:
                Intent intente=new Intent(this,account.class);
                startActivity(intente);
                return true;




            case R.id.notification:
                Intent intente4=new Intent(this,notification.class);
                startActivity(intente4);
                return true;
            case R.id.home:
                Toast.makeText(this, "Home is selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.Chat:
                //Intent intent2=new Intent(homefirst.this,succeslogin.class);
                Intent intent2=new Intent(homefirst.this,chatidsaver.class);
                startActivity(intent2);
                return true;


        }


        return false;
    }


}
