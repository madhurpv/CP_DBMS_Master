package com.mv.cp_dbms_master;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {


    Button buttonSubmit;
    EditText editTextName, editTextFlatNo, editTextNumberOfFamilyMembers;


    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference();



        buttonSubmit = findViewById(R.id.buttonSubmit);
        editTextName = findViewById(R.id.editTextName);
        editTextFlatNo = findViewById(R.id.editTextFlatNo);
        editTextNumberOfFamilyMembers = findViewById(R.id.editTextNumberOfFamilyMembers);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editTextName.getText().toString().equals("") && !editTextFlatNo.getText().toString().equals("") && !editTextNumberOfFamilyMembers.getText().toString().equals("")) {
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putLong("login", System.currentTimeMillis());
                    myEdit.putString("name", editTextName.getText().toString());
                    myEdit.putInt("flatNo", Integer.parseInt(editTextFlatNo.getText().toString()));
                    myEdit.putInt("numberOfFamilyMembers", Integer.parseInt(editTextNumberOfFamilyMembers.getText().toString()));
                    myEdit.apply();

                    Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
                    startActivity(i);




                    HashMap<String, Object> userDetails = new HashMap<>();
                    userDetails.put("first_login", System.currentTimeMillis());
                    userDetails.put("name", editTextName.getText().toString());
                    userDetails.put("flatNo", Integer.parseInt(editTextFlatNo.getText().toString()));
                    userDetails.put("numberOfFamilyMembers", Integer.parseInt(editTextNumberOfFamilyMembers.getText().toString()));

                    databaseReference.child("MasterUsers").child("Data").child(sharedPreferences.getString("phone number", "-1")).updateChildren(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(SignUpActivity.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                            Log.d("QWER", "Error : " + e);

                        }
                    });





                }
                else{
                    Toast.makeText(SignUpActivity.this, "Fill up all details!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}