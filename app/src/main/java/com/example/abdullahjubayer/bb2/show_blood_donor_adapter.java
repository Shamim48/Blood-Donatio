package com.example.abdullahjubayer.bb2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class show_blood_donor_adapter extends BaseAdapter {

    Context context;
    ArrayList<String> name;
    ArrayList<String> image;
    ArrayList<String> last_donet_date;
    ArrayList<String> phone;
    ArrayList<String> department;
    ArrayList<String> Batch;
    ArrayList<String> Village;
    ArrayList<String> Upazilla;
    ArrayList<String> Gender;
    ArrayList<String> blood_group;
    ArrayList<String> Email;
    ArrayList<String> Give_Blood;
    LayoutInflater layoutInflater;


    public show_blood_donor_adapter(Context context, ArrayList<String> name,
                                    ArrayList<String> image, ArrayList<String> last_donet_date, ArrayList<String> phone,
                                    ArrayList<String> department, ArrayList<String> batch, ArrayList<String> village,
                                    ArrayList<String> upazilla, ArrayList<String> gender, ArrayList<String> blood_group,
                                    ArrayList<String> email, ArrayList<String> give_Blood) {
        this.context = context;
        this.name = name;
        this.image = image;
        this.last_donet_date = last_donet_date;
        this.phone = phone;
        this.department = department;
        Batch = batch;
        Village = village;
        Upazilla = upazilla;
        Gender = gender;
        this.blood_group = blood_group;
        Email = email;
        Give_Blood = give_Blood;
    }

    @Override
    public int getCount() {
        if (name.size()<=0){
            Toast.makeText(context,"No Donor Register...!",Toast.LENGTH_SHORT).show();
        }
        return name.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.show_blood_donor_recycler_item_design,parent,false);
        }

        CircleImageView imageView=convertView.findViewById(R.id.show_donor_picture);
        TextView nam=convertView.findViewById(R.id.show_donor_Name);
        final ImageView call=convertView.findViewById(R.id.show_donor_call_btn);
        TextView last_donate=convertView.findViewById(R.id.show_donor_lastdonate);

        Glide.with(context).load(image.get(position)).into(imageView);
        nam.setText(name.get(position));
        last_donate.setText("Last Donate: "+last_donet_date.get(position));

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(phone.get(position));
            }
        });


        return convertView;
    }

    public void call(String str) {

        try {


            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + str));
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException e) {

            Toast.makeText(context, "App failed", Toast.LENGTH_LONG).show();
        }
    }
}
