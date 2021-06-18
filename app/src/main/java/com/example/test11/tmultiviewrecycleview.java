package com.example.test11;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

public class tmultiviewrecycleview extends RecyclerView.Adapter {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference likedata=database.getReference("like");
    DatabaseReference comeentcount=database.getReference("All_comment").child("postid");
    DatabaseReference notification=database.getReference("notification").child("to");
    DatabaseReference myref,name;
    DatabaseReference myrefback;
    Context mcontext;
    private String[] data;
    private String[] post;
    private String number;
    String namegiven,profilegiven;

    public tmultiviewrecycleview(Context context, String[] data, String[] post,String numberw) {
        this.mcontext = context;
        this.data = data;
        this.post = post;
        number=numberw;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return 0;
        }else {
            return 1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        if(viewType==0){
            View view2=inflater.inflate(R.layout.firstcard,parent,false);
            return new tmultiviewrecycleview.profile(view2);
        }else {
            View view=inflater.inflate(R.layout.card,parent,false);
            return new tmultiviewrecycleview.galary(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final RunTimeSave obh;
        obh=new RunTimeSave();
        final String number10=obh.getNumber();

        if(position==0){
            final tmultiviewrecycleview.profile profile=(tmultiviewrecycleview.profile)holder;
            myrefback=database.getReference();
            myref=database.getReference(number).child("Images").child("Profile").child("profile");
            name=database.getReference(number).child("info").child("Nmae");
            profile.hellow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    myrefback.child(number10).child("All_chat").child(number).setValue(number);
                    chating(number,number10);
                }
            });
            name.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value=dataSnapshot.getValue().toString().trim();
                    profile.tve9.setText(value);
                    namegiven=value;



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            myref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String value=dataSnapshot.getValue().toString().trim();
                        Picasso.get().load(value).into(profile.circle);
                        profilegiven=value;

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }else {

            final String titel=data[position-1].substring(10);
            final String httpphonenumber=data[position-1].substring(0,10);
            obh.setImgnumber(httpphonenumber);
            final String postid=post[position-1];
            final tmultiviewrecycleview.galary galary=(tmultiviewrecycleview.galary)holder;
            galary.nameof.setText(namegiven);
            Picasso.get().load(profilegiven).into(galary.jj);
            likedata.child(postid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(number10).exists()){
                        galary.likef.setImageResource(R.drawable.faved_foreground);

                    }else {
                        galary.likef.setImageResource(R.drawable.fav_foreground);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            galary.commentf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mcontext,comment.class);
                    intent.putExtra("phonenumber",httpphonenumber);
                    intent.putExtra("postid",postid);
                    mcontext.startActivity(intent);

                }
            });

            likedata.child(postid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String str2 = Integer.toString((int) dataSnapshot.getChildrenCount());
                    int inum = Integer.parseInt(str2);
                    if(inum==0){
                        galary.likeef.setText("");

                    }else {
                        galary.likeef.setText(str2);

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            comeentcount.child(postid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String str2 = Integer.toString((int) dataSnapshot.getChildrenCount());
                    int inum = Integer.parseInt(str2);
                    if(inum==0){
                        galary.commenetecount.setText("");

                    }else {
                        galary.commenetecount.setText(str2);

                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            Picasso.get().load(titel).into(galary.imagef);
            galary.shairf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mcontext, "tttttttttttttmul", Toast.LENGTH_SHORT).show();
                }
            });
            galary.likef.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    likedata.child(postid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(number10).exists()){
                                obh.setLike(999);
                            }else {
                                obh.setLike(0);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                    int a;
                    a=obh.getLike();
                    if(a==999){
                        likedata.child(postid).child(number10).removeValue();
                        notification.child(httpphonenumber).child("likenotify").push().setValue(number10+"Unlike Your Photo");

                    }else {
                        likedata.child(postid).child(number10).setValue("true");
                        notification.child(httpphonenumber).child("likenotify").push().setValue(number10+"like Your Photo");


                    }

                }
            });


        }

    }

    @Override
    public int getItemCount() {
        return data.length+1;
    }
    public static class galary extends  RecyclerView.ViewHolder {
        ImageView imagef,likef,commentf,shairf,savef;
        CircleImageView jj;
        TextView likeef,shairrf,commenetecount,nameof;
        public galary(@NonNull View itemView) {
            super(itemView);
            imagef=(ImageView)itemView.findViewById(R.id.icon_button);
            likef=(ImageView)itemView.findViewById(R.id.like);
            commentf=(ImageView)itemView.findViewById(R.id.Comment);
            shairf=(ImageView)itemView.findViewById(R.id.shair);
            savef=(ImageView)itemView.findViewById(R.id.save);
            likeef=(TextView)itemView.findViewById(R.id.textView);
            shairrf=(TextView)itemView.findViewById(R.id.shair2);
            commenetecount=(TextView)itemView.findViewById(R.id.countcmt);
            nameof=(TextView)itemView.findViewById(R.id.statusname);
            jj=(CircleImageView)itemView.findViewById(R.id.statusprofile);
        }
    }

    public static class  profile extends RecyclerView.ViewHolder {
        CircleImageView circle;
        TextView tve9;
        ImageView hellow;
        Button ji;
        public profile(@NonNull View itemView) {
            super(itemView);
            tve9=(TextView)itemView.findViewById(R.id.textView5);
            circle=(CircleImageView)itemView.findViewById(R.id.profile_image);
            hellow=(ImageView)itemView.findViewById(R.id.imageView3firesttv);


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
                        Intent intent2=new Intent(mcontext,chating.class);
                        intent2.putExtra("pnb3",value);
                        mcontext.startActivity(intent2);
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
