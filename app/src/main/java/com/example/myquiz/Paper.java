package com.example.myquiz;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myquiz.mqUtils.AddQuestion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Paper extends AppCompatActivity {

    EditText edtSubject, edtQNr, edtQuestion, edtO1, edtO2, edtO3, edtCA;
    Button add;

    DatabaseReference subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper);

        setTitle("Add Questions Here!");

        edtSubject = (EditText)findViewById(R.id.editTextSubject);
        edtQNr = (EditText)findViewById(R.id.editTextQNr);
        edtQuestion = (EditText)findViewById(R.id.editTextQuestion);
        edtO1 = (EditText)findViewById(R.id.editTextO1);
        edtO2 = (EditText)findViewById(R.id.editTextO2);
        edtO3 = (EditText)findViewById(R.id.editTextO3);
        edtCA = (EditText)findViewById(R.id.editTextCA);
        add = (Button)findViewById(R.id.buttonAddQuestion);

        subjects = FirebaseDatabase.getInstance().getReference().child("subjects");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String s = edtSubject.getText().toString();
                final String n = edtQNr.getText().toString();
                final AddQuestion newQuestion = new AddQuestion(edtQuestion.getText().toString(),edtO1.getText().toString(),edtO2.getText().toString(),edtO3.getText().toString(),edtCA.getText().toString());

                if(!(s.isEmpty() || n.isEmpty() || edtQuestion.getText().toString().isEmpty() || edtO1.getText().toString().isEmpty() || edtO2.getText().toString().isEmpty() || edtO3.getText().toString().isEmpty() || edtCA.getText().toString().isEmpty())) {
                    subjects.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            subjects.child(s).child(n).setValue(newQuestion).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Question is written successfully!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(), "Please fill all fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
