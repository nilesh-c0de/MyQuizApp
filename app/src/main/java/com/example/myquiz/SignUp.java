package com.example.myquiz;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.myquiz.mqUtils.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    DatabaseReference users;

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

    private TextInputLayout textInputEmail;
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    Button btnReg, btnSignIn;
    RadioGroup radioGroup;
    RadioButton student, teacher;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("New User? Register Here!");


        textInputEmail = (TextInputLayout) findViewById(R.id.TextInputLayoutEmail);
        textInputUsername = (TextInputLayout) findViewById(R.id.TextInputLayoutUsername);
        textInputPassword = (TextInputLayout) findViewById(R.id.TextInputLayoutPassword);

        btnReg = (Button) findViewById(R.id.button5);
        btnSignIn = (Button) findViewById(R.id.button6);

        radioGroup = (RadioGroup)findViewById(R.id.rg_student_teacher);
        student = (RadioButton)findViewById(R.id.radioButtonStudent);
        teacher = (RadioButton)findViewById(R.id.radioButtonTeacher);

        radioGroup.clearCheck();

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmInput();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent si = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(si);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    private boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputEmail.setError("Please enter a valid email address");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }

    private boolean validateUsername() {
        String usernameInput = textInputUsername.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            textInputUsername.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 15) {
            textInputUsername.setError("Username too long");
            return false;
        } else {
            textInputUsername.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textInputPassword.setError("Password too weak");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }

    public void confirmInput() {
        if (!validateEmail() | !validateUsername() | !validatePassword() | !validateCategory()) {
            return;
        }

      /*  String input = "Email: " + textInputEmail.getEditText().getText().toString();
        input += "\n";
        input += "Username: " + textInputUsername.getEditText().getText().toString();
        input += "\n";
        input += "Password: " + textInputPassword.getEditText().getText().toString();

        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();*/

        final User user = new User(textInputUsername.getEditText().getText().toString(), textInputPassword.getEditText().getText().toString(), textInputEmail.getEditText().getText().toString(), type);

        users = FirebaseDatabase.getInstance().getReference().child("Users").child(type);
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(user.getUsername()).exists()) {
                    Toast.makeText(getApplicationContext(), "This Username is Already Exist!", Toast.LENGTH_SHORT).show();
                } else {
                    users.child(user.getUsername()).setValue(user);
                    Toast.makeText(getApplicationContext(), "Successful Registeration!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean validateCategory() {

        if(radioGroup.getCheckedRadioButtonId() == -1)
        {
            Toast.makeText(getApplicationContext(), "Please select student if u are student\nOr Teacher if u r teacher!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton rb = (RadioButton)findViewById(selectedId);
            type = rb.getText().toString();
            return true;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
