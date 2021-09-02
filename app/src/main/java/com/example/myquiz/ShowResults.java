package com.example.myquiz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.myquiz.mqUtils.StoreResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowResults extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    DatabaseReference refToresults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_results);

        setTitle("Results");
        Intent getUsername = getIntent();
        String username = getUsername.getStringExtra(Dashboard.EXTRA_SENDNAME);

        refToresults = FirebaseDatabase.getInstance().getReference().child("results").child(username);

        //final ArrayList<ExampleItem> exampleList = new ArrayList<>();
        final ArrayList<StoreResult> exampleList = new ArrayList<>();
        /*exampleList.add(new ExampleItem("5", "Addition", "1 March 2019", "10:00"));
        exampleList.add(new ExampleItem("3", "Subtraction", "2 March 2019", "11:00"));
        exampleList.add(new ExampleItem("1", "Division", "3 Jan 2018", "12:30"));
        exampleList.add(new ExampleItem("5", "Multiplication", "2 Mar 2019", "10:00"));
        exampleList.add(new ExampleItem("3", "General Knowledge", "2 Mar 2019", "11:00"));
        exampleList.add(new ExampleItem("1", "addition", "3 Mar", "12:30"));
        exampleList.add(new ExampleItem("5", "addition", "1 Mar", "10:00"));
        exampleList.add(new ExampleItem("3", "addition", "2 Mar", "11:00"));
        exampleList.add(new ExampleItem("1", "addition", "3 Mar", "12:30"));*/

        refToresults.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot itemSnapshot: dataSnapshot.getChildren()) {
                    //ExampleItem item = dataSnapshot.getValue(ExampleItem.class);
                    StoreResult item = itemSnapshot.getValue(StoreResult.class);
                    exampleList.add(item);
                }
                mAdapter = new ExampleAdapter(exampleList);
               // mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        //mAdapter = new ExampleAdapter(exampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        //mRecyclerView.setAdapter(mAdapter);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
