package com.mv.cp_dbms_master;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {


    CardView cardViewVoting, cardViewGuests, cardViewNotices, cardViewProfile, cardViewComplaints, cardViewFees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        cardViewVoting = findViewById(R.id.cardViewVoting);
        cardViewGuests = findViewById(R.id.cardViewGuests);
        cardViewNotices = findViewById(R.id.cardViewNotices);
        cardViewProfile = findViewById(R.id.cardViewProfile);
        cardViewComplaints = findViewById(R.id.cardViewComplaints);
        cardViewFees = findViewById(R.id.cardViewFees);



        cardViewVoting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        cardViewGuests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cardViewNotices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, Notices.class);
                startActivity(i);
            }
        });

        cardViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cardViewComplaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cardViewFees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}