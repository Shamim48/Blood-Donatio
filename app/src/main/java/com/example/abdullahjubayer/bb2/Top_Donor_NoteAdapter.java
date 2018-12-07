package com.example.abdullahjubayer.bb2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class Top_Donor_NoteAdapter extends FirestoreRecyclerAdapter<Top_Donor_Note,Top_Donor_NoteAdapter.Top_NoteHolder> {
    Context context;
    public Top_Donor_NoteAdapter(@NonNull FirestoreRecyclerOptions<Top_Donor_Note> options,Context context) {
        super(options);

        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull Top_NoteHolder holder, int position, @NonNull Top_Donor_Note model) {
        holder.name_d.setText("Name : "+model.getName());
        holder.department.setText("Department : "+model.getDepartment());
        holder.batch.setText("Batch : "+model.getBatch());
        holder.gender.setText("Gender : "+model.getGender());
        holder.bloood_grop.setText("Blood Group : "+model.getBlood_Group());
        holder.given_tim.setText(String.valueOf("Already "+model.getGive_Blood()+" Times Given"));
        Glide.with(context).load(model.getImage()).into(holder.image_d);
    }

    @NonNull
    @Override
    public Top_NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_donor_design,
                parent, false);
        return new Top_NoteHolder(v);
    }


    class Top_NoteHolder extends RecyclerView.ViewHolder{

        TextView name_d;
        TextView department;
        TextView batch;
        TextView gender;
        TextView bloood_grop;
        TextView given_tim;
        CircleImageView image_d;

        public Top_NoteHolder(@NonNull View itemView) {
            super(itemView);

            name_d=itemView.findViewById(R.id.top_donor_name);
            department=itemView.findViewById(R.id.top_donor_department);
            batch=itemView.findViewById(R.id.top_donor_batch);
            gender=itemView.findViewById(R.id.top_donor_gender);
            bloood_grop=itemView.findViewById(R.id.top_donor_blood_group);
            given_tim=itemView.findViewById(R.id.top_donor_given_times);
            image_d=itemView.findViewById(R.id.top_donor_image);
        }
    }
}
