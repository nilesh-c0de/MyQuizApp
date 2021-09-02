package com.example.myquiz;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myquiz.mqUtils.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPassword extends AppCompatActivity {

    DatabaseReference usr;
    Button btnShow;
    EditText usrname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        
        setTitle("Get Your Password Here!");

        usr = FirebaseDatabase.getInstance().getReference().child("Users").child("Student");
        usrname = (EditText) findViewById(R.id.editText3);
        btnShow = (Button) findViewById(R.id.button2);

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String show = usrname.getText().toString();
                if (!show.isEmpty())
                    sendEmail(show);
                else
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Username!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendEmail(final String userName) {

        usr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userName).exists()) {
                    User user = dataSnapshot.child(userName).getValue(User.class);
                    String passwd = user.getPassword();
                    Toast.makeText(getApplicationContext(), "Password: " + passwd, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "User doesn't exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
