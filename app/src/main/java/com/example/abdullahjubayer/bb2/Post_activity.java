package com.example.abdullahjubayer.bb2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Post_activity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Notebook");

    private NoteAdapter adapter;

    String User_name,User_img,User_message;
    Calendar calander;
    SimpleDateFormat simpledateformat;
    String Date;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_activity);

        progressBar=findViewById(R.id.spin_kit_post);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);

        Intent intent=getIntent();
            User_name=intent.getStringExtra("send_name");
            User_img=intent.getStringExtra("send_img");


        calander = Calendar.getInstance();


        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = collectionReference.orderBy("time", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        adapter = new NoteAdapter(options,getApplicationContext());

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {


                LayoutInflater inflater= LayoutInflater.from(Post_activity.this);
                final View view=inflater.inflate(R.layout.alart_pass_layout,null);
                final EditText eee=view.findViewById(R.id.admin_pass);
                new AlertDialog.Builder(Post_activity.this)
                        .setView(view)
                        .setTitle("Are You Admin...!")
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String pass=eee.getText().toString();

                                if (pass.equals("12345")){
                                    adapter.deleteItem(viewHolder.getAdapterPosition());
                                }else {
                                    Toast.makeText(getApplicationContext(),"You are Not Admin",Toast.LENGTH_LONG).show();
                                }

                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create()
                        .show();


            }
        }).attachToRecyclerView(recyclerView);


        FloatingActionButton  floatingActionButton=findViewById(R.id.button_add_note);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater= LayoutInflater.from(Post_activity.this);
                final View view=inflater.inflate(R.layout.alart_dialog_layout,null);
                final EditText eee=view.findViewById(R.id.alart_message);


                new AlertDialog.Builder(Post_activity.this)
                        .setView(view)
                        .setTitle("Request For Blood")
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        progressBar.setVisibility(View.VISIBLE);
                                        User_message=eee.getText().toString();

                                        simpledateformat = new SimpleDateFormat("dd/MM/yyy HH:mm");
                                        Date = simpledateformat.format(calander.getTime());

                                        if (User_message.isEmpty()){
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(Post_activity.this,"Please Write Something",Toast.LENGTH_SHORT).show();
                                        }else {
                                            if (!User_name.isEmpty() && !User_img.isEmpty() && !User_message.isEmpty() && !Date.isEmpty()){
                                                saveMessage(User_name,User_img,User_message,Date);
                                            }
                                            else {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(Post_activity.this,"Something Missing",Toast.LENGTH_SHORT).show();
                                            }
                                        }

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show()
                ;


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void saveMessage(String user_name, String user_img, String user_message,String date){




            final Map<String, Object> hash = new HashMap<>();
            hash.clear();
            hash.put("name",user_name);
            hash.put("image",user_img);
            hash.put("time",date);
            hash.put("description",user_message);
            collectionReference.document().set(hash).addOnCompleteListener(Post_activity.this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(Post_activity.this,"Request Sent",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }).addOnFailureListener(Post_activity.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Post_activity.this,"Request Failed..!",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
