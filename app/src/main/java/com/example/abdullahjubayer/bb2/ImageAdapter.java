package com.example.abdullahjubayer.bb2;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;

    public ImageAdapter(Context context, List<Upload> uploads) {
        mContext = context;
        mUploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
        holder.w_name.setText(uploadCurrent.getName());
        holder.descri_text.setText(uploadCurrent.getmName());
        Glide.with(mContext)
                .load(uploadCurrent.getmImageUrl())
                .into(holder.des_image);
        Glide.with(mContext)
                .load(uploadCurrent.getImage())
                .into(holder.w_image);
    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView w_name;
        public TextView descri_text;
        public CircleImageView w_image;
        public ImageView des_image;

        public ImageViewHolder(View itemView) {
            super(itemView);

            w_name = itemView.findViewById(R.id.writer_cir_name);
            descri_text = itemView.findViewById(R.id.writer_cir_des);
            w_image=itemView.findViewById(R.id.writer_cir_image);
            des_image=itemView.findViewById(R.id.writer_cir_des_image);
        }
    }
}
