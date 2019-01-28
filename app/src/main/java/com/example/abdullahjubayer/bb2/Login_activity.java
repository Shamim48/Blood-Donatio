package com.example.abdullahjubayer.bb2;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class Login_activity extends AppCompatActivity {
    Spinner spinner_login2;
    ArrayAdapter<CharSequence> login_type2;
    EditText log_email,log_pass;
    Button button;
    TextView  newAccount,forgot_acc;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    ProgressBar progressBar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        spinner_login2=findViewById(R.id.login_spinner2_id);
        log_email=findViewById(R.id.login_email_id);
        log_pass=findViewById(R.id.login_password_id);
        button=findViewById(R.id.login_log_id);
        newAccount=findViewById(R.id.login_to_new_id);
        forgot_acc=findViewById(R.id.forgot_id);

        progressBar = (ProgressBar)findViewById(R.id.spin_kit_login);
        FadingCircle fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        String[] log_type2 = {"Select Blood group","A+", "A-","B+","B-","O+","O-","AB+","AB-"};
        login_type2 =    new ArrayAdapter<CharSequence>(this,R.layout.login_spinner_item,log_type2);
        login_type2.setDropDownViewResource(R.layout.login_spinner_item);
        spinner_login2.setAdapter(login_type2);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()){
                    String blood_gp = spinner_login2.getSelectedItem().toString();
                    getBloodGroup(blood_gp);
                    progressBar.setVisibility(View.VISIBLE);
                    button.setClickable(false);

                }
            }
        });

        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Add_Donor.class);
                startActivity(intent);
            }
        });

        forgot_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Forgot_activity.class);
                startActivity(intent);
            }
        });


    }


    private void login(String email,String pass) {

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (mAuth.getCurrentUser().isEmailVerified()){
                                newIntent();
                                Toast.makeText(getApplicationContext(), "Authentication Success.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "Email not Verified..!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                button.setClickable(true);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            button.setClickable(true);
                        }

                    }
                });

    }

    private void newIntent() {
        String blood = spinner_login2.getSelectedItem().toString();
        String email = log_email.getText().toString();
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        intent.putExtra("B_group",blood);
        intent.putExtra("email",email);
        startActivity(intent);
        progressBar.setVisibility(View.INVISIBLE);
        button.setClickable(true);

    }

    public boolean valid(){

        String email= log_email.getText().toString();
        String pass=log_pass.getText().toString();
        String type = spinner_login2.getSelectedItem().toString();

        boolean valid=true;
        if (email.isEmpty()){
            log_email.setError("Wrong Email Formate");
            valid=false;
        } else {
            log_email.setError(null);
        }
        if (pass.isEmpty()|| pass.length()<6){
            log_pass.setError("at last 6 character");
            valid=false;
        } else {
            log_pass.setError(null);
        }
        if (type.isEmpty()|| type.equals("Select Blood group")){
            Toast.makeText(getApplicationContext(),"Error in Blood Group",Toast.LENGTH_SHORT).show();
            valid=false;
        }
        return valid;
    }


    public void getBloodGroup(final String blood_gp){


        final String email = log_email.getText().toString();
        final String pass = log_pass.getText().toString();

            db.collection("All_Blood_Group").document(blood_gp).collection("Male").document(email)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot doc=task.getResult();
                        if (doc.exists()){
                            login_valid(doc,blood_gp,email,pass);
                        }
                        else {

                            db.collection("All_Blood_Group").document(blood_gp).collection("Female").document(email)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                    if (task.isSuccessful()){
                                        DocumentSnapshot doc=task.getResult();
                                        if (doc.exists()){
                                            login_valid(doc,blood_gp,email,pass);
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(),"User Not Found..!",Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.INVISIBLE);
                                            button.setClickable(true);
                                        }
                                    }else {
                                        Toast.makeText(getApplicationContext(),"User Not Found..!",Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        button.setClickable(true);
                                    }
                                }
                            });
                        }

                    }else {
                        Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        button.setClickable(true);

                    }
                }
            });

            }

            public void login_valid(DocumentSnapshot doc,String blood_gp,String  email,String pass){
                    String database_bg=doc.get("Blood_Group").toString();
                    Log.i("Dataaaa",database_bg);
                    if (database_bg.equals(blood_gp)){
                        login(email,pass);
                    }else {
                        Toast.makeText(getApplicationContext(),"Blood Group Not Match",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        button.setClickable(true);
                    }
            }
        }
