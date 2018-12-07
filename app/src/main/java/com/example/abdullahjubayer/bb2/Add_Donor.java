package com.example.abdullahjubayer.bb2;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Add_Donor extends AppCompatActivity  {
    private static final int RQ_CODE = 1;
    EditText d_name,d_depertmant,d_semester,d_phone1,d_village,d_upazilla,d_city,d_email,d_pass;
    Button save_button;
    CircleImageView donor_image;
    Uri picture_uri;
    String ImagedownloadUrl;
    FirebaseFirestore db;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    Spinner spinner,spinner2;
    ArrayAdapter<CharSequence> adapterGender;
    ArrayAdapter<CharSequence> adapterBlood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__donor);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("SignUp");

        donor_image=findViewById(R.id.donor_picture);
        d_name=findViewById(R.id.donor_name);
        d_depertmant=findViewById(R.id.donor_department);
        d_semester=findViewById(R.id.donor_semester);
        d_phone1=findViewById(R.id.donor_mobile_number);
        d_village=findViewById(R.id.donor_village_name);
        d_upazilla=findViewById(R.id.donor_upazilla_name);
        d_city=findViewById(R.id.donor_city_name);
        save_button=findViewById(R.id.donor_save_button);
        spinner=findViewById(R.id.gender_select_spinner);
        spinner2=findViewById(R.id.blood_select_spinner);
        d_email=findViewById(R.id.donor_email);
        d_pass=findViewById(R.id.donor_password);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String[] gend = {"Select Gender","Male", "Female"};
        String[] b_grop = {"Select Blood group","A+", "A-","B+","B-","O+","O-","AB+","AB-"};


        adapterGender =    new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,gend);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterGender);


        adapterBlood =    new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,b_grop);
        adapterBlood.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapterBlood);


        donor_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){

                        createuser();

                    }
                else {
                    Toast.makeText(getApplicationContext(),"Please Fill All Data Correctly",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }







    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RQ_CODE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            picture_uri=data.getData();
            try {
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),picture_uri);
//                donor_image.setImageBitmap(bitmap);
                Glide.with(Add_Donor.this).load(bitmap).into(donor_image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(Add_Donor.this,"Error in Select Image",Toast.LENGTH_LONG).show();
        }
    }

    public void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, RQ_CODE);
    }


    private void uploadImage() {

        String name= d_name.getText().toString();
        String batch = d_semester.getText().toString();
        final StorageReference storageReference=
                FirebaseStorage.getInstance().getReference("All_donor_photo/"+name+batch+".jpg");

        if (picture_uri!=null){
            storageReference.putFile(picture_uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        ImagedownloadUrl = task.getResult().toString();
                        UploadUserData();

                        Log.i("downloadUrllllll", "onComplete: Url: "+ ImagedownloadUrl);
                    }
                    else {
                        Toast.makeText(Add_Donor.this, "Picture Upload failed.",Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }else {
            ImagedownloadUrl="https://firebasestorage.googleapis.com/v0/b/collegeproject-318a7.appspot.com/o/ALL_USER_PHOTO%2FUser_img_abdullahjubayer%40gmail.com.jpg?alt=media&token=402467da-eeec-48c6-a1ab-2004580905c6";
            UploadUserData();
        }

    }


    private void UploadUserData() {

        final String name= d_name.getText().toString();
        final String department=d_depertmant.getText().toString();
        final String batch = d_semester.getText().toString();
        String phone1 = d_phone1.getText().toString();
        String village = d_village.getText().toString();
        String upazilla = d_upazilla.getText().toString();
        String city = d_city.getText().toString();
         final String gender=spinner.getSelectedItem().toString();
        final String blood_group=spinner2.getSelectedItem().toString();
         final String email= d_email.getText().toString();
         final String give_blood="0";


             final Map<String, Object> newContact = new HashMap<>();
            newContact.clear();

            newContact.put("Name", name);
            newContact.put("Image", ImagedownloadUrl);
            newContact.put("Department", department);
            newContact.put("Batch", batch);
            newContact.put("Phone1", phone1);
            newContact.put("Village", village);
            newContact.put("Upazilla", upazilla);
            newContact.put("City", city);
            newContact.put("Gender", gender);
            newContact.put("Blood_Group", blood_group);
            newContact.put("Email", email);
            newContact.put("Last_Donate_Date","0-0-0");
            newContact.put("Give_Blood", give_blood);


        DocumentReference user=db.collection("All_Blood_Group").document(blood_group).collection(gender).document(email);
               user.set(newContact).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       Toast.makeText(Add_Donor.this, "Donor Registered Success...!", Toast.LENGTH_SHORT).show();

                       Top_Donor_Note data=new Top_Donor_Note(name,ImagedownloadUrl,department,batch,gender,blood_group,give_blood);
                       DocumentReference user=db.collection("All_donor_Info").document(blood_group).collection("Top_Donor").document(email);
                       user.set(data);

                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(Add_Donor.this, "Donor Registered Failed...!", Toast.LENGTH_SHORT).show();
                   }
               });


    }

    private void createuser() {
        String email= d_email.getText().toString();
        String pass= d_pass.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            uploadImage();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Donor Registered failed", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public boolean validate() {
        boolean valid = true;

        String name= d_name.getText().toString();
        String department=d_depertmant.getText().toString();
        String semester = d_semester.getText().toString();
        String phone1 = d_phone1.getText().toString();
        String village = d_village.getText().toString();
        String upazilla = d_upazilla.getText().toString();
        String city = d_city.getText().toString();
        String gender=spinner.getSelectedItem().toString();
        String blood_group=spinner2.getSelectedItem().toString();
        String email= d_email.getText().toString();
        String pass= d_pass.getText().toString();


        if (name.isEmpty() || name.length() < 3) {
            d_name.setError("at least 3 characters");
            valid = false;
        } else {
            d_name.setError(null);
        }

        if (department.isEmpty()) {
            d_depertmant.setError("Department Null");
            valid = false;
        } else {
            d_depertmant.setError(null);
        }

        if (semester.isEmpty()) {
            d_semester.setError("Batch Null");
            valid = false;
        } else {
            d_semester.setError(null);
        }

        if (phone1.isEmpty()){
            d_phone1.setError("Phone Null");
            valid=false;
        } else {
            d_phone1.setError(null);
        }
        if (village.isEmpty()){
            d_village.setError("Village Null");
            valid=false;
        } else {
            d_village.setError(null);
        }
        if (upazilla.isEmpty()){
            d_upazilla.setError("Upazilla/Thana Null");
            valid=false;
        } else {
            d_upazilla.setError(null);
        }
        if (city.isEmpty()){
            d_city.setError("City Null");
            valid=false;
        } else {
            d_city.setError(null);
        }
        if (gender.isEmpty()|| gender.equals("Select Gender")){
            Toast.makeText(getApplicationContext(),"Error in Gender",Toast.LENGTH_SHORT).show();
            valid=false;
        }
        if (blood_group.isEmpty()|| blood_group.equals("Select Blood group")){
            Toast.makeText(getApplicationContext(),"Error in Gender",Toast.LENGTH_SHORT).show();
            valid=false;
        }
        if (email.isEmpty()){
            d_email.setError("Wrong Email Formate");
            valid=false;
        } else {
            d_email.setError(null);
        }
        if (pass.isEmpty()|| pass.length()<6){
            d_pass.setError("at last 6 character");
            valid=false;
        } else {
            d_pass.setError(null);
        }

        return valid;
    }




}
