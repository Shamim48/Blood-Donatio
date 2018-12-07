package com.example.abdullahjubayer.bb2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class showTopDonor extends AppCompatActivity {

    String blood_group;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();


    private Top_Donor_NoteAdapter top_donor_noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_top_donor);

        Intent intent=getIntent();
        blood_group=intent.getStringExtra("Blood_group");

        setRecyclerView();


    }

    private void setRecyclerView() {

        CollectionReference collectionReference = db.collection("All_donor_Info").document(blood_group).collection("Top_Donor");
        Query query = collectionReference.orderBy("give_Blood", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Top_Donor_Note> options = new FirestoreRecyclerOptions.Builder<Top_Donor_Note>()
                .setQuery(query, Top_Donor_Note.class)
                .build();

        top_donor_noteAdapter = new Top_Donor_NoteAdapter(options,showTopDonor.this);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_top_donor);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(top_donor_noteAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        top_donor_noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        top_donor_noteAdapter.stopListening();
    }
}
