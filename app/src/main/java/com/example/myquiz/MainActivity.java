package com.example.myquiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myquiz.mqUtils.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements  ExampleDialog.ExampleDialogListener{

    EditText et1, et2;
    CheckBox cb;
    TextView t1;
    Button btn;
    Button btnSignUp;

    DatabaseReference users, teachers;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String savedUser, savedPassword;
    public static final String EXTRA_UNAME = "com.example.myquiz.EXTRA_UNAME";

//    CoordinatorLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        layout = (CoordinatorLayout) findViewById(R.id.myLayout);


        setTitle("Already User? SignIn Here!");

        et1 = (EditText) findViewById(R.id.editTextUsername);
        et2 = (EditText) findViewById(R.id.editTextPassword);
        cb = (CheckBox) findViewById(R.id.checkBoxRM);
        t1 = (TextView) findViewById(R.id.textViewFP);
        btnSignUp = (Button) findViewById(R.id.buttonSignUp);
        btn = (Button) findViewById(R.id.buttonLogIn);
        t1.setPaintFlags(t1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //t2.setPaintFlags(t2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

       /* boolean status = isConnectedToInternet();
        if(status)
        {
            showSnackBar("online");
            setButtonsVisible(true);
        }
        else {
            setButtonsVisible(false);
            showSnackBar("No internet connection");
        }*/


        users = FirebaseDatabase.getInstance().getReference().child("Users").child("Student");
        teachers = FirebaseDatabase.getInstance().getReference().child("Users").child("Teacher");

        sharedPreferences = getApplicationContext().getSharedPreferences("SharedData", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(et1.getText().toString().isEmpty() || et2.getText().toString().isEmpty())) {
                    signIn(et1.getText().toString(), et2.getText().toString());
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please enter username and password!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 330);
                    toast.show();
                }
            }
        });

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent fp = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(fp);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(getApplicationContext(), SignUp.class);
                startActivity(register);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

       loadData();
        if (savedUser != null || savedPassword != null) {
           Intent intent = new Intent(getApplicationContext(), Dashboard.class);
            intent.putExtra(EXTRA_UNAME, savedUser);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    /*private void setButtonsVisible(boolean b) {
        btn.setEnabled(b);
        btnSignUp.setEnabled(b);
    }

    private void showSnackBar(String s) {


        if(s.equals("online"))
        {
            Snackbar snackbar =  Snackbar.make(layout, s, Snackbar.LENGTH_SHORT);
            View snackView = snackbar.getView();
            TextView textView = snackView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
        else
        {
            Snackbar.make(layout, s, Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean status = isConnectedToInternet();
                    if(status) {
                        setButtonsVisible(true);
                        showSnackBar("online");
                    }
                    else {
                        setButtonsVisible(false);
                        showSnackBar("No internet connection");
                    }
                }
            }).setActionTextColor(Color.RED).show();
        }
    }
    private boolean isConnectedToInternet() {

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) ||
                (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null  && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)) {
            //we are connected to a network
            return true;
        }
        else {
            return false;
        }
    }*/
    private void signIn(final String username, final String password) {

        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(username).exists()) {
                    User login = dataSnapshot.child(username).getValue(User.class);
                    if (login.getPassword().equals(password)) {
//                        Intent test = new Intent(getApplicationContext(), Paper.class);
//                        startActivity(test);
                        if (cb.isChecked()) {
                            //save seession
                            editor.putString("userdata", username);
                            editor.putString("passworddata", password);
                            editor.commit();
                            Toast.makeText(getApplicationContext(), "Data saved successfully", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getApplicationContext(), "Successful login!", Toast.LENGTH_SHORT).show();
                        Intent test = new Intent(getApplicationContext(), Dashboard.class);
                        test.putExtra(EXTRA_UNAME, username);
                        startActivity(test);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Password :P", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Username doesn't exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void loadData() {
        savedUser = sharedPreferences.getString("userdata", null);
        savedPassword = sharedPreferences.getString("passworddata", null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item1:
                openFacultyLoginDialog();
                return true;
            case R.id.item2:
                //Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
                openAboutDialog();
                return true;
            case R.id.item3:
//                Toast.makeText(getApplicationContext(), "Exit", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void openFacultyLoginDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }
    @Override
    public void applyTexts(String username, String password) {
        //Toast.makeText(getApplicationContext(), "Username:"+username+"\nPassword:"+password, Toast.LENGTH_SHORT).show();
        validateTeacher(username, password);
    }

    private void validateTeacher(final String username, final String password) {
        teachers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(username).exists()) {
                    User login = dataSnapshot.child(username).getValue(User.class);
                    if (login.getPassword().equals(password)) {
                        Toast.makeText(getApplicationContext(), "Successful login!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Paper.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Password :P", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Username doesn't exist!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void openAboutDialog() {
        AlertDialog.Builder about = new AlertDialog.Builder(MainActivity.this);
        about.setTitle("MyQuiz");
        about.setMessage("Developed by:");
        about.setIcon(R.drawable.app_icon);
        about.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.about, null);

        about.setView(view);
        about.show();

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
