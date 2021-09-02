package com.example.myquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ImageView start, profile, result, logout;
    public static final String EXTRA_SENDNAME = "com.example.myquiz.EXTRA_SENDNAME";
    public static final String EXTRA_USERNAME = "com.example.myquiz.EXTRA_USERNAME";
    String uName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sharedPreferences = getApplicationContext().getSharedPreferences("SharedData", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Intent getUname = getIntent();
        uName = getUname.getStringExtra(MainActivity.EXTRA_UNAME);

        setTitle("Dashboard: "+uName);

        start = (ImageView) findViewById(R.id.imageView2);
        profile = (ImageView) findViewById(R.id.imageView3);
        result = (ImageView) findViewById(R.id.imageView4);
        logout = (ImageView) findViewById(R.id.imageView5);


        //result.setEnabled(false);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfile();
            }
        });

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResults();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
    }

    private void logOut() {
        editor.remove("userdata");
        editor.remove("passworddata");
        editor.commit();

        Toast.makeText(getApplicationContext(), "Logging out...", Toast.LENGTH_SHORT).show();
        Intent logout = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(logout);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void showResults() {
        Intent sr = new Intent(getApplicationContext(), ShowResults.class);
        sr.putExtra(EXTRA_SENDNAME, uName);
        startActivity(sr);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void showProfile() {
        Intent up = new Intent(getApplicationContext(), UserProfile.class);
        up.putExtra(EXTRA_SENDNAME, uName);
        startActivity(up);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void startQuiz() {
        Intent sq = new Intent(getApplicationContext(), StartQuiz.class);
        sq.putExtra(EXTRA_USERNAME, uName);
        startActivity(sq);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dbmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.i1:
                //Toast.makeText(getApplicationContext(), "HOME", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.i2:
                startQuiz();
                return true;
            case R.id.i3:
                showProfile();
                return true;
            case R.id.i4:
                showResults();
                return true;
            case R.id.i5:
                logOut();
                return true;
            case R.id.i6:
//              Toast.makeText(getApplicationContext(), "Exit", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Log out first!", Toast.LENGTH_SHORT).show();
    }
}
