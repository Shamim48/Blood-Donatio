package com.example.abdullahjubayer.bb2;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Facts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facts);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Facts");
    }
}
