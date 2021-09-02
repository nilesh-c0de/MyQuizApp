package com.example.myquiz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StartQuiz extends AppCompatActivity {

    Button start;
    private static final int REQUEST_CODE_QUIZ = 1;
    TextView highScore;

    Spinner spinner;
    DatabaseReference refToSubjects;
    private ArrayList<String> subKeys = new ArrayList<>();
    String subjectName;
    public static final String EXTRA_SUBJECT = "com.example.myquiz.EXTRA_SUBJECT";
    public static final String EXTRA_SENDUSERNAME = "com.example.myquiz.EXTRA_SENDUSERNAME";
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startquizpage);

        Intent get = getIntent();
        username = get.getStringExtra(Dashboard.EXTRA_USERNAME);

        start = (Button)findViewById(R.id.button);
        highScore = (TextView)findViewById(R.id.textView29);

        spinner = (Spinner)findViewById(R.id.spinner);
        refToSubjects = FirebaseDatabase.getInstance().getReference().child("subjects");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,subKeys);
        spinner.setAdapter(arrayAdapter);

        refToSubjects.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String keys = dataSnapshot.getKey();
                subKeys.add(keys);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subjectName = parent.getItemAtPosition(position).toString();
//                Toast.makeText(getApplicationContext(), "You selected: "+subjectName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),QuizActivity.class);
                intent.putExtra(EXTRA_SUBJECT, subjectName);
                intent.putExtra(EXTRA_SENDUSERNAME, username);
                startActivityForResult(intent, REQUEST_CODE_QUIZ);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_QUIZ)
        {
            if(resultCode == RESULT_OK)
            {
                int result = data.getIntExtra("result",0);
                highScore.setText("Highscore: "+Integer.toString(result));
            }
            if(resultCode == RESULT_CANCELED)
            {
                highScore.setText("Highscore: xD");
            }
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
