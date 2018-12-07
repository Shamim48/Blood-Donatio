package com.example.abdullahjubayer.bb2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    CardView search_card, blood_req,facts,top_dono,photogallery,about_blood;
    private DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    FirebaseFirestore db;
    String blood_group_intent, email_intent;
    NavigationView navigationView;
    View headerView;
    Menu menu;
    private FirebaseAuth mAuth;
    String U_name,U_photo,U_phone,U_last_donate,U_how_huch_donate,gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Home");
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawer = findViewById(R.id.drawer_layout);
        toggle=new ActionBarDrawerToggle(this,drawer,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();




        search_card = findViewById(R.id.search_id);
        blood_req=findViewById(R.id.blood_request);
        top_dono=findViewById(R.id.top_donor);
        facts=findViewById(R.id.facts_id);
        photogallery=findViewById(R.id.photogalrry_id);
        about_blood=findViewById(R.id.about_blood_donation);


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);

        navigationView.setNavigationItemSelectedListener(this);
        search_card.setOnClickListener(this);
        blood_req.setOnClickListener(this);
        top_dono.setOnClickListener(this);
        facts.setOnClickListener(this);
        photogallery.setOnClickListener(this);
        about_blood.setOnClickListener(this);




        Intent intent = getIntent();
        blood_group_intent = intent.getStringExtra("B_group");
        email_intent = intent.getStringExtra("email");




        menu= navigationView.getMenu();


        loadUserdata();

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.search_id) {
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.blood_request) {
            Intent intent = new Intent(getApplicationContext(), Post_activity.class);
            intent.putExtra("send_name",U_name);
            intent.putExtra("send_img",U_photo);
            startActivity(intent);
        }
        if (v.getId() == R.id.top_donor) {
            Intent intent = new Intent(getApplicationContext(), TopDonor.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.facts_id) {
            Intent intent = new Intent(getApplicationContext(), Facts.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.photogalrry_id) {
            Intent intent = new Intent(getApplicationContext(), Photo_gallery.class);
            intent.putExtra("send_name",U_name);
            intent.putExtra("send_img",U_photo);
            startActivity(intent);
        }
        if (v.getId() == R.id.about_blood_donation) {
            Intent intent = new Intent(getApplicationContext(), About_BDG.class);
            startActivity(intent);
        }





    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadUserdata() {


        DocumentReference log_valid = db.collection("All_Blood_Group").document(blood_group_intent).collection("Male").document(email_intent);
        log_valid.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot doc = task.getResult();
                if (doc.exists()) {

                if (task.isSuccessful()) {


                    String u_name = doc.get("Name").toString();
                    String u_image = doc.get("Image").toString();
                    String u_email = doc.get("Email").toString();
                    String u_phone = doc.get("Phone1").toString();
                    String u_blood_group = doc.get("Blood_Group").toString();
                    String u_last_donate = doc.get("Last_Donate_Date").toString();
                    String u_donet_times = doc.get("Give_Blood").toString();

                    U_name=doc.get("Name").toString();
                    U_photo=doc.get("Image").toString();
                    U_phone=doc.get("Phone1").toString();
                    U_last_donate=doc.get("Last_Donate_Date").toString();
                    U_how_huch_donate=doc.get("Give_Blood").toString();
                    gender=doc.get("Gender").toString();



                    TextView navUsername = (TextView) headerView.findViewById(R.id.nav_name);
                    TextView email = (TextView) headerView.findViewById(R.id.nav_email);
                    CircleImageView imageView = headerView.findViewById(R.id.nav_image);
                    navUsername.setText(u_name);
                    email.setText(u_email);
                    Glide.with(MainActivity.this).load(u_image).into(imageView);

                    MenuItem nav_camara = menu.findItem(R.id.nav_phone);
                    nav_camara.setTitle("Phone: " + u_phone);
                    MenuItem bd_group = menu.findItem(R.id.nav_blood_gp);
                    bd_group.setTitle("Blood Group: " + u_blood_group);
                    MenuItem bd_last_donate = menu.findItem(R.id.nav_last_donate);
                    bd_last_donate.setTitle("Last Donate: " + u_last_donate);
                    MenuItem db_donet_times = menu.findItem(R.id.how_much_donate);
                    db_donet_times.setTitle(u_donet_times + " Times Donate");




                } else {
                    Toast.makeText(MainActivity.this, "Data Not Found", Toast.LENGTH_LONG).show();
                }
            }

                else {

                    DocumentReference log_valid = db.collection("All_Blood_Group").document(blood_group_intent).collection("Female").document(email_intent);
                    log_valid.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()){

                                if (task.isSuccessful()){
                                    String u_name = doc.get("Name").toString();
                                    String u_image = doc.get("Image").toString();
                                    String u_email = doc.get("Email").toString();
                                    String u_phone = doc.get("Phone1").toString();
                                    String u_blood_group = doc.get("Blood_Group").toString();
                                    String u_last_donate = doc.get("Last_Donate_Date").toString();
                                    String u_donet_times = doc.get("Give_Blood").toString();

                                    U_name=doc.get("Name").toString();
                                    U_photo=doc.get("Image").toString();
                                    U_phone=doc.get("Phone1").toString();
                                    U_last_donate=doc.get("Last_Donate_Date").toString();
                                    U_how_huch_donate=doc.get("Give_Blood").toString();
                                    gender=doc.get("Gender").toString();


                                    TextView navUsername = (TextView) headerView.findViewById(R.id.nav_name);
                                    TextView email = (TextView) headerView.findViewById(R.id.nav_email);
                                    CircleImageView imageView = headerView.findViewById(R.id.nav_image);
                                    navUsername.setText(u_name);
                                    email.setText(u_email);
                                    Glide.with(MainActivity.this).load(u_image).into(imageView);

                                    MenuItem nav_camara = menu.findItem(R.id.nav_phone);
                                    nav_camara.setTitle("Phone: " + u_phone);
                                    MenuItem bd_group = menu.findItem(R.id.nav_blood_gp);
                                    bd_group.setTitle("Blood Group: " + u_blood_group);
                                    MenuItem bd_last_donate = menu.findItem(R.id.nav_last_donate);
                                    bd_last_donate.setTitle("Last Donate: " + u_last_donate);
                                    MenuItem db_donet_times = menu.findItem(R.id.how_much_donate);
                                    db_donet_times.setTitle(u_donet_times + " Times Donate");
                                }
                            }
                            else {
                                Toast.makeText(MainActivity.this, "You don't have an Account or Wrong blood group..Please Select correct blood group and login again" +
                                        " Otherwise Apps is Stopped", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Data Set Not Found", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        if (menuItem.getItemId()==R.id.menu_edit_profile){
            Intent intent=new Intent(getApplicationContext(),EditProfile.class);
            intent.putExtra("Blood_group",blood_group_intent);
            intent.putExtra("Email",email_intent);
            intent.putExtra("Phone",U_phone);
            intent.putExtra("Last_Donate",U_last_donate);
            intent.putExtra("How_Much",U_how_huch_donate);
            intent.putExtra("Gender",gender);
            startActivity(intent);
        }
        if (menuItem.getItemId()==R.id.logout_button){
            logout();
        }
        if (menuItem.getItemId()==R.id.menu_about_us){
            Intent intent=new Intent(getApplicationContext(),About_Us.class);
            startActivity(intent);
        }

        return true;
    }

    private void logout() {
        Intent intent=new Intent(getApplicationContext(),Login_activity.class);
        startActivity(intent);
        mAuth.signOut();
    }
}
