package com.mv.cp_dbms_master;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Voting extends AppCompatActivity implements VotingRecyclerAdapter.ItemClickListener{


    public static List<VotingClass> votings = new ArrayList<>();

    public static VotingRecyclerAdapter adapter;
    RecyclerView votingsRecyclerView;


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
                        Log.d("QWER", votedFlats.toString());

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
}