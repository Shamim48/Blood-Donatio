package com.example.abdullahjubayer.bb2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class Photo_gallery extends AppCompatActivity {

    String User_name, User_img, date;
    Calendar calander;
    SimpleDateFormat simpledateFormat;


    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;

    private ProgressBar mProgressCircle;

    FirebaseFirestore db;
    private List<Upload> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);


        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        DocumentReference user = db.collection("Photogallery").document();
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    db.collection("Photogallery").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                            mUploads.clear();
                            for (DocumentSnapshot postSnapshot : documentSnapshots) {

                                String des=postSnapshot.get("mName").toString();
                                String de_img=postSnapshot.get("mImageUrl").toString();
                                String name=postSnapshot.get("name").toString();
                                String image=postSnapshot.get("image").toString();

                                Upload upload=new Upload(des,de_img,name,image);
                                mUploads.add(upload);
                            }

                            mAdapter = new ImageAdapter(Photo_gallery.this, mUploads);
                            mAdapter.notifyDataSetChanged();
                             mRecyclerView.setAdapter(mAdapter);
                            mProgressCircle.setVisibility(View.INVISIBLE);
                        }
                    });

                }else {
                    Toast.makeText(Photo_gallery.this,"Data Not Found",Toast.LENGTH_LONG).show();
                }
            }
        });


        Intent intent = getIntent();
        User_name = intent.getStringExtra("send_name");
        User_img = intent.getStringExtra("send_img");


        calander = Calendar.getInstance();
        simpledateFormat = new SimpleDateFormat("HH:mm:ss");
        date = simpledateFormat.format(calander.getTime());


        FloatingActionButton floatingActionButton = findViewById(R.id.button_add_photo);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadPhotogalleryActivity();

            }
        });


    }

    private void uploadPhotogalleryActivity() {
        Intent intent=new Intent(getApplicationContext(),uploadPhotoActivity.class);

        intent.putExtra("nameOfSender",User_name);
        intent.putExtra("imageOfSender",User_img);
        startActivity(intent);
    }

}
