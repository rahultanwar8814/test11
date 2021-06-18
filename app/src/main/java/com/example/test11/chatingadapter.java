package com.example.test11;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatingadapter extends RecyclerView.Adapter<chatingadapter.viewholder> {




    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference likedata,myRef,myrefback;




    private String[] kcomment;
    private Context context;

    public chatingadapter(Context con,String[] comment) {
        this.context=con;

        this.kcomment = comment;
    }

    @NonNull
    @Override
    public chatingadapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View vieww=inflater.inflate(R.layout.chatingcard,parent,false);
        return new chatingadapter.viewholder(vieww);
    }

    @Override
    public void onBindViewHolder(@NonNull final chatingadapter.viewholder holder, int position) {
        final RunTimeSave objj;
        objj=new RunTimeSave();
        myrefback=database.getReference();
        final String commmmnet=kcomment[position].substring(10);
        final String phonenmber=kcomment[position].substring(0,10);
        likedata=database.getReference(phonenmber).child("info").child("Nmae");
        myRef = database.getReference(phonenmber).child("Images").child("Profile").child("profile");
        likedata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nameof=dataSnapshot.getValue().toString().trim();
                holder.nameocan.setText(nameof);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.tprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mynumber=objj.getNumber();
                myrefback.child(mynumber).child("All_chat").child(phonenmber).setValue(phonenmber);
                chating(phonenmber,mynumber);

            }
        });
        holder.nameocan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,imageclikck.class);
                intent.putExtra("notifyrnumbero",phonenmber);
                context.startActivity(intent);
            }
        });



        holder.commment.setText(commmmnet);
        myRef = database.getReference(phonenmber).child("Images").child("Profile").child("profile");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String http=dataSnapshot.getValue().toString().trim();
                    Picasso.get().load(http).into(holder.tprofileimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    @Override
    public int getItemCount() {
        return kcomment.length;
    }

    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView tprofileimage;
        TextView nameocan,commment;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            tprofileimage=(CircleImageView)itemView.findViewById(R.id.profile_image_commentk);
            nameocan=(TextView)itemView.findViewById(R.id.cmnamek);
            commment=(TextView)itemView.findViewById(R.id.textView10k);

        }
    }
    void chating(String numbernumber,String hh){
        String chatid=hh+numbernumber;
        String chatid2=numbernumber+hh;
        Chatid(myrefback,chatid,chatid2);


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
                        Intent intent2=new Intent(context,chating.class);
                        intent2.putExtra("pnb3",value);
                        context.startActivity(intent2);
                        kk=1;
                        break;
                    }

                }

                if(kk==0){
                    intent(myrefback,ph11);
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
