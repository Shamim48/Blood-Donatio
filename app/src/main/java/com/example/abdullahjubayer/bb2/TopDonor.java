package com.example.abdullahjubayer.bb2;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class TopDonor extends AppCompatActivity implements View.OnClickListener {

    CardView
            a_positive,
            a_negative,
            b_positive,
            b_negative,
            o_positive,
            o_negative,
            ab_positive,
            ab_negative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_donor);


        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Top Donor");


        a_positive=findViewById(R.id.top_donor_a_plus);
        a_negative=findViewById(R.id.top_donor_a_minus);
        b_positive=findViewById(R.id.top_donor_b_plus);
        b_negative=findViewById(R.id.top_donor_b_minus);
        o_positive=findViewById(R.id.top_donor_o_plus);
        o_negative=findViewById(R.id.top_donor_o_minus);
        ab_positive=findViewById(R.id.top_donor_ab_plus);
        ab_negative=findViewById(R.id.top_donor_ab_minus);


        addListner();
    }


    private void addListner() {

        a_positive.setOnClickListener(this);
        a_negative.setOnClickListener(this);
        b_positive.setOnClickListener(this);
        b_negative.setOnClickListener(this);
        o_positive.setOnClickListener(this);
        o_negative.setOnClickListener(this);
        ab_positive.setOnClickListener(this);
        ab_negative.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.top_donor_a_plus) {
            newIntent("A+");
        }
        if (v.getId()==R.id.top_donor_a_minus) {
            newIntent("A-");
        }
        if (v.getId()==R.id.top_donor_b_plus) {
            newIntent("B+");
        }
        if (v.getId()==R.id.top_donor_b_minus) {
            newIntent("B-");
        }
        if (v.getId()==R.id.top_donor_o_plus){
            newIntent("O+");
        }
        if (v.getId()==R.id.top_donor_o_minus) {
            newIntent("O-");
        }
        if (v.getId()==R.id.top_donor_ab_plus){
            newIntent("AB+");
        }
        if (v.getId()==R.id.top_donor_ab_minus) {
            newIntent("AB-");
        }
    }

    private void newIntent(String s) {
        Intent intent=new Intent(getApplicationContext(),showTopDonor.class);
        intent.putExtra("Blood_group",s);
        startActivity(intent);
        finishActivity(0);
    }
}
