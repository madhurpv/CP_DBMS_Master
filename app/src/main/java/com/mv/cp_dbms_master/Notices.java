package com.mv.cp_dbms_master;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Notices extends AppCompatActivity implements NoticesRecyclerAdapter.ItemClickListener{



    public static List<NoticesClass> notices = new ArrayList<>();

    public static NoticesRecyclerAdapter adapter;
    RecyclerView noticesRecyclerView;
    FloatingActionButton addNotice;

    SharedPreferences sharedPreferences;

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices);


        noticesRecyclerView = findViewById(R.id.noticesRecyclerView);
        noticesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        noticesRecyclerView.setNestedScrollingEnabled(true);

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);



        // below line is used to get the
        // instance of our FIrebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference();

        databaseReference.child("Notices").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("QWER", "Error getting data", task.getException());
                }
                else {
                    Log.d("QWER", "Got Notices data", task.getException());
                    Iterator<DataSnapshot> iterator = task.getResult().getChildren().iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot childSnapshot = iterator.next();
                        Map<String, Object> noticeData = (Map<String, Object>) childSnapshot.getValue();
                        notices.add(new NoticesClass(childSnapshot.getKey(), (Long) noticeData.get("Time"), (String) noticeData.get("Details")));
                        //Toast.makeText(Notices.this, childSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                    }
                    //Log.d("QWER", "Notices : "+ notices.get(3).title);
                    //adapter.notifyDataSetChanged();
                    adapter = new NoticesRecyclerAdapter(Notices.this, notices);
                    adapter.setClickListener(Notices.this);
                    noticesRecyclerView.setAdapter(adapter);
                }
            }
        });


        addNotice = findViewById(R.id.addNotice);
        addNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNotice(Notices.this);
            }
        });

    }


    // Method to show AlertDialog
    public void addNotice(Context context) {

        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.notices_alertdialog, null);

        // Initialize the views from the custom layout
        CardView cardView = dialogView.findViewById(R.id.cardView);
        TextView textView = dialogView.findViewById(R.id.textView);
        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);
        Button submitButton = dialogView.findViewById(R.id.submitButton);
        //submitButton.setEnabled(false);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);


        AlertDialog dialog = builder.create();

        // Set up the submit button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the submission here
                HashMap<String, Object> complaintDetails = new HashMap<>();
                complaintDetails.put("title", editTextName.getText().toString());
                complaintDetails.put("Details", editTextDescription.getText().toString());
                complaintDetails.put("Time", System.currentTimeMillis());


                databaseReference.child("Notices").child(editTextName.getText().toString()).updateChildren(complaintDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Notice Added!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        finish();

                        /*displayGuests.clear();
                        guestsAll.clear();
                        refresh_listview();*/
                        //finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(Notices.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                        Log.d("QWER", "Error : " + e);

                    }
                });
            }
        });

        // Create and show the AlertDialog
        dialog.show();
    }




    @Override
    public void onItemClick(View view, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.notices_dialog, null);

        TextView tvHeading = v.findViewById(R.id.tv_heading);
        TextView tvText = v.findViewById(R.id.tv_text);
        ImageView ivClose = v.findViewById(R.id.iv_close);
        TextView date_text_view = v.findViewById(R.id.date_text_view);

        // Set your desired heading and text
        tvHeading.setText(notices.get(position).title);
        tvText.setText(notices.get(position).details);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date(notices.get(position).time);
        date_text_view.setText(sdf.format(date));

        builder.setView(v);

        final AlertDialog dialog = builder.create();

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}