package com.example.test11;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class multiviewrecycleview extends RecyclerView.Adapter {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference likedata=database.getReference("like");
    DatabaseReference comeentcount=database.getReference("All_comment").child("postid");
    DatabaseReference notification=database.getReference("notification").child("to");
    DatabaseReference myref,name;
    Context mcontext;
    private String[] data;
    private String[] post;
    String namegiven,profilegiven;

    public multiviewrecycleview(Context context, String[] data, String[] post) {
        this.mcontext = context;
        this.data = data;
        this.post = post;
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
            View view2=inflater.inflate(R.layout.viewgalarycard,parent,false);
            return new profile(view2);
        }else {
            View view=inflater.inflate(R.layout.viewgalarycard2,parent,false);
            return new galary(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final RunTimeSave obh;
        obh=new RunTimeSave();
        final String number10=obh.getNumber();

        if(position==0){
            final profile profile=(multiviewrecycleview.profile)holder;

            myref=database.getReference(number10).child("Images").child("Profile").child("profile");
            name=database.getReference(number10).child("info").child("Nmae");
            name.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value=dataSnapshot.getValue().toString().trim();
                    profile.tve9.setText(value.toUpperCase());
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
            final galary galary=(multiviewrecycleview.galary)holder;



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
            galary.nameof.setText(namegiven);
            Picasso.get().load(profilegiven).into(galary.jj);


        }

    }

    @Override
    public int getItemCount() {
        return data.length+1;
    }
    public static class galary extends  RecyclerView.ViewHolder {
        ImageView imagef,likef,commentf;
        CircleImageView jj;
        TextView likeef,commenetecount,nameof;
        public galary(@NonNull View itemView) {
            super(itemView);
            imagef=(ImageView)itemView.findViewById(R.id.icon_buttonq);
            likef=(ImageView)itemView.findViewById(R.id.likeq);
            commentf=(ImageView)itemView.findViewById(R.id.Commentq);
            likeef=(TextView)itemView.findViewById(R.id.textViewq);
            commenetecount=(TextView)itemView.findViewById(R.id.countcmtq);
            nameof=(TextView)itemView.findViewById(R.id.statusnameq);
            jj=(CircleImageView)itemView.findViewById(R.id.statusprofileq);
        }
    }

    public static class  profile extends RecyclerView.ViewHolder {
        CircleImageView circle;

        TextView tve9;
        public profile(@NonNull View itemView) {
            super(itemView);
            tve9=(TextView)itemView.findViewById(R.id.textView5g);
            circle=(CircleImageView)itemView.findViewById(R.id.profile_imageg);
        }
    }
}
