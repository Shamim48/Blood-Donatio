package com.example.abdullahjubayer.bb2;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class uploadPhotoActivity extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;

    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private EditText mEditTextFileName;
    private ImageView mImageView;

    private Uri mImageUri;
    String down_Url;

    private StorageReference mStorageRef;
    FirebaseFirestore db;


    String nameof_writer;
    String imageof_writer;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        db = FirebaseFirestore.getInstance();

        Intent intent=getIntent();
        nameof_writer=intent.getStringExtra("nameOfSender");
        imageof_writer=intent.getStringExtra("imageOfSender");



        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mImageView = findViewById(R.id.image_view);




        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message=mEditTextFileName.getText().toString();
                if (mImageUri!=null){
                    if ( !message.isEmpty()){
                        uploadImage(message);
                    }else {
                        Toast.makeText(uploadPhotoActivity.this,"Text is Empty",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(uploadPhotoActivity.this,"Image Not Select",Toast.LENGTH_SHORT).show();

                }

            }
        });
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void uploadImage(final String message) {

        final StorageReference storageReference = mStorageRef.child(System.currentTimeMillis()
                + "." + getFileExtension(mImageUri));

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

                        if (task.isSuccessful()){

                             down_Url=task.getResult().toString();

                            if (message.isEmpty()){
                                Toast.makeText(uploadPhotoActivity.this,"Please Write Something",Toast.LENGTH_SHORT).show();
                            }else {
                                if (!nameof_writer.isEmpty() && !imageof_writer.isEmpty() && !message.isEmpty() && !down_Url.isEmpty()){
                                    uploadData(message,down_Url,nameof_writer,imageof_writer);
                                }
                                else {
                                    Toast.makeText(uploadPhotoActivity.this,"Something Missing",Toast.LENGTH_SHORT).show();
                                }
                            }

                        }

                    } else {
                        Toast.makeText(uploadPhotoActivity.this, "Picture Upload failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(uploadPhotoActivity.this, "Picture Upload failed.", Toast.LENGTH_SHORT).show();
                }
            });


        }

    }

    private void uploadData(String message, String down_Url, String nameof_writer, String imageof_writer) {
        Upload upload = new Upload(message, down_Url,nameof_writer,imageof_writer);

        DocumentReference user = db.collection("Photogallery").document();
        user.set(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(uploadPhotoActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(uploadPhotoActivity.this, "Upload Failed", Toast.LENGTH_LONG).show();
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

            Glide.with(this).load(mImageUri).into(mImageView);
        }
    }


}
