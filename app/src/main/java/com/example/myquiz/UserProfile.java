package com.example.myquiz;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myquiz.mqUtils.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    public static final String EXTRA_EMAIL = "com.example.myquiz.EXTRA_EMAIL";;
    DatabaseReference db;
    EditText textSetEmail, textSetUsername, textSetPassword;
    Button saveChanges;
    String usr, password, email;
    ImageView editUsername, editEmail, editPassword;

    public static final String EXTRA_CURRENT_PASSWORD = "com.example.myquiz.EXTRA_CURRENT_PASSWORD";
    public static final String EXTRA_USERNAME = "com.example.myquiz.EXTRA_USERNAME";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        setTitle("Profile");

        Intent getUname = getIntent();
        final String username = getUname.getStringExtra(Dashboard.EXTRA_SENDNAME);

        textSetEmail = (EditText) findViewById(R.id.text_email);
        textSetUsername = (EditText) findViewById(R.id.text_username);
        textSetPassword = (EditText) findViewById(R.id.text_password);
        saveChanges = (Button)findViewById(R.id.save_changes);

        editUsername = (ImageView)findViewById(R.id.edit_username);
        editEmail = (ImageView)findViewById(R.id.edit_email);
        editPassword = (ImageView)findViewById(R.id.edit_password);

        db = FirebaseDatabase.getInstance().getReference().child("Users").child("Student");

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(username).getValue(User.class);
                //Toast.makeText(getApplicationContext(), user.getEmail(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), user.getUsername(), Toast.LENGTH_SHORT).show();

                textSetUsername.setEnabled(false);
                textSetEmail.setEnabled(false);
                textSetPassword.setEnabled(false);

                textSetUsername.setText(user.getUsername());
                textSetEmail.setText(user.getEmail());
                textSetPassword.setText(user.getPassword());

                textSetUsername.setTextColor(Color.BLUE);
                textSetEmail.setTextColor(Color.BLUE);
                textSetPassword.setTextColor(Color.BLUE);

                usr = user.getUsername();
                email = user.getEmail();
                password = user.getPassword();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        editUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Can't edit username!", Toast.LENGTH_SHORT).show();
            }
        });
        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMail();
            }
        });
        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUserPassword();
            }
        });
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
                Toast.makeText(getApplicationContext(), "Changes saved successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeMail() {
        Intent intent = new Intent(getApplicationContext(), changeEmail.class);
        intent.putExtra(EXTRA_EMAIL, email);
        intent.putExtra(EXTRA_USERNAME, usr);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void changeUserPassword() {
        Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
        intent.putExtra(EXTRA_USERNAME, usr);
        intent.putExtra(EXTRA_CURRENT_PASSWORD, password);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
