package com.example.abdullahjubayer.bb2;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    EditText e_phone, e_lastdonate, e_how;
    CircleImageView e_image;
    private Uri mImageUri;
    String down_Url;
    private StorageReference mStorageRef;
    FirebaseFirestore db;
    String bll, ell,phn,last_date,how_m,gender_m;
    Button up_button;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mStorageRef = FirebaseStorage.getInstance().getReference("editable_photo");
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        bll = intent.getStringExtra("Blood_group");
        ell = intent.getStringExtra("Email");
        phn=intent.getStringExtra("Phone");
        last_date=intent.getStringExtra("Last_Donate");
        how_m=intent.getStringExtra("How_Much");
        gender_m=intent.getStringExtra("Gender");

        progressBar=findViewById(R.id.spin_kit_edit);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);



        e_phone = findViewById(R.id.edit_pro_phon);
        e_lastdonate = findViewById(R.id.edit_pro_lastdonate);
        e_how = findViewById(R.id.edit_pro_howmuch);
        e_image = findViewById(R.id.edit_pro_image);
        up_button=findViewById(R.id.upload_button);


        e_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                up_button.setClickable(false);
                uploadImage();
            }
        });



    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Glide.with(this).load(mImageUri).into(e_image);
        }
    }


    private void uploadImage() {

        final StorageReference storageReference = mStorageRef.child(System.currentTimeMillis()
                + "." + "jpg");

        if (mImageUri != null) {
            storageReference.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();

                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                            }
                        }, 500);

                        if (task.isSuccessful()) {

                            down_Url = task.getResult().toString();
                            uploadWithImage();

                        }

                    } else {
                        Toast.makeText(EditProfile.this, "Picture Upload failed.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        up_button.setClickable(true);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfile.this, "Picture Upload failed.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    up_button.setClickable(true);
                }
            });


        }else {

            uploadWithoutImage();

        }

    }


    public void uploadWithoutImage() {

        String ph = e_phone.getText().toString();
        String lst = e_lastdonate.getText().toString();
        String how = e_how.getText().toString();

        if (ph.isEmpty()){
            ph=phn;
        }
        if (lst.isEmpty()){
            lst=last_date;
        }
        if (how.isEmpty()){
            how=how_m;
        }


        DocumentReference documentReference=db.collection("All_Blood_Group").document(bll).collection(gender_m).document(ell);
        documentReference.update("Phone1",ph);
        documentReference.update("Last_Donate_Date",lst);
        documentReference.update("Give_Blood",how)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        update2_no_image();
                        progressBar.setVisibility(View.INVISIBLE);
                        up_button.setClickable(true);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, "Updated Failed", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        up_button.setClickable(true);
                    }
                });

    }


    public void uploadWithImage() {


        String ph = e_phone.getText().toString();
        String lst = e_lastdonate.getText().toString();
        String how = e_how.getText().toString();
        if (ph.isEmpty()){
            ph=phn;
        }
        if (lst.isEmpty()){
            lst=last_date;
        }
        if (how.isEmpty()){
            how=how_m;
        }
        DocumentReference documentReference=db.collection("All_Blood_Group").document(bll).collection(gender_m).document(ell);
        documentReference.update("Phone1",ph);
        documentReference.update("Image",down_Url);
        documentReference.update("Last_Donate_Date",lst);
        documentReference.update("Give_Blood",how)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        update2_with_image();
                        progressBar.setVisibility(View.INVISIBLE);
                        up_button.setClickable(true);

                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this, "Updated Failed..!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                up_button.setClickable(true);
            }
        });


    }


    private void update2_with_image() {

        String how = e_how.getText().toString();
        if (how.isEmpty()){
            how=how_m;
        }

        DocumentReference user=db.collection("All_donor_Info").document(bll).collection("Top_Donor").document(ell);
        user.update("give_Blood",how);
        user.update("image",down_Url)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(EditProfile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),Login_activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        progressBar.setVisibility(View.INVISIBLE);
                        up_button.setClickable(true);
                    }
                });
    }

    private void update2_no_image() {

        String how = e_how.getText().toString();
        if (how.isEmpty()){
            how=how_m;
        }

        DocumentReference user=db.collection("All_donor_Info").document(bll).collection("Top_Donor").document(ell);
        user.update("give_Blood",how)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(EditProfile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),Login_activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        progressBar.setVisibility(View.INVISIBLE);
                        up_button.setClickable(true);
                    }
                });

    }


}
