package com.example.abdullahjubayer.bb2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Male_Female_Choose extends AppCompatActivity {

    CardView man_view,women_view;
    String Blood_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_male__female__choose);

        man_view=findViewById(R.id.choose_man);
        women_view=findViewById(R.id.choose_women);

        Intent intent = getIntent();
        Blood_group = intent.getStringExtra("Blood_group");


        man_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Male_Female_Choose.this,showBlood_donor.class);
                intent.putExtra("blood_group",Blood_group);
                intent.putExtra("gender","Male");
                startActivity(intent);
            }
        });

        women_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater= LayoutInflater.from(Male_Female_Choose.this);
                final View view=inflater.inflate(R.layout.alart_pass_layout,null);
                final EditText eee=view.findViewById(R.id.admin_pass);
                new AlertDialog.Builder(Male_Female_Choose.this)
                        .setView(view)
                        .setTitle("Are You Admin...!")
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String pass=eee.getText().toString();

                                if (pass.equals("12345")){
                                    Intent intent=new Intent(Male_Female_Choose.this,showBlood_donor.class);
                                    intent.putExtra("blood_group",Blood_group);
                                    intent.putExtra("gender","Female");
                                    startActivity(intent);
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
        });

    }
}
