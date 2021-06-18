package com.example.test11;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class recyclview extends RecyclerView.Adapter<recyclview.ViewHolder> {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myrefback;
    private String[] data;
    private String[] post;
    private Context mcontext;
    String pronumber;
    public recyclview(String[] data,String[] post,Context context,String number){
        mcontext=context;
        this.data=data;
        this.post=post;
        pronumber=number;


    }

    @NonNull
    @Override
    public recyclview.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view2=inflater.inflate(R.layout.card2,parent,false);
        return new recyclview.ViewHolder(view2);

    }

    @Override
    public void onBindViewHolder(@NonNull recyclview.ViewHolder holder, int position) {
        final RunTimeSave objj;
        objj=new RunTimeSave();
        myrefback=database.getReference();
        final String titel=data[position];
        final String postid=post[position];
        holder.teeee.setText(postid);
        Picasso.get().load(titel).into(holder.imee);
        holder.imee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext,imageclikck.class);
                intent.putExtra("notifyrnumbero",pronumber);
                mcontext.startActivity(intent);
            }
        });
        holder.klklkl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mynumber=objj.getNumber();
                myrefback.child(mynumber).child("All_chat").child(pronumber).setValue(pronumber);
                chating(pronumber,mynumber);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.length;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imee;
        ImageView klklkl;
        TextView teeee;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imee=(CircleImageView)itemView.findViewById(R.id.searchprofile_image_comment);
            teeee=(TextView) itemView.findViewById(R.id.searchcmname);
            klklkl=(ImageView)itemView.findViewById(R.id.searchchatideid);
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
