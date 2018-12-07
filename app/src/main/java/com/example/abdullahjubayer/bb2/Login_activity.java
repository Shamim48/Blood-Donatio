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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class Login_activity extends AppCompatActivity {
    Spinner spinner_login2;
    ArrayAdapter<CharSequence> login_type2;
    EditText log_email,log_pass;
    Button button;
    TextView  newAccount,forgot_acc;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;





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
                            newIntent();
                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
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


        final String blood = spinner_login2.getSelectedItem().toString();
        final String email = log_email.getText().toString();

        DocumentReference log_valid_mail = db.collection("All_Blood_Group").document(blood).collection("Male").document(email);
        log_valid_mail.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot doc = task.getResult();
                if (doc.exists()){
                     String u_blood_group = doc.get("Blood_Group").toString();

                        if (blood_gp.equals(u_blood_group)){
                            String email= log_email.getText().toString();
                            String pass=log_pass.getText().toString();
                            login(email,pass);
                        }else {
                            Toast.makeText(Login_activity.this, "Blood Group Not Match...!", Toast.LENGTH_LONG).show();
                        }
                }
                else {

                    DocumentReference log_valid_femail = db.collection("All_Blood_Group").document(blood).collection("Female").document(email);
                    log_valid_femail.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()){

                                String u_blood_group = doc.get("Blood_Group").toString();
                                if (blood_gp.equals(u_blood_group)){
                                    String email= log_email.getText().toString();
                                    String pass=log_pass.getText().toString();
                                    login(email,pass);
                                }else {
                                    Toast.makeText(Login_activity.this, "Blood Group Not Match...!", Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                Toast.makeText(Login_activity.this, "You don't have an Account", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }

            }
        });
    }
}
