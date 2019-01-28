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

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteHolder> {
    Context mcontext;
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options,Context context) {
        super(options);
        this.mcontext=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull Note model) {
        holder.textViewTitle.setText(model.getName());
        holder.textViewDescription.setText(model.getDescription());
        holder.textViewPriority.setText("Post :"+String.valueOf(model.getTime()));
        Glide.with(mcontext)
                .load(model.getImage())
                .into(holder.circleImageView);
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,
                parent, false);
        return new NoteHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewPriority;
        CircleImageView circleImageView;

        public NoteHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);
            circleImageView=itemView.findViewById(R.id.post_image);
        }
    }
}