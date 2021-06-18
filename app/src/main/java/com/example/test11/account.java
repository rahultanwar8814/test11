package com.example.test11;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class account extends AppCompatActivity {
    ImageView t1, t2;
    Button b1, b2, b3,b4;
    public static final int PICK_IMAGE = 1;
    Uri add;
    private FirebaseStorage storage;
    private StorageReference mStorageRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef,posturl;
    int resultt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        RunTimeSave objj3;
        objj3=new RunTimeSave();

        String number5=objj3.getNumber();


        storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReference();
        myRef = database.getReference(number5).child("Images");
        posturl=database.getReference("PostUrl");

        t1 = (ImageView) findViewById(R.id.iv);

        b1 = (Button) findViewById(R.id.bt);
        b4 = (Button) findViewById(R.id.pro);
        b2 = (Button) findViewById(R.id.bt1);
        b3 = (Button) findViewById(R.id.bt3);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Choiceimg();

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(resultt==1){
                    RunTimeSave objj4;
                    objj4=new RunTimeSave();
                    String number6=objj4.getNumber();
                    galary2(number6);


                }else {
                    Toast.makeText(account.this, "Please Choice image", Toast.LENGTH_SHORT).show();
                }


            }
        });


        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(account.this,ViewUpload.class);
                startActivity(intent);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(resultt==1){
                    RunTimeSave objj5;
                    objj5=new RunTimeSave();
                    String number7=objj5.getNumber();
                    profile(number7);

                }else {
                    Toast.makeText(account.this, "Please Choice image", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            add = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), add);
                t1.setImageBitmap(bitmap);
                resultt=1;
            } catch (IOException e) {
                e.printStackTrace();


            }
        }else {
            resultt=0;
        }
    }

    public void Choiceimg() {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery, "select picture"), PICK_IMAGE);
    }


    public void galary2(final String m){
        Toast.makeText(account.this, "Uploading starting", Toast.LENGTH_SHORT).show();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("kaam chal raha h...");
        progressDialog.show();

        final StorageReference riversRef = mStorageRef.child(m).child("galryImage").child(System.currentTimeMillis()+"."+getextantion(add));
        Toast.makeText(account.this, "kuch kuch chalo", Toast.LENGTH_SHORT).show();

        riversRef.putFile(add)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(account.this, "Upload h go re", Toast.LENGTH_SHORT).show();



                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        String postidid= myRef.child("galry").push().getKey();
                                        posturl.child(postidid).setValue(m+imageUrl);
                                        myRef.child("galry").child(postidid).setValue(m+imageUrl);
                                    }
                                });
                            }
                        }

                        progressDialog.dismiss();

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("uploaded" + (int) progress + "%");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(account.this, "Upload fail", Toast.LENGTH_SHORT).show();
                    }
                });


    }


    public String getextantion(Uri add){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(add));
    }
    public void profile(String number){
        Toast.makeText(account.this, "Uploading starting", Toast.LENGTH_SHORT).show();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("kaam chal raha h...");
        progressDialog.show();

        final StorageReference riversRef = mStorageRef.child(number).child(number+"."+getextantion(add));
        Toast.makeText(account.this, "kuch kuch chalo", Toast.LENGTH_SHORT).show();

        riversRef.putFile(add)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(account.this, "Upload h go re", Toast.LENGTH_SHORT).show();



                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        myRef.child("Profile").child("profile").setValue(imageUrl);
                                    }
                                });
                            }
                        }

                        progressDialog.dismiss();

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("uploaded" + (int) progress + "%");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(account.this, "Upload fail", Toast.LENGTH_SHORT).show();
                    }
                });



    }

}
