package com.example.myquiz;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(isConnectedToInternet()) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else {
                    startActivity(new Intent(getApplicationContext(), connection.class));
                }
                finish();
            }
        },1000);
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
    }
}
