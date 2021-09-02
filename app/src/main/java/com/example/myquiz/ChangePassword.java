package com.example.myquiz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myquiz.mqUtils.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class ChangePassword extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");
    Button change;
    TextInputLayout textCurrentPassword, textNewPassword, textConfirmPassword;
    String username, passwd;
    String passwordCurrent, passwordNew, passwordConfirm;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        setTitle("Change Password Here!");
        Intent getPassword = getIntent();
        username = getPassword.getStringExtra(UserProfile.EXTRA_USERNAME);
        passwd = getPassword.getStringExtra(UserProfile.EXTRA_CURRENT_PASSWORD);

        ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Student");

        textCurrentPassword = (TextInputLayout)findViewById(R.id.text_current_password);
        textNewPassword = (TextInputLayout)findViewById(R.id.text_new_password);
        textConfirmPassword = (TextInputLayout)findViewById(R.id.text_confirm_password);

        change = (Button)findViewById(R.id.buttonChange);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password();
            }
        });
    }

    private void password() {
        if (!validateCurrentPassword() | !validateNewPassword() | !validateConfirmPassword()) {
            return;
        }
        /*String input = "Current: " + textCurrentPassword.getEditText().getText().toString();
        input += "\n";
        input += "New: " + textNewPassword.getEditText().getText().toString();
        input += "\n";
        input += "Confirm: " + textConfirmPassword.getEditText().getText().toString();

        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();*/

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ref.child(username).child("password").setValue(passwordNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(), "New password updated successfully!", Toast.LENGTH_SHORT).show();
                        }
                        else{
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


    private boolean validateCurrentPassword() {
        passwordCurrent = textCurrentPassword.getEditText().getText().toString().trim();

        if (passwordCurrent.isEmpty()) {
            textCurrentPassword.setError("Field can't be empty");
            return false;
        } else if (!passwordCurrent.equals(passwd)) {
            textCurrentPassword.setError("Wrong Password");
            return false;
        } else {
            textCurrentPassword.setError(null);
            return true;
        }
    }
    private boolean validateNewPassword() {
        passwordNew = textNewPassword.getEditText().getText().toString().trim();

        if (passwordNew.isEmpty()) {
            textNewPassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordNew).matches()) {
            textNewPassword.setError("Password too weak");
            return false;
        } else {
            textNewPassword.setError(null);
            return true;
        }
    }
    private boolean validateConfirmPassword() {
        passwordConfirm = textConfirmPassword.getEditText().getText().toString().trim();

        if (passwordConfirm.isEmpty()) {
            textConfirmPassword.setError("Field can't be empty");
            return false;
        } else if (!passwordConfirm.equals(passwordNew)) {
            textConfirmPassword.setError("Not same passwords!");
            return false;
        } else {
            textConfirmPassword.setError(null);
            return true;
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
