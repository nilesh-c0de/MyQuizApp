package com.example.myquiz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class changeEmail extends AppCompatActivity {

    TextInputLayout textCurrentEmail, textNewEmail, textConfirmEmail;
    String cEmail, cUser, currentEmail, newEmail, confirmEmail;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        setTitle("Change Email Here!");
        Intent intent = getIntent();
        cEmail = intent.getStringExtra(UserProfile.EXTRA_EMAIL);
        cUser = intent.getStringExtra(UserProfile.EXTRA_USERNAME);

        ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Student");

        textCurrentEmail = (TextInputLayout)findViewById(R.id.text_current_email);
        textNewEmail = (TextInputLayout)findViewById(R.id.text_new_email);
        textConfirmEmail = (TextInputLayout)findViewById(R.id.text_confirm_email);

        Button btnSave = (Button)findViewById(R.id.button_save_email);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    private void validate() {

        if (!validateCurrentEmail() | !validateNewEmail() | !validateConfirmEmail()) {
            return;
        }

        /*String input = "Current: " + textCurrentEmail.getEditText().getText().toString();
        input += "\n";
        input += "New: " + textNewEmail.getEditText().getText().toString();
        input += "\n";
        input += "Confirm: " + textConfirmEmail.getEditText().getText().toString();

        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();*/

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ref.child(cUser).child("email").setValue(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(), "New email updated successfully!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
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


    private boolean validateCurrentEmail() {
        currentEmail = textCurrentEmail.getEditText().getText().toString().trim();

        if (currentEmail.isEmpty()) {
            textCurrentEmail.setError("Field can't be empty");
            return false;
        } else if (!currentEmail.equals(cEmail)) {
            textCurrentEmail.setError("Please enter a valid email address");
            return false;
        } else {
            textCurrentEmail.setError(null);
            return true;
        }
    }
    private boolean validateNewEmail() {
        newEmail = textNewEmail.getEditText().getText().toString().trim();

        if (newEmail.isEmpty()) {
            textNewEmail.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            textNewEmail.setError("Please enter a valid email address");
            return false;
        } else {
            textNewEmail.setError(null);
            return true;
        }
    }
    private boolean validateConfirmEmail() {
        confirmEmail = textConfirmEmail.getEditText().getText().toString().trim();

        if (confirmEmail.isEmpty()) {
            textConfirmEmail.setError("Field can't be empty");
            return false;
        } else if (!confirmEmail.equals(newEmail)) {
            textConfirmEmail.setError("Please enter same email address!");
            return false;
        } else {
            textConfirmEmail.setError(null);
            return true;
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
