package com.example.test11;
import android.app.ProgressDialog;
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

public class RecycleViewAdaptor extends RecyclerView.Adapter<RecycleViewAdaptor.ProgramingViewHolder> {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference likedata=database.getReference("like");
    DatabaseReference comeentcount=database.getReference("All_comment").child("postid");
    DatabaseReference notification=database.getReference("notification").child("to");
    DatabaseReference myRef=database.getReference();
    DatabaseReference myRefback=database.getReference();
    DatabaseReference profile,name;

    Context mcontext;

    private String[] data;
    private String[] post;
    public RecycleViewAdaptor(Context context,String[] data,String[] post){
        this.mcontext=context;
        this.data=data;
        this.post=post;


    }
    @NonNull
    @Override
    public ProgramingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.card,parent,false);
        return new ProgramingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProgramingViewHolder holder, int position) {

        final RunTimeSave obh;
        obh=new RunTimeSave();
        final String number10=obh.getNumber();
        final String titel=data[position].substring(10);
        final String postid=post[position];
        final String httpphonenumber=data[position].substring(0,10);
        profile=database.getReference(httpphonenumber).child("Images").child("Profile").child("profile");
        name=database.getReference(httpphonenumber).child("info").child("Nmae");
        name.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value=dataSnapshot.getValue().toString().trim();
                holder.statusname.setText(value.toUpperCase());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        profile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String value=dataSnapshot.getValue().toString().trim();
                    Picasso.get().load(value).into(holder.statusprofile);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        likedata.child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(number10).exists()){
                    holder.like.setImageResource(R.drawable.faved_foreground);




                }else {
                    holder.like.setImageResource(R.drawable.fav_foreground);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        likedata.child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String str2 = Integer.toString((int) dataSnapshot.getChildrenCount());
                int inum = Integer.parseInt(str2);
                if(inum==0){
                    holder.likee.setText("");

                }else {
                    holder.likee.setText(str2);

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
                    holder.commenetecount.setText("");

                }else {
                    holder.commenetecount.setText(str2);

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        holder.shair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mynumber=obh.getNumber();
                myRefback.child(mynumber).child("All_chat").child(httpphonenumber).setValue(httpphonenumber);
                chating(httpphonenumber);

            }
        });
        Picasso.get().load(titel).fit().into(holder.image);









        holder.statusprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext,imageclikck.class);
                intent.putExtra("notifyrnumbero",httpphonenumber);
                mcontext.startActivity(intent);
            }
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext,comment.class);
                intent.putExtra("phonenumber",httpphonenumber);
                intent.putExtra("postid",postid);
                mcontext.startActivity(intent);

            }
        });
        holder.like.setOnClickListener(new View.OnClickListener() {
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
                    notification.child(httpphonenumber).child("likenotify").push().setValue(number10+"liked Your Photo");


                }

            }
        });






    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public static class ProgramingViewHolder extends RecyclerView.ViewHolder{
        ImageView image,like,comment,shair,save,statusprofile;
        TextView likee,shairr,commenetecount,statusname;
        public ProgramingViewHolder(@NonNull View itemView) {

            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.icon_button);
            like=(ImageView)itemView.findViewById(R.id.like);
            comment=(ImageView)itemView.findViewById(R.id.Comment);
            shair=(ImageView)itemView.findViewById(R.id.shair);
            save=(ImageView)itemView.findViewById(R.id.save);
            likee=(TextView)itemView.findViewById(R.id.textView);
            shairr=(TextView)itemView.findViewById(R.id.shair2);
            commenetecount=(TextView)itemView.findViewById(R.id.countcmt);
            statusprofile=(ImageView)itemView.findViewById(R.id.statusprofile);
            statusname=(TextView)itemView.findViewById(R.id.statusname);



        }
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
                        Intent intent2=new Intent(mcontext,chating.class);
                        intent2.putExtra("pnb3",value);
                        mcontext.startActivity(intent2);
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
