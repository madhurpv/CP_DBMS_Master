package com.mv.cp_dbms_master;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Voting extends AppCompatActivity implements VotingRecyclerAdapter.ItemClickListener{


    public static List<VotingClass> votings = new ArrayList<>();

    public static VotingRecyclerAdapter adapter;
    RecyclerView votingsRecyclerView;
    Button addNewButton;


    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);


        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);


        //List<String> voting1options = new ArrayList<>();
        //voting1options.add("Yes");voting1options.add("No");voting1options.add("Maybe");
        //votings.add(new VotingClass("Voting1", 1709428286000L, 1709728286000L, "Vote for humanity", voting1options, -1, VotingClass.NOT_VOTED));




        votingsRecyclerView = findViewById(R.id.votingRecyclerView);
        votingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        votingsRecyclerView.setNestedScrollingEnabled(true);





        // below line is used to get the
        // instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference();




        databaseReference.child("Votings").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("QWER", "Error getting data", task.getException());
                }
                else {
                    Log.d("QWER", "Got Voting data", task.getException());
                    Iterator<DataSnapshot> iterator = task.getResult().getChildren().iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot childSnapshot = iterator.next();
                        Map<String, Object> noticeData = (Map<String, Object>) childSnapshot.getValue();

                        String title = childSnapshot.getKey();
                        Long startTime = (Long) noticeData.get("startTime");
                        Long endTime = (Long) noticeData.get("endTime");
                        String details = (String) noticeData.get("Details");
                        Map<String, Long> options_map = (Map<String, Long>) noticeData.get("Options");
                        List<String> options = new ArrayList<>(options_map.keySet());
                        HashMap<String, Long> votedFlats = (HashMap<String, Long>) noticeData.get("Voters");
                        if(votedFlats == null){
                            votedFlats = new HashMap<>();
                        }
                        else{
                            Log.d("QWER", votedFlats.toString());
                        }

                        boolean found = false;
                        int flatNo = sharedPreferences.getInt("flatNo", -1);
                        Log.d("QWER", "Flat No = " + flatNo);
                        for (Map.Entry<String, Long> entry : votedFlats.entrySet()) {
                            System.out.println(entry.getKey() + "/" + entry.getValue());
                            if(Integer.parseInt(entry.getKey()) == flatNo){
                                found = true;
                                votings.add(new VotingClass(title, startTime, endTime, details, options, entry.getValue().intValue(), VotingClass.VOTED, votedFlats));
                            }
                        }
                        if(found == false){
                            votings.add(new VotingClass(title, startTime, endTime, details, options, -1, VotingClass.NOT_VOTED, votedFlats));
                        }
                        //Toast.makeText(Notices.this, childSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                    }
                    //Log.d("QWER", "Notices : "+ notices.get(3).title);
                    //adapter.notifyDataSetChanged();
                    adapter = new VotingRecyclerAdapter(Voting.this, votings, databaseReference);
                    adapter.setClickListener(Voting.this);
                    votingsRecyclerView.setAdapter(adapter);
                }
            }
        });

        addNewButton = findViewById(R.id.addNewButton);
        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showCustomDialog(Voting.this);

            }
        });

    }




    @Override
    public void onItemClick(View view, int position) {

        //Toast.makeText(this, "Position : " + position, Toast.LENGTH_SHORT).show();



        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.notices_dialog, null);

        TextView tvHeading = v.findViewById(R.id.tv_heading);
        TextView tvText = v.findViewById(R.id.tv_text);
        ImageView ivClose = v.findViewById(R.id.iv_close);
        TextView date_text_view = v.findViewById(R.id.date_text_view);

        // Set your desired heading and text
        /*tvHeading.setText(notices.get(position).title);
        tvText.setText(notices.get(position).details);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date(notices.get(position).time);
        date_text_view.setText(sdf.format(date));*/

        builder.setView(v);

        final AlertDialog dialog = builder.create();

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();





        /*databaseReference.child("Notices").setValue("12345").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Toast.makeText(Notices.this, "Successful!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(Notices.this, "Error!" + e, Toast.LENGTH_SHORT).show();
                Log.d("QWER", "Error : " + e);

            }
        });*/




    }

    // Method to show AlertDialog
    public void showCustomDialog(Context context) {

        final Calendar[] startDate = {null};
        final Calendar[] endDate = {null};

        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.votings_alertdialog, null);

        // Initialize the views from the custom layout
        CardView cardView = dialogView.findViewById(R.id.cardView);
        TextView textView = dialogView.findViewById(R.id.textView);
        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextDetails = dialogView.findViewById(R.id.editTextDetails);
        EditText editTextOption1 = dialogView.findViewById(R.id.editTextOption1);
        EditText editTextOption2 = dialogView.findViewById(R.id.editTextOption2);
        Button pickStartTimeButton = dialogView.findViewById(R.id.pickStartTimeButton);
        Button pickEndTimeButton = dialogView.findViewById(R.id.pickEndTimeButton);
        Button submitButton = dialogView.findViewById(R.id.submitButton);
        submitButton.setEnabled(false);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);

        pickStartTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar currentDate = Calendar.getInstance();
                startDate[0] = Calendar.getInstance();
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDate[0].set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                startDate[0].set(Calendar.HOUR_OF_DAY, hourOfDay);
                                startDate[0].set(Calendar.MINUTE, minute);
                                Log.v("QWER", "The choosen one " + startDate[0].getTime());
                                pickStartTimeButton.setText("Chosen Date : " + startDate[0].getTime());
                                if(endDate[0] != null && startDate[0].compareTo(endDate[0]) < 0 && !editTextName.getText().toString().equals("") && !editTextDetails.getText().toString().equals("") && !editTextOption1.getText().toString().equals("") && !editTextOption2.getText().toString().equals("")){
                                    submitButton.setEnabled(true);
                                }
                            }
                        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
            }
        });


        pickEndTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar currentDate = Calendar.getInstance();
                endDate[0] = Calendar.getInstance();
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDate[0].set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                endDate[0].set(Calendar.HOUR_OF_DAY, hourOfDay);
                                endDate[0].set(Calendar.MINUTE, minute);
                                Log.v("QWER", "The choosen one " + endDate[0].getTime());
                                pickEndTimeButton.setText("Chosen Date : " + endDate[0].getTime());
                                if(startDate[0] != null && startDate[0].compareTo(endDate[0]) < 0 && !editTextName.getText().toString().equals("") && !editTextDetails.getText().toString().equals("") && !editTextOption1.getText().toString().equals("") && !editTextOption2.getText().toString().equals("")){
                                    submitButton.setEnabled(true);
                                }
                            }
                        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
                    }
                }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
            }
        });

        AlertDialog dialog = builder.create();

        // Set up the submit button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the submission here
                HashMap<String, Object> pollDetails = new HashMap<>();
                pollDetails.put("Details", editTextDetails.getText().toString());

                HashMap<String, Object> optionsDetails = new HashMap<>();
                optionsDetails.put(editTextOption1.getText().toString(), "");
                optionsDetails.put(editTextOption2.getText().toString(), "");
                pollDetails.put("Options", optionsDetails);

                pollDetails.put("startTime", startDate[0].getTimeInMillis());
                pollDetails.put("endTime", endDate[0].getTimeInMillis());


                databaseReference.child("Votings").child(editTextName.getText().toString()).updateChildren(pollDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "Details Updated!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        //Toast.makeText(SignUpActivity.this, "Some Error Occured", Toast.LENGTH_SHORT).show();
                        Log.d("QWER", "Error : " + e);

                    }
                });
            }
        });

        // Create and show the AlertDialog
        dialog.show();
    }


    @Override
    protected void onStop() {
        super.onStop();
        votings.clear();
        adapter.notifyDataSetChanged();
    }
}