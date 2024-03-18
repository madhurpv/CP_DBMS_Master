package com.mv.cp_dbms_master;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {


    SharedPreferences sharedPreferences;

    EditText editTextName, editTextFamilyMembers, editTextFlatNumber, editTextPosition;
    Button saveButton;
    ImageButton logOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


        editTextName = findViewById(R.id.editTextName);
        editTextFamilyMembers = findViewById(R.id.editTextFamilyMembers);
        editTextFlatNumber = findViewById(R.id.editTextFlatNumber);
        editTextPosition = findViewById(R.id.editTextPosition);
        saveButton = findViewById(R.id.saveButton);
        logOutButton = findViewById(R.id.logOutButton);


        editTextName.setText(sharedPreferences.getString("name", "Name"));
        editTextPosition.setText(sharedPreferences.getString("position", "Position"));
        editTextFamilyMembers.setText("" + sharedPreferences.getInt("numberOfFamilyMembers", -1));
        editTextFlatNumber.setText("" + sharedPreferences.getInt("flatNo", -1));
        editTextFlatNumber.setFocusable(false);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.clear().apply();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextName.getText().toString().equals("") || editTextFamilyMembers.getText().toString().equals("")){
                    Toast.makeText(ProfileActivity.this, "Fill all Data!", Toast.LENGTH_SHORT).show();
                    return;
                }



                HashMap<String, Object> userDetails = new HashMap<>();
                userDetails.put("name", editTextName.getText().toString());
                userDetails.put("position", editTextPosition.getText().toString());
                userDetails.put("numberOfFamilyMembers", Integer.parseInt(editTextFamilyMembers.getText().toString()));

                databaseReference.child("MasterUsers").child("Data").child(sharedPreferences.getString("phone number", "-1")).updateChildren(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putString("name", editTextName.getText().toString());
                        myEdit.putString("position", editTextPosition.getText().toString());
                        myEdit.putInt("numberOfFamilyMembers", Integer.parseInt(editTextFamilyMembers.getText().toString()));
                        myEdit.apply();
                        Toast.makeText(ProfileActivity.this, "Details Updated!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ProfileActivity.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                        Log.d("QWER", "Error : " + e);

                    }
                });
            }
        });

    }
}