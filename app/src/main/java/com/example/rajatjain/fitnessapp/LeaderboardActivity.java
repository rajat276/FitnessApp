package com.example.rajatjain.fitnessapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.rajatjain.fitnessapp.Adaptors.LeaderBoardAdapter;
import com.example.rajatjain.fitnessapp.Interface.Participant_Details;

import java.util.ArrayList;

/**
 * Created by Rajat Jain on 30-08-2016.
 */
public class LeaderboardActivity extends AppCompatActivity {
    ArrayList<Participant_Details> ParticipantArray;
    Participant_Details pd;
    RecyclerView recyclerView;
    LeaderBoardAdapter ldAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_leader_board);
        ParticipantArray=new ArrayList<Participant_Details>();
        pd=new Participant_Details("Rajat","1","2000");
        ParticipantArray.add(pd);
        pd=new Participant_Details("Monil","2","200");
        ParticipantArray.add(pd);
        pd=new Participant_Details("Maddy","3","0");
        ParticipantArray.add(pd);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        ldAdapter =new LeaderBoardAdapter(this,ParticipantArray);
        recyclerView.setAdapter(ldAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Leaderboard");
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
